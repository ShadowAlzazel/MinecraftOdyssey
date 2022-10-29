package me.shadowalzazel.mcodyssey.items.weapons

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.ModifiersUUIDs
import me.shadowalzazel.mcodyssey.items.weaponTypes.Katana
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

//
object SoulSteelKatana: Katana("Soul Steel Katana", Material.IRON_SWORD, ItemModels.SOUL_STEEL_KATANA, 7.0, 1.5) {

    override fun createItemStack(amount: Int): ItemStack {
        val newKatana = super.createItemStack(amount).also {
            val odysseyDurabilityStat = AttributeModifier(ModifiersUUIDs.DURABILITY_UUID, "odyssey.max_durability", 899.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
            it.itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, odysseyDurabilityStat)
        }
        // Assign item meta
        return newKatana
    }

}


// Soul Steel Dagger
