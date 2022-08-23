package me.shadowalzazel.mcodyssey.items.odyssey

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.resources.CustomModels
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material

// Paper's of Arcus
object PapersOfArcus : OdysseyItem("Paper's Of Arcus",
    Material.PAPER,
    Component.text("${ChatColor.DARK_GRAY}${ChatColor.ITALIC}${"Paper's Of Arcus"}-${ChatColor.MAGIC}Arcus Imperium Vol.O"),
    customModel = CustomModels.PAPERS_OF_ARCUS)

// Galvanized Steel
object GalvanizedSteel : OdysseyItem("Galvanized Steel",
    Material.IRON_BLOCK,
    Component.text("${ChatColor.DARK_GRAY}${ChatColor.ITALIC}${"Galvanized Steel"}"), customModel = CustomModels.GALVANIZED_STEEL)

// Pure-Alloy Copper
object PureAlloyCopper : OdysseyItem("Pure-Alloy Copper", Material.COPPER_BLOCK, Component.text("${ChatColor.DARK_GRAY}${ChatColor.ITALIC}${"Pure-Alloy Copper"}"), customModel = CustomModels.PURE_ALLOY_COPPER)
// Pure-Alloy Gold
object PureAlloyGold : OdysseyItem("Pure-Alloy Gold", Material.GOLD_BLOCK, Component.text("${ChatColor.DARK_GRAY}${ChatColor.ITALIC}${"Pure-Alloy Gold"}"), customModel = CustomModels.PURE_ALLOY_GOLD)

// Hawking Containment Unit
object HawkingEntangledUnit : OdysseyItem("Hawking Containment Unit", Material.ENDER_CHEST, Component.text("${ChatColor.BLUE}${ChatColor.ITALIC}${"Hawking Containment Unit"}-${ChatColor.MAGIC}0x0000008"),
    listOf("A linked vacuum of matter and energy..."), customModel = CustomModels.HAWKING_ENTANGLED_UNIT)

// Kugelblitz Containment Silo
object KugelblitzContainmentSilo : OdysseyItem("Kugelblitz Containment Silo", Material.YELLOW_SHULKER_BOX, Component.text("${ChatColor.YELLOW}${ChatColor.ITALIC}Kugelblitz Containment Silo-${ChatColor.MAGIC}0x0000008"),
    listOf("A portable source of energy and matter"), customModel = CustomModels.KUGELBLITZ_CONTAINMENT_UNIT)

// Polymorphic Glue
object PolymorphicGlue : OdysseyItem("Polymorphic Glue", Material.SLIME_BLOCK, Component.text("${ChatColor.DARK_GRAY}${"Polymorphic Glue"}"), listOf("Industrial Glue..."), customModel = CustomModels.POLYMORPHIC_GLUE)

