package me.shadowalzazel.mcodyssey.items.weapons.kinds

import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class Guandao (guandaoName: String, guandaoMaterial: Material, guandaoCustomModel: Int, private val attackDamage: Double, private val attackSpeed: Double) :
    OdysseyItem(guandaoName, guandaoMaterial, Component.text(guandaoName), customModel = guandaoCustomModel) {

    //
    override fun createItemStack(amount: Int): ItemStack {
        val newGuandao = super.createItemStack(amount)
        // Assign item meta
        newGuandao.itemMeta = this.attributeWeaponMeta(newGuandao.itemMeta, attackDamage, attackSpeed)
        return newGuandao
    }

    // Kwan Dao

}