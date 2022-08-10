package me.shadowalzazel.mcodyssey.odysseyUtility

import me.shadowalzazel.mcodyssey.odysseyUtility.utilty.OdysseyItem
import org.bukkit.Material
import org.bukkit.ChatColor
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

// MAKE OBJECTS LATER

object OdysseyItems {

    //
    val GILDED_BOOK = GildedBook

    val PAPERS_OF_ARCUS: OdysseyItem =  PapersOfArcus
    val GALVANIZED_STEEL: OdysseyItem = GalvanizedSteel
    val PURE_ALLOY_COPPER: OdysseyItem = PureAlloyCopper
    val PURE_ALLOY_GOLD: OdysseyItem = PureAlloyGold
    val NEUTRONIUM_BARK_SCRAPS: OdysseyItem = NeutroniumBarkScraps
    val HAWKING_ENTANGLED_UNIT: OdysseyItem = HawkingEntangledUnit
    val IDESCINE_SAPLING: OdysseyItem = IdescineSaplings
    val IDESCINE_ESSENCE: OdysseyItem = IdescineEssence
    val KUGELBLITZ_CONTAINMENT_SILO: OdysseyItem = KugelblitzContainmentSilo
    val RHO_ANNULUS_SCHEMATICS: OdysseyItem = RhoAnnulusSchematics
    val REFINED_NEPTUNIAN_DIAMONDS: OdysseyItem = RefinedNeptunianDiamonds
    val REFINED_IOJOVIAN_EMERALDS: OdysseyItem = RefinedIojovianEmeralds
    val POLYMORPHIC_GLUE: OdysseyItem = PolymorphicGlue
    val ARTIFICIAL_STAR_UNIT: OdysseyItem = ArtificialStarUnit
    val IMPURE_ANTIMATTER_SHARD: OdysseyItem = ImpureAntiMatterShard
    val PURE_ANTIMATTER_CRYSTAL: OdysseyItem = PureAntimatterCrystal
    val FRUIT_OF_ERISHKIGAL: OdysseyItem = FruitOfErishkigal
    val SILMARIL_OF_YGGLADIEL = SilmarilOfYggladiel

}


object GildedBook : OdysseyItem("Gilded Book", Material.ENCHANTED_BOOK) {
    override val odysseyDisplayName: String = "${ChatColor.GOLD}${ChatColor.ITALIC}Gilded Book"
    fun createGildedBook(gildedEnchantment: Enchantment, level: Int): ItemStack {
        val newGildedBook = ItemStack(Material.ENCHANTED_BOOK, 1)
        newGildedBook.addUnsafeEnchantment(gildedEnchantment, level)
        val gildedMeta: ItemMeta = newGildedBook.itemMeta
        val someBookLore = listOf("${ChatColor.GOLD}${gildedEnchantment.name} ${romanNumeralList[level]}")
        gildedMeta.lore = someBookLore
        gildedMeta.setDisplayName(odysseyDisplayName)
        newGildedBook.itemMeta = gildedMeta
        return newGildedBook
    }
}


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

// Neutronium Scraps
object NeutroniumBarkScraps : OdysseyItem("Neutronium-Bark Scraps", Material.NETHERITE_SCRAP) {
    override val odysseyDisplayName: String = "${ChatColor.DARK_GRAY}${ChatColor.ITALIC}$name"
}

// Hawking Containment Unit
object HawkingEntangledUnit : OdysseyItem("Hawking Containment Unit", Material.ENDER_CHEST) {
    override val odysseyDisplayName: String =
        "${ChatColor.BLUE}${ChatColor.ITALIC}$name-${ChatColor.MAGIC}0x0000008"
    override val odysseyLore = listOf("A linked vacuum of matter and energy...")
}

// Idescine Sapling
object IdescineSaplings : OdysseyItem("Idescine Saplings", Material.OAK_SAPLING) {
    override val odysseyDisplayName: String =
        "${ChatColor.GREEN}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("A seed not ready to fully mature", "due to the conditions of the test-world...")
}

// Idescine Essence
object IdescineEssence : OdysseyItem("Idescine Essence", Material.HONEY_BOTTLE) {
    override val odysseyDisplayName: String =
        "${ChatColor.GREEN}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.DARK_PURPLE}${ChatColor.ITALIC}A sappy substance brimming with life")
}

// Kugelblitz Containment Silo
object KugelblitzContainmentSilo : OdysseyItem("Kugelblitz Containment Silo", Material.YELLOW_SHULKER_BOX) {
    override val odysseyDisplayName: String =
        "${ChatColor.YELLOW}${ChatColor.ITALIC}Kugelblitz Containment Silo-${ChatColor.MAGIC}0x0000008"
    override val odysseyLore = listOf("A portable source of energy and matter")
}

