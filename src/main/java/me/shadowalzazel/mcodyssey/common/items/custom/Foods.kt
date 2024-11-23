package me.shadowalzazel.mcodyssey.common.items.custom

import me.shadowalzazel.mcodyssey.common.items.OdysseyItem
import me.shadowalzazel.mcodyssey.common.items.OdysseyFood
import me.shadowalzazel.mcodyssey.util.constants.CustomColors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Foods {

    private val GRAY = CustomColors.GRAY.color

    val GREEN_APPLE = OdysseyFood("green_apple", Material.APPLE, "Green Apple", 5.5F, 3, 1.6F)
    val BACON = OdysseyFood("bacon", Material.COOKED_PORKCHOP, "Bacon", 8.0F, 3, 1.2F)
    val BERRY_TART = OdysseyFood("berry_tart", Material.COOKIE, "Berry Tart", 5.5F, 3, 0.8F)
    val CHOCOLATE_MOCHI = OdysseyFood("chocolate_mochi", Material.COOKIE, "Chocolate Mochi", 5.5F, 3, 0.8F)
    val COFFEE = OdysseyFood("coffee", Material.COOKIE, "Coffee", 4.0F, 2, 0.8F)
    val CRYSTAL_CANDY = OdysseyFood("crystal_candy", Material.SWEET_BERRIES, "Crystal Candy", 5.0F, 2, 0.8F)
    val FISH_N_CHIPS = OdysseyFood("fish_n_chips", Material.COOKED_COD, "Fish N' Chips", 13.0F, 8, 1.6F)
    val FRENCH_TOAST = OdysseyFood("french_toast", Material.BREAD, "French Toast", 6.0F, 6, 1.6F)
    val FRUIT_BOWL = OdysseyFood("fruit_bowl", Material.APPLE, "Fruit Bowl", 12.0F, 9, 2.0F)
    val SALMON_ROLL = OdysseyFood("salmon_roll", Material.COOKED_SALMON, "Salmon Roll", 6.0F, 4, 1.2F)
    val SALMON_NIGIRI = OdysseyFood("salmon_nigiri", Material.SALMON, "Salmon Nigiri", 6.0F, 4, 1.2F)
    val SHOYU_RAMEN = OdysseyFood("shoyu_ramen", Material.COOKED_PORKCHOP, "Shoyu Ramen", 14.0F, 10, 2.0F)
    val SPIDER_EYE_BOBA = OdysseyFood("spider_eye_boba", Material.SPIDER_EYE, "Spider Eye Boba", 3.0F, 1, 0.8F)
    val EARL_LILY_BOBA_TEA = OdysseyFood("earl_lily_boba_tea", Material.COOKIE, "Earl Lily Boba Tea", 5.0F, 2, 0.8F)
    val BRISKET = OdysseyFood("brisket", Material.BEEF, "Brisket", 2.0F, 1, 0.8F)
    val COOKED_BRISKET = OdysseyFood("cooked_brisket", Material.COOKED_BEEF, "Cooked Brisket", 5.0F, 3, 0.8F)
    val OOLONG_ORCHID_BOBA_TEA = OdysseyFood("earl_lily_boba_tea", Material.COOKIE, "Oolong Orchid Boba Tea", 5.0F, 2, 0.8F)
    val MATCHA_MELON_BOBA_TEA = OdysseyFood("earl_lily_boba_tea", Material.COOKIE, "Matcha Melon Boba Tea", 5.0F, 2, 0.8F)
    val THAI_TULIP_BOBA_TEA = OdysseyFood("earl_lily_boba_tea", Material.COOKIE, "Thai Tulip Boba Tea", 5.0F, 2, 0.8F)
    val ALLIUM_JADE_BOBA_TEA = OdysseyFood("earl_lily_boba_tea", Material.COOKIE, "Allium Jade Boba Tea", 5.0F, 2, 0.8F)
    val CORNFLOWER_CEYLON_BOBA_TEA = OdysseyFood("earl_lily_boba_tea", Material.COOKIE, "Cornflower Ceylon Boba Tea", 5.0F, 2, 0.8F)

    /*-----------------------------------------------------------------------------------------------*/

    val DOG_SPINACH = OdysseyItem("dog_spinach", Material.COOKED_BEEF, "Dog Spinach",
        lore = listOf(Component.text("Clifford is going to pack a hefty punch.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val DOG_SIZZLE_CRISP = OdysseyItem("dog_sizzle_crisp", Material.COOKED_BEEF, "Dog Spinach",
        lore = listOf(Component.text("Keeps your furry companions safe from fire.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val DOG_MILK_BONE = OdysseyItem("dog_milk_bone", Material.COOKED_BEEF, "Dog Milk Bone",
        lore = listOf(Component.text("Makes your furry companion an absolute unit.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
}