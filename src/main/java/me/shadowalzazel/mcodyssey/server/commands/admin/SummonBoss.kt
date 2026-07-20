package me.shadowalzazel.mcodyssey.server.commands.admin

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.boss.BossManager
import me.shadowalzazel.mcodyssey.common.boss.the_ambassador.TheAmbassador
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SummonBoss : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("Only players can summon bosses.")
            return true
        }
        val key = args.getOrNull(0)
        if (key == null) {
            sender.sendMessage("Available bosses: ${BossManager.keys().joinToString(", ")}")
            return true
        }
        val boss = BossManager.summon(Odyssey.instance, key, sender.location)
        sender.sendMessage(if (boss != null) "Summoned '$key'." else "No boss registered under '$key'.")
        return true
    }
}