// Rho-Annulus Schematics
object RhoAnnulusSchematics : OdysseyItem("Rho-Annulus Schematics", Material.PAPER) {
    override val odysseyDisplayName: String =
        "${ChatColor.LIGHT_PURPLE}${ChatColor.ITALIC}Rho-Annulus Schematics-${ChatColor.MAGIC}Kepler-186f"
    override val odysseyLore = listOf("${ChatColor.MAGIC}Keple${ChatColor.RESET}r-186f", "Rho Sys${ChatColor.MAGIC}tem. Vail's Test Site... Section A2${ChatColor.RESET}002")
}

// Refined Neptunian Diamond
object RefinedNeptunianDiamonds : OdysseyItem("Refined Neptunian-Diamond", Material.DIAMOND) {
    override val odysseyDisplayName: String =
        "${ChatColor.AQUA}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("A diamond forged inside a colossal planet refined to an impressive caliber")
    override val isEnchanted: Boolean = true
    override val someEnchantType: Enchantment = Enchantment.LOOT_BONUS_BLOCKS
    override val someEnchantLevel: Int = 1
}

// Refined Iojovian Emerald
object RefinedIojovianEmeralds : OdysseyItem("Refined Iojovian-Emerald", Material.EMERALD) {
    override val odysseyDisplayName: String =
        "${ChatColor.AQUA}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("An emerald grown near a Jovian super-planet to unmatched pristine")
    override val isEnchanted: Boolean = true
    override val someEnchantType: Enchantment = Enchantment.LOOT_BONUS_BLOCKS
    override val someEnchantLevel: Int = 1
}

// Polymorphic Glue
object PolymorphicGlue : OdysseyItem("Polymorphic Glue", Material.SLIME_BLOCK) {
    override val odysseyDisplayName: String =
        "${ChatColor.DARK_GRAY}$name"
    override val odysseyLore = listOf("Industrial Glue...")
}

// Artificial Star Unit 092412X
object ArtificialStarUnit : OdysseyItem("Artificial Star Unit", Material.NETHER_STAR) {
    override val odysseyDisplayName: String =
        "${ChatColor.AQUA}${ChatColor.ITALIC}$name-${ChatColor.MAGIC}092412X"
    override val odysseyLore = listOf("${ChatColor.GOLD}${ChatColor.ITALIC}Something is speaking to you...")
    override val isEnchanted: Boolean = true
    override val someEnchantType: Enchantment = Enchantment.LOYALTY
    override val someEnchantLevel: Int = 5
}

// Impure Anti-matter Shard
object ImpureAntiMatterShard : OdysseyItem("Impure Anti-matter Shard", Material.PRISMARINE_SHARD) {
    override val odysseyDisplayName: String =
        "${ChatColor.AQUA}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GOLD}${ChatColor.ITALIC}An impure yet stabilized shard of anti-matter...")
    override val isEnchanted: Boolean = true
    override val someEnchantType: Enchantment = Enchantment.ARROW_INFINITE
    override val someEnchantLevel: Int = 1
}

// Pure Anti-Matter Crystal
object PureAntimatterCrystal : OdysseyItem("Pure Anti-matter Crystal", Material.PRISMARINE_SHARD) {
    override val odysseyDisplayName: String =
        "${ChatColor.WHITE}${ChatColor.ITALIC}$name ${ChatColor.MAGIC}DANGER!"
    override val odysseyLore = listOf("${ChatColor.DARK_PURPLE}${ChatColor.ITALIC}DANGER! DANGER! DANGER! ${ChatColor.MAGIC}DANGER!", "${ChatColor.DARK_PURPLE}${ChatColor.ITALIC}Purely Unstable! Atomic Disintegration Imminent!")
    override val isEnchanted: Boolean = true
    override val someEnchantType: Enchantment = Enchantment.ARROW_INFINITE
    override val someEnchantLevel: Int = 1
}

// Fruit of Erishkigal
object FruitOfErishkigal : OdysseyItem("Fruit of Erishkigal", Material.ENCHANTED_GOLDEN_APPLE) {
    override val odysseyDisplayName: String =
        "${ChatColor.LIGHT_PURPLE}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GREEN}${ChatColor.ITALIC}A fruit engineered at the atomic level", "${ChatColor.RED}${ChatColor.ITALIC}With the power to change one's life...")
    override val isEnchanted: Boolean = true
    override val someEnchantType: Enchantment = Enchantment.MENDING
    override val someEnchantLevel: Int = 1
}

object SilmarilOfYggladiel : OdysseyItem("Silmaril Of Yggladiel", Material.AMETHYST_CLUSTER) {
    override val odysseyDisplayName: String = "${ChatColor.LIGHT_PURPLE}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GOLD}A jewel fruit grown by the world tree Yggladiel on Lupercal")
    override val isEnchanted: Boolean = true
    override val someEnchantType: Enchantment = Enchantment.LOOT_BONUS_BLOCKS
    override val someEnchantLevel: Int = 5

}