package me.shadowalzazel.mcodyssey.items.weapons.kinds

import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class Staff (staffName: String, staffMaterial: Material, staffCustomModel: Int, private val attackDamage: Double, private val attackSpeed: Double) :
    OdysseyItem(staffName, staffMaterial, Component.text(staffName), customModel = staffCustomModel) {

    //
    override fun createItemStack(amount: Int): ItemStack {
        val newStaff = super.createItemStack(amount)
        // Assign item meta
        newStaff.itemMeta = this.attributeWeaponMeta(newStaff.itemMeta, attackDamage, attackSpeed)
        return newStaff
    }

}