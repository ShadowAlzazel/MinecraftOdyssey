package me.shadowalzazel.mcodyssey.common.effects

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.effects.tasks.*
import me.shadowalzazel.mcodyssey.util.constants.EffectTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags.setIntTag
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

interface EffectsManager : EffectTagsManager {

    //val blazingTasks: MutableMap<UUID, BlazingTask>
    //    get() = mutableMapOf()
    // Create player uuid task manager

    fun LivingEntity.addOdysseyEffect(
        effect: String,
        durationInTicks: Int,
        level: Int = 1,
        intensity: Double = 1.0 // 0.0 -> 1.0
    ) {
        val modifiedDuration = (durationInTicks * intensity).toInt()
        val hasEffect = scoreboardTags.contains(effect)
        // switch
        when(effect) {
            EffectTags.AEROSION -> {
                aerosionAssigner(modifiedDuration, level)
            }
            EffectTags.ABLAZE -> {
                ablazeAssigner(modifiedDuration, level)
            }
            EffectTags.ACCURSED -> {
                accursedAssigner(modifiedDuration, level)
            }
            EffectTags.BARRIER -> {
                barrierAssigner(modifiedDuration)
            }
            EffectTags.CORRODING-> {
                corrodingAssigner(modifiedDuration, level)
            }
            EffectTags.FREEZING -> {
                freezingAssigner(modifiedDuration, level)
            }
            EffectTags.HEMORRHAGING -> {
                hemorrhageAssigner(modifiedDuration, level)
            }
            EffectTags.HONEYED -> {
                honeyedAssigner(modifiedDuration, level)
            }
            EffectTags.IRRADIATED -> {
                irradiatedAssigner(modifiedDuration, level)
            }
            EffectTags.MIASMA -> {
                miasmaAssigner(modifiedDuration, level)
            }
            EffectTags.ROTTING -> {
                buddingAssigner(modifiedDuration, level)
            }
            EffectTags.SOUL_DAMAGE -> {
                soulDamageAssigner(modifiedDuration, level)
            }
            else -> {
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun LivingEntity.aerosionAssigner(
        durationInTicks: Int = 300,
        level: Int = 1,
        amplifier: Double = 1.0
    ) {
        if (EffectTags.AEROSION !in scoreboardTags) {
            val count = durationInTicks / 30
            addScoreboardTag(EffectTags.AEROSION)
            setIntTag(EntityTags.AEROSION_STACKS, 1)
            val task = AerosionTask(
                this,
                amplifier = amplifier,
                maxCount = count)
            // Run every 1.5 seconds for 15 seconds
            task.runTaskTimer(Odyssey.instance, 0, 15)
        }
    }

    // Ablaze
    private fun LivingEntity.ablazeAssigner(
        durationInTicks: Int,
        amplifier: Int
    ) {
        if (EffectTags.ABLAZE !in scoreboardTags) {
            fireTicks = durationInTicks
            addScoreboardTag(EffectTags.ABLAZE)
            BlazingTask(this, amplifier, durationInTicks / 20).runTaskTimer(Odyssey.instance, 0, 20)
        }
    }

    // Accursed
    private fun LivingEntity.accursedAssigner(
        durationInTicks: Int,
        amplifier: Int
    ) {
        if (EffectTags.ACCURSED !in scoreboardTags) {
            addScoreboardTag(EffectTags.ACCURSED)
            AccursedTask(this, durationInTicks / 20).runTaskTimer(Odyssey.instance, 0, 20)
        }
    }

    // Ablaze
    private fun LivingEntity.barrierAssigner(
        durationInTicks: Int
    ) {
        if (EffectTags.BARRIER !in scoreboardTags) {
            fireTicks = durationInTicks
            addScoreboardTag(EffectTags.BARRIER)
            BarrierTask(this, durationInTicks / 10).runTaskTimer(Odyssey.instance, 0, 10)
        }
    }

    // Corroding
    private fun LivingEntity.corrodingAssigner(
        durationInTicks: Int,
        amplifier: Int
    ) {
        if (EffectTags.CORRODING !in scoreboardTags) {
            addScoreboardTag(EffectTags.CORRODING)
            CorrodingTask(this, durationInTicks / 20).runTaskTimer(Odyssey.instance, 0, 20)
        }
    }

    // Freezing
    private fun LivingEntity.freezingAssigner(
        durationInTicks: Int,
        amplifier: Int
    ) {
        val freezingPotionEffect = PotionEffect(PotionEffectType.SLOWNESS, durationInTicks, 1)
        if (EffectTags.FREEZING !in scoreboardTags) {
            addPotionEffect(freezingPotionEffect)
            addScoreboardTag(EffectTags.FREEZING)
            val task = FreezingTask(this, amplifier, durationInTicks / 20)
            task.runTaskTimer(Odyssey.instance, 0, 20)
        }
    }

    // Hemorrhaging
    private fun LivingEntity.hemorrhageAssigner(
        durationInTicks: Int,
        amplifier: Int
    ) {
        if (EffectTags.HEMORRHAGING !in scoreboardTags) {
            addScoreboardTag(EffectTags.HEMORRHAGING)
            setIntTag(EffectTags.HEMORRHAGE_MODIFIER, amplifier)
            val task = HemorrhageTask(this, amplifier, 9)
            task.runTaskTimer(Odyssey.instance, 0, 20)
        }
    }

    // Honeyed
    private fun LivingEntity.honeyedAssigner(
        durationInTicks: Int,
        amplifier: Int
    ) {
        if (EffectTags.HONEYED !in scoreboardTags) {
            // ALSO REDUCE JUMP VELOCITY BY 80% !!!!!!!!!
            addScoreboardTag(EffectTags.HONEYED)
            HoneyedTask(this, (durationInTicks / 20) * 2).runTaskTimer(Odyssey.instance, 0, 20 / 2)
        }
    }

    // Irradiated
    private fun LivingEntity.irradiatedAssigner(
        durationInTicks: Int,
        amplifier: Int
    ) {
        if (EffectTags.IRRADIATED !in scoreboardTags) {
            addScoreboardTag(EffectTags.IRRADIATED)
            IrradiatedTask(this, durationInTicks / 20).runTaskTimer(Odyssey.instance, 0, 20)
        }
    }

    // Miasma
    private fun LivingEntity.miasmaAssigner(
        durationInTicks: Int,
        amplifier: Int
    ) {
        if (EffectTags.MIASMA !in scoreboardTags) {
            addScoreboardTag(EffectTags.MIASMA)
            MiasmaTask(this, durationInTicks / 20).runTaskTimer(Odyssey.instance, 0, 20)
        }
    }

    // Decaying
    private fun LivingEntity.buddingAssigner(
        durationInTicks: Int,
        amplifier: Int
    ) {
        val potionEffect = PotionEffect(PotionEffectType.HUNGER, durationInTicks, amplifier)
        addPotionEffect(potionEffect)
        if (EffectTags.ROTTING !in scoreboardTags) {
            addScoreboardTag(EffectTags.ROTTING)
        }
        BuddingTask(this, amplifier, (durationInTicks / 20) / 2).runTaskTimer(Odyssey.instance, 0, 20 * 2)
    }

    // Soul Damage
    private fun LivingEntity.soulDamageAssigner(
        durationInTicks: Int,
        amplifier: Int
    ) {
        if (health > 4.0) {
            health -= 4.0 * amplifier
            damage(0.01)
            world.spawnParticle(Particle.SCULK_SOUL, location, 25, 0.15, 0.1, 0.15)
        }
    }

}