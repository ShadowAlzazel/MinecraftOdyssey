package me.shadowalzazel.mcodyssey.datagen.recipes.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Foods
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.*
import org.bukkit.inventory.recipe.CraftingBookCategory

class Food {

    fun getRecipes(): List<Recipe> {
        return listOf(
            baconRecipe(),
            berryTartRecipe(),
            chocolateMochiRecipe(),
            coffeeRecipe(),
            crystalCandyRecipe(),
            fishNChipsRecipe(),
            frenchToastRecipe(),
            fruitBowlRecipe(),
            salmonRollRecipe(),
            salmonNigiriRecipe(),
            shoyuRamenRecipe(),
            spiderEyeBobaRecipe(),
            earlLilyBobaTeaRecipe(),
            cookedBrisketRecipe(),
            brisketRecipe(),
            oolongOrchidBobaTeaRecipe(),
            matchaMelonBobaTeaRecipe(),
            thaiTulipBobaTeaRecipe(),
            alliumJadeBobaTeaRecipe(),
            cornflowerCeylonBobaTeaRecipe(),

            dogSpinachRecipe(),
            dogSizzleCrispRecipe(),
            dogMilkBoneRecipe()
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Table

    private fun crystalCandyRecipe(): ShapelessRecipe {
        val result = Foods.CRYSTAL_CANDY.createFoodStack(2)
        return ShapelessRecipe(NamespacedKey(Odyssey.instance, "crystal_candy"), result).apply {
            addIngredient(Material.AMETHYST_SHARD)
            addIngredient(Material.SWEET_BERRIES)
            category = CraftingBookCategory.MISC
        }
    }

    private fun frenchToastRecipe(): ShapelessRecipe {
        val result = Foods.FRENCH_TOAST.createFoodStack(1)
        return ShapelessRecipe(NamespacedKey(Odyssey.instance, "french_toast"), result).apply {
            addIngredient(Material.BREAD)
            addIngredient(Material.SUGAR)
            category = CraftingBookCategory.MISC
        }
    }

    private fun chocolateMochiRecipe(): ShapedRecipe {
        val result = Foods.CHOCOLATE_MOCHI.createFoodStack(4)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "chocolate_mochi"), result).apply {
            shape(" C ", "CSC", " C ")
            setIngredient('S', Material.SUGAR)
            setIngredient('C', Material.COCOA_BEANS)
            category = CraftingBookCategory.MISC
        }
    }

