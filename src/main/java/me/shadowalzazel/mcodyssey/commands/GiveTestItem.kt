package me.shadowalzazel.mcodyssey.commands

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object GiveTestItem : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            // Create Test Sword Enchantments
            val unbiddenSword = ItemStack(Material.IRON_SWORD, 1)
            val someBook = ItemStack(Material.NETHERITE_SWORD, 1)
            someBook.addUnsafeEnchantment(OdysseyEnchantments.FREEZING_ASPECT, 2)
            //someBook.addUnsafeEnchantment(Enchantment.DURABILITY, 3)
            // Make Enchantment Lore
            val someBookMeta = someBook.itemMeta
            // Make this listener later
            val someBookLore = listOf("${ChatColor.GOLD}${OdysseyEnchantments.FREEZING_ASPECT.name} II")
            println(OdysseyEnchantments.FREEZING_ASPECT.displayName(3))
            someBookMeta.lore = someBookLore
            someBook.itemMeta = someBookMeta
            // Give
            sender.inventory.addItem(someBook)
            return true
        }
        else {
            return false
        }
    }

}