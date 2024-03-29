package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.arcane.EnchantSlotManager
import me.shadowalzazel.mcodyssey.arcane.SlotColors
import me.shadowalzazel.mcodyssey.constants.AttributeIDs
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.addStringTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.addTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasTag
import me.shadowalzazel.mcodyssey.items.Ingredients
import me.shadowalzazel.mcodyssey.trims.Trims
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial

object SmithingListeners : Listener, EnchantSlotManager {

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

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
            val isEngraved = equipment.hasTag(ItemTags.IS_ENGRAVED)
            if (isEngraved) {
                event.viewers.forEach { it.sendFailMessage("This Item Is Already Engraved!") }
                event.result = ItemStack(Material.AIR)
                return
            }
            event.result = equipment.clone().also {
                if (it.amount > 1) it.amount = 1
                it.addTag(ItemTags.IS_ENGRAVED)
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
                    it.addStringTag(ItemTags.ENGRAVED_BY, engraver.name)
                    val engraving = Component.text("$pretext by ${engraver.name}", SlotColors.AMETHYST.color, TextDecoration.ITALIC)
                    newLore.add(engraving)
                }
                it.lore(newLore)
            }
            return
        }

        /*-----------------------------------------------------------------------------------------------*/
        // Soul Steel
        if (result.type == Material.IRON_INGOT) {
            event.result = ItemStack(Material.AIR)
            if (!addition.hasItemMeta()) return
            if (!addition.itemMeta.hasCustomModelData()) return
            if (!equipment.itemMeta.hasCustomModelData()) return
            if (addition.itemMeta.customModelData != ItemModels.SOUL_STEEL_INGOT) return
            if (equipment.hasTag(ItemTags.SOUL_STEEL_TOOL)) return
            val equipmentModel = equipment.itemMeta.customModelData

            // Temporary Maps -> Move to seperate Enums
            val ironToSoulSteelMap = mapOf(
                ItemModels.KATANA to ItemModels.SOUL_STEEL_KATANA,
                ItemModels.CLAYMORE to ItemModels.SOUL_STEEL_CLAYMORE,
                ItemModels.DAGGER to ItemModels.SOUL_STEEL_DAGGER,
                ItemModels.RAPIER to ItemModels.SOUL_STEEL_RAPIER,
                ItemModels.CUTLASS to ItemModels.SOUL_STEEL_CUTLASS,
                ItemModels.SABER to ItemModels.SOUL_STEEL_SABER,
                ItemModels.SICKLE to ItemModels.SOUL_STEEL_SICKLE,
                ItemModels.CHAKRAM to ItemModels.SOUL_STEEL_CLAYMORE,
                ItemModels.SPEAR to ItemModels.SOUL_STEEL_SPEAR,
                ItemModels.HALBERD to ItemModels.SOUL_STEEL_HALBERD,
                ItemModels.LANCE to ItemModels.SOUL_STEEL_LANCE,
                ItemModels.WARHAMMER to ItemModels.SOUL_STEEL_WARHAMMER,
                ItemModels.SCYTHE to ItemModels.SOUL_STEEL_SCYTHE,
                ItemModels.LONG_AXE to ItemModels.SOUL_STEEL_LONG_AXE,
            )

            val soulSteelName = mapOf(
                ItemModels.KATANA to "Katana",
                ItemModels.CLAYMORE to "Claymore",
                ItemModels.DAGGER to "Dagger",
                ItemModels.RAPIER to "Rapier",
                ItemModels.CUTLASS to "Cutlass",
                ItemModels.SABER to "Saber",
                ItemModels.SICKLE to "Sickle",
                ItemModels.CHAKRAM to "Chakram",
                ItemModels.SPEAR to "Spear",
                ItemModels.HALBERD to "Halberd",
                ItemModels.LANCE to "Lance",
                ItemModels.WARHAMMER to "Warhammer",
                ItemModels.SCYTHE to "Scythe",
                ItemModels.LONG_AXE to "Long Axe",
            )

            if (!ironToSoulSteelMap.containsKey(equipmentModel)) return

            val newItem = equipment.clone()
            val oldDamageModifier = newItem.itemMeta.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE)?.first {
                it.uniqueId == AttributeIDs.ITEM_ATTACK_DAMAGE_UUID
            } ?: return

            newItem.addTag(ItemTags.SOUL_STEEL_TOOL)
            newItem.itemMeta = newItem.itemMeta.clone().also { meta ->
                meta.setCustomModelData(ironToSoulSteelMap[equipmentModel]!!)
                meta.displayName(Component.text("Soul Steel ${soulSteelName[equipmentModel]!!}", TextColor.color(88, 95, 123), TextDecoration.ITALIC))
                val oldDamage = oldDamageModifier.amount
                val newDamageModifier = AttributeModifier(
                    AttributeIDs.ITEM_ATTACK_DAMAGE_UUID,
                    "odyssey.attack_damage",
                    oldDamage + 1.0,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HAND)
                meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, oldDamageModifier)
                meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, newDamageModifier)
            }

            event.result = newItem
            return
        }

        /*-----------------------------------------------------------------------------------------------*/
        // Trims
        if (result.itemMeta is ArmorMeta) {
            val armorMeta = result.itemMeta as ArmorMeta
            val count = event.inventory.inputMineral!!.amount
            val newTrimMaterial: TrimMaterial

            when(event.inventory.inputMineral) {
                Ingredients.ALEXANDRITE.createItemStack(count) -> {
                    newTrimMaterial = Trims.ALEXANDRITE
                }
                Ingredients.KUNZITE.createItemStack(count) -> {
                    newTrimMaterial = Trims.KUNZITE
                }
                Ingredients.JADE.createItemStack(count) -> {
                    newTrimMaterial = Trims.JADE
                }
                Ingredients.RUBY.createItemStack(count) -> {
                    newTrimMaterial = Trims.RUBY
                }
                Ingredients.SOUL_QUARTZ.createItemStack(count) -> {
                    newTrimMaterial = Trims.SOUL_QUARTZ
                }
                Ingredients.SOUL_STEEL_INGOT.createItemStack(count) -> {
                    newTrimMaterial = Trims.SOUL_STEEL
                }
                ItemStack(Material.OBSIDIAN, count) -> {
                    // TODO: CURRENTLY DOES NOT WORK AS NO EVENT RESULT IS MADE
                    newTrimMaterial = Trims.OBSIDIAN
                }
                else -> {
                    return
                }
            }

            val newTrim = ArmorTrim(newTrimMaterial, (event.inventory.result!!.itemMeta as ArmorMeta).trim!!.pattern)
            armorMeta.trim = newTrim
            event.result = event.result!!.clone().apply {
                (itemMeta) = armorMeta
            }
            return
        }

        /*-----------------------------------------------------------------------------------------------*/
        // Netherite
        if (addition.type == Material.NETHERITE_INGOT && result.itemMeta.hasCustomModelData()) {
            val newItem = event.inventory.result!!.clone()
            if (newItem.hasTag(ItemTags.NETHERITE_TOOL)) return
            val oldDamageModifier = newItem.itemMeta.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE)?.first {
                it.uniqueId == AttributeIDs.ITEM_ATTACK_DAMAGE_UUID
            } ?: return
            newItem.itemMeta = newItem.itemMeta.clone().also { meta ->
                val oldDamage = oldDamageModifier.amount
                val newDamageModifier = AttributeModifier(
                    AttributeIDs.ITEM_ATTACK_DAMAGE_UUID,
                    "odyssey.attack_damage",
                    oldDamage + 1.0,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HAND)
                meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, oldDamageModifier)
                meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, newDamageModifier)
            }
            newItem.addTag(ItemTags.NETHERITE_TOOL)
            event.result = newItem
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
}
