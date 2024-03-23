package me.shadowalzazel.mcodyssey.commands.admin

import me.shadowalzazel.mcodyssey.arcane.EnchantSlotManager
import me.shadowalzazel.mcodyssey.enchantments.EnchantRegistryManager
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.items.Arcane
import me.shadowalzazel.mcodyssey.items.Arcane.createGildedBook
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object GiveGildedBook : CommandExecutor, EnchantSlotManager, EnchantRegistryManager {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        if (!sender.isOp) return false
        if (args?.size != 2) return false
        if (sender.equipment.itemInMainHand.type == Material.AIR) return false
        // Enchant
        val level = args[1].toInt()
        // Switch
        val text0 = args[0]
        val enchantToAdd: OdysseyEnchantment? = getEnchantmentFromString(text0)
        // Check
        if (enchantToAdd != null) {
            val item = Arcane.GILDED_BOOK.createGildedBook(enchantToAdd, level)
            sender.inventory.addItem(item)
        }
        else {
            return false
        }
        return true
    }
}
