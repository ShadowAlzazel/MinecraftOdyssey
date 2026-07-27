package me.shadowalzazel.mcodyssey.common.recipes

import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.common.mobs.passive.TreasurePig.getItemNameId
import org.bukkit.Bukkit
import org.bukkit.Keyed
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import java.util.EnumMap
import java.util.UUID

/* ------------------------------------------------------------------------- */
/*  Triggers                                                                  */
/* ------------------------------------------------------------------------- */

enum class UnlockTrigger {
    /** A recipe was discovered (vanilla progression, advancement, or by us). */
    DISCOVER,
    /** An item was crafted at a crafting table / inventory grid. */
    CRAFT,
    /** An item was picked up off the ground. */
    PICKUP,
    /** An item was pulled out of a furnace / blast furnace / smoker. */
    SMELT,
    /** Player joined — use for baseline / always-available recipes. */
    JOIN,
    /** Fired manually from your own code via RecipeUnlocks.fire(...). */
    MANUAL;

    companion object {
        /** Everything except JOIN — the sensible default for item-driven unlocks. */
        val ITEM_TRIGGERS: Set<UnlockTrigger> =
            setOf(DISCOVER, CRAFT, PICKUP, SMELT, MANUAL)
    }
}

/* ------------------------------------------------------------------------- */
/*  Context — everything a rule can look at                                   */
/* ------------------------------------------------------------------------- */

class UnlockContext(
    val player: Player,
    val trigger: UnlockTrigger,
    /** Normalised, namespace-free id. e.g. "diamond_sword", "titanium_ingot". */
    val id: String,
    val namespace: String = "minecraft",
    val item: ItemStack? = null,
    val recipeKey: NamespacedKey? = null,
) {
    val material: Material? = item?.type

    /** Longest-matching tool material found in the id, or null. */
    val toolMaterial: ToolMaterial? by lazy {
        ToolMaterial.entries.longestMatchIn(id) { it.nameId }
    }

    /** Longest-matching *vanilla* tool type found in the id, or null. */
    val toolType: ToolType? by lazy {
        ToolType.getVanillaTypes().longestMatchIn(id) { it.toolName }
    }

    val fullId: String get() = "$namespace:$id"

    override fun toString(): String = "[$trigger] $fullId"
}

/**
 * Picks the *longest* candidate contained in [haystack].
 * Stops "copper" from winning over "copper_alloy" just because it registered first.
 */
private inline fun <T> Iterable<T>.longestMatchIn(haystack: String, selector: (T) -> String): T? =
    filter { selector(it).isNotEmpty() && haystack.contains(selector(it), ignoreCase = true) }
        .maxByOrNull { selector(it).length }

/* ------------------------------------------------------------------------- */
/*  Rule                                                                      */
/* ------------------------------------------------------------------------- */

class UnlockRule internal constructor(
    val id: String,
    val triggers: Set<UnlockTrigger>,
    private val conditions: List<(UnlockContext) -> Boolean>,
    private val grants: List<(UnlockContext) -> Collection<String>>,
) {
    /** Trigger is handled by the index, so this only evaluates conditions. */
    fun matches(ctx: UnlockContext): Boolean = conditions.all { it(ctx) }

    fun resolve(ctx: UnlockContext): List<NamespacedKey> =
        grants.flatMap { it(ctx) }.distinct().map { it.toRecipeKey() }
}

/** "arcane_pen" -> odyssey:arcane_pen   |   "minecraft:stick" -> minecraft:stick */
internal fun String.toRecipeKey(): NamespacedKey {
    val clean = trim().lowercase()
    return if (':' in clean) {
        NamespacedKey.fromString(clean) ?: error("Malformed recipe key: $this")
    } else {
        NamespacedKey(RecipeUnlocks.defaultNamespace, clean)
    }
}

/* ------------------------------------------------------------------------- */
/*  DSL builder                                                               */
/* ------------------------------------------------------------------------- */

class UnlockRuleBuilder internal constructor(private val id: String) {

    private val triggers = mutableSetOf<UnlockTrigger>()
    private val conditions = mutableListOf<(UnlockContext) -> Boolean>()
    private val grants = mutableListOf<(UnlockContext) -> Collection<String>>()

    // --- when should this rule even be considered? -------------------------
    fun on(vararg trigger: UnlockTrigger) = apply { triggers += trigger }
    fun onAnyTrigger() = apply { triggers += UnlockTrigger.entries }
    fun onItemTriggers() = apply { triggers += UnlockTrigger.ITEM_TRIGGERS }

