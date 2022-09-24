package me.shadowalzazel.mcodyssey.items.weaponTypes

import me.shadowalzazel.mcodyssey.items.odyssey.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class Claymore (claymoreName: String, claymoreMaterial: Material, claymoreCustomModel: Int, private val attackDamage: Double, private val attackSpeed: Double) :
    OdysseyItem(claymoreName, claymoreMaterial, Component.text(claymoreName), customModel = claymoreCustomModel) {


    //
    override fun createItemStack(amount: Int): ItemStack {
        val newClaymore = super.createItemStack(amount)
        // Assign item meta
        newClaymore.itemMeta = this.attributeWeaponMeta(newClaymore.itemMeta, attackDamage, attackSpeed)
        return newClaymore
    }

}