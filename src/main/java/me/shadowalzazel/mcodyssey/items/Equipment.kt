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
    val SOUL_STEEL_UPGRADE_TEMPLATE = OdysseyItem("soul_steel_upgrade_template", Material.COPPER_INGOT, "Soul Steel Upgrade Template", ItemModels.SOUL_STEEL_UPGRADE_TEMPLATE,
        lore = listOf(Component.text("A Smithing Template used to upgrade Iron tools into Soul Steel.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))


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

    // TODO: Work with tome of polymerization and Volumes
    // Basic ranged wand to trigger on book
    val ARCANE_WAND  = OdysseyItem("arcane_wand", Material.WOODEN_SHOVEL, "Arcane Wand", ItemModels.ARCANE_WAND,
        lore = listOf(Component.text("The standard arcane tool for using gilded books at range.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val WARPING_WAND = OdysseyItem("warping_wand", Material.WOODEN_SHOVEL, "Warping Wand", ItemModels.ARCANE_WAND,
        lore = listOf(Component.text("The standard arcane tool for using gilded books at range.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

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


        /*
    val ABZU_BLADE = OdysseyItem(
        itemName = "",
        overrideMaterial = Material.DIAMOND_SWORD,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.STAFF)

    val NETHERITE_ZWEIHANDER = OdysseyItem(
        itemName = "netherite_zweihander",
        overrideMaterial = Material.NETHERITE_SWORD,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.ZWEIHANDER)

     */
}