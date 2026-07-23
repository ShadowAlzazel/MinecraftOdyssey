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
    MANUAL
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
    private val triggers: Set<UnlockTrigger>,
    private val conditions: List<(UnlockContext) -> Boolean>,
    private val grants: List<(UnlockContext) -> Collection<String>>,
) {
    fun matches(ctx: UnlockContext): Boolean =
        ctx.trigger in triggers && conditions.all { it(ctx) }

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

    /** Namespace used for bare recipe names like "arcane_pen". */
    var defaultNamespace: String = "odyssey"

    /** Skip keys with no registered recipe on the server — catches typos silently. */
    var validateRecipes: Boolean = true

    /** Log every grant + every bad key. Turn on while adding content. */
    var debug: Boolean = false

    /** How far an unlock is allowed to chain into further unlocks in one dispatch. */
    var maxCascadeDepth: Int = 8

    private val rules = mutableListOf<UnlockRule>()
    private val groups = mutableMapOf<String, MutableSet<String>>()
    private val running = HashSet<UUID>()

    val ruleCount: Int get() = rules.size

    // --- registration ------------------------------------------------------

    fun rule(id: String, block: UnlockRuleBuilder.() -> Unit) {
        rules += UnlockRuleBuilder(id).apply(block).build()
    }

    /** A reusable bundle of recipe names, referenced with grantGroup("name"). */
    fun group(name: String, vararg recipes: String) {
        groups.getOrPut(name) { mutableSetOf() } += recipes
    }

    fun group(name: String, recipes: Collection<String>) {
        groups.getOrPut(name) { mutableSetOf() } += recipes
    }

    internal fun resolveGroup(name: String): Set<String> = groups[name] ?: emptySet()

    /**
     * Bulk shorthand: "seeing/crafting X unlocks Y and Z", on any trigger.
     * This is the one you'll use for the long tail of items.
     */
    fun table(vararg entries: Pair<String, List<String>>) {
        entries.forEach { (source, unlocks) ->
            rule("table:$source") {
                onAnyTrigger()
                whenIdIs(source)
                grant(unlocks)
            }
        }
    }

    fun clear() {
        rules.clear()
        groups.clear()
    }

    // --- entry points ------------------------------------------------------

    fun onRecipe(player: Player, key: NamespacedKey, trigger: UnlockTrigger = UnlockTrigger.DISCOVER): Int =
        dispatch(UnlockContext(player, trigger, key.key.lowercase(), key.namespace, recipeKey = key))

    fun onItem(player: Player, item: ItemStack, trigger: UnlockTrigger, recipe: Recipe? = null): Int {
        val key = (recipe as? Keyed)?.key
        return dispatch(
            UnlockContext(
                player = player,
                trigger = trigger,
                id = item.unlockId(),
                namespace = key?.namespace ?: item.type.key.namespace,
                item = item,
                recipeKey = key,
            )
        )
    }

    /** Call this from anywhere in your own code — quests, mob drops, blocks placed, whatever. */
    fun fire(player: Player, id: String, trigger: UnlockTrigger = UnlockTrigger.MANUAL): Int =
        dispatch(UnlockContext(player, trigger, id.lowercase().substringAfter(':')))

    /** Grants recipes directly, bypassing rules. Returns how many were new. */
    fun give(player: Player, vararg recipes: String): Int =
        player.discoverRecipes(recipes.map { it.toRecipeKey() }.filter { canDiscover(player, it) })

    // --- core --------------------------------------------------------------

    fun dispatch(context: UnlockContext): Int {
        val player = context.player
        // Re-entrancy guard: discoverRecipes() fires PlayerRecipeDiscoverEvent,
        // which comes straight back here. Cascading is handled below instead.
        if (!running.add(player.uniqueId)) return 0
        try {
            var granted = 0
            var frontier = listOf(context)
            val visited = HashSet<String>()
            var depth = 0

            while (frontier.isNotEmpty() && depth < maxCascadeDepth) {
                depth++
                val wanted = LinkedHashSet<NamespacedKey>()

                for (ctx in frontier) {
                    if (!visited.add(ctx.toString())) continue
                    for (rule in rules) {
                        if (!rule.matches(ctx)) continue
                        val keys = rule.resolve(ctx)
                        if (debug && keys.isNotEmpty()) {
                            Bukkit.getLogger().info("[Unlocks] $ctx -> rule '${rule.id}' -> $keys")
                        }
                        wanted += keys
                    }
                }

                val pending = wanted.filter { canDiscover(player, it) }
                if (pending.isEmpty()) break

                granted += player.discoverRecipes(pending)

                // Let newly unlocked recipes trigger rules of their own.
                frontier = pending.map {
                    UnlockContext(player, UnlockTrigger.DISCOVER, it.key, it.namespace, recipeKey = it)
                }
            }
            return granted
        } finally {
            running.remove(player.uniqueId)
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
 * Custom id if the item has one, otherwise the vanilla material name.
 * Always lowercase and without a namespace prefix.
 */
fun ItemStack.unlockId(): String {
    val custom = runCatching { getItemNameId() }.getOrNull()?.toString()
    val raw = if (custom.isNullOrBlank()) type.key.key else custom
    return raw.lowercase().substringAfter(':')
}