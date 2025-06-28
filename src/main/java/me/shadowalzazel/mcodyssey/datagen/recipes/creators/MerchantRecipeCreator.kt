package me.shadowalzazel.mcodyssey.datagen.recipes.creators

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import me.shadowalzazel.mcodyssey.api.LootTableManager
import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.util.EquipmentGenerator
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe
import kotlin.random.Random

@Suppress("UnstableApiUsage")
class MerchantRecipeCreator : EquipmentGenerator {

    // Weapon Customization
    private val ALL_WEAPONS = listOf(
        ToolType.SABER, ToolType.KATANA, ToolType.LONGSWORD, ToolType.CUTLASS, ToolType.RAPIER,
        ToolType.CLAYMORE, ToolType.KRIEGSMESSER,
        ToolType.POLEAXE, ToolType.LONGAXE, ToolType.GLAIVE,
        ToolType.WARHAMMER,  ToolType.SCYTHE, ToolType.SPEAR, ToolType.HALBERD,
        ToolType.DAGGER, ToolType.SICKLE, ToolType.CHAKRAM // Double
    )
    private val ALL_PARTS_PATTERNS = listOf(
        "crusader", "danger", "fancy", "humble", "imperial", "marauder", "seraph", "vandal", "voyager")

    private val PARTS_CUSTOM_MODEL_DATA = listOf(
        "tool_type", "blade", "handle", "hilt", "pommel", "no_trim"
    )

    fun createIronWeaponTrade(): MerchantRecipe {
        val weaponResult = generateNewRandomWeapon(ALL_WEAPONS, listOf(ToolMaterial.IRON))

        val trade = MerchantRecipe(weaponResult, 3).apply {
            villagerExperience = (16..25).random()
            addIngredient(ItemStack(Material.EMERALD, (18..26).random()))
        }
        return trade
    }

    fun createCustomizedIronWeaponTrade(partsToCustomize: List<String> = listOf()): MerchantRecipe {
        val weaponType = ALL_WEAPONS.random()
        val weaponResult = createToolStack(ToolMaterial.IRON, weaponType)

        val randomPartPattern = { name: String -> "${ALL_PARTS_PATTERNS.random()}_${name}"}

        // Create the weapon parts data
        val weaponPartsData = PARTS_CUSTOM_MODEL_DATA.toMutableList()
        weaponPartsData[0] = weaponType.name
        PARTS_CUSTOM_MODEL_DATA.forEachIndexed { index, part ->
            // If part matches with custom_model_data, create the part Data
            if (part in partsToCustomize) {
                weaponPartsData[index] = randomPartPattern("blade")
            }
            // Else, just keep fall back
        }
        // Set the custom model data
        val customData = CustomModelData.customModelData().addStrings(weaponPartsData)
        weaponResult.setData(DataComponentTypes.CUSTOM_MODEL_DATA, customData)
        val randomSeed = java.util.Random((0L..100000L).random())
        weaponResult.enchantWithLevels((10..20).random(), false, randomSeed)

        // Create the trade
        val trade = MerchantRecipe(weaponResult, 3).apply {
            villagerExperience = (23..33).random()
            addIngredient(ItemStack(Material.EMERALD, (23..31).random()))
        }
        return trade
    }

    fun createCustomizedDiamondWeaponTrade(partsToCustomize: List<String> = listOf()): MerchantRecipe {
        val weaponType = ALL_WEAPONS.random()
        val weaponResult = createToolStack(ToolMaterial.DIAMOND, weaponType)

        val randomPartPattern = { name: String -> "${ALL_PARTS_PATTERNS.random()}_${name}"}

        // Create the weapon parts data
        val weaponPartsData = PARTS_CUSTOM_MODEL_DATA.toMutableList()
        weaponPartsData[0] = weaponType.name
        PARTS_CUSTOM_MODEL_DATA.forEachIndexed { index, part ->
            // If part matches with custom_model_data, create the part Data
            if (part in partsToCustomize) {
                weaponPartsData[index] = randomPartPattern(part)
            }
            // Else, just keep fall back
        }
        // Set the custom model data
        val customData = CustomModelData.customModelData().addStrings(weaponPartsData)
        weaponResult.setData(DataComponentTypes.CUSTOM_MODEL_DATA, customData)
        val randomSeed = java.util.Random((0L..100000L).random())
        weaponResult.enchantWithLevels((20..30).random(), false, randomSeed)

        // Create the trade
        val trade = MerchantRecipe(weaponResult, 3).apply {
            villagerExperience = (35..49).random()
            addIngredient(ItemStack(Material.EMERALD, (29..41).random()))
        }
        return trade
    }


    fun createPartUpgradeTemplateTrade(): MerchantRecipe {
        val result = LootTableManager.generateItemFromLootTable("loot/random_part_upgrade_template")
        // Create the trade
        val trade = MerchantRecipe(result, 3).apply {
            villagerExperience = (7..14).random()
            addIngredient(ItemStack(Material.EMERALD, (6..11).random()))
        }
        return trade

    }

}