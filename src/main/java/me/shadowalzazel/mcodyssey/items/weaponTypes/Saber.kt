package me.shadowalzazel.mcodyssey.items.weaponTypes

import me.shadowalzazel.mcodyssey.items.odyssey.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class Saber(saberName: String, saberMaterial: Material, saberCustomModel: Int, private val attackDamage: Double, private val attackSpeed: Double) :
    OdysseyItem(saberName, saberMaterial, Component.text(saberName), customModel = saberCustomModel) {

    //
    override fun createItemStack(amount: Int): ItemStack {
        val newSaber = super.createItemStack(amount)
        // Assign item meta
        newSaber.itemMeta = this.attributeWeaponMeta(newSaber.itemMeta, attackDamage, attackSpeed)
        return newSaber
    }

}