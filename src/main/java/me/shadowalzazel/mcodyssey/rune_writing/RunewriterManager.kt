package me.shadowalzazel.mcodyssey.rune_writing

import me.shadowalzazel.mcodyssey.attributes.AttributeManager
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.addTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasTag
import me.shadowalzazel.mcodyssey.rune_writing.base.OdysseyRunesherd
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

interface RunewriterManager: AttributeManager {

    /* ---------------------------------------- */
    // TAGS

    fun ItemStack.addRunesherdTag() {
        addTag(ItemTags.IS_RUNESHERD)
    }

    fun ItemStack.hasRunesherdTag(): Boolean {
        return hasTag(ItemTags.IS_RUNESHERD)
    }


    // Add Attribute modifier to new runesherd
    fun ItemStack.addModifierToRunesherd(runesherd: OdysseyRunesherd) {
        if (runesherd.attribute == null) return
        if (runesherd.affectedEquipment == null) return
        if (runesherd.affectedEquipment.isEmpty()) return
        // Values for attribute manager
        val value = runesherd.value
        val attributeName = "odyssey." + runesherd.name + "_modifier"
        // When
        when(runesherd.attribute) {
            Attribute.GENERIC_ATTACK_DAMAGE -> {
                addAttackDamageAttribute(value, attributeName)
            }
            else -> {

            }
        }
    }


    // Add runesherd to item



    // MAYBE FOR MAKING HIGHER QUALITY RUNES
    // use pots
    // since sherds are in pottery
    // have a pot and the rune sherd




    fun createGenericRuneSherd(
        attribute: Attribute,
        equipmentSlot: EquipmentSlot
    ) {

    }

    fun addRunesherdAttribute(runesherd: ItemStack, equipment: ItemStack) {

    }

}

// To make a better world