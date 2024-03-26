package me.shadowalzazel.mcodyssey.commands.admin

import me.shadowalzazel.mcodyssey.arcane.EnchantSlotManager
import me.shadowalzazel.mcodyssey.enchantments.EnchantRegistryManager
import me.shadowalzazel.mcodyssey.items.Arcane
import me.shadowalzazel.mcodyssey.items.Arcane.createGildedBook
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object GiveGildedBook : CommandExecutor, EnchantSlotManager, EnchantRegistryManager {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        if (!sender.isOp) return false
        if (args?.size != 2) return false
        // Get args
        val level = args[1].toInt()
        val string = args[0]
        val odysseyEnchantment = getEnchantmentFromString(string) ?: return false
        //val registeredEnchantment = convertToBukkitEnchant(odysseyEnchantment) ?: return false
        // Add
        val item = Arcane.GILDED_BOOK.createGildedBook(odysseyEnchantment, level)
        sender.inventory.addItem(item)
        return true
    }
}
