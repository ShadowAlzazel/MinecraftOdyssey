package me.shadowalzazel.mcodyssey.recipe_creators.crafting

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.items.Weapons
import me.shadowalzazel.mcodyssey.items.Weapons.createWeapon
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe

class Weapon {

    // Register Recipes
    fun getRecipes(): List<ShapedRecipe> {
        return listOf(
            createWoodenKatanaRecipe(),
            createGoldenKatanaRecipe(),
            createStoneKatanaRecipe(),
            createIronKatanaRecipe(),
            createDiamondKatanaRecipe(),
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
            createWoodenLongAxeRecipe(),
            createGoldenLongAxeRecipe(),
            createStoneLongAxeRecipe(),
            createIronLongAxeRecipe(),
            createDiamondLongAxeRecipe(),
            createNetheriteZweihanderRecipe(),
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
        val someResult = Weapons.WOODEN_KATANA.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_katana"), someResult)

        someRecipe.shape("  X", " X ", "YZ ")
        someRecipe.setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
        someRecipe.setIngredient('Y', Material.STICK)
        someRecipe.setIngredient('Z', Material.COPPER_INGOT)
        return someRecipe
    }

    private fun createGoldenKatanaRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_KATANA.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_katana"), someResult)

        someRecipe.shape("  X", " X ", "YZ ")
        someRecipe.setIngredient('X', Material.GOLD_INGOT)
        someRecipe.setIngredient('Y', Material.STICK)
        someRecipe.setIngredient('Z', Material.COPPER_INGOT)
        return someRecipe
    }

    private fun createStoneKatanaRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_KATANA.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_katana"), someResult)

