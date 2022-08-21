package me.shadowalzazel.mcodyssey.items.odyssey

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

// Rho-Annulus Schematics
object RhoAnnulusSchematics : OdysseyItem("Rho-Annulus Schematics", Material.PAPER) {
    override val odysseyDisplayName: String =
        "${ChatColor.LIGHT_PURPLE}${ChatColor.ITALIC}Rho-Annulus Schematics-${ChatColor.MAGIC}Kepler-186f"
    override val odysseyLore = listOf("${ChatColor.MAGIC}Keple${ChatColor.RESET}r-186f", "Rho Sys${ChatColor.MAGIC}tem. Vail's Test Site... Section A2${ChatColor.RESET}002")
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

// NEUTRONIUM_BARK_INGOT
object NeutroniumBarkIngot: OdysseyItem("Neutronium-Bark Ingot", Material.NETHERITE_INGOT) {
    override val odysseyDisplayName: String =
        "${ChatColor.AQUA}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GOLD}${ChatColor.ITALIC}A refined plank of dense matter...")
    override val isEnchanted: Boolean = true
    override val someEnchantType: Enchantment = Enchantment.DURABILITY
    override val someEnchantLevel: Int = 5
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