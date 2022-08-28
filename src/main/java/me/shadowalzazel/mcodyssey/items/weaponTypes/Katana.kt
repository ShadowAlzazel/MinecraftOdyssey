package me.shadowalzazel.mcodyssey.items.weaponTypes

import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

// Katana
open class Katana(katanaName: String, katanaMaterial: Material, katanaCustomModel: Int, private val attackDamage: Double, private val attackSpeed: Double) :
    OdysseyItem(katanaName, katanaMaterial, Component.text(katanaName), customModel = katanaCustomModel) {

    //
    override fun createItemStack(amount: Int): ItemStack {
        val newKatana = super.createItemStack(amount)
        // Assign item meta
        newKatana.itemMeta = this.attributeWeaponMeta(newKatana.itemMeta, attackDamage, attackSpeed)
        return newKatana
    }

}