        someRecipe.shape("  X", " X ", "YZ ")
        someRecipe.setIngredient('X', Material.COBBLESTONE)
        someRecipe.setIngredient('Y', Material.STICK)
        someRecipe.setIngredient('Z', Material.COPPER_INGOT)
        return someRecipe
    }

    private fun createIronKatanaRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_KATANA.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_katana"), someResult)

        someRecipe.shape("  X", " X ", "YZ ")
        someRecipe.setIngredient('X', Material.IRON_INGOT)
        someRecipe.setIngredient('Y', Material.STICK)
        someRecipe.setIngredient('Z', Material.COPPER_INGOT)
        return someRecipe
    }

    private fun createDiamondKatanaRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_KATANA.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamondkatana"), someResult)

        someRecipe.shape("  X", " X ", "YZ ")
        someRecipe.setIngredient('X', Material.DIAMOND)
        someRecipe.setIngredient('Y', Material.STICK)
        someRecipe.setIngredient('Z', Material.COPPER_INGOT)
        return someRecipe
    }


    /*----------------------------------------CLAYMORES-----------------------------------------*/

    private fun createWoodenClaymoreRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_CLAYMORE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_claymore"), someResult)

        someRecipe.shape(" X ", "XXX", " Y ")
        someRecipe.setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
        someRecipe.setIngredient('Y', Material.STICK)
        return someRecipe
    }

    private fun createGoldenClaymoreRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_CLAYMORE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_claymore"), someResult)

        someRecipe.shape(" X ", "XXX", " Y ")
        someRecipe.setIngredient('X', RecipeChoice.MaterialChoice(Material.GOLD_INGOT))
        someRecipe.setIngredient('Y', Material.STICK)
        return someRecipe
    }

    private fun createStoneClaymoreRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_CLAYMORE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_claymore"), someResult)

        someRecipe.shape(" X ", "XXX", " Y ")
        someRecipe.setIngredient('X', RecipeChoice.MaterialChoice(Material.COBBLESTONE))
        someRecipe.setIngredient('Y', Material.STICK)
        return someRecipe
    }

    private fun createIronClaymoreRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_CLAYMORE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_claymore"), someResult)

        someRecipe.shape(" X ", "XXX", " Y ")
        someRecipe.setIngredient('X', RecipeChoice.MaterialChoice(Material.IRON_INGOT))
        someRecipe.setIngredient('Y', Material.STICK)
        return someRecipe
    }

    private fun createDiamondClaymoreRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_CLAYMORE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_claymore"), someResult).apply {
            shape(" X ", "XXX", " Y ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.DIAMOND))
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }


    /*----------------------------------------SPEARS-----------------------------------------*/

    private fun createWoodenSpearRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_SPEAR.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_spear"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createGoldenSpearRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_SPEAR.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_spear"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createStoneSpearRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_SPEAR.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_spear"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createIronSpearRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_SPEAR.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_spear"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createDiamondSpearRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_SPEAR.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_spear"), someResult).apply {
            shape("  X", " Y ", "Y  ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    /*----------------------------------------DAGGERS-----------------------------------------*/

    private fun createWoodenDaggerRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_DAGGER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_dagger"), someResult).apply {
            shape(" X", "Y ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createGoldenDaggerRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_DAGGER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_dagger"), someResult).apply {
            shape(" X", "Y ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createStoneDaggerRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_DAGGER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_dagger"), someResult).apply {
            shape(" X", "Y ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createIronDaggerRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_DAGGER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_dagger"), someResult).apply {
            shape(" X", "Y ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createDiamondDaggerRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_DAGGER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_dagger"), someResult).apply {
            shape(" X", "Y ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }


    /*----------------------------------------RAPIERS-----------------------------------------*/

    private fun createWoodenRapierRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_RAPIER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_rapier"), someResult).apply {
            shape("  X", " X ", "Y  ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createGoldenRapierRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_RAPIER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_rapier"), someResult).apply {
            shape("  X", " X ", "Y  ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createStoneRapierRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_RAPIER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_rapier"), someResult).apply {
            shape("  X", " X ", "Y  ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createIronRapierRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_RAPIER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_rapier"), someResult).apply {
            shape("  X", " X ", "Y  ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createDiamondRapierRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_RAPIER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_rapier"), someResult).apply {
            shape("  X", " X ", "Y  ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }


    /*----------------------------------------CUTLASSES-----------------------------------------*/

    private fun createWoodenCutlassRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_CUTLASS.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_cutlass"), someResult).apply {
            shape(" X ", " X ", "ZY ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.GOLD_INGOT)
        }
        return someRecipe
    }

    private fun createGoldenCutlassRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_CUTLASS.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_cutlass"), someResult).apply {
            shape(" X ", " X ", "ZY ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.GOLD_INGOT)
        }
        return someRecipe
    }

    private fun createStoneCutlassRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_CUTLASS.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_cutlass"), someResult).apply {
            shape(" X ", " X ", "ZY ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.GOLD_INGOT)
        }
        return someRecipe
    }
    private fun createIronCutlassRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_CUTLASS.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_cutlass"), someResult).apply {
            shape(" X ", " X ", "ZY ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.GOLD_INGOT)
        }
        return someRecipe
    }

    private fun createDiamondCutlassRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_CUTLASS.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_cutlass"), someResult).apply {
            shape(" X ", " X ", "ZY ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.GOLD_INGOT)
        }
        return someRecipe
    }

    /*----------------------------------------SABERS-----------------------------------------*/

    private fun createWoodenSaberRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_SABER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_saber"), someResult).apply {
            shape("X  ", " X ", " Y ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createGoldenSaberRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_SABER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_saber"), someResult).apply {
            shape("X  ", " X ", " Y ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createStoneSaberRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_SABER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_saber"), someResult).apply {
            shape("X  ", " X ", " Y ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }
    private fun createIronSaberRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_SABER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_saber"), someResult).apply {
            shape("X  ", " X ", " Y ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createDiamondSaberRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_SABER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_saber"), someResult).apply {
            shape("X  ", " X ", " Y ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    /*----------------------------------------HALBERDS-----------------------------------------*/

    private fun createWoodenHalberdRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_HALBERD.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_halberd"), someResult).apply {
            shape(" XZ", " Y ", "Y  ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.PRISMARINE_SHARD)
        }
        return someRecipe
    }

    private fun createGoldenHalberdRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_HALBERD.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_halberd"), someResult).apply {
            shape(" XZ", " Y ", "Y  ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.PRISMARINE_SHARD)
        }
        return someRecipe
    }

    private fun createStoneHalberdRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_HALBERD.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_halberd"), someResult).apply {
            shape(" XZ", " Y ", "Y  ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.PRISMARINE_SHARD)
        }
        return someRecipe
    }

    private fun createIronHalberdRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_HALBERD.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_halberd"), someResult).apply {
            shape(" XZ", " Y ", "Y  ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.PRISMARINE_SHARD)
        }
        return someRecipe
    }

    private fun createDiamondHalberdRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_HALBERD.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_halberd"), someResult).apply {
            shape(" XZ", " Y ", "Y  ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.PRISMARINE_SHARD)
        }
        return someRecipe
    }


    /*----------------------------------------WARHAMMERS-----------------------------------------*/

    private fun createIronWarhammerRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_WARHAMMER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_warhammer"), someResult).apply {
            shape("XXX", "XZX", " Y ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
            setIngredient('Z', Material.RABBIT_HIDE)
        }
        return someRecipe
    }

    /*----------------------------------------LONG AXE-----------------------------------------*/

    private fun createWoodenLongAxeRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_LONG_AXE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_long_axe"), someResult).apply {
            shape("YXX", "YXX", "Y  ")
            setIngredient('X', RecipeChoice.MaterialChoice(Material.ACACIA_PLANKS, Material.BIRCH_PLANKS, Material.DARK_OAK_PLANKS, Material.OAK_PLANKS, Material.SPRUCE_PLANKS, Material.JUNGLE_PLANKS))
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createGoldenLongAxeRecipe(): ShapedRecipe {
        val someResult = Weapons.GOLDEN_LONG_AXE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "golden_long_axe"), someResult).apply {
            shape("YXX", "YXX", "Y  ")
            setIngredient('X', Material.GOLD_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createStoneLongAxeRecipe(): ShapedRecipe {
        val someResult = Weapons.STONE_LONG_AXE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "stone_long_axe"), someResult).apply {
            shape("YXX", "YXX", "Y  ")
            setIngredient('X', Material.COBBLESTONE)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createIronLongAxeRecipe(): ShapedRecipe {
        val someResult = Weapons.IRON_LONG_AXE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "iron_long_axe"), someResult).apply {
            shape("YXX", "YXX", "Y  ")
            setIngredient('X', Material.IRON_INGOT)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createDiamondLongAxeRecipe(): ShapedRecipe {
        val someResult = Weapons.DIAMOND_LONG_AXE.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "diamond_long_axe"), someResult).apply {
            shape("YXX", "YXX", "Y  ")
            setIngredient('X', Material.DIAMOND)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    /*--------------------------------------ZWEIHANDER---------------------------------------*/

    private fun createNetheriteZweihanderRecipe(): ShapedRecipe {
        val someResult = Weapons.NETHERITE_ZWEIHANDER.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "netherite_zweihander"), someResult).apply {
            shape("  X", "XX ", "YX ")
            setIngredient('X', Material.NETHERITE_INGOT)
            setIngredient('Y', Material.BLAZE_ROD)
        }
        return someRecipe
    }



    /*----------------------------------------STAFFS-----------------------------------------*/

    private fun createBambooStaffRecipe(): ShapedRecipe {
        val someResult = Weapons.BAMBOO_STAFF.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "bamboo_staff"), someResult).apply {
            shape(" Y ", "XYX", " Y ")
            setIngredient('X', Material.RABBIT_HIDE)
            setIngredient('Y', Material.BAMBOO)
        }
        return someRecipe
    }

    private fun createBoneStaffRecipe(): ShapedRecipe {
        val someResult = Weapons.BONE_STAFF.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "bone_staff"), someResult).apply {
            shape(" Y ", "XYX", " Y ")
            setIngredient('X', Material.RABBIT_HIDE)
            setIngredient('Y', Material.BONE)
        }
        return someRecipe
    }

    private fun createWoodenStaffRecipe(): ShapedRecipe {
        val someResult = Weapons.WOODEN_STAFF.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "wooden_staff"), someResult).apply {
            shape(" Y ", "XYX", " Y ")
            setIngredient('X', Material.RABBIT_HIDE)
            setIngredient('Y', Material.STICK)
        }
        return someRecipe
    }

    private fun createBlazeRodStaffRecipe(): ShapedRecipe {
        val someResult = Weapons.BLAZE_ROD_STAFF.createWeapon()
        val someRecipe = ShapedRecipe(NamespacedKey(Odyssey.instance, "blaze_rod_staff"), someResult).apply {
            shape(" Y ", "XYX", " Y ")
            setIngredient('X', Material.RABBIT_HIDE)
            setIngredient('Y', Material.BLAZE_ROD)
        }
        return someRecipe
    }

}