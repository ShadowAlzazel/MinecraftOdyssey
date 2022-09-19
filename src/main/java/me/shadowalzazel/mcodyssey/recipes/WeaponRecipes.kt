package me.shadowalzazel.mcodyssey.recipes

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.items.OdysseyBooks
import me.shadowalzazel.mcodyssey.items.OdysseyWeapons
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe

object WeaponRecipes {

    // Register Recipes
    fun registerRecipes(): List<ShapedRecipe> {
        return listOf(
            createWoodenKatanaRecipe(),
            createGoldenKatanaRecipe(),
            createStoneKatanaRecipe(),
            createIronKatanaRecipe(),
            createDiamondKatanaRecipe(),
            createNetheriteKatanaRecipe(),
            createWoodenClaymoreRecipe(),
            createGoldenClaymoreRecipe(),
            createStoneClaymoreRecipe(),
            createIronClaymoreRecipe(),
            createDiamondClaymoreRecipe(),
            createWoodenSpearRecipe(),
            createGoldenSpearRecipe(),
            createStoneSpearRecipe(),
            createIronSpearRecipe(),
            createDiamondSpearRecipe(),
            createWoodenDaggerRecipe(),
            createGoldenDaggerRecipe(),
            createStoneDaggerRecipe(),
            createIronDaggerRecipe(),
            createDiamondDaggerRecipe(),
            createWoodenRapierRecipe(),
            createGoldenRapierRecipe(),
            createStoneRapierRecipe(),
            createIronRapierRecipe(),
            createDiamondRapierRecipe(),
            createWoodenCutlassRecipe(),
            createGoldenCutlassRecipe(),
            createStoneCutlassRecipe(),
            createIronCutlassRecipe(),
            createDiamondCutlassRecipe(),
            createWoodenSaberRecipe(),
            createGoldenSaberRecipe(),
            createStoneSaberRecipe(),
            createIronSaberRecipe(),
            createDiamondSaberRecipe(),
            createWoodenHalberdRecipe(),
            createGoldenHalberdRecipe(),
            createStoneHalberdRecipe(),
            createIronHalberdRecipe(),
            createDiamondHalberdRecipe(),
            createIronWarhammerRecipe(),
            createBambooStaffRecipe(),
            createBoneStaffRecipe(),
            createWoodenStaffRecipe(),
            createBlazeRodStaffRecipe()
        )
    }

    /* ---------------------------------------------------------------------------*/

    /*----------------------------------------KATANAS-----------------------------------------*/

