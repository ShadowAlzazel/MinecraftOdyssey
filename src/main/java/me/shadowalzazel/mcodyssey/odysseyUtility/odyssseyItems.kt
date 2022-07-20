package me.shadowalzazel.mcodyssey.odysseyUtility

import me.shadowalzazel.mcodyssey.odysseyUtility.utilty.OdysseyItem
import org.bukkit.Material
import org.bukkit.ChatColor

// MAKE OBJECTS LATER

// Galvanized Steel
class GalvanizedSteel : OdysseyItem("Galvanized Steel", Material.IRON_BLOCK) {
    override val odysseyDisplayName: String = "${ChatColor.DARK_GRAY}${ChatColor.ITALIC}$name"
}

// Pure-Alloy Copper
class PureAlloyCopper : OdysseyItem("Pure-Alloy Copper", Material.COPPER_BLOCK) {
    override val odysseyDisplayName: String = "${ChatColor.DARK_GRAY}${ChatColor.ITALIC}$name"
}

// Neutronium Scraps
class NeutroniumBarkScraps : OdysseyItem("Neutronium-Bark Scraps", Material.NETHERITE_SCRAP) {
    override val odysseyDisplayName: String = "${ChatColor.DARK_GRAY}${ChatColor.ITALIC}$name"
}

// Hawking Containment Unit
class HawkingEntangledUnit : OdysseyItem("Hawking Containment Unit", Material.ENDER_CHEST) {
    override val odysseyDisplayName: String =
        "${ChatColor.BLUE}${ChatColor.ITALIC}$name-${ChatColor.MAGIC}0x0000008"
    override val odysseyLore = listOf("A linked vacuum of matter and energy...")
}

// Idescine Sapling
class IdescineSaplings : OdysseyItem("Idescine Saplings", Material.OAK_SAPLING) {
    override val odysseyDisplayName: String =
        "${ChatColor.GREEN}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("A seed not ready to fully mature", "due to the conditions of the test-world...")
}

// Kugelblitz Containment Silo
class KugelblitzContainmentSilo : OdysseyItem("Kugelblitz Containment Silo", Material.YELLOW_SHULKER_BOX) {
    override val odysseyDisplayName: String =
        "${ChatColor.YELLOW}${ChatColor.ITALIC}Kugelblitz Containment Silo-${ChatColor.MAGIC}0x0000008"
    override val odysseyLore = listOf("A portable source of energy and matter")
}

// Rho-Annulus Schematics
class RhoAnnulusSchematics : OdysseyItem("Rho-Annulus Schematics", Material.PAPER) {
    override val odysseyDisplayName: String =
        "${ChatColor.LIGHT_PURPLE}${ChatColor.ITALIC}Rho-Annulus Schematics-${ChatColor.MAGIC}Kepler-186f"
    override val odysseyLore = listOf("${ChatColor.MAGIC}Keple${ChatColor.RESET}r-186f", "Rho Sys${ChatColor.MAGIC}tem. Vail's Test Site... Section A2${ChatColor.RESET}002")
}

// Refined Neptunian Diamond
class RefinedNeptunianDiamonds : OdysseyItem("Refined Neptunian-Diamond", Material.DIAMOND) {
    override val odysseyDisplayName: String =
        "${ChatColor.AQUA}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("A diamond forged inside a colossal planet refined to an impressive caliber")
}

// Refined Iojovian Emerald
class RefinedIojovianEmeralds : OdysseyItem("Refined Iojovian-Emerald", Material.EMERALD) {
    override val odysseyDisplayName: String =
        "${ChatColor.AQUA}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("An emerald grown near a Jovian super-planet to unmatched pristine")
}

// Polymorphic Glue
class PolymorphicGlue : OdysseyItem("Polymorphic Glue", Material.SLIME_BLOCK) {
    override val odysseyDisplayName: String =
        "${ChatColor.GRAY}${ChatColor.DARK_GRAY}$name"
    override val odysseyLore = listOf("Industrial Glue...")
}

// Artificial Star Unit 092412X
class ArtificialStarUnit : OdysseyItem("Artificial Star Unit", Material.EMERALD) {
    override val odysseyDisplayName: String =
        "${ChatColor.AQUA}${ChatColor.ITALIC}$name-${ChatColor.MAGIC}092412X"
    override val odysseyLore = listOf("${ChatColor.GOLD}${ChatColor.ITALIC}Something is speaking to you...")
}
