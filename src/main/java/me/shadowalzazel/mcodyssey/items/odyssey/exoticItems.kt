package me.shadowalzazel.mcodyssey.items.odyssey

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import me.shadowalzazel.mcodyssey.models.CustomModels
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

// Rho-Annulus Schematics
object BabelAnnulusSchematics : OdysseyItem("Babel Annulus Schematics",
    Material.PAPER,
    Component.text("Babel Annulus", TextColor.color(255, 255, 85), TextDecoration.ITALIC).append(Component.text("-Schematics").decorate(TextDecoration.OBFUSCATED).color(TextColor.color(255, 255, 85))),
    listOf("${ChatColor.MAGIC}Keple${ChatColor.RESET}r-186f", "Babel Sys${ChatColor.MAGIC}tem. Vail's Test Site... Section A2${ChatColor.RESET}002"),
    CustomModels.BABEL_ANNULUS_SCHEMATICS)

// FIX!!
// DormantSentientStar
object DormantSentientStar : OdysseyItem("Dormant Star System",
    Material.NETHER_STAR,
    Component.text("Dormant Star System-", TextColor.color(255, 255, 85), TextDecoration.ITALIC).append(Component.text("Mini-Matrioshka").decorate(TextDecoration.OBFUSCATED).color(TextColor.color(255, 255, 85))),
    listOf("${ChatColor.GOLD}${ChatColor.ITALIC}Something is speaking to you..."),
    CustomModels.DORMANT_SENTIENT_STAR,
    mapOf(Enchantment.LOYALTY to 5))


// IMPURE_ANTI_MATTER_SHARD
object ImpureAntiMatterShard : OdysseyItem("Impure Anti-matter Shard",
    Material.PRISMARINE_SHARD,
    Component.text("Impure Anti-matter Shard", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf("${ChatColor.GOLD}${ChatColor.ITALIC}An impure yet stabilized shard of anti-matter..."),
    CustomModels.IMPURE_ANTI_MATTER_SHARD,
    mapOf(Enchantment.ARROW_INFINITE to 1))


// PURE_ANTIMATTER_CRYSTAL
object PureAntimatterCrystal : OdysseyItem("Pure Anti-matter Crystal",
    Material.PRISMARINE_SHARD,
    Component.text("Pure Anti-matter Shard", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf("${ChatColor.DARK_PURPLE}${ChatColor.ITALIC}DANGER! DANGER! DANGER! ${ChatColor.MAGIC}DANGER!", "${ChatColor.DARK_PURPLE}${ChatColor.ITALIC}Purely Unstable! Atomic Disintegration Imminent!"),
    CustomModels.PURE_ANTIMATTER_CRYSTAL,
    mapOf(Enchantment.ARROW_INFINITE to 1))


// NEUTRONIUM_BARK_INGOT
object NeutroniumBarkIngot: OdysseyItem("Neutronium-Bark Ingot",
    Material.NETHERITE_INGOT,
    Component.text("Neutronium-Bark Ingot", TextColor.color(255, 255, 85), TextDecoration.ITALIC),
    listOf("${ChatColor.GOLD}${ChatColor.ITALIC}A refined plank of very dense matter..."),
    CustomModels.NEUTRONIUM_BARK_INGOT,
    mapOf(Enchantment.DURABILITY to 5))


// FRUIT_OF_ERISHKIGAL
object FruitOfErishkigal : OdysseyItem("Fruit of Erishkigal",
    Material.ENCHANTED_GOLDEN_APPLE,
    Component.text("Fruit of Erishkigal", TextColor.color(255, 84, 255), TextDecoration.ITALIC),
    listOf("${ChatColor.GREEN}${ChatColor.ITALIC}A fruit engineered at the atomic level", "${ChatColor.RED}${ChatColor.ITALIC}With the power to alter one's life..."),
    CustomModels.FRUIT_OF_ERISHKIGAL,
    mapOf(Enchantment.MENDING to 1))


// SILMARIL_OF_YGGLADIEL
object SilmarilOfYggladiel : OdysseyItem("Silmaril Of Yggladiel",
    Material.AMETHYST_CLUSTER,
    Component.text("Silmaril Of Yggladiel", TextColor.color(255, 84, 255), TextDecoration.ITALIC),
    listOf("${ChatColor.GOLD}A jewel fruit grown by the world tree on Lupercal","${ChatColor.GOLD}Shining with stellar light..."),
    CustomModels.SILMARIL_OF_YGGLADIEL,
    mapOf(Enchantment.MENDING to 1))