package me.shadowalzazel.mcodyssey.items.utility

import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class Sickle (battleaxeName: String, battleaxeMaterial: Material, battleaxeCustomModel: Int, private val attackDamage: Double, private val attackSpeed: Double) :
    OdysseyItem(battleaxeName, battleaxeMaterial, Component.text(battleaxeName), customModel = battleaxeCustomModel) {

    //
    override fun createItemStack(amount: Int): ItemStack {
        val newBattleaxe = super.createItemStack(amount)
        // Assign item meta
        newBattleaxe.itemMeta = this.attributeWeaponMeta(newBattleaxe.itemMeta, attackDamage, attackSpeed)
        return newBattleaxe
    }

    // DOUBLE BLADED

}