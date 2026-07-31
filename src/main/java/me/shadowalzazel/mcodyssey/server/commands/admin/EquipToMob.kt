package me.shadowalzazel.mcodyssey.server.commands.admin

import me.shadowalzazel.mcodyssey.common.arcane.MobSpellCastManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class EquipToMob : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        val result = sender.rayTraceEntities(10) ?: return false
        val entity = result.hitEntity ?: return false
        if (entity !is LivingEntity) return false
        if (entity is Player) return false
        val item = sender.inventory.itemInMainHand
        entity.equipment?.setItemInMainHand(item) ?: return false

        // Caster
        MobSpellCastManager.registerCaster(
            entity = entity,
            spell = MobSpellCastManager.MobSpell.SPELL_SCROLL,
            minIntervalTicks = 80,
            maxIntervalTicks = 160,
            castRange = 32.0
        )

        return true
    }
}