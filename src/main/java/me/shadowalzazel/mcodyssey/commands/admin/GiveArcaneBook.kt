package me.shadowalzazel.mcodyssey.commands.admin

import me.shadowalzazel.mcodyssey.arcane.EnchantSlotManager
import me.shadowalzazel.mcodyssey.items.Miscellaneous
import me.shadowalzazel.mcodyssey.items.creators.ItemCreator
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object GiveArcaneBook : CommandExecutor, EnchantSlotManager, ItemCreator {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        if (!sender.isOp) return false
        if (args?.size != 2) return false
        // Get args
        val level = args[1].toInt()
        val string = args[0]
        val odysseyEnchantment = getOdysseyEnchantFromString(string) ?: return false
        val item = Miscellaneous.ARCANE_BOOK.createArcaneBook(odysseyEnchantment, level)
        sender.inventory.addItem(item)
        return true
    }
}
