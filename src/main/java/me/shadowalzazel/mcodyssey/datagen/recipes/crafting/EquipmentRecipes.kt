package me.shadowalzazel.mcodyssey.datagen.recipes.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.items.Item
import me.shadowalzazel.mcodyssey.datagen.ChoiceManager
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

class EquipmentRecipes : ChoiceManager {

    fun getRecipes(): List<Recipe> {
        return listOf(
            chainHookRecipe(), tinkeredMusketRecipe(), tinkeredBowRecipe(), autoCrossbowRecipe(), warpingWandRecipe(),
            arcaneWandRecipe(), arcaneBladeRecipe(), arcaneScepterRecipe(), explosiveArrowRecipe(), alchemicalDriverRecipe(),
            arcanePenRecipe(), arcaneStylusRecipe(), arcanePointyHatRecipe(), arcaneWitchHatRecipe(),
            compactCrossbowRecipe(), voidLinkedKunaiRecipe(), alchemicalDiffuserRecipe()
        )
    }

    private fun chainHookRecipe(): ShapedRecipe {
        val result = Item.CHAIN_HOOK.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "chain_hook"), result).apply {
            shape("CXC", "STS", " L ")
            setIngredient('X', titaniumChoices())
            setIngredient('C', Material.COPPER_INGOT)
            setIngredient('S', Material.STRING)
            setIngredient('T', Material.CHAIN)
            setIngredient('L', Material.STICK)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun tinkeredMusketRecipe(): ShapedRecipe {
        val result = Item.TINKERED_MUSKET.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "tinkered_musket"), result).apply {
            shape("TTH", "  T", "  C")
            setIngredient('T', titaniumChoices())
            setIngredient('H', Material.HEAVY_CORE)
            setIngredient('C', Material.COPPER_INGOT)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun tinkeredBowRecipe(): ShapedRecipe {
        val result = Item.TINKERED_BOW.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "tinkered_bow"), result).apply {
            shape(" TS", "TCS", " TS")
            setIngredient('T', titaniumChoices())
            setIngredient('S', Material.STRING)
            setIngredient('C', Material.COPPER_INGOT)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun autoCrossbowRecipe(): ShapedRecipe {
        val result = Item.AUTO_CROSSBOW.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "auto_crossbow"), result).apply {
            shape("NHN", "SBS", "CBC")
            setIngredient('N', Material.NETHERITE_INGOT)
            setIngredient('B', Material.BLAZE_ROD)
            setIngredient('H', Material.HEAVY_CORE)
            setIngredient('S', Material.STRING)
            setIngredient('C', Material.COPPER_INGOT)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun compactCrossbowRecipe(): ShapedRecipe {
        val result = Item.COMPACT_CROSSBOW.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "compact_crossbow"), result).apply {
            shape(" C ", "STS", " L ")
            setIngredient('C', Material.CRIMSON_PLANKS)
            setIngredient('S', Material.STRING)
            setIngredient('T', Material.TRIPWIRE_HOOK)
            setIngredient('L', Material.STICK)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun alchemicalDriverRecipe(): ShapedRecipe {
        val result = Item.ALCHEMICAL_DRIVER.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "alchemical_driver"), result).apply {
            shape("A", "X", "Q")
            setIngredient('A', silverChoices())
            setIngredient('Q', Material.DRAGON_BREATH)
            setIngredient('X', Material.CROSSBOW)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun alchemicalDiffuserRecipe(): ShapedRecipe {
        val result = Item.ALCHEMICAL_DIFFUSER.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "alchemical_diffuser"), result).apply {
            shape(" D ", "QXQ", " A ")
            setIngredient('A', silverChoices())
            setIngredient('D', Material.DIAMOND)
            setIngredient('Q', Material.DRAGON_BREATH)
            setIngredient('X', Material.CROSSBOW)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun explosiveArrowRecipe(): ShapedRecipe {
        val result = Item.EXPLOSIVE_ARROW.newItemStack(2)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "explosive_arrow"), result).apply {
            shape("G", "A")
            setIngredient('G', Material.GUNPOWDER)
            setIngredient('A', Material.ARROW)
            group = "arrows"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun arcaneWitchHatRecipe(): ShapedRecipe {
        val result = Item.ARCANE_WITCH_HAT.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "arcane_witch_hat"), result).apply {
            shape(" L ", "LAL")
            setIngredient('A', Item.CRYSTAL_ALLOY_INGOT.toRecipeChoice())
            setIngredient('L', Material.RABBIT_HIDE)
            category = CraftingBookCategory.EQUIPMENT

        }
        return recipe
    }

    private fun arcanePointyHatRecipe(): ShapedRecipe {
        val result = Item.ARCANE_POINTY_HAT.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "arcane_pointy_hat"), result).apply {
            shape("G", "L", "A")
            setIngredient('A', Item.CRYSTAL_ALLOY_INGOT.toRecipeChoice())
            setIngredient('L', Material.RABBIT_HIDE)
            setIngredient('G', Material.GOLD_NUGGET)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun arcaneWandRecipe(): ShapedRecipe {
        val result = Item.ARCANE_WAND.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "arcane_wand"), result).apply {
            shape("A", "S")
            setIngredient('A', Item.CRYSTAL_ALLOY_INGOT.toRecipeChoice())
            setIngredient('S', Material.STICK)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun arcaneBladeRecipe(): ShapedRecipe {
        val result = Item.ARCANE_BLADE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "arcane_blade"), result).apply {
            shape("A A", "AQA", " S ")
            setIngredient('A', Item.CRYSTAL_ALLOY_INGOT.toRecipeChoice())
            setIngredient('Q', Material.GOLD_INGOT)
            setIngredient('S', Material.STICK)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun arcaneScepterRecipe(): ShapedRecipe {
        val result = Item.ARCANE_SCEPTER.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "arcane_scepter"), result).apply {
            shape("QQQ", "ASA", " S ")
            setIngredient('A', Item.CRYSTAL_ALLOY_INGOT.toRecipeChoice())
            setIngredient('Q', Material.AMETHYST_SHARD)
            setIngredient('S', Material.STICK)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }


    private fun arcaneStylusRecipe(): ShapedRecipe {
        val result = Item.ARCANE_STYLUS.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "arcane_stylus"), result).apply {
            shape(" R ", " X ", "QAQ")
            setIngredient('R', Material.RABBIT_HIDE)
            setIngredient('Q', Material.RESIN_CLUMP)
            setIngredient('X', Material.STICK)
            setIngredient('A', Item.CRYSTAL_ALLOY_INGOT.toRecipeChoice())
        }
        return recipe
    }

    private fun arcanePenRecipe(): ShapedRecipe {
        val result = Item.ARCANE_PEN.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "arcane_pen"), result).apply {
            shape("SR", "X ", "A ")
            setIngredient('R', Material.RABBIT_HIDE)
            setIngredient('S', Item.SILVER_NUGGET.toRecipeChoice())
            setIngredient('X', Material.STICK)
            setIngredient('A', Item.CRYSTAL_ALLOY_INGOT.toRecipeChoice())
        }
        return recipe
    }


    private fun warpingWandRecipe(): ShapedRecipe {
        val result = Item.WARPING_WAND.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "warping_wand"), result).apply {
            shape("W", "V", "V")
            setIngredient('W', Material.WARPED_FUNGUS)
            setIngredient('V', Material.TWISTING_VINES)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }


    private fun voidLinkedKunaiRecipe(): ShapedRecipe {
        val result = Item.VOID_LINKED_KUNAI.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "void_linked_kunai"), result).apply {
            shape("N", "B", "E")
            setIngredient('N', Material.NETHERITE_INGOT)
            setIngredient('B', Material.BREEZE_ROD)
            setIngredient('E', Material.ENDER_EYE)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }


}