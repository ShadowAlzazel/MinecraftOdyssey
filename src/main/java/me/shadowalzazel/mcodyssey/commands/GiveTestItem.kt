package me.shadowalzazel.mcodyssey.commands

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.odysseyUtility.OdysseyItems
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object GiveTestItem : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            /*
            val someBook = ItemStack(Material.ENCHANTED_BOOK, 1)
            someBook.addUnsafeEnchantment(OdysseyEnchantments.BUZZY_BEES, 2)
            // Make Enchantment Lore
            val someBookMeta = someBook.itemMeta
            val someBookLore = listOf("${ChatColor.GOLD}${OdysseyEnchantments.BUZZY_BEES.name} II")
            println(OdysseyEnchantments.BUZZY_BEES.displayName(1))
            someBookMeta.lore = someBookLore
            someBook.itemMeta = someBookMeta
            // Give
            sender.inventory.addItem(someBook)

             */
            //sender.inventory.addItem(OdysseyItems.GILDED_BOOK.createGildedBook(OdysseyEnchantments.HEMORRHAGE, 2))
            //sender.inventory.addItem(OdysseyItems.GILDED_BOOK.createGildedBook(OdysseyEnchantments.POTION_BARRIER, 2))
            sender.inventory.addItem(OdysseyItems.GILDED_BOOK.createGildedBook(OdysseyEnchantments.DOUSE, 2))

            sender.inventory.addItem(OdysseyItems.NEUTRONIUM_BARK_SCRAPS.createItemStack(1))
            sender.inventory.addItem(OdysseyItems.IDESCINE_ESSENCE.createItemStack(1))
            sender.inventory.addItem(OdysseyItems.REFINED_NEPTUNIAN_DIAMONDS.createItemStack(1))
            sender.inventory.addItem(OdysseyItems.IMPURE_ANTIMATTER_SHARD.createItemStack(1))

            return true
        }
        else {
            return false
        }
    }

}