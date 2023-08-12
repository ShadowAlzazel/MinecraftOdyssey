package me.shadowalzazel.mcodyssey.effects

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.effects.tasks.*
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object OdysseyEffectsHandler {
    // Duration in seconds

    // Accursed
    fun accursedEffect(victimList: MutableCollection<LivingEntity>, duration: Int) {
        victimList.forEach {
            if (EffectTags.ACCURSED !in it.scoreboardTags) {
                it.addScoreboardTag(EffectTags.ACCURSED)
                AccursedTask(it, duration).runTaskTimer(Odyssey.instance, 0, 20)
            }
        }
    }

    // Ablaze
    fun ablazeEffect(victimList: MutableCollection<LivingEntity>, duration: Int, amplifier: Int = 1) {
        victimList.forEach {
            if (EffectTags.ABLAZE !in it.scoreboardTags) {
                it.fireTicks = duration * 20
                it.addScoreboardTag(EffectTags.ABLAZE)
                BlazingTask(it, amplifier, duration).runTaskTimer(Odyssey.instance, 0, 20)
            }
        }
    }

    // Doused
    fun dousedEffect(victimList: MutableCollection<LivingEntity>, duration: Int, amplifier: Int = 1) {
        victimList.forEach {
            if (EffectTags.DOUSED !in it.scoreboardTags) {
                it.addScoreboardTag(EffectTags.DOUSED)
                it.addScoreboardTag("${EffectTags.DOUSE_MODIFIER}${amplifier}")
                DousedTask(it, duration).runTaskTimer(Odyssey.instance, 0, 20)
            }
        }
    }

    // Freezing
    fun freezingEffect(victimList: MutableCollection<LivingEntity>, duration: Int, amplifier: Int) {
        val freezingPotionEffect = PotionEffect(PotionEffectType.SLOW, duration * 20, amplifier)
        victimList.forEach {
            if (EffectTags.FREEZING !in it.scoreboardTags) {
                it.addPotionEffect(freezingPotionEffect)
                it.addScoreboardTag(EffectTags.FREEZING)
                FreezingTask(it, amplifier, duration).runTaskTimer(Odyssey.instance, 0, 20)
            }
        }
    }

    // Hemorrhaging
    fun hemorrhagingEffect(victimList: MutableCollection<LivingEntity>, amplifier: Int = 1) {
        victimList.forEach {
            if (EffectTags.HEMORRHAGING !in it.scoreboardTags) {
                it.addScoreboardTag(EffectTags.HEMORRHAGING)
                it.addScoreboardTag(EffectTags.HEMORRHAGE_MODIFIER + 1)
                HemorrhageTask(it, amplifier).runTaskTimer(Odyssey.instance, 0, 20)
            }
            else {
                for (x in 1..5) {
                    if (it.scoreboardTags.contains(EffectTags.HEMORRHAGE_MODIFIER + x)) {
                        it.scoreboardTags.remove(EffectTags.HEMORRHAGE_MODIFIER + x)
                        it.scoreboardTags.add(EffectTags.HEMORRHAGE_MODIFIER + (x + 1))
                        break
                    }
                }
            }
        }
    }

    // Honeyed
    fun honeyedEffect(victimList: MutableCollection<LivingEntity>, duration: Int) {
        val honeyedPotionEffect = PotionEffect(PotionEffectType.SLOW, duration * 20, 0)
        victimList.forEach {
            if (EffectTags.HONEYED !in it.scoreboardTags) {
                it.addScoreboardTag(EffectTags.HONEYED)
                HoneyedTask(it, duration * 2).runTaskTimer(Odyssey.instance, 0, 10)
            }
        }
    }

    // Thorns
    fun miasmaEffect(victimList: MutableCollection<LivingEntity>, duration: Int) {
        victimList.forEach {
            if (EffectTags.MIASMA !in it.scoreboardTags) {
                it.addScoreboardTag(EffectTags.MIASMA)
                MiasmaTask(it, duration).runTaskTimer(Odyssey.instance, 0, 20)
            }
        }
    }

    // Decaying
    fun rottingEffect(victimList: MutableCollection<LivingEntity>, duration: Int, amplifier: Int) {
        val potionEffect = PotionEffect(PotionEffectType.HUNGER, duration * 20, amplifier)
        victimList.forEach {
            it.addPotionEffect(potionEffect)
            if (EffectTags.ROTTING !in it.scoreboardTags) { it.addScoreboardTag(EffectTags.ROTTING) }
            RottingTask(it, amplifier, duration / 2).runTaskTimer(Odyssey.instance, 0, 20 * 2)
        }
    }

    // Soul Damage
    fun soulDamageEffect(victimList: MutableCollection<LivingEntity>, amplifier: Int = 1) {
        victimList.forEach {
            if (it.health > 4.0) {
                it.health -= 4.0 * amplifier
                it.damage(0.01)
                it.world.spawnParticle(Particle.SCULK_SOUL, it.location, 25, 0.25, 0.35, 0.25)
            }
        }
    }

    // Thorns
    fun thornsEffect(victimList: MutableCollection<LivingEntity>, duration: Int) {
        victimList.forEach {
            if (EffectTags.ASPHYXIATE !in it.scoreboardTags) {
                it.addScoreboardTag(EffectTags.ASPHYXIATE)
                ThornyTask(it, duration).runTaskTimer(Odyssey.instance, 0, 20)
            }
        }
    }
}
