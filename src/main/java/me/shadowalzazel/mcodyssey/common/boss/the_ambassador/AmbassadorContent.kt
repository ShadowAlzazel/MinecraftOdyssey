@file:Suppress("UnstableApiUsage")

package me.shadowalzazel.mcodyssey.common.boss.the_ambassador

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemEnchantments
import me.shadowalzazel.mcodyssey.common.boss.Dialogue
import me.shadowalzazel.mcodyssey.common.boss.DialogueKey
import me.shadowalzazel.mcodyssey.common.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.common.items.Item
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/* ============================================================================
 *  1. SPEECH
 *  Every line the Ambassador can say. Names describe *when* the line fires;
 *  the text is entirely yours to edit. Add as many variants per line as you
 *  like — one is chosen at random each time. Use <player> / <host> tokens.
 * ========================================================================== */

enum class AmbassadorLine : DialogueKey {
    // Arrival / departure
    ARRIVAL,
    DEFEATED,
    DEPARTED,

    // Being struck (trading phase)
    FIRST_STRIKE,          // a player's very first hit
    STRUCK_NEUTRAL,        // ordinary hit, uses <player>
    STRUCK_DISLIKED,       // hit from a low-likeness player, uses <player>
    STRUCK_LIKED,          // likeness >= 25
    STRUCK_BELOVED,        // likeness >= 65
    APPEASEMENT_WARNING,   // appeasement dipping dangerously low
    ENRAGED,               // patience broken -> combat begins
    NONPLAYER_STRUCK,      // damaged by something that isn't a player
    NONPLAYER_PATIENCE_BROKEN,

    // Attacks
    SKY_BOMBARD,
    GRAVITY_LAUNCH,
    HIJACK,
    GRAVITY_ANCHOR,
    FALLING_SINGULARITY,
    VOID_PULL,

    // Gifts
    GIFT_COOLDOWN,
    GIFT_MAXED,
    GIFT_REJECT_ENCHANTS,
    GIFT_SCULK,
    GIFT_BAD,
    GIFT_NICE;

    override val id: String get() = name
}

object AmbassadorDialogue {

    private val PINK = TextColor.color(255, 85, 255)
    private val WHITE = TextColor.color(255, 255, 255)
    private val PREFIX = Component.text("[The Ambassador] ", PINK)

    val pack: Dialogue = Dialogue.of(PREFIX, WHITE) {
        line(AmbassadorLine.ARRIVAL,
            "I am descending upon <host>'s land...",
            "Your realm has been... selected. Try to be worthy of it, <host>.")
        line(AmbassadorLine.DEFEATED,
            "Impossible... such insolence...")
        line(AmbassadorLine.DEPARTED,
            "This realm bores me. I take my leave.")

        line(AmbassadorLine.FIRST_STRIKE,
            "You dare raise a hand to me? How novel.",
            "<player>, that is not an appropriate way to introduce yourself, though what is expected from such a lowlife...")
        line(AmbassadorLine.STRUCK_NEUTRAL,
            "Such folly, <player>.",
            "Your blows are... noted, <player>.",
            "The lack of intelligence on this test world is apparent.")
        line(AmbassadorLine.STRUCK_DISLIKED,
            "I remember you, <player>. I remember all of it.",
            "<player>... Does your integrity not amount to anything?!")
        line(AmbassadorLine.STRUCK_LIKED,
            "And here I thought we understood one another.",
            "<player>... I expected more from you...")
        line(AmbassadorLine.STRUCK_BELOVED,
            "After everything I gave you? Disappointing.")
        line(AmbassadorLine.APPEASEMENT_WARNING,
            "My patience is a gift. Do not spend it so freely.",
            "<player> threatens your safety...",
            "<player> is testing my patience!")
        line(AmbassadorLine.ENRAGED,
            "Enough. If it is ruin you crave, I shall provide.",
            "<player>'s insolence has ended my graciousness. Fear me, if you dare.",
            "... Time to teach the lowlifes basic manners...")
        line(AmbassadorLine.NONPLAYER_STRUCK,
            "Your machines cannot harm me.",
            "Who is doing that?!")
        line(AmbassadorLine.NONPLAYER_PATIENCE_BROKEN,
            "You would let a mindless thing test me? So be it.")

        line(AmbassadorLine.SKY_BOMBARD,
            "Look to the heavens.",
            "I like fireworks, the heavens and gifts. But not you.")
        line(AmbassadorLine.GRAVITY_LAUNCH,
            "Rise. And do not come down gently.",
            "The universe is unfathomable. You are just mundane...")
        line(AmbassadorLine.HIJACK,
            "Come here. Let me show them what you are.",
            "It appears your friends are actually your foes...")
        line(AmbassadorLine.GRAVITY_LAUNCH,
            "Gravity answers to me now.")
        line(AmbassadorLine.FALLING_SINGULARITY,
            "The point is that it's super massive...")
        line(AmbassadorLine.VOID_PULL,
            "You cannot flee what pulls from within.",
            "You dare use such puny voidflight apparatus.")

        line(AmbassadorLine.GIFT_COOLDOWN,
            "Patience. I can only consider one trinket at a time.",
            "<player>, please wait. Still... thinking...")
        line(AmbassadorLine.GIFT_MAXED,
            "You already have my favour, <player>. Save your treasures.",
            "<player>, I am sorry. I can not receive any more pleasantries from you.")
        line(AmbassadorLine.GIFT_REJECT_ENCHANTS,
            "Enchanted baubles? I make my own magic.",
            "The Arcane is not something to give...")
        line(AmbassadorLine.GIFT_SCULK,
            "You bring me THAT? The silence of the deep offends me.",
            "The Sculk has corrupted this forsaken place... Its eradication is inevitable")
        line(AmbassadorLine.GIFT_BAD,
            "A poor offering. I expected better.",
            "This is not a gift, but left over debris, suitable for fodder.")
        line(AmbassadorLine.GIFT_NICE,
            "Now this... this pleases me, <player>.",
            "Your gifts are enjoyable.")
    }

