package me.shadowalzazel.mcodyssey.commands.admin

import me.shadowalzazel.mcodyssey.common.items.custom.Runesherds
import me.shadowalzazel.mcodyssey.common.items.custom.Runesherds.createSpaceRuneTablet
import me.shadowalzazel.mcodyssey.util.SpaceRuneManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object GiveSpaceRuneTablet : CommandExecutor, SpaceRuneManager {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        if (!sender.isOp) return false
        if (args?.size != 9) return false
        // Variables
        val item = Runesherds.SPACERUNE_TABLET.createSpaceRuneTablet(1)
        item.apply {
            setIntTag("m00", args[0].toInt())
            setIntTag("m10", args[1].toInt())
            setIntTag("m20", args[2].toInt())
            setIntTag("m01", args[3].toInt())
            setIntTag("m11", args[4].toInt())
            setIntTag("m21", args[5].toInt())
            setIntTag("m02", args[6].toInt())
            setIntTag("m12", args[7].toInt())
            setIntTag("m22", args[8].toInt())
        }
        item.createSpaceRuneMatrixLore()
        sender.inventory.addItem(item)
        return true
    }
}
