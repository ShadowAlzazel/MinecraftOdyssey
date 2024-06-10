package me.shadowalzazel.mcodyssey.items.creators

import me.shadowalzazel.mcodyssey.enchantments.api.SlotColors
import me.shadowalzazel.mcodyssey.constants.AttributeIDs
import me.shadowalzazel.mcodyssey.constants.DataKeys
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.enchantments.api.EnchantabilityPointsManager
import me.shadowalzazel.mcodyssey.items.Exotics
import me.shadowalzazel.mcodyssey.items.Ingredients
import me.shadowalzazel.mcodyssey.items.Miscellaneous
import me.shadowalzazel.mcodyssey.items.Potions
import me.shadowalzazel.mcodyssey.items.Potions.createPotionStack
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.persistence.PersistentDataType

interface ItemCreator : ExoticCreator, EnchantabilityPointsManager {

    fun OdysseyItem.createStack(amount: Int = 1): ItemStack {
        val itemStack = ItemStack(overrideMaterial, amount).also {
            // Set Variables
            val meta  = it.itemMeta
            meta.setCustomModelData(customModel)
            meta.persistentDataContainer.set(DataKeys.ITEM_KEY, PersistentDataType.STRING, itemName) // Change for 1.20.5 to itemName component
            meta.displayName(Component.text(customName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
            // Optional Variables
            if (lore != null) {
                meta.lore(lore)
            }
            // Assemble meta
            it.itemMeta = meta
        }
        return itemStack
    }

    fun OdysseyItem.createArcaneBook(enchantment: OdysseyEnchantment, level: Int = 1) : ItemStack {
        if (itemName != "arcane_book") return ItemStack(Material.AIR)
        val newBook = this.newItemStack(1)
        newBook.itemMeta = (newBook.itemMeta as EnchantmentStorageMeta).also {
            // Set lore, description, and display name
            val enchantName = enchantment.displayName(level)
            val newToolTip = enchantment.getDescriptionToolTip(level)
            val textLore = mutableListOf(enchantName) + Component.text("") + newToolTip
            val bookText = this.customName + " - "
            val bookName = Component.text(bookText).color(SlotColors.ARCANE.color)
            val fullName = bookName.append(enchantName.color(SlotColors.ARCANE.color))
            it.displayName(fullName)
            it.lore(textLore)
        }
        //newBook.addOdysseyEnchantment(enchantment, level, true)
        return newBook
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
            val bookName = Component.text(this.customName + " - ").color(SlotColors.ARCANE.color)
            val fullName = bookName.append(enchantment.displayName(level).color(SlotColors.ARCANE.color))
            it.displayName(fullName)
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

    fun OdysseyItem.createArmor(bonus: Double = 0.0): ItemStack {
        val armor = this.newItemStack(1)
        val meta = armor.itemMeta.also {
            val armorModifier = AttributeModifier(AttributeIDs.ARMOR_HELMET_UUID, "odyssey.armor_helmet", bonus, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD)
            it.addAttributeModifier(Attribute.GENERIC_ARMOR, armorModifier)
        }
        armor.itemMeta = meta
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