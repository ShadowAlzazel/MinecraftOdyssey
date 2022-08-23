package me.shadowalzazel.mcodyssey.items.weapons

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

// NEUTRONIUM_BARK_SWORD
/*
object NeutroniumBarkSword : OdysseyItem("Neutronium-Bark Sword", Material.NETHERITE_SWORD) {
    override val odysseyDisplayName: String = "${ChatColor.AQUA}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GOLD}${ChatColor.ITALIC}A sword made from neutronium bark")

    //
    override fun createItemStack(amount: Int): ItemStack {
        val newItem = super.createItemStack(amount)
        val newItemMeta = newItem.itemMeta

        val odysseyAttackDamageUUID: UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A28DB5CF")
        val odysseyAttackSpeedUUID: UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A29DB5CF")
        val someAttackDamageStat = AttributeModifier(odysseyAttackDamageUUID, "generic.attack_damage", 9.42, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
        val someAttackSpeedStat = AttributeModifier(odysseyAttackSpeedUUID, "generic.attack_speed", 1.62, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)

        newItemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, someAttackSpeedStat)
        newItemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, someAttackDamageStat)
        newItem.itemMeta = newItemMeta
        return newItem
    }

}

 */