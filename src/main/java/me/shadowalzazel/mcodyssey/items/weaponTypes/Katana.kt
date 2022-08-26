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
        val newKatana = super.createItemStack(amount)

        // Assign item meta
        newKatana.itemMeta = newKatana.itemMeta.also {
            val removeAttackSpeedUUID: UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9565ACA3")
            val odysseyAttackDamageUUID: UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A34DB5CF")
            val odysseyAttackSpeedUUID: UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9985ACA3")
            val someAttackSpeedStat = AttributeModifier(removeAttackSpeedUUID, "generic.attack_speed", -4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
            val odysseyAttackDamageStat = AttributeModifier(odysseyAttackDamageUUID, "generic.attack_damage", attackDamage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
            val odysseyAttackSpeedStat = AttributeModifier(odysseyAttackSpeedUUID, "odyssey.attack_speed", 1.85, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)

            it.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, someAttackSpeedStat)
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, odysseyAttackDamageStat)
            it.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, odysseyAttackSpeedStat)
            it.setCustomModelData(customModel)
            //println(it.attributeModifiers)
        }

        return newKatana
    }

}