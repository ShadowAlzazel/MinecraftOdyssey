package me.shadowalzazel.mcodyssey.items.weapons.kinds

import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class Cutlass(cutlassName: String, cutlassMaterial: Material, cutlassCustomModel: Int, private val attackDamage: Double, private val attackSpeed: Double) :
    OdysseyItem(cutlassName, cutlassMaterial, Component.text(cutlassName), customModel = cutlassCustomModel) {

    //
    override fun createItemStack(amount: Int): ItemStack {
        val newCutlass = super.createItemStack(amount)
        // Assign item meta
        newCutlass.itemMeta = this.attributeWeaponMeta(newCutlass.itemMeta, attackDamage, attackSpeed)
        return newCutlass
    }

}