    // --- conditions --------------------------------------------------------
    fun whenIdIs(vararg ids: String) = require { ctx -> ids.any { ctx.id.equals(it, true) } }
    fun whenIdContains(vararg parts: String) = require { ctx -> parts.any { ctx.id.contains(it, true) } }
    fun whenIdMatches(regex: Regex) = require { regex.matches(it.id) }
    fun whenNamespace(vararg ns: String) = require { ctx -> ns.any { ctx.namespace.equals(it, true) } }
    fun whenMaterial(vararg mats: Material) = require { it.material in mats }
    fun whenToolMaterial(vararg mats: ToolMaterial) = require { it.toolMaterial in mats }
    fun whenToolType(vararg types: ToolType) = require { it.toolType in types }
    fun requireToolMaterial() = require { it.toolMaterial != null }
    fun requireVanillaTool() = require { it.toolType != null }
    fun whenPermission(node: String) = require { it.player.hasPermission(node) }

    /** Escape hatch — any predicate you like. */
    fun require(predicate: (UnlockContext) -> Boolean) = apply { conditions += predicate }

    // --- grants ------------------------------------------------------------
    fun grant(vararg recipes: String) = grant(recipes.toList())
    fun grant(recipes: Collection<String>) = apply { val frozen = recipes.toList(); grants += { frozen } }

    /** Grants everything registered under these group names (resolved at runtime). */
    fun grantGroup(vararg names: String) = apply {
        val frozen = names.toList()
        grants += { frozen.flatMap(RecipeUnlocks::resolveGroup) }
    }

    /** Computes the recipe names from the context — for pattern-based unlocks. */
    fun grantEach(block: (UnlockContext) -> Collection<String>) = apply { grants += block }

    internal fun build(): UnlockRule = UnlockRule(
        id = id,
        triggers = triggers.ifEmpty { UnlockTrigger.entries.toSet() },
        conditions = conditions.toList(),
        grants = grants.toList(),
    )
}

/* ------------------------------------------------------------------------- */
/*  Registry + dispatcher                                                     */
/* ------------------------------------------------------------------------- */

object RecipeUnlocks {

    var defaultNamespace: String = "odyssey"
    var validateRecipes: Boolean = true
    var debug: Boolean = false
    var maxCascadeDepth: Int = 8

    /** Skip a pickup if this player just picked up the same id. Kills farm churn. */
    var dedupePickups: Boolean = true

    // --- storage -----------------------------------------------------------

    /** Rules bucketed by trigger — dispatch only ever scans one bucket. */
    private val rulesByTrigger: EnumMap<UnlockTrigger, MutableList<UnlockRule>> =
        EnumMap(UnlockTrigger::class.java)

    /** Exact-id table entries — O(1) regardless of how many thousands you add. */
    private class TableEntry(val triggers: Set<UnlockTrigger>, val recipes: List<String>)
    private val tableIndex = HashMap<String, MutableList<TableEntry>>()

    private val groups = mutableMapOf<String, MutableSet<String>>()
    private val running = HashSet<UUID>()
    private val lastPickup = HashMap<UUID, String>()

    /** Per-trigger population count, covering both rules and table entries. */
    private val triggerCounts: EnumMap<UnlockTrigger, Int> = EnumMap(UnlockTrigger::class.java)

    val ruleCount: Int get() = rulesByTrigger.values.sumOf { it.size }
    val tableCount: Int get() = tableIndex.values.sumOf { it.size }

    /** The early-exit. False means a listener can return before touching the item. */
    fun hasRulesFor(trigger: UnlockTrigger): Boolean = (triggerCounts[trigger] ?: 0) > 0

    // --- registration ------------------------------------------------------

    fun rule(id: String, block: UnlockRuleBuilder.() -> Unit) {
        val rule = UnlockRuleBuilder(id).apply(block).build()
        for (trigger in rule.triggers) {
            rulesByTrigger.getOrPut(trigger) { mutableListOf() } += rule
            bump(trigger)
        }
    }

    fun group(name: String, vararg recipes: String) {
        groups.getOrPut(name) { mutableSetOf() } += recipes
    }

    fun group(name: String, recipes: Collection<String>) {
        groups.getOrPut(name) { mutableSetOf() } += recipes
    }

    internal fun resolveGroup(name: String): Set<String> = groups[name] ?: emptySet()

    /**
     * Bulk shorthand. Goes into a hash index rather than becoming rules,
     * so 5000 entries cost the same per event as 5.
     */
    fun table(
        vararg entries: Pair<String, List<String>>,
        triggers: Set<UnlockTrigger> = UnlockTrigger.ITEM_TRIGGERS,
    ) {
        for ((source, unlocks) in entries) {
            val key = source.normaliseId()
            tableIndex.getOrPut(key) { mutableListOf() } += TableEntry(triggers, unlocks)
            triggers.forEach(::bump)
        }
    }

