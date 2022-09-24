package me.shadowalzazel.mcodyssey.items.odyssey

import me.shadowalzazel.mcodyssey.constants.ModifiersUUIDs
import net.kyori.adventure.text.Component
//import net.kyori.adventure.text.TextComponent
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

open class OdysseyItem(val name: String,
                       internal val material: Material,
                       internal val odysseyDisplayName: Component? = null,
                       internal val odysseyLore: List<Component>? = null,
                       internal val customModel: Int? = null,
                       private val itemEnchantments: Map<Enchantment, Int>? = null) {

    val romanNumeralList = mapOf(1 to "I", 2 to "II", 3 to "III", 4 to "IV", 5 to "V", 6 to "VI", 7 to "VII", 8 to "VIII", 9 to "IX", 10 to "X")

    open fun createItemStack(amount: Int): ItemStack {
        val newOdysseyItemStack = ItemStack(material, amount)

        // Assign item meta
        newOdysseyItemStack.itemMeta = (newOdysseyItemStack.itemMeta as ItemMeta).also {
            // Add enchantments, lore, display name, and custom model if applicable
            if (itemEnchantments != null) { for (enchant in itemEnchantments) { it.addEnchant(enchant.key, enchant.value, true) } }
            if (odysseyLore != null) { it.lore(odysseyLore) } // FIX
            if (odysseyDisplayName != null) { it.displayName(odysseyDisplayName) }
            it.setCustomModelData(customModel)
        }

        return newOdysseyItemStack
    }

    // Function to add weapon attributes for odyssey items
    internal fun attributeWeaponMeta(weaponMeta: ItemMeta, weaponDamage: Double, weaponSpeed: Double): ItemMeta {
        // Add attributes
        weaponMeta.also {
            // Modify Attack Speed
            val someAttackSpeedStat = AttributeModifier(ModifiersUUIDs.ATTACK_SPEED_RESET_UUID, "odyssey.attack_speed", -4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
            val odysseyAttackSpeedStat = AttributeModifier(ModifiersUUIDs.ATTACK_SPEED_UUID, "odyssey.attack_speed", weaponSpeed, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, someAttackSpeedStat)
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, odysseyAttackSpeedStat)

            // Modify Attack Damage
            val odysseyAttackDamageStat = AttributeModifier(ModifiersUUIDs.ATTACK_DAMAGE_UUID, "odyssey.attack_damage", weaponDamage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, odysseyAttackDamageStat)

            // Modify Knock Back
            //val odysseyAttackKnockBackUUID: UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE8785ACA3")
            //val odysseyAttackKnockBackStat = AttributeModifier(odysseyAttackKnockBackUUID, "generic.attack_speed", 10.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
            //it.addAttributeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, odysseyAttackKnockBackStat)

            it.setCustomModelData(customModel)
            //println(it.attributeModifiers)
        }

        return weaponMeta
    }

}