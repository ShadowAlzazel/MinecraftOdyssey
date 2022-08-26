package me.shadowalzazel.mcodyssey.items.weaponTypes

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

open class Claymore (claymoreName: String, claymoreMaterial: Material, claymoreCustomModel: Int, private val attackDamage: Double, private val attackSpeed: Double) :
    OdysseyItem(claymoreName, claymoreMaterial, Component.text(claymoreName), customModel = claymoreCustomModel) {


    //
    override fun createItemStack(amount: Int): ItemStack {
        val newClaymore = super.createItemStack(amount)

        // Assign item meta
        newClaymore.itemMeta = newClaymore.itemMeta.also {
            val removeAttackSpeedUUID: UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9565ACA3")
            val odysseyAttackDamageUUID: UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A34DB5CF")
            val odysseyAttackSpeedUUID: UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9985ACA3")
            //val odysseyAttackKnockBackUUID: UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE8785ACA3")
            val someAttackSpeedStat = AttributeModifier(removeAttackSpeedUUID, "generic.attack_speed", -4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
            val odysseyAttackDamageStat = AttributeModifier(odysseyAttackDamageUUID, "generic.attack_damage", attackDamage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
            val odysseyAttackSpeedStat = AttributeModifier(odysseyAttackSpeedUUID, "generic.attack_speed", attackSpeed, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
            //val odysseyAttackKnockBackStat = AttributeModifier(odysseyAttackKnockBackUUID, "generic.attack_speed", 10.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)

            it.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, someAttackSpeedStat)
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, odysseyAttackDamageStat)
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, odysseyAttackSpeedStat)
            //it.addAttributeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, odysseyAttackKnockBackStat)
            it.setCustomModelData(customModel)
            //println(it.attributeModifiers)
        }

        return newClaymore
    }

}