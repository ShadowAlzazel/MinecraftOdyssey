package me.shadowalzazel.mcodyssey.items.utility

import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class Zweihander (zweihanderName: String, zweihanderMaterial: Material, zweihanderCustomModel: Int, private val attackDamage: Double, private val attackSpeed: Double) :
    OdysseyItem(zweihanderName, zweihanderMaterial, Component.text(zweihanderName), customModel = zweihanderCustomModel) {

    //
    override fun createItemStack(amount: Int): ItemStack {
        val newZweihander = super.createItemStack(amount)
        // Assign item meta
        newZweihander.itemMeta = this.attributeWeaponMeta(newZweihander.itemMeta, attackDamage, attackSpeed)
        return newZweihander
    }

}