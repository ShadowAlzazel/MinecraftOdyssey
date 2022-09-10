package me.shadowalzazel.mcodyssey.items.weaponTypes

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

// Rapier
open class Rapier(rapierName: String, rapierMaterial: Material, rapierCustomModel: Int, private val attackDamage: Double, private val attackSpeed: Double) :
    OdysseyItem(rapierName, rapierMaterial, Component.text(rapierName), customModel = rapierCustomModel) {

    //
    override fun createItemStack(amount: Int): ItemStack {
        val newKatana = super.createItemStack(amount)
        // Assign item meta
        newKatana.itemMeta = this.attributeWeaponMeta(newKatana.itemMeta, attackDamage, attackSpeed)
        return newKatana
    }

}