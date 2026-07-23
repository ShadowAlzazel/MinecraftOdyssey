package me.shadowalzazel.mcodyssey.datagen.recipes.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.items.Item
import me.shadowalzazel.mcodyssey.datagen.ChoiceManager
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ItemType
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

@Suppress("UnstableApiUsage")
class ItemRecipes : ChoiceManager {

    fun getRecipes(): List<Recipe> {
        return listOf(
            // Odyssey
            soulSteelUpgradeTemplateRecipe(), titaniumUpgradeTemplateRecipe(), iridiumUpgradeTemplateRecipe(),
            silverIngotRecipe(), silverNuggetRecipe(), iridiumIngotRecipe(), iridiumNuggetRecipe(),
            mithrilUpgradeTemplateRecipe(), blankTomeRecipe(), clayTotemRecipe(), clayOrbRecipe(), claySkullRecipe(), clayDowelRecipe(),
            clayKeyRecipe(), clayRodsRecipe(), scrollRecipe(),
            imperialTrimReplicateRecipe(), voyagerTrimReplicateRecipe(), dangerTrimReplicateRecipe(), leafTrimReplicateRecipe(), ringTrimReplicateRecipe(),
            pommelPartUpgradeTemplateRecipe(), hiltPartUpgradeTemplateRecipe(), bladePartUpgradeTemplateRecipe(), handlePartUpgradeTemplateRecipe(),
            mastercraftedToolTemplateRecipe(), crystalAlloyIngotRecipe(), crystalAlloyUpgradeTemplateRecipe(),
            // New Recipes for Vanilla
            sulfurGunpowderRecipe(), sulfurRecipe(), crystallineCompostRecipe(), prismaticNameTagRecipe()
        )
    }

