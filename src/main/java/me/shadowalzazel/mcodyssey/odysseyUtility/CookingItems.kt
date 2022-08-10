package me.shadowalzazel.mcodyssey.odysseyUtility

import me.shadowalzazel.mcodyssey.odysseyUtility.utilty.OdysseyItem
import org.bukkit.ChatColor
import org.bukkit.Material


object CookingItems {

    val BEETROOT_COOKIE = BeetrootCookie
    val PUMPKIN_COOKIE = PumpkinCookie
    val HONEY_COOKIE = HoneyCookie
    val APPLE_COOKIE = AppleCookie

    val FRENCH_TOAST = FrenchToast

    val SUGARY_BREAD = SugaryBread

    val BACON = Bacon

    val SALMON_ROLL = SalmonRollSushi

}



// BEETROOT_COOKIE
object BeetrootCookie : OdysseyItem("Beetroot Cookie", Material.COOKIE) {
    override val odysseyDisplayName: String = "${ChatColor.RED}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GRAY}A beetroot cookie!")
}

// PUMPKIN_COOKIE
object PumpkinCookie : OdysseyItem("Pumpkin Cookie", Material.COOKIE) {
    override val odysseyDisplayName: String = "${ChatColor.RED}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GRAY}A pumpkin cookie!")
}

// HONEY_COOKIE
object HoneyCookie : OdysseyItem("Honey Cookie", Material.COOKIE) {
    override val odysseyDisplayName: String = "${ChatColor.RED}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GRAY}A honey cookie!")
}

// APPLE_COOKIE
object AppleCookie : OdysseyItem("Apple Cookie", Material.COOKIE) {
    override val odysseyDisplayName: String = "${ChatColor.RED}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GRAY}An apple cookie!")
}

// FRENCH_TOAST
object FrenchToast : OdysseyItem("French Toast", Material.BREAD) {
    override val odysseyDisplayName: String = "${ChatColor.RED}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GRAY}French Toast!")
}

// BACON
object Bacon : OdysseyItem("Bacon", Material.COOKED_PORKCHOP) {
    override val odysseyDisplayName: String = "${ChatColor.RED}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GRAY}Bacon!")
}

// SUGARY_BREAD
object SugaryBread : OdysseyItem("Sugary Bread", Material.BREAD) {
    override val odysseyDisplayName: String = "${ChatColor.RED}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GRAY}Sugary Bread!")
}

// BEETROOT_COOKIE
object SalmonRollSushi : OdysseyItem("Salmon Roll", Material.COOKED_SALMON) {
    override val odysseyDisplayName: String = "${ChatColor.RED}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GRAY}Salmon Roll Sushi!")
}