    fun send(player: Player, line: AmbassadorLine, vars: Map<String, String> = emptyMap()) =
        pack.send(player, line, vars)

    fun send(audience: Collection<Player>, line: AmbassadorLine, vars: Map<String, String> = emptyMap()) =
        pack.send(audience, line, vars)
}

/* ============================================================================
 *  2. THE TRADE ECONOMY
 *  Was a 200-line `when` block; now it's a table you can read at a glance.
 *
 *  Each entry says how much a gift shifts likeness/patience, what line (if any)
 *  it triggers, and what the player gets back. Rewards run in a [GiftContext],
 *  so they can branch on the boss's current appeasement or the giver's rank.
 * ========================================================================== */

/** Context handed to a reward block: who gave, to whom, and the current mood. */
class GiftContext(
    val player: Player,
    val boss: TheAmbassador,
    /** 1..N scaling factor derived from the giver's likeness (higher = better rewards). */
    val extraValue: Int,
) {
    val appeasement: Double get() = boss.appeasement

    fun give(material: Material, amount: Int = 1) {
        player.inventory.addItem(ItemStack(material, amount))
    }

    fun give(stack: ItemStack) {
        player.inventory.addItem(stack)
    }
}

class GiftReaction(
    val likeness: Int = 0,
    val patiencePenalty: Double = 0.0,
    val message: AmbassadorLine? = null,
    val reward: GiftContext.() -> Unit = {},
)

class GiftTable private constructor(private val reactions: Map<Material, GiftReaction>) {

    /** null = the Ambassador simply isn't interested; the item is handed back. */
    fun reactionFor(material: Material): GiftReaction? = reactions[material]

    class Builder {
        private val reactions = mutableMapOf<Material, GiftReaction>()

        /** Register one reaction for one or more materials. */
        fun gift(
            vararg materials: Material,
            likeness: Int = 0,
            patiencePenalty: Double = 0.0,
            message: AmbassadorLine? = null,
            reward: GiftContext.() -> Unit = {},
        ) = apply {
            val reaction = GiftReaction(likeness, patiencePenalty, message, reward)
            materials.forEach { reactions[it] = reaction }
        }

        fun build() = GiftTable(reactions)
    }

