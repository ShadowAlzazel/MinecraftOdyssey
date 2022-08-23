package me.shadowalzazel.mcodyssey.items.odyssey

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.resources.CustomModels
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

// Rho-Annulus Schematics
object RhoAnnulusSchematics : OdysseyItem("Rho-Annulus Schematics", Material.PAPER, Component.text("${ChatColor.LIGHT_PURPLE}${ChatColor.ITALIC}Rho-Annulus Schematics-${ChatColor.MAGIC}Kepler-186f"),
    listOf("${ChatColor.MAGIC}Keple${ChatColor.RESET}r-186f", "Rho Sys${ChatColor.MAGIC}tem. Vail's Test Site... Section A2${ChatColor.RESET}002"), customModel = CustomModels.RHO_ANNULUS_SCHEMATICS)

// Artificial Star Unit 092412X
object ArtificialStarUnit : OdysseyItem("Artificial Star Unit", Material.NETHER_STAR, Component.text("${ChatColor.AQUA}${ChatColor.ITALIC}${"Artificial Star Unit"}-${ChatColor.MAGIC}092412X"),
    listOf("${ChatColor.GOLD}${ChatColor.ITALIC}Something is speaking to you..."), CustomModels.ARTIFICIAL_STAR_UNIT, mapOf(Enchantment.LOYALTY to 5))


// Impure Anti-matter Shard
object ImpureAntiMatterShard : OdysseyItem("Impure Anti-matter Shard", Material.PRISMARINE_SHARD, Component.text("${ChatColor.AQUA}${ChatColor.ITALIC}${"Impure Anti-matter Shard"}"),
    listOf("${ChatColor.GOLD}${ChatColor.ITALIC}An impure yet stabilized shard of anti-matter..."), CustomModels.IMPURE_ANTI_MATTER_SHARD, mapOf(Enchantment.ARROW_INFINITE to 1))

// NEUTRONIUM_BARK_INGOT
object NeutroniumBarkIngot: OdysseyItem("Neutronium-Bark Ingot", Material.NETHERITE_INGOT, Component.text("${ChatColor.AQUA}${ChatColor.ITALIC}${"Neutronium-Bark Ingot"}"),
    listOf("${ChatColor.GOLD}${ChatColor.ITALIC}A refined plank of dense matter..."), CustomModels.NEUTRONIUM_BARK_INGOT, mapOf(Enchantment.DURABILITY to 5))

// Pure Anti-Matter Crystal
object PureAntimatterCrystal : OdysseyItem("Pure Anti-matter Crystal", Material.PRISMARINE_SHARD, Component.text("${ChatColor.WHITE}${ChatColor.ITALIC}${"Pure Anti-matter Crystal"} ${ChatColor.MAGIC}DANGER!"),
    listOf("${ChatColor.DARK_PURPLE}${ChatColor.ITALIC}DANGER! DANGER! DANGER! ${ChatColor.MAGIC}DANGER!", "${ChatColor.DARK_PURPLE}${ChatColor.ITALIC}Purely Unstable! Atomic Disintegration Imminent!"),
    CustomModels.PURE_ANTIMATTER_CRYSTAL, mapOf(Enchantment.ARROW_INFINITE to 1))

// Fruit of Erishkigal
object FruitOfErishkigal : OdysseyItem("Fruit of Erishkigal", Material.ENCHANTED_GOLDEN_APPLE, Component.text("${ChatColor.LIGHT_PURPLE}${ChatColor.ITALIC}${"Fruit of Erishkigal"}"),
    listOf("${ChatColor.GREEN}${ChatColor.ITALIC}A fruit engineered at the atomic level", "${ChatColor.RED}${ChatColor.ITALIC}With the power to alter one's life..."),
    CustomModels.FRUIT_OF_ERISHKIGAL, mapOf(Enchantment.MENDING to 1)) {

}

// Silmaril
object SilmarilOfYggladiel : OdysseyItem("Silmaril Of Yggladiel", Material.AMETHYST_CLUSTER, Component.text("${ChatColor.LIGHT_PURPLE}${ChatColor.ITALIC}${"Silmaril Of Yggladiel"}"),
    listOf("${ChatColor.GOLD}A jewel fruit grown by the world tree Yggladiel on Lupercal"), CustomModels.SILMARIL_OF_YGGLADIEL, mapOf(Enchantment.MENDING to 1))