    private fun berryTartRecipe(): ShapedRecipe {
        val result = Foods.BERRY_TART.createFoodStack(4)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "berry_tart"), result).apply {
            shape(" C ", "CSC", " C ")
            setIngredient('S', Material.SUGAR)
            setIngredient('C', Material.SWEET_BERRIES)
            category = CraftingBookCategory.MISC
        }
    }

    private fun fishNChipsRecipe(): ShapelessRecipe {
        val result = Foods.FISH_N_CHIPS.createFoodStack(1)
        return ShapelessRecipe(NamespacedKey(Odyssey.instance, "fish_n_chips"), result).apply {
            addIngredient(Material.BAKED_POTATO)
            addIngredient(Material.COOKED_COD)
            category = CraftingBookCategory.MISC
        }
    }

    private fun fruitBowlRecipe(): ShapelessRecipe {
        val result = Foods.FRUIT_BOWL.createFoodStack(1)
        return ShapelessRecipe(NamespacedKey(Odyssey.instance, "fruit_bowl"), result).apply {
            addIngredient(Material.BOWL)
            addIngredient(Material.APPLE)
            addIngredient(Material.MELON_SLICE)
            addIngredient(Material.GLOW_BERRIES)
            addIngredient(Material.SWEET_BERRIES)
            category = CraftingBookCategory.MISC
        }
    }

    private fun spiderEyeBobaRecipe(): ShapedRecipe {
        val result = Foods.SPIDER_EYE_BOBA.createFoodStack(1)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "spider_eye_boba"), result).apply {
            shape(" R ", " G ", " S ")
            setIngredient('R', Material.ROTTEN_FLESH)
            setIngredient('G', Material.GLASS_BOTTLE)
            setIngredient('S', Material.SPIDER_EYE)
            category = CraftingBookCategory.MISC
        }
    }

    private fun coffeeRecipe(): ShapedRecipe {
        val result = Foods.COFFEE.createFoodStack(2)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "coffee"), result).apply {
            shape("SCC", " B ", " X ")
            setIngredient('S', Material.SUGAR)
            setIngredient('C', Material.COCOA_BEANS)
            setIngredient('B', Material.GLASS_BOTTLE)
            setIngredient('X', Material.BRICK)
            category = CraftingBookCategory.MISC
        }
    }

    private fun salmonRollRecipe(): ShapelessRecipe {
        val result = Foods.SALMON_ROLL.createFoodStack(2)
        return ShapelessRecipe(NamespacedKey(Odyssey.instance, "salmon_roll"), result).apply {
            addIngredient(Material.CARROT)
            addIngredient(Material.DRIED_KELP)
            addIngredient(Material.SALMON)
            category = CraftingBookCategory.MISC
        }
    }

    private fun salmonNigiriRecipe(): ShapelessRecipe {
        val result = Foods.SALMON_NIGIRI.createFoodStack(2)
        return ShapelessRecipe(NamespacedKey(Odyssey.instance, "salmon_nigiri"), result).apply {
            addIngredient(Material.WHEAT)
            addIngredient(Material.SALMON)
            category = CraftingBookCategory.MISC
        }
    }

    private fun shoyuRamenRecipe(): ShapelessRecipe {
        val result = Foods.SHOYU_RAMEN.createFoodStack(1)
        return ShapelessRecipe(NamespacedKey(Odyssey.instance, "shoyu_ramen"), result).apply {
            addIngredient(Material.COOKED_PORKCHOP)
            addIngredient(Material.WHEAT)
            addIngredient(Material.EGG)
            category = CraftingBookCategory.MISC
        }
    }

    private fun brisketRecipe(): ShapelessRecipe {
        val result = Foods.BRISKET.createFoodStack(4)
        return ShapelessRecipe(NamespacedKey(Odyssey.instance, "brisket"), result).apply {
            addIngredient(Material.BEEF)
            addIngredient(Material.BEEF)
            addIngredient(Material.BEEF)
            addIngredient(Material.BEEF)
            category = CraftingBookCategory.MISC
        }
    }

    private fun earlLilyBobaTeaRecipe(): ShapedRecipe {
        val result = Foods.EARL_LILY_BOBA_TEA.createFoodStack(2)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "earl_lily_boba_tea"), result).apply {
            shape("SCL", " B ", " C ")
            setIngredient('L', Material.LILY_OF_THE_VALLEY)
            setIngredient('S', Material.SUGAR)
            setIngredient('C', Material.COCOA_BEANS)
            setIngredient('B', Material.GLASS_BOTTLE)
            category = CraftingBookCategory.MISC
        }
    }

    private fun oolongOrchidBobaTeaRecipe(): ShapedRecipe {
        val result = Foods.OOLONG_ORCHID_BOBA_TEA.createFoodStack(2)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "oolong_orchid_boba_tea"), result).apply {
            shape("SCL", " B ", " C ")
            setIngredient('L', Material.BLUE_ORCHID)
            setIngredient('S', Material.SUGAR)
            setIngredient('C', Material.COCOA_BEANS)
            setIngredient('B', Material.GLASS_BOTTLE)
            category = CraftingBookCategory.MISC
        }
    }

    private fun matchaMelonBobaTeaRecipe(): ShapedRecipe {
        val result = Foods.MATCHA_MELON_BOBA_TEA.createFoodStack(2)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "matcha_melon_boba_tea"), result).apply {
            shape("SCL", " B ", " C ")
            setIngredient('L', Material.MELON_SLICE)
            setIngredient('S', Material.SUGAR)
            setIngredient('C', Material.COCOA_BEANS)
            setIngredient('B', Material.GLASS_BOTTLE)
            category = CraftingBookCategory.MISC
        }
    }

    private fun thaiTulipBobaTeaRecipe(): ShapedRecipe {
        val result = Foods.THAI_TULIP_BOBA_TEA.createFoodStack(2)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "thai_tulip_boba_tea"), result).apply {
            shape("SCL", " B ", " C ")
            setIngredient('L', Material.ORANGE_TULIP)
            setIngredient('S', Material.SUGAR)
            setIngredient('C', Material.COCOA_BEANS)
            setIngredient('B', Material.GLASS_BOTTLE)
            category = CraftingBookCategory.MISC
        }
    }

    private fun alliumJadeBobaTeaRecipe(): ShapedRecipe {
        val result = Foods.ALLIUM_JADE_BOBA_TEA.createFoodStack(2)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "allium_jade_boba_tea"), result).apply {
            shape("SCL", " B ", " C ")
            setIngredient('L', Material.ALLIUM)
            setIngredient('S', Material.SUGAR)
            setIngredient('C', Material.COCOA_BEANS)
            setIngredient('B', Material.GLASS_BOTTLE)
            category = CraftingBookCategory.MISC
        }
    }

    private fun cornflowerCeylonBobaTeaRecipe(): ShapedRecipe {
        val result = Foods.CORNFLOWER_CEYLON_BOBA_TEA.createFoodStack(2)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "cornflower_ceylon_boba_tea"), result).apply {
            shape("SCL", " B ", " C ")
            setIngredient('L', Material.CORNFLOWER)
            setIngredient('S', Material.SUGAR)
            setIngredient('C', Material.COCOA_BEANS)
            setIngredient('B', Material.GLASS_BOTTLE)
            category = CraftingBookCategory.MISC
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Dog Foods

    private fun dogSpinachRecipe(): ShapedRecipe {
        val result = Foods.DOG_SPINACH.newItemStack(2)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "dog_spinach"), result).apply {
            shape(" K ", "KBK", " K ")
            setIngredient('K', Material.KELP)
            setIngredient('B', Material.COOKED_BEEF)
        }
    }

    private fun dogSizzleCrispRecipe(): ShapedRecipe {
        val result = Foods.DOG_SIZZLE_CRISP.newItemStack(2)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "dog_sizzle_crisp"), result).apply {
            shape("RMR", "MBM", "RMR")
            setIngredient('R', Material.REDSTONE)
            setIngredient('M', Material.MAGMA_CREAM)
            setIngredient('B', Material.COOKED_BEEF)
        }
    }

    private fun dogMilkBoneRecipe(): ShapelessRecipe {
        val result = Foods.DOG_MILK_BONE.newItemStack(1)
        return ShapelessRecipe(NamespacedKey(Odyssey.instance, "dog_milk_bone"), result).apply {
            addIngredient(Material.MILK_BUCKET)
            addIngredient(Material.BONE)
            addIngredient(Material.COOKED_BEEF)
            addIngredient(Material.EGG)
            addIngredient(Material.HONEY_BOTTLE)
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Campfire

    private fun baconRecipe(): CampfireRecipe {
        val result = Foods.BACON.createFoodStack()
        return CampfireRecipe(
            NamespacedKey(Odyssey.instance, "bacon"),
            result,
            Material.COOKED_PORKCHOP,
            1.0F,
            10 * 20
        )
    }

    private fun cookedBrisketRecipe(): CampfireRecipe {
        val result = Foods.COOKED_BRISKET.createFoodStack()
        val ingredient = RecipeChoice.ExactChoice(Foods.BRISKET.createFoodStack())
        return CampfireRecipe(
            NamespacedKey(Odyssey.instance, "cooked_brisket"),
            result,
            ingredient,
            1.0F,
            10 * 20
        )
    }

}