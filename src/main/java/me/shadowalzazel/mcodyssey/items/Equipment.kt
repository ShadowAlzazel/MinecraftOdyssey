package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Equipment {

    private val GRAY = TextColor.color(170, 170, 170)

    // Templates
    val SOUL_STEEL_UPGRADE_TEMPLATE = OdysseyItem("soul_steel_upgrade_template", Material.PAPER, "Soul Steel Upgrade Template", ItemModels.SOUL_STEEL_UPGRADE_TEMPLATE,
        lore = listOf(Component.text("A Smithing Template used to upgrade Iron tools into Soul Steel.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
    val TITANIUM_UPGRADE_TEMPLATE = OdysseyItem("titanium_upgrade_template", Material.PAPER, "Titanium Upgrade Template", ItemModels.TITANIUM_UPGRADE_TEMPLATE,
        lore = listOf(Component.text("A Smithing Template used to upgrade Iron tools into Titanium.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
    val IRIDIUM_UPGRADE_TEMPLATE = OdysseyItem("iridium_upgrade_template", Material.PAPER, "Iridium Upgrade Template", ItemModels.IRIDIUM_UPGRADE_TEMPLATE,
        lore = listOf(Component.text("A Smithing Template used to upgrade Diamond tools into Iridium.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
    val MITHRIL_UPGRADE_TEMPLATE = OdysseyItem("mithril_upgrade_template", Material.PAPER, "Mithril Upgrade Template", ItemModels.MITHRIL_UPGRADE_TEMPLATE,
        lore = listOf(Component.text("A Smithing Template used to upgrade Diamond tools into Mithril.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))


    val IMPERIAL_ARMOR_TRIM_SMITHING_TEMPLATE = OdysseyItem("imperial_armor_trim_smithing_template", Material.PAPER, "Smithing Template", ItemModels.IMPERIAL_ARMOR_TRIM_SMITHING_TEMPLATE,
        lore = listOf(Component.text("Imperial Armor Trim", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // Weapon Molds
    // WIP [Molds - Katana/Weapons]
    // Copper Molds +0.5dmg [CONSUMED]
    // Silver Molds +0.5dmg [NOT CONSUMED]
    // Golden Molds +0.5dmg
    // Iridium Molds +1.0dmg []

    // Armor
    val HORNED_HELMET = OdysseyItem("horned_helmet", Material.CARVED_PUMPKIN, "Horned Helmet", ItemModels.HORNED_HELMET,
        lore = listOf(Component.text("The head wear of a viking!", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // Weapon Extras
    val SAYA = OdysseyItem("saya", Material.RABBIT_HIDE, "Saya", ItemModels.HORNED_HELMET,
        lore = listOf(Component.text("The sheath of a katana.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))


    // Arcane Tool
    val ARCANE_WAND  = OdysseyItem("arcane_wand", Material.STICK, "Arcane Wand", ItemModels.ARCANE_WAND,
        lore = listOf(Component.text("The standard arcane tool for using books at range.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        maxDamage = 217, maxStackSize = 1
    )
    val ARCANE_BLADE = OdysseyItem("arcane_sword", Material.STICK, "Arcane Sword", ItemModels.ARCANE_BLADE,
        lore = listOf(Component.text("An arcane blade that slices enemies in front.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        maxDamage = 217, maxStackSize = 1
    )
    val ARCANE_SCEPTER = OdysseyItem("arcane_scepter", Material.STICK, "Arcane Scepter", ItemModels.ARCANE_SCEPTER,
        lore = listOf(Component.text("A scepter that launches a homing projectile", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        maxDamage = 217, maxStackSize = 1
    )

    // Grapples
    val GRAPPLING_HOOK_MK1 = OdysseyItem("grappling_hook_mk1", Material.CROSSBOW, "Grappling Hook Mk.1", ItemModels.GRAPPLING_HOOK_MK1,
        lore = listOf(Component.text("Launch an arrow that will grapple on hit.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        maxDamage = 53
    )

    // Misc
    val WARPING_WAND = OdysseyItem("warping_wand", Material.STICK, "Warping Wand", ItemModels.ARCANE_WAND,
        lore = listOf(Component.text("A wand capable of attacking enemies in a cone", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val EXPLOSIVE_ARROW = OdysseyItem("explosive_arrow", Material.ARROW, "Explosive Arrow", ItemModels.EXPLOSIVE_ARROW,
        lore = listOf(Component.text("An arrow that explodes on impact!", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // Crossbows
    val COMPACT_CROSSBOW = OdysseyItem("compact_crossbow", Material.CROSSBOW, "Compact Crossbow", ItemModels.COMPACT_CROSSBOW,
        lore = listOf(Component.text("A mini crossbow that can be loaded in the off hand.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val AUTO_CROSSBOW = OdysseyItem("auto_crossbow", Material.CROSSBOW, "Full-Auto Crossbow", ItemModels.AUTO_CROSSBOW,
        lore = listOf(Component.text("Reload ammo when shooting straight from the off hand!", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val ALCHEMICAL_BOLTER = OdysseyItem("alchemical_bolter", Material.CROSSBOW, "Alchemical Bolter", ItemModels.ALCHEMICAL_BOLTER,
        lore = listOf(Component.text("Load in Throwable Potions and brew ammo with Thick Potions.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    // Weapons
    val VOID_LINKED_KUNAI = OdysseyItem("void_linked_kunai", Material.NETHERITE_SWORD, "Void Linked Kunai", ItemModels.VOID_LINKED_KUNAI,
        lore = listOf(Component.text("This kunai has the ability to attack a linked target.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))


}