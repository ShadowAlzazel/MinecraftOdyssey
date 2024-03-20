package me.shadowalzazel.mcodyssey.recipe_creators.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Equipment
import me.shadowalzazel.mcodyssey.items.Weapons
import me.shadowalzazel.mcodyssey.items.Weapons.createWeapon
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

class Weapons {

    fun getRecipes(): List<ShapedRecipe> {
        return listOf(
            compactCrossbowRecipe(),
            autoCrossbowRecipe(),
            alchemicalBolterRecipe(),

            woodenKatanaRecipe(),
            goldenKatanaRecipe(),
            stoneKatanaRecipe(),
            ironKatanaRecipe(),
            diamondKatanaRecipe(),

            woodenClaymoreRecipe(),
            goldenClaymoreRecipe(),
            stoneClaymoreRecipe(),
            ironClaymoreRecipe(),
            diamondClaymoreRecipe(),

            woodenSpearRecipe(),
            goldenSpearRecipe(),
            stoneSpearRecipe(),
            ironSpearRecipe(),
            diamondSpearRecipe(),

            woodenDaggerRecipe(),
            goldenDaggerRecipe(),
            stoneDaggerRecipe(),
            ironDaggerRecipe(),
            diamondDaggerRecipe(),

            woodenSickleRecipe(),
            goldenSickleRecipe(),
            stoneSickleRecipe(),
            ironSickleRecipe(),
            diamondSickleRecipe(),

            woodenChakramRecipe(),
            goldenChakramRecipe(),
            stoneChakramRecipe(),
            ironChakramRecipe(),
            diamondChakramRecipe(),

            woodenRapierRecipe(),
            goldenRapierRecipe(),
            stoneRapierRecipe(),
            ironRapierRecipe(),
            diamondRapierRecipe(),

            woodenCutlassRecipe(),
            goldenCutlassRecipe(),
            stoneCutlassRecipe(),
            ironCutlassRecipe(),
            diamondCutlassRecipe(),

            woodenSaberRecipe(),
            goldenSaberRecipe(),
            stoneSaberRecipe(),
            ironSaberRecipe(),
            diamondSaberRecipe(),

            woodenHalberdRecipe(),
            goldenHalberdRecipe(),
            stoneHalberdRecipe(),
            ironHalberdRecipe(),
            diamondHalberdRecipe(),

            woodenLanceRecipe(),
            goldenLanceRecipe(),
            stoneLanceRecipe(),
            ironLanceRecipe(),
            diamondLanceRecipe(),

            woodenWarhammerRecipe(),
            goldenWarhammerRecipe(),
            stoneWarhammerRecipe(),
            ironWarhammerRecipe(),
            diamondWarhammerRecipe(),

            woodenScytheRecipe(),
            goldenScytheRecipe(),
            stoneScytheRecipe(),
            ironScytheRecipe(),
            diamondScytheRecipe(),

            woodenLongAxeRecipe(),
            goldenLongAxeRecipe(),
            stoneLongAxeRecipe(),
            ironLongAxeRecipe(),
            diamondLongAxeRecipe(),

            bambooStaffRecipe(),
            boneStaffRecipe(),
            createWoodenStaffRecipe(),
            blazeRodStaffRecipe()
        )
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun compactCrossbowRecipe(): ShapedRecipe {
        val result = Weapons.COMPACT_CROSSBOW.createItemStack(1)
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
        val result = Weapons.AUTO_CROSSBOW.createItemStack(1)
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
        val result = Weapons.ALCHEMICAL_BOLTER.createItemStack(1)
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "alchemical_bolter"), result).apply {
            shape("AGD", "GTQ", "BQS")
            setIngredient('A', Material.DRAGON_BREATH)
            setIngredient('G', Material.TINTED_GLASS)
            setIngredient('D', Material.DIAMOND)
            setIngredient('Q', Material.QUARTZ)
            setIngredient('T', Material.TRIPWIRE_HOOK)
            setIngredient('S', Material.STRING)
            setIngredient('B', Material.BLAZE_POWDER)
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun woodenKatanaRecipe(): ShapedRecipe {
        val result = Weapons.WOODEN_KATANA.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_katana_crafting"), result).apply {
            shape("  X", " X ", "YZ ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.COPPER_INGOT)
            group = "katanas"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun goldenKatanaRecipe(): ShapedRecipe {
        val result = Weapons.GOLDEN_KATANA.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_katana_crafting"), result).apply {
            shape("  X", " X ", "YZ ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.COPPER_INGOT)
            group = "katanas"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun stoneKatanaRecipe(): ShapedRecipe {
        val result = Weapons.STONE_KATANA.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_katana_crafting"), result).apply {
            shape("  X", " X ", "YZ ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.COPPER_INGOT)
            group = "katanas"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun ironKatanaRecipe(): ShapedRecipe {
        val result = Weapons.IRON_KATANA.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_katana_crafting"), result).apply {
            shape("  X", " X ", "YZ ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.COPPER_INGOT)
            group = "katanas"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun diamondKatanaRecipe(): ShapedRecipe {
        val result = Weapons.DIAMOND_KATANA.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_katana_crafting"), result).apply {
            shape("  X", " X ", "YZ ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.COPPER_INGOT)
            group = "katanas"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun woodenClaymoreRecipe(): ShapedRecipe {
        val result = Weapons.WOODEN_CLAYMORE.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_claymore_crafting"), result).apply {
            shape(" X ", "XXX", " Y ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            group = "claymores"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun goldenClaymoreRecipe(): ShapedRecipe {
        val result = Weapons.GOLDEN_CLAYMORE.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_claymore_crafting"), result).apply {
            shape(" X ", "XXX", " Y ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.GOLD_INGOT))
            setIngredient('Y', Material.STICK)
            group = "claymores"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun stoneClaymoreRecipe(): ShapedRecipe {
        val result = Weapons.STONE_CLAYMORE.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_claymore_crafting"), result).apply {
            shape(" X ", "XXX", " Y ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.COBBLESTONE))
            setIngredient('Y', Material.STICK)
            group = "claymores"
            category = CraftingBookCategory.EQUIPMENT

        }
        return recipe
    }

    private fun ironClaymoreRecipe(): ShapedRecipe {
        val result = Weapons.IRON_CLAYMORE.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_claymore_crafting"), result).apply {
            shape(" X ", "XXX", " Y ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.IRON_INGOT))
            setIngredient('Y', Material.STICK)
            group = "claymores"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun diamondClaymoreRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_CLAYMORE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_claymore_crafting"), someResult).apply {
            shape(" X ", "XXX", " Y ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.DIAMOND))
            setIngredient('Y', Material.STICK)
            group = "claymores"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun woodenSpearRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_SPEAR.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_spear_crafting"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            group = "spears"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun goldenSpearRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_SPEAR.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_spear_crafting"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
            group = "spears"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun stoneSpearRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_SPEAR.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_spear_crafting"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
            group = "spears"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun ironSpearRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_SPEAR.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_spear_crafting"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            group = "spears"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun diamondSpearRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_SPEAR.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_spear_crafting"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
            group = "spears"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun woodenDaggerRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_DAGGER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_dagger_crafting"), someResult).apply {
            shape(" X", "Y ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            group = "daggers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun goldenDaggerRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_DAGGER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_dagger_crafting"), someResult).apply {
            shape(" X", "Y ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
            group = "daggers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun stoneDaggerRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_DAGGER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_dagger_crafting"), someResult).apply {
            shape(" X", "Y ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
            group = "daggers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun ironDaggerRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_DAGGER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_dagger_crafting"), someResult).apply {
            shape(" X", "Y ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            group = "daggers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun diamondDaggerRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_DAGGER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_dagger_crafting"), someResult).apply {
            shape(" X", "Y ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
            group = "daggers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun woodenSickleRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_SICKLE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_sickle_crafting"), someResult).apply {
            shape("XX", "Y ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            group = "sickles"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun goldenSickleRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_SICKLE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_sickle_crafting"), someResult).apply {
            shape("XX", "Y ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
            group = "sickles"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun stoneSickleRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_SICKLE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_sickle_crafting"), someResult).apply {
            shape("XX", "Y ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
            group = "sickles"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun ironSickleRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_SICKLE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_sickle_crafting"), someResult).apply {
            shape("XX", "Y ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            group = "sickles"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun diamondSickleRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_SICKLE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_sickle_crafting"), someResult).apply {
            shape("XX", "Y ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
            group = "sickles"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun woodenChakramRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_CHAKRAM.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_chakram_crafting"), someResult).apply {
            shape("XZ", "YX")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            setIngredient('Y', Material.RABBIT_HIDE)
            group = "chakrams"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun goldenChakramRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_CHAKRAM.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_chakram_crafting"), someResult).apply {
            shape("XZ", "YX")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Y', Material.RABBIT_HIDE)
            group = "chakrams"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun stoneChakramRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_CHAKRAM.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_chakram_crafting"), someResult).apply {
            shape("XZ", "YX")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
            setIngredient('Y', Material.RABBIT_HIDE)
            group = "chakrams"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun ironChakramRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_CHAKRAM.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_chakram_crafting"), someResult).apply {
            shape("XZ", "YX")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Y', Material.RABBIT_HIDE)
            group = "chakrams"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun diamondChakramRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_CHAKRAM.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_chakram_crafting"), someResult).apply {
            shape("XZ", "YX")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
            setIngredient('Y', Material.RABBIT_HIDE)
            group = "chakrams"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun woodenRapierRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_RAPIER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_rapier_crafting"), someResult).apply {
            shape("  X", " X ", "Y  ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            group = "rapiers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun goldenRapierRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_RAPIER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_rapier_crafting"), someResult).apply {
            shape("  X", " X ", "Y  ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
            group = "rapiers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun stoneRapierRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_RAPIER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_rapier_crafting"), someResult).apply {
            shape("  X", " X ", "Y  ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
            group = "rapiers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun ironRapierRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_RAPIER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_rapier_crafting"), someResult).apply {
            shape("  X", " X ", "Y  ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            group = "rapiers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun diamondRapierRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_RAPIER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_rapier_crafting"), someResult).apply {
            shape("  X", " X ", "Y  ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
            group = "rapiers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }


    /*-----------------------------------------------------------------------------------------------*/

    private fun woodenCutlassRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_CUTLASS.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_cutlass_crafting"), someResult).apply {
            shape(" X ", " X ", "ZY ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.COPPER_INGOT)
            group = "cutlasses"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun goldenCutlassRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_CUTLASS.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_cutlass_crafting"), someResult).apply {
            shape(" X ", " X ", "ZY ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.COPPER_INGOT)
            group = "cutlasses"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun stoneCutlassRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_CUTLASS.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_cutlass_crafting"), someResult).apply {
            shape(" X ", " X ", "ZY ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.COPPER_INGOT)
            group = "cutlasses"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }
    private fun ironCutlassRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_CUTLASS.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_cutlass_crafting"), someResult).apply {
            shape(" X ", " X ", "ZY ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.COPPER_INGOT)
            group = "cutlasses"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun diamondCutlassRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_CUTLASS.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_cutlass_crafting"), someResult).apply {
            shape(" X ", " X ", "ZY ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.COPPER_INGOT)
            group = "cutlasses"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun woodenSaberRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_SABER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_saber_crafting"), someResult).apply {
            shape("X  ", " X ", " Y ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            group = "sabers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun goldenSaberRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_SABER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_saber_crafting"), someResult).apply {
            shape("X  ", " X ", " Y ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
            group = "sabers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun stoneSaberRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_SABER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_saber_crafting"), someResult).apply {
            shape("X  ", " X ", " Y ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
            group = "sabers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }
    private fun ironSaberRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_SABER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_saber_crafting"), someResult).apply {
            shape("X  ", " X ", " Y ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            group = "sabers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun diamondSaberRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_SABER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_saber_crafting"), someResult).apply {
            shape("X  ", " X ", " Y ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
            group = "sabers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun woodenHalberdRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_HALBERD.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_halberd_crafting"), someResult).apply {
            shape(" XZ", " Y ", "Y  ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.PRISMARINE_SHARD)
            group = "halberds"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun goldenHalberdRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_HALBERD.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_halberd_crafting"), someResult).apply {
            shape(" XZ", " Y ", "Y  ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.PRISMARINE_SHARD)
            group = "halberds"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun stoneHalberdRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_HALBERD.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_halberd_crafting"), someResult).apply {
            shape(" XZ", " Y ", "Y  ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.PRISMARINE_SHARD)
            group = "halberds"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun ironHalberdRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_HALBERD.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_halberd_crafting"), someResult).apply {
            shape(" XZ", " Y ", "Y  ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.PRISMARINE_SHARD)
            group = "halberds"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun diamondHalberdRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_HALBERD.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_halberd_crafting"), someResult).apply {
            shape(" XZ", " Y ", "Y  ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.PRISMARINE_SHARD)
            group = "halberds"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun woodenLanceRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_LANCE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_lance_crafting"), someResult).apply {
            shape(" X ", " X ", " YZ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.IRON_NUGGET)
            group = "lances"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun goldenLanceRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_LANCE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_lance_crafting"), someResult).apply {
            shape(" X ", " X ", " YZ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.IRON_NUGGET)
            group = "lances"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun stoneLanceRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_LANCE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_lance_crafting"), someResult).apply {
            shape(" X ", " X ", " YZ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.IRON_NUGGET)
            group = "lances"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun ironLanceRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_LANCE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_lance_crafting"), someResult).apply {
            shape(" X ", " X ", " YZ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.IRON_NUGGET)
            group = "lances"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun diamondLanceRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_LANCE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_lance_crafting"), someResult).apply {
            shape(" X ", " X ", " YZ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.IRON_NUGGET)
            group = "lances"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun woodenWarhammerRecipe(): ShapedRecipe {
        val result = Weapons.WOODEN_WARHAMMER.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_warhammer_crafting"), result).apply {
            shape(" X ", "XYX", " Y ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            group = "warhammers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun goldenWarhammerRecipe(): ShapedRecipe {
        val result = Weapons.GOLDEN_WARHAMMER.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_warhammer_crafting"), result).apply {
            shape(" X ", "XYX", " Y ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
            group = "warhammers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun stoneWarhammerRecipe(): ShapedRecipe {
        val result = Weapons.STONE_WARHAMMER.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_warhammer_crafting"), result).apply {
            shape(" X ", "XYX", " Y ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
            group = "warhammers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun ironWarhammerRecipe(): ShapedRecipe {
        val result = Weapons.IRON_WARHAMMER.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_warhammer_crafting"), result).apply {
            shape(" X ", "XYX", " Y ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            group = "warhammers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun diamondWarhammerRecipe(): ShapedRecipe {
        val result = Weapons.DIAMOND_WARHAMMER.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_warhammer_crafting"), result).apply {
            shape(" X ", "XYX", " Y ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
            group = "warhammers"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun woodenScytheRecipe(): ShapedRecipe {
        val result = Weapons.WOODEN_SCYTHE.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_scythe_crafting"), result).apply {
            shape("CX ", "Y X", "Y  ")
            setIngredient('C', Material.COPPER_INGOT)
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            group = "scythes"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun goldenScytheRecipe(): ShapedRecipe {
        val result = Weapons.GOLDEN_SCYTHE.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_scythe_crafting"), result).apply {
            shape("CX ", "Y X", "Y  ")
            setIngredient('C', Material.COPPER_INGOT)
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
            group = "scythes"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun stoneScytheRecipe(): ShapedRecipe {
        val result = Weapons.STONE_SCYTHE.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_scythe_crafting"), result).apply {
            shape("CX ", "Y X", "Y  ")
            setIngredient('C', Material.COPPER_INGOT)
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
            group = "scythes"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun ironScytheRecipe(): ShapedRecipe {
        val result = Weapons.IRON_SCYTHE.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_scythe_crafting"), result).apply {
            shape("CX ", "Y X", "Y  ")
            setIngredient('C', Material.COPPER_INGOT)
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            group = "scythes"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    private fun diamondScytheRecipe(): ShapedRecipe {
        val result = Weapons.DIAMOND_SCYTHE.createWeapon()
        val recipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_scythe_crafting"), result).apply {
            shape("CX ", "Y X", "Y  ")
            setIngredient('C', Material.COPPER_INGOT)
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
            group = "scythes"
            category = CraftingBookCategory.EQUIPMENT
        }
        return recipe
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun woodenLongAxeRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_LONG_AXE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_long_axe_crafting"), someResult).apply {
            shape("YXX", "YXX", "Y  ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            group = "long_axes"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun goldenLongAxeRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_LONG_AXE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_long_axe_crafting"), someResult).apply {
            shape("YXX", "YXX", "Y  ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
            group = "long_axes"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun stoneLongAxeRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_LONG_AXE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_long_axe_crafting"), someResult).apply {
            shape("YXX", "YXX", "Y  ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
            group = "long_axes"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun ironLongAxeRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_LONG_AXE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_long_axe_crafting"), someResult).apply {
            shape("YXX", "YXX", "Y  ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            group = "long_axes"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun diamondLongAxeRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_LONG_AXE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_long_axe_crafting"), someResult).apply {
            shape("YXX", "YXX", "Y  ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
            group = "long_axes"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun bambooStaffRecipe(): ShapedRecipe {
        val someResult = Weapons.BAMBOO_STAFF.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "bamboo_staff_crafting"), someResult).apply {
            shape(" Y ", "XYX", " Y ")
            setIngredient('X', Material.RABBIT_HIDE)
            setIngredient('Y', Material.BAMBOO)
            group = "staffs"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun boneStaffRecipe(): ShapedRecipe {
        val someResult = Weapons.BONE_STAFF.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "bone_staff_crafting"), someResult).apply {
            shape(" Y ", "XYX", " Y ")
            setIngredient('X', Material.RABBIT_HIDE)
            setIngredient('Y', Material.BONE)
            group = "staffs"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun createWoodenStaffRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_STAFF.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_staff_crafting"), someResult).apply {
            shape(" Y ", "XYX", " Y ")
            setIngredient('X', Material.RABBIT_HIDE)
            setIngredient('Y', Material.STICK)
            group = "staffs"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    private fun blazeRodStaffRecipe(): ShapedRecipe {
        val someResult = Weapons.BLAZE_ROD_STAFF.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "blaze_rod_staff_crafting"), someResult).apply {
            shape(" Y ", "XYX", " Y ")
            setIngredient('X', Material.RABBIT_HIDE)
            setIngredient('Y', Material.BLAZE_ROD)
            group = "staffs"
            category = CraftingBookCategory.EQUIPMENT
        }
        return someRecipe
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun netheriteZweihanderRecipe(): ShapedRecipe {
        val someResult = Weapons.NETHERITE_ZWEIHANDER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "netherite_zweihander"), someResult).apply {
            shape("  X", "XX ", "YX ")
            setIngredient('X', Material.NETHERITE_INGOT)
            setIngredient('Y', Material.BLAZE_ROD)
        }
        return someRecipe
    }

}