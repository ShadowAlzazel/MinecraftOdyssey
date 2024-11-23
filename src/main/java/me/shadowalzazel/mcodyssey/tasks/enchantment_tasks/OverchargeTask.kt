package me.shadowalzazel.mcodyssey.tasks.enchantment_tasks

import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.getIntTag
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.removeTag
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.setIntTag
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

// OVERCHARGE TASK
class OverchargeTask(
    private val shooter: Player,
    val bow: ItemStack,
    private val level: Int) : BukkitRunnable()
{
    private var counter = 0

    override fun run() {
        val stopOvercharge = shooter.activeItem != bow || shooter.isDead || !shooter.isOnline
        // Checks if mount is player to add effects
        if (stopOvercharge) {
            if (shooter.scoreboardTags.contains(EntityTags.OVERCHARGING)) {
                shooter.scoreboardTags.remove(EntityTags.OVERCHARGING)
            }
            if (shooter.getIntTag(EntityTags.OVERCHARGE_MODIFIER) != null) {
                shooter.removeTag(EntityTags.OVERCHARGE_MODIFIER)
            }
            this.cancel()
            return
        }
        // Run Effects every other 10 ticks /0.5secs
        counter += 1
        if (counter % 2 == 0) return
        // Check Tags
        var modifier = shooter.getIntTag(EntityTags.OVERCHARGE_MODIFIER)
        if (modifier == null) {
            shooter.setIntTag(EntityTags.OVERCHARGE_MODIFIER, 0)
            modifier = 0
            return
        }
        // Prevent more stuff from happening
        if (modifier > level) {
            return
        }
        // Add Tags
        shooter.setIntTag(EntityTags.OVERCHARGE_MODIFIER, modifier + 1)

        // Effects
        with(shooter.world) {
            spawnParticle(Particle.END_ROD, shooter.location, 10 * modifier, 0.5, 0.5, 0.5)
            spawnParticle(Particle.ELECTRIC_SPARK, shooter.location, 5 * modifier, 0.5, 0.5, 0.5)
            playSound(shooter.location, Sound.BLOCK_MANGROVE_ROOTS_PLACE, 2.5F, 0.9F)
            if (level == modifier) { // Max Charge
                playSound(shooter.location, Sound.ENTITY_WARDEN_SONIC_BOOM, 1.5F, 1.8F)
            }
            shooter.addPotionEffect(PotionEffect(PotionEffectType.SLOWNESS, 20 * 2, modifier))
        }
    }
}