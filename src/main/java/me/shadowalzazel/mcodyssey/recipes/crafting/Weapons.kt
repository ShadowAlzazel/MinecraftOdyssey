package me.shadowalzazel.mcodyssey.recipes.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Equipment
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

class Weapons {

    fun getRecipes(): List<ShapedRecipe> {
        return listOf(
            compactCrossbowRecipe(),
            autoCrossbowRecipe(),
            alchemicalBolterRecipe(),
            voidLinkedKunaiRecipe()
        )
    }

    private fun compactCrossbowRecipe(): ShapedRecipe {
        val result = Equipment.COMPACT_CROSSBOW.createItemStack(1)
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

    private fun autoCrossbowRecipe(): ShapedRecipe {
        val result = Equipment.AUTO_CROSSBOW.createItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "auto_crossbow"), result).apply {
            shape("NBN", "STS", "CBC")
            setIngredient('N', Material.NETHERITE_INGOT)
            setIngredient('B', Material.BLAZE_ROD)
            setIngredient('T', Material.TRIPWIRE_HOOK)
            setIngredient('S', Material.STRING)
            setIngredient('C', Material.COPPER_INGOT)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun alchemicalBolterRecipe(): ShapedRecipe {
        val result = Equipment.ALCHEMICAL_BOLTER.createItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "alchemical_bolter"), result).apply {
            shape("QDQ", "STS", "AQG")
            setIngredient('A', Material.DRAGON_BREATH)
            setIngredient('G', Material.TINTED_GLASS)
            setIngredient('D', Material.DIAMOND)
            setIngredient('Q', Material.QUARTZ)
            setIngredient('T', Material.TRIPWIRE_HOOK)
            setIngredient('S', Material.STRING)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }


    private fun voidLinkedKunaiRecipe(): ShapedRecipe {
        val result = Equipment.VOID_LINKED_KUNAI.createItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "void_linked_kunai"), result).apply {
            shape("N", "B", "E")
            setIngredient('N', Material.NETHERITE_INGOT)
            setIngredient('B', Material.BLAZE_ROD)  // TODO :Change to breeze rod
            setIngredient('E', Material.ENDER_EYE)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    /*
    private fun netheriteZweihanderRecipe(): ShapedRecipe {
        val result = Equipment.NETHERITE_ZWEIHANDER.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "netherite_zweihander"), result).apply {
            shape("  X", "XX ", "YX ")
            setIngredient('X', Material.NETHERITE_INGOT)
            setIngredient('Y', Material.BLAZE_ROD)
        }
        return recipe
    }

     */

    /*
    private fun bambooStaffRecipe(): ShapedRecipe {
        val result = Weapons.BAMBOO_STAFF.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "bamboo_staff"), result).apply {
            shape(" Y ", "XYX", " Y ")
            setIngredient('X', Material.RABBIT_HIDE)
            setIngredient('Y', Material.BAMBOO)
            group = "staffs"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun boneStaffRecipe(): ShapedRecipe {
        val result = Weapons.BONE_STAFF.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "bone_staff"), result).apply {
            shape(" Y ", "XYX", " Y ")
            setIngredient('X', Material.RABBIT_HIDE)
            setIngredient('Y', Material.BONE)
            group = "staffs"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun createWoodenStaffRecipe(): ShapedRecipe {
        val result = Weapons.WOODEN_STAFF.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_staff"), result).apply {
            shape(" Y ", "XYX", " Y ")
            setIngredient('X', Material.RABBIT_HIDE)
            setIngredient('Y', Material.STICK)
            group = "staffs"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun blazeRodStaffRecipe(): ShapedRecipe {
        val result = Weapons.BLAZE_ROD_STAFF.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "blaze_rod_staff"), result).apply {
            shape(" Y ", "XYX", " Y ")
            setIngredient('X', Material.RABBIT_HIDE)
            setIngredient('Y', Material.BLAZE_ROD)
            group = "staffs"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

     */

}