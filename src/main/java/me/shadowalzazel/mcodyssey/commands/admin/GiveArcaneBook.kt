package me.shadowalzazel.mcodyssey.commands.admin

import me.shadowalzazel.mcodyssey.enchantments.utility.EnchantmentsManager
import me.shadowalzazel.mcodyssey.items.Miscellaneous
import me.shadowalzazel.mcodyssey.items.creators.ItemCreator
import org.bukkit.command.BlockCommandSender
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object GiveArcaneBook : CommandExecutor, ItemCreator, EnchantmentsManager {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val isOp = sender is Player && sender.isOp
        val isCommandBlock = sender is BlockCommandSender
        if (!isOp && !isCommandBlock) return false
        if (args == null) return false
        if (args.size != 3) return false
        // Get args
        val player = sender.server.getPlayer(args[0]) ?: return false
        val name = args[1]
        val enchant = getOdysseyEnchantmentFromString(name) ?: getMinecraftEnchantmentFromString(name)
        if (enchant == null) return false
        val level = args[2].toInt()
        // new Stack
        val book = Miscellaneous.ARCANE_BOOK.createArcaneBookStack(enchant, level)
        player.inventory.addItem(book)
        return true
    }
}
