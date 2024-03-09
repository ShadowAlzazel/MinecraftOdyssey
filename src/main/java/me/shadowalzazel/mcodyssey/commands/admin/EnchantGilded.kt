package me.shadowalzazel.mcodyssey.commands.admin

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.base.OdysseyEnchantment
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object EnchantGilded : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        if (!sender.isOp) return false
        if (args?.size != 2) return false
        if (sender.equipment.itemInMainHand.type == Material.AIR) return false
        // Enchant
        val level = args[1].toInt()
        // Switch
        val enchantToAdd: OdysseyEnchantment? = when (args[0])  {
            "gravity_well" -> {
                OdysseyEnchantments.GRAVITY_WELL
            }
            "sculk_sensitive" -> {
                OdysseyEnchantments.SCULK_SENSITIVE
            }
            else -> {
                null
            }
        }
        // Check
        if (enchantToAdd != null) {
            sender.equipment.itemInMainHand.addUnsafeEnchantment(enchantToAdd, level)
        }
        else {
            return false
        }
        return true
    }
}
