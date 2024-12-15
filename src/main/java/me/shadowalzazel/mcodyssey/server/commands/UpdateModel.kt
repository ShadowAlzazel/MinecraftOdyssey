package me.shadowalzazel.mcodyssey.server.commands

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.RegistryTagManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
object UpdateModel : CommandExecutor, RegistryTagManager, DataTagManager {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        val allItems = args != null && args.isNotEmpty() && args[0] == "*"
        if (!allItems) sender.inventory.itemInMainHand.updateItemModel()
        else sender.inventory.contents.forEach { it?.updateItemModel() }
        return true
    }

    private fun ItemStack.updateItemModel() {
        val oldModel = this.getData(DataComponentTypes.ITEM_MODEL)
        val itemId = this.getItemIdentifier() ?: return
        // Set
        if (oldModel == null || oldModel.key().namespace() == "minecraft") {
            this.resetData(DataComponentTypes.CUSTOM_MODEL_DATA) // Reset old override data
        }
        val newModel = createOdysseyKey(itemId)
        this.setData(DataComponentTypes.ITEM_MODEL, newModel)
    }

}