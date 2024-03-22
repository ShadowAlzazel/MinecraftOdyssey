package me.shadowalzazel.mcodyssey.commands.admin

import me.shadowalzazel.mcodyssey.arcane.EnchantSlotManager
import me.shadowalzazel.mcodyssey.enchantments.EnchantRegistryManager
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object EnchantGilded : CommandExecutor, EnchantSlotManager, EnchantRegistryManager {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        if (!sender.isOp) return false
        if (args?.size != 2) return false
        if (sender.equipment.itemInMainHand.type == Material.AIR) return false
        // Enchant
        val level = args[1].toInt()
        // Switch
        println(args[0])
        val odysseyEnchantment = getEnchantmentFromString(args[0]) ?: return false
        println(odysseyEnchantment)
        val registeredEnchantment = getEnchantmentFromRegistry(odysseyEnchantment) ?: return false
        println(registeredEnchantment)
        // Passed Checks
        val item = sender.equipment.itemInMainHand
        item.addUnsafeEnchantment(registeredEnchantment, level)
        item.createNewEnchantSlots()
        return true
    }
}
