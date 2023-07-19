package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.Identifiers
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.ENGRAVED
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

object SmithingListeners : Listener {

    // TODO: Handler upgrading tools using 1.20 smithing table!
    // Handle weapon quality

    private val AMETHYST_COLOR = TextColor.color(1141, 109, 209)

    @EventHandler
    fun smithingHandler(event: PrepareSmithingEvent) {
        val recipe = event.inventory.recipe ?: return

        // Avoid Conflict with enchanting tomes
        if (recipe.result.type == Material.ENCHANTED_BOOK) { return }
        // Check Equipment
        if (event.inventory.inputEquipment == null) { return }
        val equipment = event.inventory.inputEquipment!!
        // Make Sure Recipe has result
        if (event.result == null) { return }
        val eventResult = event.result!!
        val addition = event.inventory.inputMineral!!

        /*-----------------------------------------------------------------------------------------------*/
        /*-----------------------------------------------------------------------------------------------*/
        // Engraving
        if (eventResult.type == Material.AMETHYST_SHARD) {
            val engraved = equipment.clone()
            if (engraved.hasTag(ENGRAVED)) {
                event.result = ItemStack(Material.AIR)
                return
            }
            engraved.also {
                it.addTag(ENGRAVED)
                val itemLore = it.lore()
                val forgerLore = mutableListOf(Component.text(""))
                for (viewer in event.viewers) {
                    forgerLore.add(Component.text("Created by ${viewer.name}",
                        AMETHYST_COLOR,
                        TextDecoration.ITALIC))
                }
                if (itemLore == null) {
                    it.lore(forgerLore as List<Component>?)
                }
                else {
                    //if (itemLore.contains(Component.text(""))
                    it.lore(itemLore + forgerLore)
                }
            }
            event.result = engraved
            return
        }

        /*-----------------------------------------------------------------------------------------------*/
        /*-----------------------------------------------------------------------------------------------*/
        // Soul Steel
        if (eventResult.type == Material.IRON_INGOT) {
            event.result = ItemStack(Material.AIR)
            if (!addition.hasItemMeta()) return
            if (!addition.itemMeta.hasCustomModelData()) return
            if (!equipment.itemMeta.hasCustomModelData()) return
            if (addition.itemMeta.customModelData != ItemModels.SOUL_STEEL_INGOT) return
            if (equipment.hasTag(ItemTags.SOUL_STEEL_TOOL)) return
            val equipmentModel = equipment.itemMeta.customModelData

            // TEMP
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
                it.uniqueId == Identifiers.ATTACK_DAMAGE_UUID
            } ?: return

            newItem.addTag(ItemTags.SOUL_STEEL_TOOL)
            newItem.itemMeta = newItem.itemMeta.clone().also { meta ->
                meta.setCustomModelData(ironToSoulSteelMap[equipmentModel]!!)
                meta.displayName(Component.text("Soul Steel ${soulSteelName[equipmentModel]!!}", TextColor.color(88, 95, 123), TextDecoration.ITALIC))
                val oldDamage = oldDamageModifier.amount
                val newDamageModifier = AttributeModifier(
                    Identifiers.ATTACK_DAMAGE_UUID,
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
        /*-----------------------------------------------------------------------------------------------*/
        // Trims
        if (eventResult.itemMeta is ArmorMeta) {
            val armorMeta = eventResult.itemMeta as ArmorMeta
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
                    // CURRENTLY DOES NOT WORK AS NO EVENT RESULT IS MADE
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
        /*-----------------------------------------------------------------------------------------------*/
        // Netherite
        if (addition.type == Material.NETHERITE_INGOT && eventResult.itemMeta.hasCustomModelData()) {
            val newItem = event.inventory.result!!.clone()
            if (newItem.hasTag(ItemTags.NETHERITE_TOOL)) return
            val oldDamageModifier = newItem.itemMeta.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE)?.first {
                it.uniqueId == Identifiers.ATTACK_DAMAGE_UUID
            } ?: return
            newItem.itemMeta = newItem.itemMeta.clone().also { meta ->
                val oldDamage = oldDamageModifier.amount
                val newDamageModifier = AttributeModifier(
                    Identifiers.ATTACK_DAMAGE_UUID,
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
        /*-----------------------------------------------------------------------------------------------*/
        // CURRENTLY DOES MC DOES NOT COPY RESULT NBT
        if (!eventResult.itemMeta.hasCustomModelData()) {
            // Check Recipes
            if (!recipe.result.hasItemMeta()) { return }
            if (!recipe.result.itemMeta.hasCustomModelData()) { return }
            event.inventory.result = recipe.result
            event.result = recipe.result
        }
    }
}
