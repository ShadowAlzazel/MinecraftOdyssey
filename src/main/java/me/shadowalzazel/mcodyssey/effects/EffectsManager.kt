package me.shadowalzazel.mcodyssey.effects

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.effects.tasks.*
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

interface EffectsManager {

    fun LivingEntity.addOdysseyEffect(
        effect: String,
        durationInTicks: Int,
        amplifier: Int,
        intensity: Double = 1.0 // 0.0 -> 1.0
    ) {
        val modifiedDuration = (durationInTicks * intensity).toInt()
        // Matcher
        when (effect) {
            EffectTags.ABLAZE -> {
                ablazeAssigner(modifiedDuration, amplifier)
            }
            EffectTags.ACCURSED -> {
                accursedAssigner(modifiedDuration, amplifier)
            }
            EffectTags.CORRODING-> {
                corrodingAssigner(modifiedDuration, amplifier)
            }
            EffectTags.FREEZING -> {
                freezingAssigner(modifiedDuration, amplifier)
            }
            EffectTags.HEMORRHAGING -> {
                hemorrhageAssigner(modifiedDuration, amplifier)
            }
            EffectTags.HONEYED -> {
                honeyedAssigner(modifiedDuration, amplifier)
            }
            EffectTags.IRRADIATED -> {
                irradiatedAssigner(modifiedDuration, amplifier)
            }
            EffectTags.MIASMA -> {
                miasmaAssigner(modifiedDuration, amplifier)
            }
            EffectTags.ROTTING -> {
                rottingAssigner(modifiedDuration, amplifier)
            }
            EffectTags.SOUL_DAMAGE -> {
                soulDamageAssigner(modifiedDuration, amplifier)
            }
            EffectTags.TARRED -> {
                tarredAssigner(modifiedDuration, amplifier)
            }
            else -> {
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Assigners

    // Accursed
    private fun LivingEntity.accursedAssigner(
        durationInTicks: Int,
        amplifier: Int,
    ) {
        if (EffectTags.ACCURSED !in scoreboardTags) {
            addScoreboardTag(EffectTags.ACCURSED)
            AccursedTask(this, durationInTicks / 20).runTaskTimer(Odyssey.instance, 0, 20)
        }
    }

    // Ablaze
    private fun LivingEntity.ablazeAssigner(
        durationInTicks: Int,
        amplifier: Int,
    ) {
        if (EffectTags.ABLAZE !in scoreboardTags) {
            fireTicks = durationInTicks
            addScoreboardTag(EffectTags.ABLAZE)
            BlazingTask(this, amplifier, durationInTicks / 20).runTaskTimer(Odyssey.instance, 0, 20)
        }
    }

    // Corroding
    private fun LivingEntity.corrodingAssigner(
        durationInTicks: Int,
        amplifier: Int,
    ) {
        if (EffectTags.ASPHYXIATE !in scoreboardTags) {
            addScoreboardTag(EffectTags.CORRODING)
            CorrodingTask(this, durationInTicks / 20).runTaskTimer(Odyssey.instance, 0, 20)
        }
    }

    // Freezing
    private fun LivingEntity.freezingAssigner(
        durationInTicks: Int,
        amplifier: Int,
    ) {
        val freezingPotionEffect = PotionEffect(PotionEffectType.SLOW, durationInTicks, amplifier)
        if (EffectTags.FREEZING !in scoreboardTags) {
            addPotionEffect(freezingPotionEffect)
            addScoreboardTag(EffectTags.FREEZING)
            FreezingTask(this, amplifier, durationInTicks / 20).runTaskTimer(Odyssey.instance, 0, 20)
        }
    }

    // Hemorrhaging
    private fun LivingEntity.hemorrhageAssigner(
        durationInTicks: Int,
        amplifier: Int,
    ) {
        if (EffectTags.HEMORRHAGING !in scoreboardTags) {
            addScoreboardTag(EffectTags.HEMORRHAGING)
            addScoreboardTag(EffectTags.HEMORRHAGE_MODIFIER + 1)
            HemorrhageTask(this, amplifier).runTaskTimer(Odyssey.instance, 0, 20)
        }
        else {
            for (x in 1..5) {
                if (scoreboardTags.contains(EffectTags.HEMORRHAGE_MODIFIER + x)) {
                    scoreboardTags.remove(EffectTags.HEMORRHAGE_MODIFIER + x)
                    scoreboardTags.add(EffectTags.HEMORRHAGE_MODIFIER + (x + 1))
                    break
                }
            }
        }
    }

    // Honeyed
    private fun LivingEntity.honeyedAssigner(
        durationInTicks: Int,
        amplifier: Int,
    ) {
        if (EffectTags.HONEYED !in scoreboardTags) {
            // ALSO REDUCE JUMP VELOCITY BY 80%
            addScoreboardTag(EffectTags.HONEYED)
            HoneyedTask(this, (durationInTicks / 20) * 2).runTaskTimer(Odyssey.instance, 0, 20 / 2)
        }
    }

    // Irradiated
    private fun LivingEntity.irradiatedAssigner(
        durationInTicks: Int,
        amplifier: Int,
    ) {
        if (EffectTags.IRRADIATED !in scoreboardTags) {
            addScoreboardTag(EffectTags.IRRADIATED)
            IrradiatedTask(this, durationInTicks / 20).runTaskTimer(Odyssey.instance, 0, 20)
        }
    }

    // Miasma
    private fun LivingEntity.miasmaAssigner(
        durationInTicks: Int,
        amplifier: Int,
    ) {
        if (EffectTags.MIASMA !in scoreboardTags) {
            addScoreboardTag(EffectTags.MIASMA)
            MiasmaTask(this, durationInTicks / 20).runTaskTimer(Odyssey.instance, 0, 20)
        }
    }

    // Decaying
    private fun LivingEntity.rottingAssigner(
        durationInTicks: Int,
        amplifier: Int,
    ) {
        val potionEffect = PotionEffect(PotionEffectType.HUNGER, durationInTicks, amplifier)
        addPotionEffect(potionEffect)
        if (EffectTags.ROTTING !in scoreboardTags) {
            addScoreboardTag(EffectTags.ROTTING)
        }
        RottingTask(this, amplifier, (durationInTicks / 20) / 2).runTaskTimer(Odyssey.instance, 0, 20 * 2)
    }

    // Soul Damage
    private fun LivingEntity.soulDamageAssigner(
        durationInTicks: Int,
        amplifier: Int,
    ) {
        if (health > 4.0) {
            health -= 4.0 * amplifier
            damage(0.01)
            world.spawnParticle(Particle.SCULK_SOUL, location, 25, 0.15, 0.1, 0.15)
        }
    }

    // Tarred
    private fun LivingEntity.tarredAssigner(
        durationInTicks: Int,
        amplifier: Int,
    ) {
        if (EffectTags.TARRED !in scoreboardTags) {
            addScoreboardTag(EffectTags.TARRED)
            addScoreboardTag("${EffectTags.TARRED_MODIFIER}${amplifier}")
            TarredTask(this, durationInTicks / 20).runTaskTimer(Odyssey.instance, 0, 20)
        }
    }


}