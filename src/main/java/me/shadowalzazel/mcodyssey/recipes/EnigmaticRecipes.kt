package me.shadowalzazel.mcodyssey.recipes

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.OdysseyItems
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe

object EnigmaticRecipes {

    // Register Recipes
    fun getRecipes(): List<ShapedRecipe> {
        return listOf(
            createSorrowingSoulRecipe(),
            createSoulCatalystRecipe(),
            createEnigmaticOmamoriRecipe()
        )
    }

    /* ---------------------------------------------------------------------------*/

    private fun createSorrowingSoulRecipe(): ShapedRecipe {
        val someResult = OdysseyItems.SORROWING_SOUL.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "sorrowingsoul"), someResult)
        val exactEctoplasm = OdysseyItems.ECTOPLASM.createItemStack(1)

        someRecipe.shape("XZX", "XYX", "XZX")
        someRecipe.setIngredient('Z', Material.PAPER)
        someRecipe.setIngredient('X', Material.ROTTEN_FLESH)
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactEctoplasm))
        return someRecipe
    }

    private fun createSoulCatalystRecipe(): ShapedRecipe {
        val someResult = OdysseyItems.SOUL_CATALYST.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "soulcatalyst"), someResult)
        val exactSoulCrystal = OdysseyItems.SOUL_CRYSTAL.createItemStack(1)
        val exactEctoplasm = OdysseyItems.ECTOPLASM.createItemStack(1)

        someRecipe.shape("XZX", "ZYZ", "XZX")
        someRecipe.setIngredient('X', Material.TINTED_GLASS)
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactEctoplasm))
        someRecipe.setIngredient('Z', RecipeChoice.ExactChoice(exactSoulCrystal))
        return someRecipe
    }

    private fun createEnigmaticOmamoriRecipe(): ShapedRecipe {
        val someResult = OdysseyItems.ENIGMATIC_OMAMORI.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "enigmaticomamori"), someResult)
        val exactSoulCrystal = OdysseyItems.SOUL_CRYSTAL.createItemStack(1)
        val exactSoulIngot = OdysseyItems.SOUL_STEEL_INGOT.createItemStack(1)

        someRecipe.shape(" W ", "XYX", "XZX")
        someRecipe.setIngredient('Z', RecipeChoice.ExactChoice(exactSoulIngot))
        someRecipe.setIngredient('Y', RecipeChoice.ExactChoice(exactSoulCrystal))
        someRecipe.setIngredient('X', Material.PAPER)
        someRecipe.setIngredient('W', Material.STRING)
        return someRecipe
    }

// Soul Ore
    // 4 crying obsidian
    // 1 ectoplasm
    // 4 soul crystal

    // Liberating Bell
    // If used on skeleton or zombie, kills them instantly


    // TEARS OF THE DAMNED
    // 1 tear
    // 4 ectoplasm


    // SOUL WELL
    // Respawn anchor
    // 5 soul crystal
    // 3 ectoplasm
    // -> Do not drop EXP
    //


    // IDEAS: Store EXP, drop EXP, prevent drops, prevent reinforcements
    //


}