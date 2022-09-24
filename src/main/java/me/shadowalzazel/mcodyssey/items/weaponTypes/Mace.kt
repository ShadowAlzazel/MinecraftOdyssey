package me.shadowalzazel.mcodyssey.items.weaponTypes

import me.shadowalzazel.mcodyssey.items.odyssey.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class Mace (maceName: String, maceMaterial: Material, maceCustomModel: Int, private val attackDamage: Double, private val attackSpeed: Double) :
    OdysseyItem(maceName, maceMaterial, Component.text(maceName), customModel = maceCustomModel) {

    //
    override fun createItemStack(amount: Int): ItemStack {
        val newMace = super.createItemStack(amount)
        // Assign item meta
        newMace.itemMeta = this.attributeWeaponMeta(newMace.itemMeta, attackDamage, attackSpeed)
        return newMace
    }

    // DOUBLE BLADED

}