    companion object {
        // Sapling -> (log, planks) so nine near-identical blocks collapse to a loop.
        private val SAPLINGS = mapOf(
            Material.OAK_SAPLING to (Material.OAK_LOG to Material.OAK_PLANKS),
            Material.BIRCH_SAPLING to (Material.BIRCH_LOG to Material.BIRCH_PLANKS),
            Material.SPRUCE_SAPLING to (Material.SPRUCE_LOG to Material.SPRUCE_PLANKS),
            Material.JUNGLE_SAPLING to (Material.JUNGLE_LOG to Material.JUNGLE_PLANKS),
            Material.ACACIA_SAPLING to (Material.ACACIA_LOG to Material.ACACIA_PLANKS),
            Material.DARK_OAK_SAPLING to (Material.DARK_OAK_LOG to Material.DARK_OAK_PLANKS),
            Material.CHERRY_SAPLING to (Material.CHERRY_LOG to Material.CHERRY_PLANKS),
            Material.MANGROVE_PROPAGULE to (Material.MANGROVE_LOG to Material.MANGROVE_PLANKS),
        )

        private val FLOWERS = arrayOf(
            Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET,
            Material.ORANGE_TULIP, Material.RED_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.CORNFLOWER,
            Material.LILY_OF_THE_VALLEY, Material.SUNFLOWER, Material.LILAC, Material.ROSE_BUSH, Material.PEONY,
        )

        private val MUSIC_DISCS = arrayOf(
            Material.MUSIC_DISC_WARD, Material.MUSIC_DISC_11, Material.MUSIC_DISC_5, Material.MUSIC_DISC_13,
            Material.MUSIC_DISC_CHIRP, Material.MUSIC_DISC_FAR, Material.MUSIC_DISC_MALL, Material.MUSIC_DISC_MELLOHI,
            Material.MUSIC_DISC_BLOCKS, Material.MUSIC_DISC_PIGSTEP, Material.MUSIC_DISC_STAL,
            Material.MUSIC_DISC_OTHERSIDE, Material.MUSIC_DISC_STRAD, Material.MUSIC_DISC_WAIT, Material.MUSIC_DISC_CAT,
        )

        /** The Ambassador's palate. Edit freely — this is the whole economy. */
        fun ambassador(): GiftTable = Builder().apply {
            gift(Material.NETHER_STAR, likeness = 30) {
                val enchantments = ItemEnchantments.itemEnchantments().add(
                    OdysseyEnchantments.GRAVITY_WELL,
                    1
                )
                val book = ItemStack(Material.ENCHANTED_BOOK)
                book.setData(DataComponentTypes.STORED_ENCHANTMENTS, enchantments)
                give(book)
            }
            gift(Material.NETHERITE_INGOT, likeness = 8) {
                when {
                    appeasement > 40 -> give(Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, 3)
                    appeasement > 20 -> give(Material.NETHERITE_AXE)
                    else -> give(Material.NETHERITE_AXE)
                }
            }
            gift(Material.DIAMOND, likeness = 5) {
                val loot = listOf(
                    Item.CRYSTAL_ALLOY_INGOT.newItemStack(),
                    Item.CRYSTAL_ALLOY_UPGRADE_TEMPLATE.newItemStack(),
                    Item.TITANIUM_INGOT.newItemStack(),
                    Item.TITANIUM_UPGRADE_TEMPLATE.newItemStack(),
                    Item.IRIDIUM_INGOT.newItemStack(),
                    Item.IRIDIUM_UPGRADE_TEMPLATE.newItemStack(),
                    Item.TOME_OF_IMITATION.newItemStack(),
                    Item.TOME_OF_REPLICATION.newItemStack()
                )
                give(loot.random())
            }
            gift(Material.AMETHYST_SHARD, likeness = 2) {
                give(Item.BLANK_TOME.newItemStack())
            }
            gift(Material.AMETHYST_BLOCK, likeness = 8) {
                val tomes = listOf(
                    Item.TOME_OF_PROMOTION.newItemStack(),
                    Item.TOME_OF_IMITATION.newItemStack(),
                    Item.TOME_OF_REPLICATION.newItemStack()
                )
                give(tomes.random())
            }
            gift(Material.EMERALD, likeness = 4) {
                val bricks = listOf(
                    Material.BRICKS, Material.RED_NETHER_BRICKS, Material.TERRACOTTA,
                    Material.MUD_BRICKS, Material.QUARTZ_BRICKS,
                )
                give(bricks.random(), 3 + 2 * extraValue)
                give(bricks.random(), 7)
                give(bricks.random(), 3 * extraValue)
            }
            gift(
                Material.SCULK, Material.SCULK_CATALYST, Material.SCULK_SENSOR,
                Material.SCULK_SHRIEKER, Material.SCULK_VEIN,
                likeness = -45, patiencePenalty = 45.0, message = AmbassadorLine.GIFT_SCULK,
            )
            gift(Material.ROTTEN_FLESH, Material.SPIDER_EYE, likeness = -2, patiencePenalty = 2.0)
            gift(Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.COPPER_BLOCK, likeness = -3) {
                val blocks = listOf(
                    Material.BARREL, Material.CHEST, Material.SMITHING_TABLE,
                    Material.BLAST_FURNACE, Material.FURNACE,
                )
                if (appeasement > 40) give(Material.ENCHANTING_TABLE) else give(blocks.random(), 4)
                give(blocks.random(), extraValue)
            }
            gift(Material.DIRT, Material.COARSE_DIRT, Material.ROOTED_DIRT, likeness = 1) {
                give(Material.GRASS_BLOCK, extraValue)
            }
            gift(Material.WRITTEN_BOOK, Material.PAINTING) {
                val trims = listOf(
                    Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE,
                    Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE,
                )
                if (appeasement > 60) give(trims.random()) else give(Material.RAW_GOLD, extraValue)
            }
            gift(
                Material.BEETROOT_SEEDS, Material.PUMPKIN_SEEDS,
                Material.WHEAT_SEEDS, Material.MELON_SEEDS, likeness = 1,
            ) {
                give(listOf(Material.PUMPKIN_PIE, Material.BREAD, Material.BEETROOT_SOUP).random(), extraValue)
            }
            gift(Material.ENCHANTED_GOLDEN_APPLE, likeness = 15) {
                give(Item.IRRADIATED_FRUIT.newItemStack(1))
            }
            gift(Material.HEART_OF_THE_SEA, likeness = 15) {
                if (appeasement > 50) give(Material.TRIDENT) else give(Material.RAW_GOLD, extraValue * 6)
            }
            gift(*FLOWERS, likeness = 7) {
                if (appeasement > 40) give(Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE) else give(Material.SNIFFER_EGG, extraValue)
            }
            gift(*MUSIC_DISCS, likeness = 7) {
                give(Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE)
            }

            // All the tree saplings, generated instead of hand-written.
            SAPLINGS.forEach { (sapling, wood) ->
                val (log, planks) = wood
                gift(sapling, likeness = 5) {
                    give(log, extraValue * 3)
                    give(planks, extraValue * 5)
                }
            }
        }.build()
    }
}