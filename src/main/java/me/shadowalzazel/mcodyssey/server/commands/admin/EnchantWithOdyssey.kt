package me.shadowalzazel.mcodyssey.server.commands.admin

import me.shadowalzazel.mcodyssey.util.EnchantabilityHandler
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object EnchantWithOdyssey : CommandExecutor, EnchantabilityHandler {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        if (!sender.isOp) return false
        if (args?.size != 2) return false
        if (sender.equipment.itemInMainHand.type == Material.AIR) return false
        // Get args
        val level = args[1].toInt()
        val name = args[0]
        val enchant = getOdysseyEnchantmentFromString(name) ?: getMinecraftEnchantmentFromString(name)
        if (enchant == null) return false
        // Passed Checks
        val item = sender.equipment.itemInMainHand
        item.addEnchantment(enchant, level)
        item.updateEnchantabilityPoints()
        return true
    }
}
