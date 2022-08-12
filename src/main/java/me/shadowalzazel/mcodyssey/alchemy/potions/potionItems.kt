package me.shadowalzazel.mcodyssey.alchemy.potions

import me.shadowalzazel.mcodyssey.alchemy.utility.OdysseyPotion
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object PotionOfLevitation : OdysseyPotion("Potion of Levitation", PotionEffect(PotionEffectType.LEVITATION, 5 * 20 , 0), Color.TEAL) {
    override val odysseyDisplayName: String = "${ChatColor.WHITE}$name"
}

object PotionOfWithering : OdysseyPotion("Potion of Withering", PotionEffect(PotionEffectType.WITHER, 5 * 20 , 0), Color.fromRGB(23, 20, 19)) {
    override val odysseyDisplayName: String = "${ChatColor.WHITE}$name"
}

object PotionOfBioluminescence : OdysseyPotion("Potion of Bioluminescence", PotionEffect(PotionEffectType.GLOWING, 15 * 20 , 0), Color.fromRGB(0, 255, 179)) {
    override val odysseyDisplayName: String = "${ChatColor.WHITE}$name"
}

object PotionOfLuck : OdysseyPotion("Potion of Luck", PotionEffect(PotionEffectType.LUCK, 60 * 20 , 0), Color.fromRGB(255, 157, 0)) {
    override val odysseyDisplayName: String = "${ChatColor.WHITE}$name"
}

object CrystallinePotion : OdysseyPotion("Crystalline Potion", PotionEffect(PotionEffectType.LUCK, 0 , 0), Color.fromRGB(147, 84, 255)) {
    override val odysseyDisplayName: String = "${ChatColor.WHITE}$name"
    override val odysseyLore = listOf("${ChatColor.GRAY}${ChatColor.ITALIC}A crystalline liquid...")
}