package me.shadowalzazel.mcodyssey.commands.admin

import me.shadowalzazel.mcodyssey.arcane.EnchantSlotManager
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object EnchantWithOdyssey : CommandExecutor, EnchantSlotManager {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        if (!sender.isOp) return false
        if (args?.size != 2) return false
        if (sender.equipment.itemInMainHand.type == Material.AIR) return false
        // Get args
        val level = args[1].toInt()
        val string = args[0]
        val enchant = getOdysseyEnchantFromString(string) ?: return false
        // Passed Checks
        val item = sender.equipment.itemInMainHand
        //item.addUnsafeEnchantment(registeredEnchantment, level) // TODO!!!!!!!!!!!!
        // CHANGE
        item.createNewEnchantSlots()
        return true
    }
}
