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
            val someBook = ItemStack(Material.ENCHANTED_BOOK, 1)
            someBook.addUnsafeEnchantment(OdysseyEnchantments.EXPLODING, 3)
            //someBook.addUnsafeEnchantment(Enchantment.DURABILITY, 3)
            // Make Enchantment Lore
            val someBookMeta = someBook.itemMeta
            // Make this listener later
            val someBookLore = listOf("${ChatColor.GOLD}${OdysseyEnchantments.EXPLODING.name} III")
            println(OdysseyEnchantments.EXPLODING.displayName(2))
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