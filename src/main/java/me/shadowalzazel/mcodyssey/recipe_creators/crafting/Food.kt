package me.shadowalzazel.mcodyssey.recipe_creators.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Foods
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.*


class Food {

    // Register Recipes
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
            createSalmonRollRecipe(),
            createBaconRecipe(),
            createFrenchToastRecipe()
        )
    }

    /* ---------------------------------------------------------------------------*/

    // SHAPED
    private fun createBeetrootCookieRecipe(): ShapedRecipe {
        val someResult = Foods.BEETROOT_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "beetrootcookie"), someResult)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.BEETROOT)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createPumpkinCookieRecipe(): ShapedRecipe {
        val someCookie = Foods.PUMPKIN_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "pumpkincookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.PUMPKIN_SEEDS)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createHoneyCookieRecipe(): ShapedRecipe {
        val someCookie = Foods.HONEY_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "honeycookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.HONEY_BOTTLE)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createAppleCookieRecipe(): ShapedRecipe {
        val someCookie = Foods.APPLE_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "applecookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.APPLE)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createBerryCookieRecipe(): ShapedRecipe {
        val someCookie = Foods.BERRY_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "berrycookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.SWEET_BERRIES)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createGlowBerryCookieRecipe(): ShapedRecipe {
        val someCookie = Foods.GLOW_BERRY_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "glowberrycookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.GLOW_BERRIES)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createMelonCookieRecipe(): ShapedRecipe {
        val someCookie = Foods.MELON_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "meloncookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.MELON_SLICE)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createSugarCookieRecipe(): ShapedRecipe {
        val someCookie = Foods.SUGAR_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "sugarcookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.SUGAR)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createGoldenCookieRecipe(): ShapedRecipe {
        val someCookie = Foods.GOLDEN_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "goldencookie"), someCookie)
        someRecipe.shape("XXX", "XYX", "XXX")
        someRecipe.setIngredient('X', Material.GOLD_INGOT)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createSugaryBreadRecipe(): ShapedRecipe {
        val someResult = Foods.SUGARY_BREAD.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "sugarybread"), someResult)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.SUGAR)
        someRecipe.setIngredient('Y', Material.BREAD)
        return someRecipe
    }

    private fun createSalmonRollRecipe(): ShapedRecipe {
        val someResult = Foods.SALMON_ROLL.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "salmonroll"), someResult)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.DRIED_KELP)
        someRecipe.setIngredient('Y', Material.SALMON)
        return someRecipe
    }

    // CAMPFIRE
    private fun createBaconRecipe(): CampfireRecipe {
        val someResult = Foods.BACON.createItemStack(1)
        return CampfireRecipe(
            NamespacedKey(Odyssey.instance, "bacon"),
            someResult, Material.COOKED_PORKCHOP, 1.0F, 10 * 20
        )
    }

    // SMOKING
    private fun createFrenchToastRecipe(): SmokingRecipe {
        val someResult = Foods.FRENCH_TOAST.createItemStack(1)
        val someExactIngredient = Foods.SUGARY_BREAD.createItemStack(1)
        return SmokingRecipe(
            NamespacedKey(Odyssey.instance, "frenchtoast"),
            someResult, RecipeChoice.ExactChoice(someExactIngredient), 3.5F, 15 * 20
        )
    }


}