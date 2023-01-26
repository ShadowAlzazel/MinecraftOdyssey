package me.shadowalzazel.mcodyssey.items.utility

import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

open class OdysseyWeapon(
    weaponName: String,
    weaponMaterial: Material, weaponCustomModel: Int,
    private val attackDamage: Double,
    private val attackSpeed: Double,
    val weaponType: WeaponTypes
    ) : OdysseyItem(weaponName, weaponMaterial, Component.text(weaponName), customModel = weaponCustomModel) {

    // Override Weapon
    override fun createItemStack(amount: Int): ItemStack {
        val newWeapon = super.createItemStack(amount)
        // Assign item meta
        newWeapon.itemMeta = this.attributeWeaponMeta(newWeapon.itemMeta, attackDamage, attackSpeed)
        return newWeapon
    }

}

// ---------------------- NORMALS -----------------------
// BATTLEAXE - Double Bladed
// CLAYMORE - Long Sword, Two-handed
// CUTLASS -
// DAGGER - Short
// HALBERD - Bladed Pole Arm
// KATANA
// LONGAXE - Two-handed
// MACE - Long
// RAPIER - Piercing
// SABER -
// SCYTHE -
// SICKLE -
// SPEAR -
// STAFF -
// WARHAMMER - long hammer pole arm

// --------------------- UNIQUES ---------------------------

// ZWEIHANDER
// NAGINATA
// CUDGEL - Small Club
// YAN YUE DAO
// MORGENSTERN - Star Ball and Chain
// FLAIL -
// PSI
// KAMA
// LANCE