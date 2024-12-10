package me.shadowalzazel.mcodyssey.server.commands.admin

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.unused.bosses.the_ambassador.TheAmbassador
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SummonBoss : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        if (!sender.isOp) return false
        if (args?.size != 1) return false
        val bossManager = Odyssey.instance.bossManager
        // Spawn
        if (args[0] == "ambassador") {
            bossManager.hasBossActive = true
            bossManager.currentBoss = TheAmbassador(sender.location)
            (bossManager.currentBoss as TheAmbassador).createEvent(sender.world)
            return true
        }
        return false
    }
}
