package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import me.shadowalzazel.mcodyssey.items.utility.OdysseyFood
import me.shadowalzazel.mcodyssey.util.CustomColors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

object Foods {

    private val GRAY = CustomColors.GRAY.color

    val GREEN_APPLE = OdysseyFood("green_apple", Material.APPLE, "Green Apple", 5.5F, 3, 1.6F, ItemModels.GREEN_APPLE)
    val BACON = OdysseyFood("bacon", Material.COOKED_PORKCHOP, "Bacon", 8.0F, 3, 1.2F, ItemModels.BACON)
    val BERRY_TART = OdysseyFood("berry_tart", Material.COOKIE, "Berry Tart", 5.5F, 3, 0.8F, ItemModels.BERRY_TART)
    val CHOCOLATE_MOCHI = OdysseyFood("chocolate_mochi", Material.COOKIE, "Chocolate Mochi", 5.5F, 3, 0.8F, ItemModels.CHOCOLATE_MOCHI)
    val COFFEE = OdysseyFood("coffee", Material.COOKIE, "Coffee", 4.0F, 2, 0.8F,  ItemModels.COFFEE)
    val CRYSTAL_CANDY = OdysseyFood("crystal_candy", Material.SWEET_BERRIES, "Crystal Candy", 5.0F, 2, 0.8F, ItemModels.CRYSTAL_CANDY)
    val FISH_N_CHIPS = OdysseyFood("fish_n_chips", Material.COOKED_COD, "Fish N' Chips", 13.0F, 8, 1.6F,  ItemModels.FISH_N_CHIPS)
    val FRENCH_TOAST = OdysseyFood("french_toast", Material.BREAD, "French Toast", 6.0F, 6, 1.6F, ItemModels.FRENCH_TOAST)
    val FRUIT_BOWL = OdysseyFood("fruit_bowl", Material.APPLE, "Fruit Bowl", 12.0F, 9, 2.0F, ItemModels.FRUIT_BOWL)
    val SALMON_ROLL = OdysseyFood("salmon_roll", Material.COOKED_SALMON, "Salmon Roll", 6.0F, 4, 1.2F,  ItemModels.SALMON_ROLL)
    val SALMON_NIGIRI = OdysseyFood("salmon_nigiri", Material.SALMON, "Salmon Nigiri", 6.0F, 4, 1.2F, ItemModels.SALMON_NIGIRI)
    val SHOYU_RAMEN = OdysseyFood("shoyu_ramen", Material.COOKED_PORKCHOP, "Shoyu Ramen", 14.0F, 10, 2.0F, ItemModels.SHOYU_RAMEN)
    val SPIDER_EYE_BOBA = OdysseyFood("spider_eye_boba", Material.SPIDER_EYE, "Spider Eye Boba", 3.0F, 1, 0.8F, ItemModels.SPIDER_EYE_BOBA)
    val EARL_LILY_BOBA_TEA = OdysseyFood("earl_lily_boba_tea", Material.COOKIE, "Earl Lily Boba Tea", 5.0F, 2, 0.8F, ItemModels.EARL_LILY_BOBA_TEA)
    val BRISKET = OdysseyFood("brisket", Material.BEEF, "Brisket", 2.0F, 1, 0.8F, ItemModels.BRISKET)
    val COOKED_BRISKET = OdysseyFood("cooked_brisket", Material.COOKED_BEEF, "Cooked Brisket", 5.0F, 3, 0.8F, ItemModels.COOKED_BRISKET)
    val OOLONG_ORCHID_BOBA_TEA = OdysseyFood("earl_lily_boba_tea", Material.COOKIE, "Oolong Orchid Boba Tea", 5.0F, 2, 0.8F, ItemModels.OOLONG_ORCHID_BOBA_TEA)
    val MATCHA_MELON_BOBA_TEA = OdysseyFood("earl_lily_boba_tea", Material.COOKIE, "Matcha Melon Boba Tea", 5.0F, 2, 0.8F, ItemModels.MATCHA_MELON_BOBA_TEA)
    val THAI_TULIP_BOBA_TEA = OdysseyFood("earl_lily_boba_tea", Material.COOKIE, "Thai Tulip Boba Tea", 5.0F, 2, 0.8F, ItemModels.THAI_TULIP_BOBA_TEA)
    val ALLIUM_JADE_BOBA_TEA = OdysseyFood("earl_lily_boba_tea", Material.COOKIE, "Allium Jade Boba Tea", 5.0F, 2, 0.8F, ItemModels.ALLIUM_JADE_BOBA_TEA)
    val CORNFLOWER_CEYLON_BOBA_TEA = OdysseyFood("earl_lily_boba_tea", Material.COOKIE, "Cornflower Ceylon Boba Tea", 5.0F, 2, 0.8F, ItemModels.CORNFLOWER_CEYLON_BOBA_TEA)

    /*-----------------------------------------------------------------------------------------------*/

    val DOG_SPINACH = OdysseyItem("dog_spinach", Material.COOKED_BEEF, "Dog Spinach", ItemModels.DOG_SPINACH,
        lore = listOf(Component.text("Clifford is going to pack a hefty punch.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val DOG_SIZZLE_CRISP = OdysseyItem("dog_sizzle_crisp", Material.COOKED_BEEF, "Dog Spinach", ItemModels.DOG_SIZZLE_CRISP,
        lore = listOf(Component.text("Keeps your furry companions safe from fire.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))

    val DOG_MILK_BONE = OdysseyItem("dog_milk_bone", Material.COOKED_BEEF, "Dog Milk Bone", ItemModels.DOG_MILK_BONE,
        lore = listOf(Component.text("Makes your furry companion an absolute unit.", GRAY).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)))
}