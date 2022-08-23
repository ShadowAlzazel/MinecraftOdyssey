package me.shadowalzazel.mcodyssey.items.weaponTypes

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

// Katana
open class Katana(katanaName: String, katanaMaterial: Material, katanaCustomModel: Int, private val attackDamage: Double, private val attackSpeed: Double) :
    OdysseyItem(katanaName, katanaMaterial, Component.text(katanaName), customModel = katanaCustomModel) {


    //
    override fun createItemStack(amount: Int): ItemStack {
        val newItem = super.createItemStack(amount)
        val newItemMeta = newItem.itemMeta

        val odysseyAttackDamageUUID: UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A646B5CF")
        val odysseyAttackSpeedUUID: UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9464ACA3")
        val someAttackSpeedStat = AttributeModifier(odysseyAttackSpeedUUID, "generic.attack_speed", attackSpeed, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
        val someAttackDamageStat = AttributeModifier(odysseyAttackDamageUUID, "generic.attack_damage", attackDamage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)

        newItemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, someAttackSpeedStat)
        newItemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, someAttackDamageStat)
        newItemMeta.setCustomModelData(customModel)
        println(newItemMeta.attributeModifiers)
        newItem.itemMeta = newItemMeta
        return newItem
    }

}