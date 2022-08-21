package me.shadowalzazel.mcodyssey.items.odyssey

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import org.bukkit.ChatColor
import org.bukkit.Material

// Paper's of Arcus
object PapersOfArcus : OdysseyItem("Paper's Of Arcus", Material.PAPER) {
    override val odysseyDisplayName: String = "${ChatColor.DARK_GRAY}${ChatColor.ITALIC}$name-${ChatColor.MAGIC}Arcus Imperium Vol.O"
}


// Galvanized Steel
object GalvanizedSteel : OdysseyItem("Galvanized Steel", Material.IRON_BLOCK) {
    override val odysseyDisplayName: String = "${ChatColor.DARK_GRAY}${ChatColor.ITALIC}$name"
}

// Pure-Alloy Copper
object PureAlloyCopper : OdysseyItem("Pure-Alloy Copper", Material.COPPER_BLOCK) {
    override val odysseyDisplayName: String = "${ChatColor.DARK_GRAY}${ChatColor.ITALIC}$name"
}

// Pure-Alloy Gold
object PureAlloyGold : OdysseyItem("Pure-Alloy Gold", Material.GOLD_BLOCK) {
    override val odysseyDisplayName: String = "${ChatColor.DARK_GRAY}${ChatColor.ITALIC}$name"
}

// Hawking Containment Unit
object HawkingEntangledUnit : OdysseyItem("Hawking Containment Unit", Material.ENDER_CHEST) {
    override val odysseyDisplayName: String =
        "${ChatColor.BLUE}${ChatColor.ITALIC}$name-${ChatColor.MAGIC}0x0000008"
    override val odysseyLore = listOf("A linked vacuum of matter and energy...")
}

// Kugelblitz Containment Silo
object KugelblitzContainmentSilo : OdysseyItem("Kugelblitz Containment Silo", Material.YELLOW_SHULKER_BOX) {
    override val odysseyDisplayName: String =
        "${ChatColor.YELLOW}${ChatColor.ITALIC}Kugelblitz Containment Silo-${ChatColor.MAGIC}0x0000008"
    override val odysseyLore = listOf("A portable source of energy and matter")
}

// Polymorphic Glue
object PolymorphicGlue : OdysseyItem("Polymorphic Glue", Material.SLIME_BLOCK) {
    override val odysseyDisplayName: String =
        "${ChatColor.DARK_GRAY}$name"
    override val odysseyLore = listOf("Industrial Glue...")
}

