package me.shadowalzazel.mcodyssey.alchemy.potions

import me.shadowalzazel.mcodyssey.alchemy.utility.OdysseyPotion
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object CrystallinePotion : OdysseyPotion("Crystalline Potion",
    listOf(PotionEffect(PotionEffectType.LUCK, 0 , 0)), Color.fromRGB(147, 84, 255)) {
    override val odysseyDisplayName: String = "${ChatColor.WHITE}$name"
    override val odysseyLore = listOf("${ChatColor.GRAY}${ChatColor.ITALIC}A crystalline liquid...")
}

object PotionOfLevitation : OdysseyPotion("Potion of Levitation",
    listOf(PotionEffect(PotionEffectType.LEVITATION, 10 * 20 , 0)),
    Color.TEAL) {
    override val odysseyDisplayName: String = "${ChatColor.WHITE}$name"
}

object PotionOfWithering : OdysseyPotion("Potion of Withering",
    listOf(PotionEffect(PotionEffectType.WITHER, 30 * 20 , 0)), Color.fromRGB(23, 20, 19)) {
    override val odysseyDisplayName: String = "${ChatColor.WHITE}$name"
}

object PotionOfBioluminescence : OdysseyPotion("Potion of Bioluminescence",
    listOf(PotionEffect(PotionEffectType.GLOWING, 60 * 20 , 0)), Color.fromRGB(0, 255, 179)) {
    override val odysseyDisplayName: String = "${ChatColor.AQUA}$name"
}

object PotionOfLuck : OdysseyPotion("Potion of Luck",
    listOf(PotionEffect(PotionEffectType.LUCK, 60 * 20 , 0)), Color.fromRGB(255, 157, 0)) {
    override val odysseyDisplayName: String = "${ChatColor.WHITE}$name"
}

object PotionOfResistance : OdysseyPotion("Potion of Resistance",
    listOf(PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 30 * 20 , 0)), Color.fromRGB(65, 97, 122)) {
    override val odysseyDisplayName: String = "${ChatColor.WHITE}$name"
}

object PotionOfHaste : OdysseyPotion("Potion of Haste",
    listOf(PotionEffect(PotionEffectType.FAST_DIGGING, 30 * 20 , 0)), Color.fromRGB(255, 233, 133)) {
    override val odysseyDisplayName: String = "${ChatColor.WHITE}$name"
}

object PotionOfConstitution : OdysseyPotion("Potion of Constitution",
    listOf(PotionEffect(PotionEffectType.HEALTH_BOOST, 30 * 20 , 0)), Color.fromRGB(209, 0, 49)) {
    override val odysseyDisplayName: String = "${ChatColor.WHITE}$name"
}

object PotionOfStoneSkin : OdysseyPotion("Potion of Stone Skin",
    listOf(
        PotionEffect(PotionEffectType.ABSORPTION, 40 * 20 , 2),
        PotionEffect(PotionEffectType.SLOW, 40 * 20 , 0)
    ), Color.fromRGB(42, 51, 38)) {
    override val odysseyDisplayName: String = "${ChatColor.AQUA}$name"
}

object PotionOfWrath : OdysseyPotion("Potion of Wrath",
    listOf(
        PotionEffect(PotionEffectType.INCREASE_DAMAGE, 40 * 20 , 2),
        PotionEffect(PotionEffectType.BLINDNESS, 40 * 20 , 0)
    ), Color.fromRGB(250, 60, 17)) {
    override val odysseyDisplayName: String = "${ChatColor.AQUA}$name"
}

object PotionOfDecay : OdysseyPotion("Bottle o' Decay", emptyList(), Color.fromRGB(134, 227, 116)) {
    override val odysseyDisplayName: String = "${ChatColor.YELLOW}$name"
    override val odysseyLore = listOf("${ChatColor.GOLD}Decaying (0:20)")
}

object PotionOfFrost : OdysseyPotion("Bottle o' Frost", emptyList(), Color.fromRGB(163, 211, 255)) {
    override val odysseyDisplayName: String = "${ChatColor.YELLOW}$name"
    override val odysseyLore = listOf("${ChatColor.GOLD}Freezing (0:20)")
}

object PotionOfDouse : OdysseyPotion("Bottle o' Douse", emptyList(), Color.fromRGB(66, 46, 38)) {
    override val odysseyDisplayName: String = "${ChatColor.YELLOW}$name"
    override val odysseyLore = listOf("${ChatColor.GOLD}Douse (0:30)")
}

object PotionOfAblaze : OdysseyPotion("Bottle o' Ablaze", emptyList(), Color.fromRGB(247, 74, 0)) {
    override val odysseyDisplayName: String = "${ChatColor.YELLOW}$name"
    override val odysseyLore = listOf("${ChatColor.GOLD}Blazing (0:20)")
}

object PotionOfShimmer : OdysseyPotion("Bottle o' Shimmer",
    listOf(
        PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 20, 1),
        PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 20, 1),
        PotionEffect(PotionEffectType.GLOWING, 20 * 20, 1),
        PotionEffect(PotionEffectType.HUNGER, 20 * 20, 0),
        PotionEffect(PotionEffectType.CONFUSION, 15 * 20, 0),
    ), Color.fromRGB(78, 0, 181)) {
    override val odysseyDisplayName: String = "${ChatColor.DARK_PURPLE}$name"
    override val odysseyLore = listOf("${ChatColor.DARK_PURPLE}Shimmer (0:20)")
}