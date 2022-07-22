package me.shadowalzazel.mcodyssey.odysseyUtility

import me.shadowalzazel.mcodyssey.odysseyUtility.utilty.OdysseyItem
import org.bukkit.Material
import org.bukkit.ChatColor
import org.bukkit.enchantments.Enchantment

// MAKE OBJECTS LATER

object OdysseyItems {

    val PAPERS_OF_ARCUS: OdysseyItem = PapersOfArcus()
    val GALVANIZED_STEEL: OdysseyItem = GalvanizedSteel()
    val PURE_ALLOY_COPPER: OdysseyItem = PureAlloyCopper()
    val PURE_ALLOY_GOLD: OdysseyItem = PureAlloyGold()
    val NEUTRONIUM_BARK_SCRAPS: OdysseyItem = NeutroniumBarkScraps()
    val HAWKING_ENTANGLED_UNIT: OdysseyItem = HawkingEntangledUnit()
    val IDESCINE_SAPLING: OdysseyItem = IdescineSaplings()
    val KUGELBLITZ_CONTAINMENT_SILO: OdysseyItem = KugelblitzContainmentSilo()
    val RHO_ANNULUS_SCHEMATICS: OdysseyItem = RhoAnnulusSchematics()
    val REFINED_NEPTUNIAN_DIAMONDS: OdysseyItem = RefinedNeptunianDiamonds()
    val REFINED_IOJOVIAN_EMERALDS: OdysseyItem = RefinedIojovianEmeralds()
    val POLYMORPHIC_GLUE: OdysseyItem = PolymorphicGlue()
    val ARTIFICIAL_STAR_UNIT: OdysseyItem = ArtificialStarUnit()

}

// Paper's of Arcus
class PapersOfArcus : OdysseyItem("Paper's Of Arcus", Material.PAPER) {
    override val odysseyDisplayName: String = "${ChatColor.DARK_GRAY}${ChatColor.ITALIC}$name-${ChatColor.MAGIC}Arcus Imperium Vol.O"
}


// Galvanized Steel
class GalvanizedSteel : OdysseyItem("Galvanized Steel", Material.IRON_BLOCK) {
    override val odysseyDisplayName: String = "${ChatColor.DARK_GRAY}${ChatColor.ITALIC}$name"
}

// Pure-Alloy Copper
class PureAlloyCopper : OdysseyItem("Pure-Alloy Copper", Material.COPPER_BLOCK) {
    override val odysseyDisplayName: String = "${ChatColor.DARK_GRAY}${ChatColor.ITALIC}$name"
}

// Pure-Alloy Gold
class PureAlloyGold : OdysseyItem("Pure-Alloy Gold", Material.GOLD_BLOCK) {
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
    override val isEnchanted: Boolean = true
    override val someEnchantType: Enchantment = Enchantment.LOOT_BONUS_BLOCKS
    override val someEnchantLevel: Int = 1
}

// Refined Iojovian Emerald
class RefinedIojovianEmeralds : OdysseyItem("Refined Iojovian-Emerald", Material.EMERALD) {
    override val odysseyDisplayName: String =
        "${ChatColor.AQUA}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("An emerald grown near a Jovian super-planet to unmatched pristine")
    override val isEnchanted: Boolean = true
    override val someEnchantType: Enchantment = Enchantment.LOOT_BONUS_BLOCKS
    override val someEnchantLevel: Int = 1
}

// Polymorphic Glue
class PolymorphicGlue : OdysseyItem("Polymorphic Glue", Material.SLIME_BLOCK) {
    override val odysseyDisplayName: String =
        "${ChatColor.GRAY}${ChatColor.DARK_GRAY}$name"
    override val odysseyLore = listOf("Industrial Glue...")
}

// Artificial Star Unit 092412X
class ArtificialStarUnit : OdysseyItem("Artificial Star Unit", Material.NETHER_STAR) {
    override val odysseyDisplayName: String =
        "${ChatColor.AQUA}${ChatColor.ITALIC}$name-${ChatColor.MAGIC}092412X"
    override val odysseyLore = listOf("${ChatColor.GOLD}${ChatColor.ITALIC}Something is speaking to you...")
    override val isEnchanted: Boolean = true
    override val someEnchantType: Enchantment = Enchantment.LOYALTY
    override val someEnchantLevel: Int = 5
}
