package me.shadowalzazel.mcodyssey.items.weaponTypes

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class Longaxe (longaxeName: String, longaxeMaterial: Material, longaxeCustomModel: Int, private val attackDamage: Double, private val attackSpeed: Double) :
    OdysseyItem(longaxeName, longaxeMaterial, Component.text(longaxeName), customModel = longaxeCustomModel) {

    //
    override fun createItemStack(amount: Int): ItemStack {
        val newLongaxe = super.createItemStack(amount)
        // Assign item meta
        newLongaxe.itemMeta = this.attributeWeaponMeta(newLongaxe.itemMeta, attackDamage, attackSpeed)
        return newLongaxe
    }

    // DANISH

}