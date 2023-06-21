package me.shadowalzazel.mcodyssey.recipe_creators

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Templates
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.SmithingTransformRecipe

class Smithing {

    fun getRecipes(): List<Recipe> {
        return listOf(
            bookCombining(),
            itemBookSmithing()
        )
    }

    // SHAPED
    private fun templateTestRecipe(): ShapedRecipe {
        val someResult = Templates.KATANA_TEMPLATE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "template_test_recipe"), someResult)
        someRecipe.shape("X", "Y", "X")
        someRecipe.setIngredient('X', Material.DRAGON_EGG)
        someRecipe.setIngredient('Y', Material.GOLDEN_SWORD)
        return someRecipe
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    // Tomes
    private fun bookCombining(): SmithingTransformRecipe {
        val result = ItemStack(Material.ENCHANTED_BOOK)
        val template = RecipeChoice.MaterialChoice(Material.ENCHANTED_BOOK)

        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "book_combining"),
            result,
            template,
            RecipeChoice.MaterialChoice(Material.ENCHANTED_BOOK),
            RecipeChoice.MaterialChoice(Material.PRISMARINE_CRYSTALS, Material.GOLD_NUGGET)
        )
    }

    // Tomes
    private fun itemBookSmithing(): SmithingTransformRecipe {
        val result = ItemStack(Material.ENCHANTED_BOOK)
        val template = RecipeChoice.MaterialChoice(Material.ENCHANTED_BOOK)

        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "book_smithing"),
            result,
            template,
            RecipeChoice.MaterialChoice(
                Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
                Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE,
                Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE,
                Material.NETHERITE_SHOVEL, Material.DIAMOND_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL,
                Material.NETHERITE_HOE, Material.DIAMOND_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.STONE_HOE, Material.WOODEN_HOE,
                Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.LEATHER_BOOTS,
                Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.LEATHER_LEGGINGS,
                Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE,
                Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET,
                Material.ELYTRA, Material.SHIELD, Material.BOW, Material.CROSSBOW, Material.TRIDENT, Material.FISHING_ROD,
                Material.SUNFLOWER,
            ),
            RecipeChoice.MaterialChoice(Material.PRISMARINE_CRYSTALS, Material.GOLD_NUGGET)
        )
    }

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    private fun engravingSmithingRecipe(): SmithingTransformRecipe {
        val result = ItemStack(Material.AMETHYST_SHARD)
        val template = RecipeChoice.MaterialChoice(Material.PAPER)

        return SmithingTransformRecipe(
            NamespacedKey(Odyssey.instance, "amethyst_engraving"),
            result,
            template,
            RecipeChoice.MaterialChoice(
                Material.NETHERITE_SWORD, Material.DIAMOND_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD, Material.WOODEN_SWORD,
                Material.NETHERITE_AXE, Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE,
                Material.NETHERITE_PICKAXE, Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE, Material.WOODEN_PICKAXE,
                Material.NETHERITE_SHOVEL, Material.DIAMOND_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL, Material.WOODEN_SHOVEL,
                Material.NETHERITE_HOE, Material.DIAMOND_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.STONE_HOE, Material.WOODEN_HOE,
                Material.NETHERITE_BOOTS, Material.DIAMOND_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS, Material.LEATHER_BOOTS,
                Material.NETHERITE_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.LEATHER_LEGGINGS,
                Material.NETHERITE_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.LEATHER_CHESTPLATE,
                Material.NETHERITE_HELMET, Material.DIAMOND_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.CHAINMAIL_HELMET, Material.LEATHER_HELMET,
                Material.ELYTRA, Material.SHIELD, Material.BOW, Material.CROSSBOW, Material.TRIDENT, Material.FISHING_ROD,
                Material.SUNFLOWER,
            ),
            RecipeChoice.MaterialChoice(Material.AMETHYST_SHARD)
        )
    }


}