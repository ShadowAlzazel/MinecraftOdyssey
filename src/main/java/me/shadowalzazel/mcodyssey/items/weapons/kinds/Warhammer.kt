package me.shadowalzazel.mcodyssey.items.weapons.kinds

import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class Warhammer (warhammerName: String, warhammerMaterial: Material, warhammerCustomModel: Int, private val attackDamage: Double, private val attackSpeed: Double) :
    OdysseyItem(warhammerName, warhammerMaterial, Component.text(warhammerName), customModel = warhammerCustomModel) {

    //
    override fun createItemStack(amount: Int): ItemStack {
        val newWarhammer = super.createItemStack(amount)
        // Assign item meta
        newWarhammer.itemMeta = this.attributeWeaponMeta(newWarhammer.itemMeta, attackDamage, attackSpeed)
        return newWarhammer
    }


    // CHANGE TO BIG AND LONG
}