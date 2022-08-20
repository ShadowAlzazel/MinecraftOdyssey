package me.shadowalzazel.mcodyssey.recipes

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.items.OdysseyFood
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.CampfireRecipe
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.SmokingRecipe


object CookingRecipes {

    // Shaped Recipes
    private val BEETROOT_COOKIE_RECIPE: ShapedRecipe = createBeetrootCookieRecipe()
    private val PUMPKIN_COOKIE_RECIPE: ShapedRecipe = createPumpkinCookieRecipe()
    private val HONEY_COOKIE_RECIPE: ShapedRecipe = createHoneyCookieRecipe()
    private val APPLE_COOKIE_RECIPE: ShapedRecipe = createAppleCookieRecipe()
    private val BERRY_COOKIE_RECIPE: ShapedRecipe = createBerryCookieRecipe()
    private val GLOW_BERRY_COOKIE_RECIPE: ShapedRecipe = createGlowBerryCookieRecipe()
    private val MELON_COOKIE_RECIPE: ShapedRecipe = createMelonCookieRecipe()
    private val SUGAR_COOKIE_RECIPE: ShapedRecipe = createSugarCookieRecipe()
    private val GOLDEN_COOKIE_RECIPE: ShapedRecipe = createGoldenCookieRecipe()

    private val SUGARY_BREAD_RECIPE: ShapedRecipe = createSugaryBreadRecipe()
    private val SALMON_ROLL_RECIPE: ShapedRecipe = createSalmonRollRecipe()
    // Campfire Recipes
    private val BACON_RECIPE: CampfireRecipe = createBaconRecipe()
    // Smoking Recipes
    private val FRENCH_TOAST_RECIPE: SmokingRecipe = createFrenchToastRecipe()

    val recipeSet = setOf<Recipe>(
        BEETROOT_COOKIE_RECIPE, PUMPKIN_COOKIE_RECIPE, HONEY_COOKIE_RECIPE, APPLE_COOKIE_RECIPE, BERRY_COOKIE_RECIPE, GLOW_BERRY_COOKIE_RECIPE, MELON_COOKIE_RECIPE, SUGAR_COOKIE_RECIPE, GOLDEN_COOKIE_RECIPE,
        SUGARY_BREAD_RECIPE, SALMON_ROLL_RECIPE, BACON_RECIPE, FRENCH_TOAST_RECIPE
    )

    // make function calls on init later

    // SHAPED
    private fun createBeetrootCookieRecipe(): ShapedRecipe {
        val someResult = OdysseyFood.BEETROOT_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "beetrootcookie"), someResult)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.BEETROOT)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createPumpkinCookieRecipe(): ShapedRecipe {
        val someCookie = OdysseyFood.PUMPKIN_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "pumpkincookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.PUMPKIN_SEEDS)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createHoneyCookieRecipe(): ShapedRecipe {
        val someCookie = OdysseyFood.HONEY_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "honeycookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.HONEY_BOTTLE)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createAppleCookieRecipe(): ShapedRecipe {
        val someCookie = OdysseyFood.APPLE_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "applecookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.APPLE)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createBerryCookieRecipe(): ShapedRecipe {
        val someCookie = OdysseyFood.BERRY_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "berrycookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.SWEET_BERRIES)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createGlowBerryCookieRecipe(): ShapedRecipe {
        val someCookie = OdysseyFood.GLOW_BERRY_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "glowberrycookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.GLOW_BERRIES)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createMelonCookieRecipe(): ShapedRecipe {
        val someCookie = OdysseyFood.MELON_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "meloncookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.MELON_SLICE)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createSugarCookieRecipe(): ShapedRecipe {
        val someCookie = OdysseyFood.SUGAR_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "sugarcookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.SUGAR)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createGoldenCookieRecipe(): ShapedRecipe {
        val someCookie = OdysseyFood.GOLDEN_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "goldencookie"), someCookie)
        someRecipe.shape("XXX", "XYX", "XXX")
        someRecipe.setIngredient('X', Material.GOLD_INGOT)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createSugaryBreadRecipe(): ShapedRecipe {
        val someResult = OdysseyFood.SUGARY_BREAD.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "sugarybread"), someResult)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.SUGAR)
        someRecipe.setIngredient('Y', Material.BREAD)
        return someRecipe
    }

    private fun createSalmonRollRecipe(): ShapedRecipe {
        val someResult = OdysseyFood.SALMON_ROLL.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "salmonroll"), someResult)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.DRIED_KELP)
        someRecipe.setIngredient('Y', Material.SALMON)
        return someRecipe
    }

    // CAMPFIRE
    private fun createBaconRecipe(): CampfireRecipe {
        val someResult = OdysseyFood.BACON.createItemStack(1)
        return CampfireRecipe(
            NamespacedKey(MinecraftOdyssey.instance, "bacon"),
            someResult, Material.COOKED_PORKCHOP, 1.0F, 10 * 20
        )
    }

    // SMOKING
    private fun createFrenchToastRecipe(): SmokingRecipe {
        val someResult = OdysseyFood.FRENCH_TOAST.createItemStack(1)
        val someExactIngredient = OdysseyFood.SUGARY_BREAD.createItemStack(1)
        return SmokingRecipe(
            NamespacedKey(MinecraftOdyssey.instance, "frenchtoast"),
            someResult, RecipeChoice.ExactChoice(someExactIngredient), 3.5F, 15 * 20
        )
    }


}