package me.shadowalzazel.mcodyssey.items.utility

import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class Halberd (halberdName: String, halberdMaterial: Material, halberdCustomModel: Int, private val attackDamage: Double, private val attackSpeed: Double) :
    OdysseyItem(halberdName, halberdMaterial, Component.text(halberdName), customModel = halberdCustomModel) {

    //
    override fun createItemStack(amount: Int): ItemStack {
        val newHalberd = super.createItemStack(amount)
        // Assign item meta
        newHalberd.itemMeta = this.attributeWeaponMeta(newHalberd.itemMeta, attackDamage, attackSpeed)
        return newHalberd
    }

}