package me.shadowalzazel.mcodyssey.datagen.items

import me.shadowalzazel.mcodyssey.util.DataKeys
import me.shadowalzazel.mcodyssey.util.EnchantabilityHandler
import me.shadowalzazel.mcodyssey.common.items.custom.Potions.createPotionStack
import me.shadowalzazel.mcodyssey.common.items.custom.Glyphsherds.createPresetSherdStack
import me.shadowalzazel.mcodyssey.common.items.OdysseyItem
import me.shadowalzazel.mcodyssey.common.items.custom.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.persistence.PersistentDataType

interface ItemCreator : ExoticCreator, EnchantabilityHandler {

    fun OdysseyItem.createStack(amount: Int = 1): ItemStack {
        val itemStack = ItemStack(overrideMaterial, amount).also {
            // Set Variables
            val meta  = it.itemMeta
            meta.itemModel = DataKeys.newKey(itemName)
            meta.persistentDataContainer.set(DataKeys.ITEM_KEY, PersistentDataType.STRING, itemName) // Change for 1.20.5 to itemName component
            meta.displayName(Component.text(customName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
            meta.itemName(Component.text(this.itemName))
            // Optional Variables
            if (lore != null) {
                meta.lore(lore)
            }
            // Assemble meta
            it.itemMeta = meta
        }
        return itemStack
    }

    fun OdysseyItem.createArcaneBookStack(enchantment: Enchantment, level: Int = 1) : ItemStack {
        if (itemName != "arcane_book") return ItemStack(Material.AIR)
        val newBook = this.newItemStack(1)
        newBook.itemMeta = (newBook.itemMeta as EnchantmentStorageMeta).also {
            // Set lore -> description and enchantability Cost
            val pointCost = enchantment.enchantabilityCost(level)
            val costToolTip = createEnchantLoreComponent(enchantment, level, pointCost)
            val descriptionToolTip = enchantment.getDescriptionTooltip(level)
            val fullLore = listOf(costToolTip) + descriptionToolTip
            it.lore(fullLore)
            // Set name
            val bookName = Component.text(this.customName + ": ")
            val fullName = bookName.append(enchantment.displayName(level).color(bookName.color())).color(bookName.color())
            it.displayName(fullName.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
            it.addStoredEnchant(enchantment, level, true)
            it.addItemFlags(ItemFlag.HIDE_STORED_ENCHANTS)
        }
        return newBook
    }

    fun OdysseyItem.createTome() : ItemStack {
        val tome = this.newItemStack()
        val meta = tome.itemMeta
        meta.displayName(
            Component.text(customName, TextColor.color(255, 255, 85))
        )
        tome.itemMeta = meta
        return tome
    }

    @Suppress("UnstableApiUsage")
    fun OdysseyItem.createArmor(bonus: Double = 0.0): ItemStack {
        val armor = this.newItemStack(1)
        armor.addArmorAttribute(bonus, "bonus_armor", EquipmentSlotGroup.ARMOR)
        return armor
    }


    // General Creator Class
    fun createItemFromName(name: String, amount: Int = 1): ItemStack? {
        val odysseyStack = createStackFromName(name, amount)
        if (odysseyStack == null) { // Fallback class
            val odysseyItem = findFromName(name) ?: return null
            return odysseyItem.createStack(amount)
        }
        return odysseyStack
    }

    // Create itemStack from matching name
    fun createStackFromName(name: String, amount: Int): ItemStack? {
        return when(name) {
            "tome_of_discharge" -> Miscellaneous.TOME_OF_DISCHARGE.newItemStack(amount)
            "tome_of_embrace" -> Miscellaneous.TOME_OF_EMBRACE.newItemStack(amount)
            "tome_of_promotion" -> Miscellaneous.TOME_OF_PROMOTION.newItemStack(amount)
            "tome_of_harmony" -> Miscellaneous.TOME_OF_HARMONY.newItemStack(amount)
            "tome_of_replication" -> Miscellaneous.TOME_OF_REPLICATION.newItemStack(amount)
            "tome_of_banishment" -> Miscellaneous.TOME_OF_BANISHMENT.newItemStack(amount)
            "tome_of_imitation" -> Miscellaneous.TOME_OF_IMITATION.newItemStack(amount)
            "tome_of_expenditure" -> Miscellaneous.TOME_OF_EXPENDITURE.newItemStack(amount)
            "tome_of_avarice" -> Miscellaneous.TOME_OF_AVARICE.newItemStack(amount)
            "tome_of_polymerization" -> Miscellaneous.TOME_OF_POLYMERIZATION.newItemStack(amount)
            "crystalline_potion" -> Potions.CRYSTALLINE_POTION.createPotionStack()
            "irradiated_rod" -> Ingredients.IRRADIATED_ROD.newItemStack(amount)
            "irradiated_shard" -> Ingredients.IRRADIATED_SHARD.newItemStack(amount)
            "ectoplasm" -> Ingredients.ECTOPLASM.newItemStack(amount)
            "coagulated_blood" -> Ingredients.COAGULATED_BLOOD.newItemStack(amount)
            "warden_entrails" -> Ingredients.WARDEN_ENTRAILS.newItemStack(amount)
            "soul_quartz" -> Ingredients.SOUL_QUARTZ.newItemStack(amount)
            "kunzite" -> Ingredients.KUNZITE.newItemStack(amount)
            "alexandrite" -> Ingredients.ALEXANDRITE.newItemStack(amount)
            "jade" -> Ingredients.JADE.newItemStack(amount)
            "ruby" -> Ingredients.RUBY.newItemStack(amount)
            "iridium_ingot" -> Ingredients.IRIDIUM_INGOT.newItemStack(amount)
            "anodized_titanium_ingot" -> Ingredients.ANODIZED_TITANIUM_INGOT.newItemStack(amount)
            "heated_titanium_ingot" -> Ingredients.HEATED_TITANIUM_INGOT.newItemStack(amount)
            "titanium_ingot" -> Ingredients.TITANIUM_INGOT.newItemStack(amount)
            "mithril_ingot" -> Ingredients.MITHRIL_INGOT.newItemStack(amount)
            "silver_ingot" -> Ingredients.SILVER_INGOT.newItemStack(amount)
            "silver_nugget" -> Ingredients.SILVER_NUGGET.newItemStack(amount)
            "soul_steel_ingot" -> Ingredients.SOUL_STEEL_INGOT.newItemStack(amount)
            "knight_breaker" -> Exotics.KNIGHT_BREAKER.createExoticWeapon()
            "shogun_lightning" -> Exotics.SHOGUN_LIGHTNING.createExoticWeapon()
            "abzu_blade" -> Exotics.ABZU_BLADE.createExoticWeapon()
            "excalibur" -> Exotics.EXCALIBUR.createExoticWeapon()
            "frost_fang" -> Exotics.FROST_FANG.createExoticWeapon()
            "elucidator" -> Exotics.ELUCIDATOR.createExoticWeapon()
            "guard_runesherd" -> Glyphsherds.GUARD_RUNESHERD.createPresetSherdStack(amount)
            else -> null
        }
    }

    // FALLBACK
    fun findFromName(name: String): OdysseyItem? {
        return when(name) {
            "iridium_ingot" -> Ingredients.IRIDIUM_INGOT
            "anodized_titanium_ingot" -> Ingredients.ANODIZED_TITANIUM_INGOT
            "titanium_ingot" -> Ingredients.TITANIUM_INGOT
            "mithril_ingot" -> Ingredients.MITHRIL_INGOT
            "silver_ingot" -> Ingredients.SILVER_INGOT
            "soul_steel_ingot" -> Ingredients.SOUL_STEEL_INGOT
            else -> null
        }
    }


}