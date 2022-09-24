package me.shadowalzazel.mcodyssey.items.weaponTypes

import me.shadowalzazel.mcodyssey.items.odyssey.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

// Katana
open class Dagger(daggerName: String, daggerMaterial: Material, daggerCustomModel: Int, private val attackDamage: Double, private val attackSpeed: Double) :
    OdysseyItem(daggerName, daggerMaterial, Component.text(daggerName), customModel = daggerCustomModel) {

    //
    override fun createItemStack(amount: Int): ItemStack {

        val newDagger = super.createItemStack(amount)
        // Assign item meta
        newDagger.itemMeta = this.attributeWeaponMeta(newDagger.itemMeta, attackDamage, attackSpeed)
        return newDagger
    }

}