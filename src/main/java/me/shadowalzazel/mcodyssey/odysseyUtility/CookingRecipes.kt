package me.shadowalzazel.mcodyssey.odysseyUtility

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
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
    private val SUGARY_BREAD_RECIPE: ShapedRecipe = createSugaryBreadRecipe()

    private val SALMON_ROLL_RECIPE: ShapedRecipe = createSalmonRollRecipe()
    // Campfire Recipes
    private val BACON_RECIPE: CampfireRecipe = createBaconRecipe()
    // Smoking Recipes
    private val FRENCH_TOAST_RECIPE: SmokingRecipe = createFrenchToastRecipe()

    val recipeSet = setOf<Recipe>(BEETROOT_COOKIE_RECIPE, PUMPKIN_COOKIE_RECIPE, HONEY_COOKIE_RECIPE, APPLE_COOKIE_RECIPE, SUGARY_BREAD_RECIPE, SALMON_ROLL_RECIPE, BACON_RECIPE, FRENCH_TOAST_RECIPE)

    // make function calls on init later

    // SHAPED
    private fun createBeetrootCookieRecipe(): ShapedRecipe {
        val someResult = CookingItems.BEETROOT_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "beetrootcookie"), someResult)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.BEETROOT)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createPumpkinCookieRecipe(): ShapedRecipe {
        val someCookie = CookingItems.PUMPKIN_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "pumpkincookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.PUMPKIN_SEEDS)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createHoneyCookieRecipe(): ShapedRecipe {
        val someCookie = CookingItems.HONEY_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "honeycookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.HONEY_BOTTLE)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createAppleCookieRecipe(): ShapedRecipe {
        val someCookie = CookingItems.APPLE_COOKIE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "applecookie"), someCookie)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.APPLE)
        someRecipe.setIngredient('Y', Material.COOKIE)
        return someRecipe
    }

    private fun createSugaryBreadRecipe(): ShapedRecipe {
        val someResult = CookingItems.SUGARY_BREAD.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "sugarybread"), someResult)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.SUGAR)
        someRecipe.setIngredient('Y', Material.BREAD)
        return someRecipe
    }

    private fun createSalmonRollRecipe(): ShapedRecipe {
        val someResult = CookingItems.SALMON_ROLL.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "salmonroll"), someResult)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.DRIED_KELP)
        someRecipe.setIngredient('Y', Material.SALMON)
        return someRecipe
    }

    // CAMPFIRE
    private fun createBaconRecipe(): CampfireRecipe {
        val someResult = CookingItems.BACON.createItemStack(1)
        val someRecipe = CampfireRecipe(NamespacedKey(MinecraftOdyssey.instance, "bacon"),
            someResult, Material.COOKED_PORKCHOP, 1.0F, 10 * 20)
        return someRecipe
    }

    // SMOKING
    private fun createFrenchToastRecipe(): SmokingRecipe {
        val someResult = CookingItems.FRENCH_TOAST.createItemStack(1)
        val someExactIngredient = CookingItems.SUGARY_BREAD.createItemStack(1)
        val someRecipe = SmokingRecipe(NamespacedKey(MinecraftOdyssey.instance, "frenchtoast"),
            someResult, RecipeChoice.ExactChoice(someExactIngredient), 3.5F, 15 * 20)
        return someRecipe
    }


}