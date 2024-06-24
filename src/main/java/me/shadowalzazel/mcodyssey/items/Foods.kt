package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Foods {

    private val GRAY = TextColor.color(170, 170, 170)

    val BEETROOT_COOKIE = OdysseyItem("beetroot_cookie", Material.COOKIE, "Beetroot Cookie", ItemModels.BEETROOT_COOKIE)
    val PUMPKIN_COOKIE = OdysseyItem("pumpkin_cookie", Material.COOKIE, "Pumpkin Cookie", ItemModels.PUMPKIN_COOKIE)
    val HONEY_COOKIE = OdysseyItem("honey_cookie", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val APPLE_COOKIE = OdysseyItem("honey_cookie", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val BERRY_COOKIE = OdysseyItem("berry_cookie", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val GLOW_BERRY_COOKIE = OdysseyItem("glow_berry_cookie", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val MELON_COOKIE = OdysseyItem("melon_cookie", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val SUGAR_COOKIE = OdysseyItem("sugar_cookie", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val GOLDEN_COOKIE = OdysseyItem("golden_cookie", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val CHORUS_FRUIT_COOKIE = OdysseyItem("honey_cookie", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val FRENCH_TOAST = OdysseyItem("french_toast", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val SUGARY_BREAD = OdysseyItem("sugary_bread", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val CRYSTAL_CANDY = OdysseyItem("crystal_candie", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val BACON = OdysseyItem("bacon", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val SALMON_ROLL = OdysseyItem("salmon_roll", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val FISH_N_CHIPS = OdysseyItem("fish_n_chips", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val FRUIT_BOWL = OdysseyItem("fruit_bowl", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val COFFEE = OdysseyItem("coffee", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val SPIDER_EYE_BOBA = OdysseyItem("spider_eye_boba", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val STRAWBERRY_TART = OdysseyItem("strawberry_tart", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val CHOCOLATE_MOCHI = OdysseyItem("chocolate_mochi", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val RAINBOW_TROUT = OdysseyItem("rainbow_trout", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)
    val BLUEGILL = OdysseyItem("blue_gill", Material.COOKIE, "Honey Cookie", ItemModels.PUMPKIN_COOKIE)

    /*-----------------------------------------------------------------------------------------------*/

    val DOG_SPINACH = OdysseyItem("dog_spinach", Material.COOKED_BEEF, "Dog Spinach", ItemModels.DOG_SPINACH,
        lore = listOf(Component.text("Clifford is going to pack a hefty punch.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val DOG_SIZZLE_CRISP = OdysseyItem("dog_sizzle_crisp", Material.COOKED_BEEF, "Dog Spinach", ItemModels.DOG_SIZZLE_CRISP,
        lore = listOf(Component.text("Keeps your furry companions safe from fire.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val DOG_MILK_BONE = OdysseyItem("dog_milk_bone", Material.COOKED_BEEF, "Dog Milk Bone", ItemModels.DOG_MILK_BONE,
        lore = listOf(Component.text("Makes your furry companion an absolute unit.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
}