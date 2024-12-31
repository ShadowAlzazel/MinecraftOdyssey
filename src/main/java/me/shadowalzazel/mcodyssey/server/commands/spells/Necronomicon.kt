package me.shadowalzazel.mcodyssey.server.commands.spells

import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.meta.BookMeta

object Necronomicon  {

   fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is LivingEntity) {
            if (sender.equipment?.itemInMainHand?.type == Material.WRITTEN_BOOK) {
                val someBook = sender.equipment!!.itemInMainHand
                if (someBook.hasItemMeta()) {
                    val someBookMeta = someBook.itemMeta as BookMeta
                    if (someBookMeta.generation == BookMeta.Generation.TATTERED) {
                        if (args.size == 1) {
                            if (args[0] == "summon") {
                                println("X")
                                sender.world.spawnEntity(sender.location, EntityType.ZOMBIE)
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }
}