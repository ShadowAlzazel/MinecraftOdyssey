package me.shadowalzazel.mcodyssey.server.commands.admin

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.listeners.DragonListeners
import me.shadowalzazel.mcodyssey.util.AttributeManager
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import org.bukkit.attribute.Attribute
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.scheduler.BukkitRunnable

class EnderDragonBossBattle : CommandExecutor, AttributeManager {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val isOP = sender is Player && sender.isOp
        if (!isOP) return false
        // Get Dragon
        val player = sender
        val dragonBattle = player.world.enderDragonBattle ?: return false
        val dragon = dragonBattle.enderDragon ?: return false
        // Buff Dragon
        dragon.setHealthAttribute(1500.0, AttributeTags.EXTRA_HEALTH_GENERIC)
        dragon.heal(1500.0, EntityRegainHealthEvent.RegainReason.CUSTOM)
        dragon.removeAttributeModifier("odyssey.dragon_armor", Attribute.ARMOR)
        dragon.setAttributeModifier(
            30.0,
            "odyssey.dragon_armor",
            Attribute.ARMOR
        )
        dragon.removeAttributeModifier("odyssey.dragon_armor_toughness", Attribute.ARMOR_TOUGHNESS)
        dragon.setAttributeModifier(
            10.0,
            "odyssey.dragon_armor_toughness",
            Attribute.ARMOR_TOUGHNESS
        )
        dragon.removeAttributeModifier("odyssey.dragon_speed", Attribute.AIR_DRAG_MODIFIER)
        dragon.setAttributeModifier(
            -0.25,
            "odyssey.dragon_speed",
            Attribute.AIR_DRAG_MODIFIER
        )
        player.server.pluginManager.registerEvents(DragonListeners, Odyssey.instance)

        // Passive regeneration: 1 HP per second
        val regenTask = object : BukkitRunnable() {
            override fun run() {
                // Stop the task once the fight is over
                if (dragon.isDead || !dragon.isValid) {
                    cancel()
                    return
                }
                dragon.heal(1.0, EntityRegainHealthEvent.RegainReason.REGEN)
            }
        }
        regenTask.runTaskTimer(Odyssey.instance, 20L, 20L)

        println("[Odyssey] Activated the hard Ender Dragon boss")

        return true
    }

}