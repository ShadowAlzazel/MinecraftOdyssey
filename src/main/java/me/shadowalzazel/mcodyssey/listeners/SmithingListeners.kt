package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.enchantments.api.SlotColors
import me.shadowalzazel.mcodyssey.constants.AttributeTags
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.trims.TrimMaterials
import me.shadowalzazel.mcodyssey.trims.TrimPatterns
import me.shadowalzazel.mcodyssey.util.DataTagManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object SmithingListeners : Listener, DataTagManager {

    /*-----------------------------------------------------------------------------------------------*/

    @Suppress("UnstableApiUsage")
    @EventHandler
    fun smithingHandler(event: PrepareSmithingEvent) {
        val recipe = event.inventory.recipe ?: return
        // Avoid Conflict with enchanting tomes
        if (recipe.result.type == Material.ENCHANTED_BOOK) return
        // Assign vars to non null values
        val equipment = event.inventory.inputEquipment ?: return
        val result = event.result ?: return
        val addition = event.inventory.inputMineral ?: return

        /*-----------------------------------------------------------------------------------------------*/
        // Engraving
        if (result.type == Material.AMETHYST_SHARD && addition.type == Material.AMETHYST_SHARD) {
            val isEngraved = equipment.hasTag(ItemDataTags.IS_ENGRAVED)
            if (isEngraved) {
                event.viewers.forEach { it.sendBarMessage("This Item Is Already Engraved!") }
                event.result = ItemStack(Material.AIR)
                return
            }
            event.result = equipment.clone().also {
                if (it.amount > 1) it.amount = 1
                it.addTag(ItemDataTags.IS_ENGRAVED)
                val newLore = it.itemMeta.lore() ?: mutableListOf()
                val pretext = when (equipment.type) {
                    Material.POTION -> {
                        "Brewed"
                    }
                    else -> {
                        "Created"
                    }
                }
                for (engraver in event.viewers) {
                    it.addStringTag(ItemDataTags.ENGRAVED_BY, engraver.name)
                    val engraving = Component.text("$pretext by ${engraver.name}", SlotColors.AMETHYST.color, TextDecoration.ITALIC)
                    newLore.add(engraving)
                }
                it.lore(newLore)
            }
            return
        }

        /*-----------------------------------------------------------------------------------------------*/
        // Soul Steel
        else if (result.type == Material.IRON_INGOT) {
            event.result = ItemStack(Material.AIR)
            if (!addition.hasItemMeta()) return
            if (!addition.itemMeta.hasCustomModelData()) return
            if (!equipment.itemMeta.hasCustomModelData()) return
            if (addition.itemMeta.customModelData != ItemModels.SOUL_STEEL_INGOT) return
            if (equipment.hasTag(ItemDataTags.SOUL_STEEL_TOOL)) return
            // First Checks
            val item = equipment.clone()
            val materialType = item.getStringTag(ItemDataTags.MATERIAL_TYPE)
            // Check to make sure its iron
            val notUpgradableMaterials = listOf(
                "titanium", "iridium", "mithril", "anodized_titanium", "silver"
            )
            if (materialType in notUpgradableMaterials) return
            // Get new model
            val weaponType = item.getStringTag(ItemDataTags.WEAPON_TYPE) ?: "none"
            val weaponModel = weaponModelMap(weaponType)
            val newModel = (ItemModels.SOUL_STEEL_MATERIAL * 100) + (weaponModel)
            // Get Damage
            val oldDamageModifier = item.itemMeta.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE)?.first {
                it.name == AttributeTags.ITEM_BASE_ATTACK_DAMAGE
            } ?: return
            val newDamage = oldDamageModifier.amount + 1.0
            val slots = EquipmentSlotGroup.MAINHAND
            val nameKey = NamespacedKey(Odyssey.instance, AttributeTags.ITEM_BASE_ATTACK_DAMAGE)
            // meta
            item.addTag(ItemDataTags.SOUL_STEEL_TOOL)
            val newMeta = item.itemMeta
            newMeta.also {
                it.setCustomModelData(newModel)
                it.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, oldDamageModifier)
                val newModifier = AttributeModifier(nameKey, newDamage, AttributeModifier.Operation.ADD_NUMBER, slots)
                it.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, newModifier)
            }
            item.itemMeta = newMeta
            event.result = item
            return
        }
        /*-----------------------------------------------------------------------------------------------*/
        // Netherite
        else if (addition.type == Material.NETHERITE_INGOT && result.itemMeta.hasCustomModelData()) {
            val item = event.inventory.result!!.clone()
            if (item.hasTag(ItemDataTags.NETHERITE_TOOL)) return
            // Get new model
            val weaponType = item.getStringTag(ItemDataTags.WEAPON_TYPE) ?: "none"
            val weaponModel = weaponModelMap(weaponType)
            val newModel = (ItemModels.NETHERITE_MATERIAL * 100) + (weaponModel)
            // Get Damage
            val oldDamageModifier = item.itemMeta.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE)?.first {
                it.name == AttributeTags.ITEM_BASE_ATTACK_DAMAGE
            } ?: return
            val newDamage = oldDamageModifier.amount + 1.0
            val slots = EquipmentSlotGroup.MAINHAND
            val nameKey = NamespacedKey(Odyssey.instance, AttributeTags.ITEM_BASE_ATTACK_DAMAGE)
            // meta
            item.addTag(ItemDataTags.NETHERITE_TOOL)
            val newMeta = item.itemMeta
            newMeta.also {
                it.setCustomModelData(newModel)
                it.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, oldDamageModifier)
                val newModifier = AttributeModifier(nameKey, newDamage, AttributeModifier.Operation.ADD_NUMBER, slots)
                it.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, newModifier)
            }
            item.itemMeta = newMeta
            event.result = item
            return
        }

        /*-----------------------------------------------------------------------------------------------*/
        // Trims
        else if (result.itemMeta is ArmorMeta) {
            val resultMeta = result.itemMeta as ArmorMeta
            // Get IDs
            val trimMaterial = event.inventory.inputMineral ?: return
            val trimTemplate = event.inventory.inputTemplate ?: return
            val materialName = trimMaterial.getItemIdentifier()
            val trimName = trimTemplate.getItemIdentifier()
            // Get Trim Material
            val customTrimMaterial: TrimMaterial? = when(materialName) {
                "alexandrite" ->  TrimMaterials.ALEXANDRITE
                "kunzite" -> TrimMaterials.KUNZITE
                "jade" -> TrimMaterials.JADE
                "ruby" -> TrimMaterials.RUBY
                "soul_quartz" -> TrimMaterials.SOUL_QUARTZ
                "soul_steel_ingot" -> TrimMaterials.SOUL_STEEL
                "iridium_ingot" -> TrimMaterials.IRIDIUM
                "mithril_ingot" -> TrimMaterials.MITHRIL
                "titanium_ingot" -> TrimMaterials.TITANIUM
                "andonized_titanium_ingot" -> TrimMaterials.ANODIZED_TITANIUM
                "silver_ingot" -> TrimMaterials.SILVER
                "obsidian" -> TrimMaterials.OBSIDIAN // DOES NOT WORK
                else -> null
            }
            // Get Trim Pattern
            val customTrimPattern: TrimPattern? = when(trimName) {
                "imperial_armor_trim_smithing_template" -> TrimPatterns.IMPERIAL
                else -> null
            }
            // Get Trim
            val finalTrim = customTrimPattern ?: resultMeta.trim?.pattern
            if (finalTrim == null) return
            // Get material
            val finalMaterial = customTrimMaterial ?: (trimMap(trimMaterial) ?: resultMeta.trim?.material)
            if (finalMaterial == null) return

            // Apply new trim
            val newTrim = ArmorTrim(finalMaterial, finalTrim)
            resultMeta.trim = newTrim
            event.result = event.result!!.clone().apply {
                itemMeta = resultMeta
            }
            return
        }
        /*-----------------------------------------------------------------------------------------------*/
        // CURRENTLY DOES MC DOES NOT COPY RESULT NBT
        if (!result.itemMeta.hasCustomModelData()) {
            // Check Recipes
            if (!recipe.result.hasItemMeta()) { return }
            if (!recipe.result.itemMeta.hasCustomModelData()) { return }
            event.inventory.result = recipe.result
            event.result = recipe.result
        }
    }


    private fun trimMap(item: ItemStack): TrimMaterial? {
        return when(item.type) {
            Material.DIAMOND -> TrimMaterial.DIAMOND
            Material.AMETHYST_SHARD -> TrimMaterial.AMETHYST
            Material.IRON_INGOT -> TrimMaterial.IRON
            Material.EMERALD -> TrimMaterial.EMERALD
            Material.GOLD_INGOT -> TrimMaterial.GOLD
            Material.COPPER_INGOT -> TrimMaterial.COPPER
            Material.LAPIS_LAZULI -> TrimMaterial.LAPIS
            Material.NETHERITE_INGOT -> TrimMaterial.NETHERITE
            Material.QUARTZ -> TrimMaterial.QUARTZ
            Material.REDSTONE -> TrimMaterial.REDSTONE
            else -> null
        }
    }


    /*-----------------------------------------------------------------------------------------------*/
    private fun weaponModelMap(weaponType: String): Int {
        return when(weaponType) {
            "katana" -> ItemModels.KATANA
            "claymore" -> ItemModels.CLAYMORE
            "dagger" -> ItemModels.DAGGER
            "rapier" -> ItemModels.RAPIER
            "cutlass" -> ItemModels.CUTLASS
            "saber" -> ItemModels.SABER
            "sickle" -> ItemModels.SICKLE
            "chakram" -> ItemModels.CHAKRAM
            "kunai" -> ItemModels.KUNAI
            "longsword" -> ItemModels.LONGSWORD
            "spear" -> ItemModels.SPEAR
            "halberd" -> ItemModels.HALBERD
            "lance" -> ItemModels.LANCE
            "longaxe" -> ItemModels.LONG_AXE
            "poleaxe" -> ItemModels.POLEAXE
            "warhammer" -> ItemModels.WARHAMMER
            "scythe" -> ItemModels.SCYTHE
            else -> 0
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    private fun LivingEntity.sendBarMessage(reason: String, color: TextColor = SlotColors.ENCHANT.color) {
        this.sendActionBar(
            Component.text(
                reason,
                color
            )
        )
    }

}
