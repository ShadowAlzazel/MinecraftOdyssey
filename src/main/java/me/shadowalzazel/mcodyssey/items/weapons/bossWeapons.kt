package me.shadowalzazel.mcodyssey.items.weapons

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.utilty.OdysseyItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack


/* --------------------------BOSS WEAPONS ---------------------------*/

object KineticBlaster : OdysseyItem("Kinetic Blaster",
    Material.BOW,
    odysseyDisplayName = Component.text("Kinetic Blaster", TextColor.color(39, 79, 152)),
    odysseyLore =  listOf(Component.text("A weapon commissioned to shoot kinetic projectiles", TextColor.color(39, 79, 152)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    customModel = ItemModels.KINETIC_BLASTER) {

    override fun createItemStack(amount: Int): ItemStack {
        val newKineticBlaster = super.createItemStack(amount)
        newKineticBlaster.itemMeta.apply {
            addEnchant(Enchantment.ARROW_DAMAGE, 5, true)
            addEnchant(Enchantment.ARROW_INFINITE, 1, true)
            addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true)
            addEnchant(Enchantment.DURABILITY, 3, true)
        }
        return newKineticBlaster

    }

}