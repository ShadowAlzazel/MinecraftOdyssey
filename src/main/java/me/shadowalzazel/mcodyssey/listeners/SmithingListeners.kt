package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.enchantments.api.SlotColors
import me.shadowalzazel.mcodyssey.constants.AttributeTags
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.items.utility.ToolMiningManager
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
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object SmithingListeners : Listener, DataTagManager, ToolMiningManager {

    /*-----------------------------------------------------------------------------------------------*/

    @Suppress("UnstableApiUsage")
    @EventHandler
    fun smithingHandler(event: PrepareSmithingEvent) {
        val recipe = event.inventory.recipe
        // Avoid Conflict with enchanting tomes
        if (recipe?.result?.type == Material.ENCHANTED_BOOK) return
        // Assign vars to non null values
        val equipment = event.inventory.inputEquipment ?: return
        val result = event.result
        val addition = event.inventory.inputMineral ?: return

        /*-----------------------------------------------------------------------------------------------*/
        // Engraving
        if (result?.type == Material.AMETHYST_SHARD && addition.type == Material.AMETHYST_SHARD) {
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
        // Custom Upgrades
        else if (result?.type == Material.IRON_INGOT) {
            event.result = ItemStack(Material.AIR)
            if (!addition.hasItemMeta()) return
            if (!addition.itemMeta.hasCustomModelData()) return
            // Check if not custom
            val materialType = equipment.getStringTag(ItemDataTags.MATERIAL_TYPE)
            // Check to make sure final
            val notUpgradableMaterials = listOf(
                "titanium", "iridium", "mithril", "anodized_titanium", "silver", "soul_steel")
            if (materialType in notUpgradableMaterials) return
            // Switch to
            val additionName = addition.getItemIdentifier()
            val templateName = event.inventory.inputTemplate?.getItemIdentifier() ?: return
            when(additionName) {
                "soul_steel_ingot" -> {
                    if (templateName != "soul_steel_upgrade_template") return
                    if (equipment.hasTag(ItemDataTags.SOUL_STEEL_TOOL)) return
                    if (!isIron(equipment.type)) return
                    // Apply
                    val item = equipment.clone()
                    item.addTag(ItemDataTags.SOUL_STEEL_TOOL)
                    item.addStringTag(ItemDataTags.MATERIAL_TYPE, "soul_steel")
                    // Get new model
                    upgradeModel(item, ItemModels.SOUL_STEEL_MATERIAL_PRE)
                    upgradeDamage(item, 1.0)
                    // Misc Upgrades
                    setStackItemName(item)
                    val newMeta = item.itemMeta as Damageable
                    newMeta.setMaxDamage(666)
                    item.itemMeta = newMeta
                    event.result = item
                }
                "titanium_ingot" -> {
                    if (templateName != "titanium_upgrade_template") return
                    if (equipment.hasTag(ItemDataTags.TITANIUM_TOOL)) return
                    if (!isIron(equipment.type)) return
                    // Apply
                    val item = equipment.clone()
                    item.addTag(ItemDataTags.TITANIUM_TOOL)
                    item.addStringTag(ItemDataTags.MATERIAL_TYPE, "titanium")
                    // Get new model
                    upgradeModel(item, ItemModels.TITANIUM_MATERIAL_PRE)
                    upgradeDamage(item, 1.0)
                    modifyAttackSpeed(item, 1.1)
                    // Misc Upgrades
                    setStackItemName(item)
                    val newMeta = item.itemMeta as Damageable
                    newMeta.setMaxDamage(1002)
                    item.itemMeta = newMeta
                    event.result = item
                }
                "iridium_ingot" -> {
                    if (templateName != "iridium_upgrade_template") return
                    if (equipment.hasTag(ItemDataTags.IRIDIUM_TOOL)) return
                    if (!isDiamond(equipment.type)) return
                    // Apply
                    val item = equipment.clone()
                    item.addTag(ItemDataTags.IRIDIUM_TOOL)
                    item.addStringTag(ItemDataTags.MATERIAL_TYPE, "iridium")
                    // Get new model
                    upgradeModel(item, ItemModels.IRIDIUM_MATERIAL_PRE)
                    upgradeDamage(item, 1.0)
                    modifyAttackSpeed(item, 0.9)
                    // Misc Upgrades
                    setStackItemName(item)
                    val newMeta = item.itemMeta as Damageable
                    newMeta.setMaxDamage(3178)
                    item.itemMeta = newMeta
                    // Change minecraft material
                    val newType = when(item.type) {
                        Material.DIAMOND_SWORD -> Material.IRON_SWORD
                        Material.DIAMOND_PICKAXE -> Material.IRON_PICKAXE
                        Material.DIAMOND_AXE -> Material.IRON_AXE
                        Material.DIAMOND_SHOVEL -> Material.IRON_SHOVEL
                        Material.DIAMOND_HOE -> Material.IRON_HOE
                        else -> null
                    }
                    if (newType != null) {
                        val newItem = ItemStack(newType)
                        newItem.itemMeta = item.itemMeta
                        event.result = newItem
                    }
                    else {
                        event.result = item
                    }
                }
                "mithril_ingot" -> {
                    if (templateName != "mithril_upgrade_template") return
                    if (equipment.hasTag(ItemDataTags.MITHRIL_TOOL)) return
                    if (!isDiamond(equipment.type)) return
                    // Apply
                    val item = equipment.clone()
                    item.addTag(ItemDataTags.MITHRIL_TOOL)
                    item.addStringTag(ItemDataTags.MATERIAL_TYPE, "mithril")
                    // Get new model
                    upgradeModel(item, ItemModels.MITHRIL_MATERIAL_PRE)
                    upgradeDamage(item, 2.0)
                    // Misc Upgrades
                    setStackItemName(item)
                    val newMeta = item.itemMeta as Damageable
                    newMeta.setMaxDamage(1789)
                    item.itemMeta = newMeta
                    // Change minecraft material
                    val newType = when(item.type) {
                        Material.DIAMOND_SWORD -> Material.IRON_SWORD
                        Material.DIAMOND_PICKAXE -> Material.IRON_PICKAXE
                        Material.DIAMOND_AXE -> Material.IRON_AXE
                        Material.DIAMOND_SHOVEL -> Material.IRON_SHOVEL
                        Material.DIAMOND_HOE -> Material.IRON_HOE
                        else -> null
                    }
                    if (newType != null) {
                        val newItem = ItemStack(newType)
                        newItem.itemMeta = item.itemMeta
                        event.result = newItem
                    }
                    else {
                        event.result = item
                    }
                }
            }
            // set mining
            // Tools with mining ToolComponent
            val newResult = event.result!!
            val toolType = newResult.getStringTag(ItemDataTags.TOOL_TYPE) ?: materialTypeMap(newResult.type)
            val toolMaterial = newResult.getStringTag(ItemDataTags.MATERIAL_TYPE)!!
            val mineableTags = getTypeMineableTags(toolType!!)
            // Set tool
            val newMeta = newResult.itemMeta
            if (mineableTags != null) {
                newMeta.setTool(null)
                val newToolComponent = createMiningToolComponent(newResult.itemMeta.tool, toolMaterial, mineableTags)
                if (newToolComponent != null) {
                    newToolComponent.damagePerBlock = 1
                    newMeta.setTool(newToolComponent)
                }
            }
            newResult.itemMeta = newMeta
            event.result = newResult
        }
        /*-----------------------------------------------------------------------------------------------*/
        // Netherite
        else if (addition.type == Material.NETHERITE_INGOT && result?.itemMeta?.hasCustomModelData() == true) {
            val item = event.inventory.result!!.clone()
            if (item.hasTag(ItemDataTags.NETHERITE_TOOL)) return
            // Get new model
            val weaponType = item.getStringTag(ItemDataTags.TOOL_TYPE) ?: "none"
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
            setStackItemName(item)
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
        else if (equipment.itemMeta is ArmorMeta) {
            val armorMeta = (equipment.itemMeta as ArmorMeta).clone()
            // Get IDs
            val trimMaterial = event.inventory.inputMineral ?: return
            val trimTemplate = event.inventory.inputTemplate ?: return
            if (trimMaterial.type == Material.NETHERITE_INGOT) return // Fix netherite upgrade bug
            val materialName = trimMaterial.getItemIdentifier()
            val patternName = trimTemplate.getItemIdentifier()
            // Get meta
            val resultMeta = if (event.result?.hasItemMeta() == true) event.result?.itemMeta else null
            // Get Trim Pattern
            val finalPattern: TrimPattern? = when (patternName) {
                "imperial_armor_trim_smithing_template" -> TrimPatterns.IMPERIAL
                else -> null
            } ?: if (resultMeta is ArmorMeta) {
                resultMeta.trim?.pattern
            } else {
                null
            }
            if (finalPattern == null) return
            // Get Trim Material
            val finalMaterial: TrimMaterial? = when (materialName) {
                "alexandrite" -> TrimMaterials.ALEXANDRITE
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
            } ?: if (resultMeta is ArmorMeta) {
                resultMeta.trim?.material
            }
            else {
                when(trimMaterial.type) {
                    Material.IRON_INGOT -> TrimMaterial.IRON
                    Material.GOLD_INGOT -> TrimMaterial.GOLD
                    Material.REDSTONE -> TrimMaterial.REDSTONE
                    Material.DIAMOND -> TrimMaterial.DIAMOND
                    Material.EMERALD -> TrimMaterial.EMERALD
                    Material.AMETHYST_SHARD -> TrimMaterial.AMETHYST
                    Material.NETHERITE_INGOT -> TrimMaterial.NETHERITE
                    Material.LAPIS_LAZULI -> TrimMaterial.LAPIS
                    Material.QUARTZ -> TrimMaterial.QUARTZ
                    Material.COPPER_INGOT -> TrimMaterial.COPPER
                    Material.OBSIDIAN -> TrimMaterials.OBSIDIAN
                    else -> null
                }
            }
            if (finalMaterial == null) return

            // Apply new trim
            val newTrim = ArmorTrim(finalMaterial, finalPattern)
            armorMeta.trim = newTrim
            val armorResult = equipment.clone()
            armorResult.itemMeta = armorMeta
            event.result = armorResult
            event.inventory.result = armorResult
            return
        }
        /*-----------------------------------------------------------------------------------------------*/
        // CURRENTLY DOES MC DOES NOT COPY RESULT NBT
        if (result?.itemMeta?.hasCustomModelData() != true) {
            // Check Recipes
            if (recipe == null) return
            if (!recipe.result.hasItemMeta()) return
            if (!recipe.result.itemMeta.hasCustomModelData()) return
            event.inventory.result = recipe.result
            event.result = recipe.result
        }
    }


    /*-----------------------------------------------------------------------------------------------*/

    @Suppress("UnstableApiUsage")
    private fun upgradeDamage(item: ItemStack, bonus: Double = 1.0) {
        // Get Damage
        val oldDamageModifier = item.itemMeta.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE)?.first {
            it.name == AttributeTags.ITEM_BASE_ATTACK_DAMAGE
        } ?: return
        val newDamage = oldDamageModifier.amount + bonus
        val slots = EquipmentSlotGroup.MAINHAND
        val nameKey = NamespacedKey(Odyssey.instance, AttributeTags.ITEM_BASE_ATTACK_DAMAGE)
        val newMeta = item.itemMeta
        // Set Damage
        newMeta.also {
            it.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, oldDamageModifier)
            val newModifier = AttributeModifier(nameKey, newDamage, AttributeModifier.Operation.ADD_NUMBER, slots)
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, newModifier)
        }
        item.itemMeta = newMeta
    }

    private fun modifyAttackSpeed(item: ItemStack, speed: Double = 1.0) {
        // Get Damage
        val oldDamageModifier = item.itemMeta.getAttributeModifiers(Attribute.GENERIC_ATTACK_SPEED)?.first {
            it.name == AttributeTags.ITEM_BASE_ATTACK_SPEED
        } ?: return
        val newSpeed = oldDamageModifier.amount * speed
        val slots = EquipmentSlotGroup.MAINHAND
        val nameKey = NamespacedKey(Odyssey.instance, AttributeTags.ITEM_BASE_ATTACK_SPEED)
        val newMeta = item.itemMeta
        // Set Damage
        newMeta.also {
            it.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, oldDamageModifier)
            val newModifier = AttributeModifier(nameKey, newSpeed, AttributeModifier.Operation.ADD_NUMBER, slots)
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, newModifier)
        }
        item.itemMeta = newMeta
    }

    private fun upgradeModel(item: ItemStack, newModelPre: Int) {
        val weaponType = item.getStringTag(ItemDataTags.TOOL_TYPE) ?: materialTypeMap(item.type)
        if (weaponType == null) return
        val weaponModel = weaponModelMap(weaponType)
        val newModel = (newModelPre * 100) + (weaponModel)
        // Set Model
        val newMeta = item.itemMeta
        newMeta.also {
            it.setCustomModelData(newModel)
        }
        item.itemMeta = newMeta
    }

    private fun isDiamond(material: Material): Boolean {
        return when(material) {
            Material.DIAMOND_SWORD, Material.DIAMOND_PICKAXE, Material.DIAMOND_AXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE -> true
            else -> false
        }
    }

    private fun weaponModelMap(weaponType: String): Int {
        return when(weaponType) {
            // Base
            "sword" -> ItemModels.SWORD
            "pickaxe" -> ItemModels.PICKAXE
            "axe" -> ItemModels.AXE
            "shovel" -> ItemModels.SHOVEL
            "hoe" -> ItemModels.HOE
            // Odyssey
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

    private fun materialTypeMap(material: Material): String? {
        return when(material) {
            Material.WOODEN_SWORD, Material.GOLDEN_SWORD, Material.STONE_SWORD,
            Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD -> {
                "sword"
            }
            Material.WOODEN_PICKAXE, Material.GOLDEN_PICKAXE, Material.STONE_PICKAXE,
            Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE -> {
                "pickaxe"
            }
            Material.WOODEN_AXE, Material.GOLDEN_AXE, Material.STONE_AXE,
            Material.IRON_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE -> {
                "axe"
            }
            Material.WOODEN_SHOVEL, Material.GOLDEN_SHOVEL, Material.STONE_SHOVEL,
            Material.IRON_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL -> {
                "shovel"
            }
            Material.WOODEN_HOE, Material.GOLDEN_HOE, Material.STONE_HOE,
            Material.IRON_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE -> {
                "hoe"
            }
            else -> {
                null
            }
        }
    }

    private fun isIron(material: Material): Boolean {
        return when(material) {
            Material.IRON_SWORD, Material.IRON_PICKAXE, Material.IRON_AXE, Material.IRON_SHOVEL, Material.IRON_HOE -> true
            else -> false
        }
    }

    private fun setStackItemName(item: ItemStack) {
        val weaponType = item.getStringTag(ItemDataTags.TOOL_TYPE) ?: materialTypeMap(item.type)
        val newName = "${item.getStringTag(ItemDataTags.MATERIAL_TYPE)}_$weaponType"
        val newMeta = item.itemMeta
        newMeta.itemName(Component.text(newName))
        item.itemMeta = newMeta
    }

    /*-----------------------------------------------------------------------------------------------*/

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
    private fun LivingEntity.sendBarMessage(reason: String, color: TextColor = SlotColors.ENCHANT.color) {
        this.sendActionBar(
            Component.text(
                reason,
                color
            )
        )
    }

}