    private fun bump(trigger: UnlockTrigger) {
        triggerCounts[trigger] = (triggerCounts[trigger] ?: 0) + 1
    }

    fun clear() {
        rulesByTrigger.clear()
        tableIndex.clear()
        groups.clear()
        triggerCounts.clear()
    }

    /** Call from PlayerQuitEvent so the dedupe map does not leak. */
    fun forget(player: Player) {
        lastPickup.remove(player.uniqueId)
        running.remove(player.uniqueId)
    }

    // --- entry points ------------------------------------------------------

    fun onRecipe(player: Player, key: NamespacedKey, trigger: UnlockTrigger = UnlockTrigger.DISCOVER): Int {
        if (!hasRulesFor(trigger)) return 0
        return dispatch(UnlockContext(player, trigger, key.key.lowercase(), key.namespace, recipeKey = key))
    }

    fun onItem(player: Player, item: ItemStack, trigger: UnlockTrigger, recipe: Recipe? = null): Int {
        if (!hasRulesFor(trigger)) return 0
        val id = item.unlockId()

        if (trigger == UnlockTrigger.PICKUP && dedupePickups) {
            if (lastPickup.put(player.uniqueId, id) == id) return 0
        }

        val key = (recipe as? Keyed)?.key
        return dispatch(
            UnlockContext(
                player = player,
                trigger = trigger,
                id = id,
                namespace = key?.namespace ?: item.type.key.namespace,
                item = item,
                recipeKey = key,
            )
        )
    }

    fun fire(player: Player, id: String, trigger: UnlockTrigger = UnlockTrigger.MANUAL): Int {
        if (!hasRulesFor(trigger)) return 0
        return dispatch(UnlockContext(player, trigger, id.normaliseId()))
    }

    fun give(player: Player, vararg recipes: String): Int =
        player.discoverRecipes(recipes.map { it.toRecipeKey() }.filter { canDiscover(player, it) })

    // --- core --------------------------------------------------------------

    fun dispatch(context: UnlockContext): Int {
        val player = context.player
        if (!running.add(player.uniqueId)) return 0
        try {
            var granted = 0
            var frontier = listOf(context)
            val seenKeys = HashSet<NamespacedKey>()
            var depth = 0

            while (frontier.isNotEmpty() && depth < maxCascadeDepth) {
                depth++
                val wanted = LinkedHashSet<NamespacedKey>()

                for (ctx in frontier) {
                    collectTable(ctx, wanted)
                    collectRules(ctx, wanted)
                }

                val pending = wanted.filter { seenKeys.add(it) && canDiscover(player, it) }
                if (pending.isEmpty()) break

                granted += player.discoverRecipes(pending)

                frontier = pending.map {
                    UnlockContext(player, UnlockTrigger.DISCOVER, it.key, it.namespace, recipeKey = it)
                }
            }
            return granted
        } finally {
            running.remove(player.uniqueId)
        }
    }

    private fun collectTable(ctx: UnlockContext, into: MutableSet<NamespacedKey>) {
        val entries = tableIndex[ctx.id] ?: return
        for (entry in entries) {
            if (ctx.trigger !in entry.triggers) continue
            for (name in entry.recipes) into += name.toRecipeKey()
            if (debug) Bukkit.getLogger().info("[Unlocks] $ctx -> table -> ${entry.recipes}")
        }
    }

    private fun collectRules(ctx: UnlockContext, into: MutableSet<NamespacedKey>) {
        val bucket = rulesByTrigger[ctx.trigger] ?: return
        for (rule in bucket) {
            if (!rule.matches(ctx)) continue
            val keys = rule.resolve(ctx)
            if (keys.isEmpty()) continue
            if (debug) Bukkit.getLogger().info("[Unlocks] $ctx -> rule '${rule.id}' -> $keys")
            into += keys
        }
    }

    private fun canDiscover(player: Player, key: NamespacedKey): Boolean {
        if (player.hasDiscoveredRecipe(key)) return false
        if (validateRecipes && Bukkit.getRecipe(key) == null) {
            if (debug) Bukkit.getLogger().warning("[Unlocks] no recipe registered for '$key' — typo?")
            return false
        }
        return true
    }
}

/* ------------------------------------------------------------------------- */
/*  Item id extraction                                                        */
/* ------------------------------------------------------------------------- */

/**
 * Lowercase, namespace-free, underscore-separated.
 * Normalising here means it does not matter whether your item_name component
 * holds "iron_zweihander" or "Iron Zweihander" — both land on the same id.
 */
internal fun String.normaliseId(): String =
    substringAfter(':').lowercase().replace(' ', '_')

/**
 * Custom id if the item has one, otherwise the vanilla material name.
 * Always lowercase and without a namespace prefix.
 */
fun ItemStack.unlockId(): String = getItemNameId().normaliseId()