    private fun createWoodenKatanaRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.WOODEN_KATANA.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "woodenkatana"), someResult)

        someRecipe.shape("  X", " X ", "YZ ")
        someRecipe.setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
        someRecipe.setIngredient('Y', Material.STICK)
        someRecipe.setIngredient('Z', Material.COPPER_INGOT)
        return someRecipe
    }

    private fun createGoldenKatanaRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.GOLDEN_KATANA.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "goldenkatana"), someResult)

        someRecipe.shape("  X", " X ", "YZ ")
        someRecipe.setIngredient('X', Material.GOLD_INGOT)
        someRecipe.setIngredient('Y', Material.STICK)
        someRecipe.setIngredient('Z', Material.COPPER_INGOT)
        return someRecipe
    }

    private fun createStoneKatanaRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.STONE_KATANA.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "stonekatana"), someResult)

        someRecipe.shape("  X", " X ", "YZ ")
        someRecipe.setIngredient('X', Material.COBBLESTONE)
        someRecipe.setIngredient('Y', Material.STICK)
        someRecipe.setIngredient('Z', Material.COPPER_INGOT)
        return someRecipe
    }

    private fun createIronKatanaRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.IRON_KATANA.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "ironkatana"), someResult)

        someRecipe.shape("  X", " X ", "YZ ")
        someRecipe.setIngredient('X', Material.IRON_INGOT)
        someRecipe.setIngredient('Y', Material.STICK)
        someRecipe.setIngredient('Z', Material.COPPER_INGOT)
        return someRecipe
    }

    private fun createDiamondKatanaRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.DIAMOND_KATANA.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "diamondkatana"), someResult)

        someRecipe.shape("  X", " X ", "YZ ")
        someRecipe.setIngredient('X', Material.DIAMOND)
        someRecipe.setIngredient('Y', Material.STICK)
        someRecipe.setIngredient('Z', Material.COPPER_INGOT)
        return someRecipe
    }

    // ?? TEMP
    private fun createNetheriteKatanaRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.NETHERITE_KATANA.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "netheritekatana"), someResult)

        someRecipe.shape("  X", " X ", "YZ ")
        someRecipe.setIngredient('X', Material.NETHERITE_INGOT)
        someRecipe.setIngredient('Y', Material.BLAZE_ROD)
        return someRecipe
    }


    /*----------------------------------------CLAYMORES-----------------------------------------*/

    private fun createWoodenClaymoreRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.WOODEN_CLAYMORE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "woodenclaymore"), someResult)

        someRecipe.shape(" X ", "XXX", " Y ")
        someRecipe.setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
        someRecipe.setIngredient('Y', Material.STICK)
        return someRecipe
    }

    private fun createGoldenClaymoreRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.GOLDEN_CLAYMORE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "goldenclaymore"), someResult)

        someRecipe.shape(" X ", "XXX", " Y ")
        someRecipe.setIngredient('X', RecipeChoice.MaterialChoice(Material.GOLD_INGOT))
        someRecipe.setIngredient('Y', Material.STICK)
        return someRecipe
    }

    private fun createStoneClaymoreRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.STONE_CLAYMORE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "stoneclaymore"), someResult)

        someRecipe.shape(" X ", "XXX", " Y ")
        someRecipe.setIngredient('X', RecipeChoice.MaterialChoice(Material.COBBLESTONE))
        someRecipe.setIngredient('Y', Material.STICK)
        return someRecipe
    }

    private fun createIronClaymoreRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.IRON_CLAYMORE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "ironclaymore"), someResult)

        someRecipe.shape(" X ", "XXX", " Y ")
        someRecipe.setIngredient('X', RecipeChoice.MaterialChoice(Material.IRON_INGOT))
        someRecipe.setIngredient('Y', Material.STICK)
        return someRecipe
    }

    private fun createDiamondClaymoreRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.DIAMOND_CLAYMORE.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "diamondclaymore"), someResult).apply {
            shape(" X ", "XXX", " Y ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.DIAMOND))
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }


    /*----------------------------------------SPEARS-----------------------------------------*/

    private fun createWoodenSpearRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.WOODEN_SPEAR.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "woodenspear"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createGoldenSpearRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.GOLDEN_SPEAR.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "goldenspear"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createStoneSpearRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.STONE_SPEAR.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "stonespear"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createIronSpearRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.IRON_SPEAR.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "ironspear"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createDiamondSpearRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.DIAMOND_SPEAR.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "diamondspear"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    /*----------------------------------------DAGGERS-----------------------------------------*/

    private fun createWoodenDaggerRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.WOODEN_DAGGER.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "woodendagger"), someResult).apply {
            shape(" X", "Y ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createGoldenDaggerRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.GOLDEN_DAGGER.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "goldendagger"), someResult).apply {
            shape(" X", "Y ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createStoneDaggerRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.STONE_DAGGER.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "stonedagger"), someResult).apply {
            shape(" X", "Y ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createIronDaggerRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.IRON_DAGGER.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "irondagger"), someResult).apply {
            shape(" X", "Y ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createDiamondDaggerRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.DIAMOND_DAGGER.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "diamonddagger"), someResult).apply {
            shape(" X", "Y ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }


    /*----------------------------------------RAPIERS-----------------------------------------*/

    private fun createWoodenRapierRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.WOODEN_RAPIER.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "woodenrapier"), someResult).apply {
            shape("  X", " X ", "Y  ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createGoldenRapierRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.GOLDEN_RAPIER.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "goldenrapier"), someResult).apply {
            shape("  X", " X ", "Y  ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createStoneRapierRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.STONE_RAPIER.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "stonerapier"), someResult).apply {
            shape("  X", " X ", "Y  ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createIronRapierRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.IRON_RAPIER.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "ironrapier"), someResult).apply {
            shape("  X", " X ", "Y  ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createDiamondRapierRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.DIAMOND_RAPIER.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "diamondrapier"), someResult).apply {
            shape("  X", " X ", "Y  ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }


    /*----------------------------------------CUTLASSES-----------------------------------------*/

    private fun createWoodenCutlassRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.WOODEN_CUTLASS.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "woodencutlass"), someResult).apply {
            shape(" X ", " X ", "ZY ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.GOLD_INGOT)
        }
        return someRecipe
    }

    private fun createGoldenCutlassRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.GOLDEN_CUTLASS.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "goldencutlass"), someResult).apply {
            shape(" X ", " X ", "ZY ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.GOLD_INGOT)
        }
        return someRecipe
    }

    private fun createStoneCutlassRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.STONE_CUTLASS.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "stonecutlass"), someResult).apply {
            shape(" X ", " X ", "ZY ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.GOLD_INGOT)
        }
        return someRecipe
    }
    private fun createIronCutlassRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.IRON_CUTLASS.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "ironcutlass"), someResult).apply {
            shape(" X ", " X ", "ZY ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.GOLD_INGOT)
        }
        return someRecipe
    }

    private fun createDiamondCutlassRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.DIAMOND_CUTLASS.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "diamondcutlass"), someResult).apply {
            shape(" X ", " X ", "ZY ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.GOLD_INGOT)
        }
        return someRecipe
    }

    /*----------------------------------------SABERS-----------------------------------------*/

    private fun createWoodenSaberRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.WOODEN_SABER.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "woodensaber"), someResult).apply {
            shape("X  ", " X ", " Y ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createGoldenSaberRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.GOLDEN_SABER.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "goldensaber"), someResult).apply {
            shape("X  ", " X ", " Y ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createStoneSaberRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.STONE_SABER.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "stonesaber"), someResult).apply {
            shape("X  ", " X ", " Y ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }
    private fun createIronSaberRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.IRON_SABER.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "ironsaber"), someResult).apply {
            shape("X  ", " X ", " Y ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createDiamondSaberRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.DIAMOND_SABER.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "diamondsaber"), someResult).apply {
            shape("X  ", " X ", " Y ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    /*----------------------------------------HALBERDS-----------------------------------------*/

    private fun createWoodenHalberdRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.WOODEN_HALBERD.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "woodenhalberd"), someResult).apply {
            shape(" XZ", " Y ", "Y  ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.PRISMARINE_SHARD)
        }
        return someRecipe
    }

    private fun createGoldenHalberdRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.GOLDEN_HALBERD.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "goldenhalberd"), someResult).apply {
            shape(" XZ", " Y ", "Y  ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.PRISMARINE_SHARD)
        }
        return someRecipe
    }

    private fun createStoneHalberdRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.STONE_HALBERD.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "stonehalberd"), someResult).apply {
            shape(" XZ", " Y ", "Y  ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.PRISMARINE_SHARD)
        }
        return someRecipe
    }

    private fun createIronHalberdRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.IRON_HALBERD.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "ironhalberd"), someResult).apply {
            shape(" XZ", " Y ", "Y  ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.PRISMARINE_SHARD)
        }
        return someRecipe
    }

    private fun createDiamondHalberdRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.DIAMOND_HALBERD.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "diamondhalberd"), someResult).apply {
            shape(" XZ", " Y ", "Y  ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.PRISMARINE_SHARD)
        }
        return someRecipe
    }


    /*----------------------------------------WARHAMMERS-----------------------------------------*/

    private fun createIronWarhammerRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.IRON_WARHAMMER.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "ironwarhammer"), someResult).apply {
            shape("XXX", "XZX", " Y ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.RABBIT_HIDE)
        }
        return someRecipe
    }


    /*----------------------------------------STAFFS-----------------------------------------*/

    private fun createBambooStaffRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.BAMBOO_STAFF.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "bamboostaff"), someResult).apply {
            shape(" Y ", "XYX", " Y ")
            setIngredient('X', Material.RABBIT_HIDE)
            setIngredient('Y', Material.BAMBOO)
        }
        return someRecipe
    }

    private fun createBoneStaffRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.BONE_STAFF.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "bonestaff"), someResult).apply {
            shape(" Y ", "XYX", " Y ")
            setIngredient('X', Material.RABBIT_HIDE)
            setIngredient('Y', Material.BONE)
        }
        return someRecipe
    }

    private fun createWoodenStaffRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.WOODEN_STAFF.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "woodenstaff"), someResult).apply {
            shape(" Y ", "XYX", " Y ")
            setIngredient('X', Material.RABBIT_HIDE)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createBlazeRodStaffRecipe(): ShapedRecipe {
        val someResult = OdysseyWeapons.BLAZE_ROD_STAFF.createItemStack(1)
        val someRecipe = ShapedRecipe(NamespacedKey(MinecraftOdyssey.instance, "blazerodstaff"), someResult).apply {
            shape(" Y ", "XYX", " Y ")
            setIngredient('X', Material.RABBIT_HIDE)
            setIngredient('Y', Material.BLAZE_ROD)
        }
        return someRecipe
    }

}