    // Smithing
    private fun soulSteelUpgradeTemplateRecipe(): ShapedRecipe {
        val result = Item.SOUL_STEEL_UPGRADE_TEMPLATE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "soul_steel_upgrade_template"), result).apply {
            shape("SES", "SCS", "SSS")
            setIngredient('C', Item.SOUL_QUARTZ.toRecipeChoice())
            setIngredient('E', Item.ECTOPLASM.toRecipeChoice())
            setIngredient('S', Material.SOUL_SAND)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun titaniumUpgradeTemplateRecipe(): ShapedRecipe {
        val result = Item.TITANIUM_UPGRADE_TEMPLATE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "titanium_upgrade_template"), result).apply {
            shape("SES", "SCS", "SSS")
            setIngredient('C', Material.IRON_INGOT)
            setIngredient('E', Material.FLINT)
            setIngredient('S', Material.TUFF)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun iridiumUpgradeTemplateRecipe(): ShapedRecipe {
        val result = Item.IRIDIUM_UPGRADE_TEMPLATE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iridium_upgrade_template"), result).apply {
            shape("SES", "SCS", "SSS")
            setIngredient('C', Material.BREEZE_ROD)
            setIngredient('E', Material.AMETHYST_SHARD)
            setIngredient('S', Material.OBSIDIAN)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun mithrilUpgradeTemplateRecipe(): ShapedRecipe {
        val result = Item.MITHRIL_UPGRADE_TEMPLATE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "mithril_upgrade_template"), result).apply {
            shape("SES", "SCS", "SSS")
            setIngredient('C', silverChoices())
            setIngredient('E', Material.DIAMOND)
            setIngredient('S', Material.COBBLED_DEEPSLATE)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun silverIngotRecipe(): ShapedRecipe {
        val result = Item.SILVER_INGOT.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "silver_ingot"), result).apply {
            shape("NNN", "NNN", "NNN")
            setIngredient('N', Item.SILVER_NUGGET.toRecipeChoice())
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun silverNuggetRecipe(): ShapelessRecipe {
        val result = Item.SILVER_NUGGET.newItemStack(9)
        val recipe = ShapelessRecipe(NamespacedKey(Odyssey.instance, "silver_nugget"), result).apply {
            addIngredient(Item.SILVER_INGOT.newItemStack(1))
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun iridiumIngotRecipe(): ShapedRecipe {
        val result = Item.IRIDIUM_INGOT.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iridium_ingot"), result).apply {
            shape("NNN", "NNN", "NNN")
            setIngredient('N', Item.IRIDIUM_NUGGET.toRecipeChoice())
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun iridiumNuggetRecipe(): ShapelessRecipe {
        val result = Item.IRIDIUM_NUGGET.newItemStack(9)
        val recipe = ShapelessRecipe(NamespacedKey(Odyssey.instance, "iridium_nugget"), result).apply {
            addIngredient(Item.IRIDIUM_INGOT.newItemStack(1))
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    // Trims
    private fun imperialTrimReplicateRecipe(): ShapedRecipe {
        val result = Item.IMPERIAL_ARMOR_TRIM_SMITHING_TEMPLATE.newItemStack(2)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "imperial_armor_trim_smithing_template_replicate"), result).apply {
            shape("DTD", "DXD", "DDD")
            setIngredient('D', Material.DIAMOND)
            setIngredient('X', Material.TUFF_BRICKS)
            setIngredient('T', Item.IMPERIAL_ARMOR_TRIM_SMITHING_TEMPLATE.toRecipeChoice())
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun voyagerTrimReplicateRecipe(): ShapedRecipe {
        val result = Item.VOYAGER_ARMOR_TRIM_SMITHING_TEMPLATE.newItemStack(2)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "voyager_armor_trim_smithing_template_replicate"), result).apply {
            shape("DTD", "DXD", "DDD")
            setIngredient('D', Material.DIAMOND)
            setIngredient('X', Material.COBBLED_DEEPSLATE)
            setIngredient('T', Item.VOYAGER_ARMOR_TRIM_SMITHING_TEMPLATE.toRecipeChoice())
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun ringTrimReplicateRecipe(): ShapedRecipe {
        val result = Item.RING_ARMOR_TRIM_SMITHING_TEMPLATE.newItemStack(2)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "ring_armor_trim_smithing_template_replicate"), result).apply {
            shape("DTD", "DXD", "DDD")
            setIngredient('D', Material.DIAMOND)
            setIngredient('X', Material.COPPER_BLOCK)
            setIngredient('T', Item.RING_ARMOR_TRIM_SMITHING_TEMPLATE.toRecipeChoice())
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun leafTrimReplicateRecipe(): ShapedRecipe {
        val result = Item.LEAF_ARMOR_TRIM_SMITHING_TEMPLATE.newItemStack(2)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "leaf_armor_trim_smithing_template_replicate"), result).apply {
            shape("DTD", "DXD", "DDD")
            setIngredient('D', Material.DIAMOND)
            setIngredient('X', Material.PACKED_MUD)
            setIngredient('T', Item.LEAF_ARMOR_TRIM_SMITHING_TEMPLATE.toRecipeChoice())
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun dangerTrimReplicateRecipe(): ShapedRecipe {
        val result = Item.DANGER_ARMOR_TRIM_SMITHING_TEMPLATE.newItemStack(2)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "danger_armor_trim_smithing_template_replicate"), result).apply {
            shape("DTD", "DXD", "DDD")
            setIngredient('D', Material.DIAMOND)
            setIngredient('X', Material.MOSSY_COBBLESTONE)
            setIngredient('T', Item.DANGER_ARMOR_TRIM_SMITHING_TEMPLATE.toRecipeChoice())
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    // Enchanting
    private fun blankTomeRecipe(): ShapedRecipe {
        val result = Item.BLANK_TOME.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "blank_tome"), result).apply {
            shape(" C ", "ABA", " C ")
            setIngredient('A', Material.AMETHYST_SHARD)
            setIngredient('B', Material.BOOK)
            setIngredient('C', Material.PRISMARINE_CRYSTALS)
        }
        return recipe
    }

    private fun scrollRecipe(): ShapedRecipe {
        val result = Item.SCROLL.newItemStack(2)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "scroll"), result).apply {
            shape("XC", "CX")
            setIngredient('X', Material.PAPER)
            setIngredient('C', Material.STICK)
        }
        return recipe
    }

    // Glyphic
    private fun clayOrbRecipe(): ShapedRecipe {
        val result = Item.CLAY_ORB.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_orb"), result).apply {
            shape(" C ", "CFC", " C ")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('F', Material.FLINT)
        }
        return recipe
    }

    private fun clayTotemRecipe(): ShapedRecipe {
        val result = Item.CLAY_TOTEM.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_totem"), result).apply {
            shape(" L ", "CCC", " C ")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('L', Material.LAPIS_LAZULI)
        }
        return recipe
    }

    private fun claySkullRecipe(): ShapedRecipe {
        val result = Item.CLAY_SKULL.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_skull"), result).apply {
            shape("   ", "LCL", "CCC")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('L', Material.LAPIS_LAZULI)
        }
        return recipe
    }

    private fun clayDowelRecipe(): ShapedRecipe {
        val result = Item.CLAY_DOWEL.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_dowel"), result).apply {
            shape("  C", " L ", "C  ")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('L', Material.LAPIS_LAZULI)
        }
        return recipe
    }

    private fun clayRodsRecipe(): ShapedRecipe {
        val result = Item.CLAY_RODS.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_rods"), result).apply {
            shape("F F", "CFC", " C ")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('F', Material.FLINT)
        }
        return recipe
    }

    private fun clayKeyRecipe(): ShapedRecipe {
        val result = Item.CLAY_KEY.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "clay_key"), result).apply {
            shape("L ", "C ", "CC")
            setIngredient('C', Material.CLAY_BALL)
            setIngredient('L', Material.LAPIS_LAZULI)
        }
        return recipe
    }

    private fun bladePartUpgradeTemplateRecipe(): ShapedRecipe {
        val result = Item.BLADE_PART_UPGRADE_TEMPLATE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "blade_part_upgrade_template"), result).apply {
            shape("GGX", "GXG", "GGG")
            setIngredient('G', Material.GOLD_INGOT)
            setIngredient('X', Material.LAPIS_LAZULI)
            category = CraftingBookCategory.MISC
            group = "part_upgrade_templates"
        }
        return recipe
    }

    private fun handlePartUpgradeTemplateRecipe(): ShapedRecipe {
        val result = Item.HANDLE_PART_UPGRADE_TEMPLATE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "handle_part_upgrade_template"), result).apply {
            shape("GGG", "GXG", "GXG")
            setIngredient('G', Material.GOLD_INGOT)
            setIngredient('X', Material.LAPIS_LAZULI)
            category = CraftingBookCategory.MISC
            group = "part_upgrade_templates"
        }
        return recipe
    }

    private fun pommelPartUpgradeTemplateRecipe(): ShapedRecipe {
        val result = Item.POMMEL_PART_UPGRADE_TEMPLATE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "pommel_part_upgrade_template"), result).apply {
            shape("GGG", "GXG", "XGG")
            setIngredient('G', Material.GOLD_INGOT)
            setIngredient('X', Material.LAPIS_LAZULI)
            category = CraftingBookCategory.MISC
            group = "part_upgrade_templates"
        }
        return recipe
    }

    private fun hiltPartUpgradeTemplateRecipe(): ShapedRecipe {
        val result = Item.HILT_PART_UPGRADE_TEMPLATE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "hilt_part_upgrade_template"), result).apply {
            shape("GGG", "XGX", "GGG")
            setIngredient('G', Material.GOLD_INGOT)
            setIngredient('X', Material.LAPIS_LAZULI)
            category = CraftingBookCategory.MISC
            group = "part_upgrade_templates"
        }
        return recipe
    }

    private fun crystalAlloyIngotRecipe(): ShapedRecipe {
        val result = Item.CRYSTAL_ALLOY_INGOT.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "crystal_alloy_ingot"), result).apply {
            shape("DG", "GX")
            setIngredient('G', Material.GOLD_INGOT)
            setIngredient('D', Material.DIAMOND)
            setIngredient('X', Material.AMETHYST_SHARD)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun crystalAlloyUpgradeTemplateRecipe(): ShapedRecipe {
        val result = Item.CRYSTAL_ALLOY_UPGRADE_TEMPLATE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "crystal_alloy_upgrade_template"), result).apply {
            shape("SES", "SCS", "SSS")
            setIngredient('C', Material.LAPIS_LAZULI)
            setIngredient('E', Material.AMETHYST_SHARD)
            setIngredient('S', Material.COBBLESTONE)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun mastercraftedToolTemplateRecipe(): ShapedRecipe {
        val result = Item.MASTERCRAFTED_TOOL_TEMPLATE.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "mastercrafted_tool_template"), result).apply {
            shape(" G ", "GIG", " G ")
            setIngredient('G', Material.GOLD_INGOT)
            setIngredient('I', iridiumChoices())
            category = CraftingBookCategory.MISC
        }
        return recipe
    }

    private fun sulfurGunpowderRecipe(): ShapelessRecipe {
        val result = ItemStack(Material.GUNPOWDER, 4)
        return ShapelessRecipe(NamespacedKey(Odyssey.instance, "gunpowder_from_sulfur"), result).apply {
            addIngredient(Item.SULFUR_POWDER.toRecipeChoice())
            addIngredient(Material.CHARCOAL)
            addIngredient(RecipeChoice.itemType(ItemType.SUGAR))
            category = CraftingBookCategory.MISC
        }
    }

    private fun sulfurRecipe(): ShapelessRecipe {
        val result = Item.SULFUR_POWDER.newItemStack(2)
        return ShapelessRecipe(NamespacedKey(Odyssey.instance, "sulfur_powder"), result).apply {
            addIngredient(Material.SULFUR_SPIKE)
            category = CraftingBookCategory.MISC
        }
    }

    private fun crystallineCompostRecipe(): ShapelessRecipe {
        val result = Item.CRYSTALLINE_COMPOST.newItemStack(4)
        return ShapelessRecipe(NamespacedKey(Odyssey.instance, "crystalline_compost"), result).apply {
            addIngredient(RecipeChoice.itemType(ItemType.BONE_MEAL))
            addIngredient(RecipeChoice.itemType(ItemType.AMETHYST_SHARD))
            addIngredient(Item.SULFUR_POWDER.toRecipeChoice())
            category = CraftingBookCategory.MISC
        }
    }

    private fun prismaticNameTagRecipe(): ShapedRecipe {
        val result = Item.PRISMATIC_NAME_TAG.newItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "prismatic_name_tag"), result).apply {
            shape(" S ", "SCS", " S ")
            setIngredient('C', Material.NAME_TAG)
            setIngredient('S', Material.AMETHYST_SHARD)
            category = CraftingBookCategory.MISC
        }
        return recipe
    }


}