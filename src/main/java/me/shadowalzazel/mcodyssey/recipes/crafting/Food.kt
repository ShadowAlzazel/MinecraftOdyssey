package me.shadowalzazel.mcodyssey.recipes.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Foods
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.*

class Food {

    fun getRecipes(): List<Recipe> {
        return listOf(
            createBeetrootCookieRecipe(),
            createPumpkinCookieRecipe(),
            createHoneyCookieRecipe(),
            createAppleCookieRecipe(),
            createBerryCookieRecipe(),
            createGlowBerryCookieRecipe(),
            createMelonCookieRecipe(),
            createSugarCookieRecipe(),
            createGoldenCookieRecipe(),
            createSugaryBreadRecipe(),
            createBaconRecipe(),
            createFrenchToastRecipe(),
            chocolateMochi(),
            salmonRollRecipe(),
            fishNChipsRecipe(),
            fruitBowlRecipe(),
            coffeeRecipe(),
            spiderEyeBobaRecipe(),
            dogSpinach(),
            dogSizzleCrisp(),
            dogMilkBone()
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Shaped

    private fun createBeetrootCookieRecipe(): ShapedRecipe {
        val someResult = Foods.BEETROOT_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "beetroot_cookie"), someResult)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.BEETROOT)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createPumpkinCookieRecipe(): ShapedRecipe {
        val someCookie = Foods.PUMPKIN_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "pumpkin_cookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.PUMPKIN_SEEDS)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createHoneyCookieRecipe(): ShapedRecipe {
        val someCookie = Foods.HONEY_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "honey_cookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.HONEY_BOTTLE)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createAppleCookieRecipe(): ShapedRecipe {
        val someCookie = Foods.APPLE_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "apple_cookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.APPLE)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createBerryCookieRecipe(): ShapedRecipe {
        val someCookie = Foods.BERRY_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "berry_cookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.SWEET_BERRIES)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createGlowBerryCookieRecipe(): ShapedRecipe {
        val someCookie = Foods.GLOW_BERRY_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "glowberry_cookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.GLOW_BERRIES)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createMelonCookieRecipe(): ShapedRecipe {
        val someCookie = Foods.MELON_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "melon_cookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.MELON_SLICE)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createSugarCookieRecipe(): ShapedRecipe {
        val someCookie = Foods.SUGAR_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "sugar_cookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.SUGAR)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createGoldenCookieRecipe(): ShapedRecipe {
        val someCookie = Foods.GOLDEN_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_cookie"), someCookie)
        someRecipe.shape("XXX", "XYX", "XXX")
        someRecipe.setIngredient('X', Material.GOLD_INGOT)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createSugaryBreadRecipe(): ShapedRecipe {
        val someResult = Foods.SUGARY_BREAD.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "sugary_bread"), someResult)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.SUGAR)
        someRecipe.setIngredient('Y', Material.BREAD)
        return someRecipe
    }

    private fun chocolateMochi(): ShapedRecipe {
        val result = Foods.CHOCOLATE_MOCHI.createItemStack(1)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "chocolate_mochi"), result).apply {
            shape(" C ", "CSC", " C ")
            setIngredient('S', Material.SUGAR)
            setIngredient('C', Material.COCOA_BEANS)
        }
    }

    private fun fishNChipsRecipe(): ShapedRecipe {
        val result = Foods.FISH_N_CHIPS.createItemStack(1)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "fish_n_chips"), result).apply {
            shape("PP ", " C ", "   ")
            setIngredient('P', Material.BAKED_POTATO)
            setIngredient('C', Material.COOKED_COD)
        }
    }

    private fun fruitBowlRecipe(): ShapelessRecipe {
        val result = Foods.FRUIT_BOWL.createItemStack(1)
        return ShapelessRecipe(NamespacedKey(Odyssey.instance, "fruit_bowl"), result).apply {
            addIngredient(Material.BOWL)
            addIngredient(Material.APPLE)
            addIngredient(Material.MELON_SLICE)
            addIngredient(Material.GLOW_BERRIES)
            addIngredient(Material.SWEET_BERRIES)
        }
    }

    private fun spiderEyeBobaRecipe(): ShapedRecipe {
        val result = Foods.SPIDER_EYE_BOBA.createItemStack(1)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "spider_eye_boba"), result).apply {
            shape(" R ", " G ", " S ")
            setIngredient('R', Material.ROTTEN_FLESH)
            setIngredient('G', Material.GLASS_BOTTLE)
            setIngredient('S', Material.SPIDER_EYE)
        }
    }


    private fun coffeeRecipe(): ShapedRecipe {
        val result = Foods.COFFEE.createItemStack(1)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "coffee"), result).apply {
            shape("SCC", " B ", " X ")
            setIngredient('S', Material.SUGAR)
            setIngredient('C', Material.COCOA_BEANS)
            setIngredient('B', Material.GLASS_BOTTLE)
            setIngredient('X', Material.BRICK)
        }
    }

    private fun salmonRollRecipe(): ShapedRecipe {
        val result = Foods.SALMON_ROLL.createItemStack(2)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "salmon_roll"), result).apply {
            shape(" C ", "KSK", " K ")
            setIngredient('C', Material.CARROT)
            setIngredient('K', Material.DRIED_KELP)
            setIngredient('S', Material.SALMON)
        }
    }

    private fun dogSpinach(): ShapedRecipe {
        val result = Foods.DOG_SPINACH.createItemStack(2)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "dog_spinach"), result).apply {
            shape(" K ", "KBK", " K ")
            setIngredient('K', Material.KELP)
            setIngredient('B', Material.COOKED_BEEF)
        }
    }

    private fun dogSizzleCrisp(): ShapedRecipe {
        val result = Foods.DOG_SIZZLE_CRISP.createItemStack(2)
        return ShapedRecipe(NamespacedKey(Odyssey.instance, "dog_sizzle_crisp"), result).apply {
            shape("RMR", "MBM", "RMR")
            setIngredient('R', Material.REDSTONE)
            setIngredient('M', Material.MAGMA_CREAM)
            setIngredient('B', Material.COOKED_BEEF)
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Shapeless

    private fun dogMilkBone(): ShapelessRecipe {
        val result = Foods.DOG_MILK_BONE.createItemStack(1)
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

    private fun createBaconRecipe(): CampfireRecipe {
        val someResult = Foods.BACON.createItemStack(1)
        return CampfireRecipe(
            NamespacedKey(Odyssey.instance, "bacon"),
            someResult, Material.COOKED_PORKCHOP, 1.0F, 10 * 20
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Smoking

    private fun createFrenchToastRecipe(): SmokingRecipe {
        val someResult = Foods.FRENCH_TOAST.createItemStack(1)
        val someExactIngredient = Foods.SUGARY_BREAD.createItemStack(1)
        return SmokingRecipe(
            NamespacedKey(Odyssey.instance, "french_toast"),
            someResult, RecipeChoice.ExactChoice(someExactIngredient), 3.5F, 15 * 20
        )
    }


}