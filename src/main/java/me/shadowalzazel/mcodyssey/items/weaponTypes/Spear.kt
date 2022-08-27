package me.shadowalzazel.mcodyssey.items.weaponTypes

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class Spear (spearName: String, spearMaterial: Material, spearCustomModel: Int, private val attackDamage: Double, private val attackSpeed: Double) :
    OdysseyItem(spearName, spearMaterial, Component.text(spearName), customModel = spearCustomModel) {


    //
    override fun createItemStack(amount: Int): ItemStack {
        val newSpear = super.createItemStack(amount)
        // Assign item meta
        newSpear.itemMeta = this.attributeWeaponMeta(newSpear.itemMeta, attackDamage, attackSpeed)
        return newSpear
    }

}