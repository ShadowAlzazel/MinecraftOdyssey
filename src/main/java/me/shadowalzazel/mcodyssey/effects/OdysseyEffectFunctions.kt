package me.shadowalzazel.mcodyssey.effects

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import org.bukkit.Particle
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object OdysseyEffectFunctions {
    // Duration in seconds


    // Decaying
    fun decayingEffect(victimList: MutableCollection<LivingEntity>, duration: Int, amplifier: Int = 0) {
        val decayingPotionEffect = PotionEffect(PotionEffectType.HUNGER, duration * 20, amplifier)
        victimList.forEach {
            it.addPotionEffect(decayingPotionEffect)
            it.addScoreboardTag(OdysseyEffectTags.DECAYING)
            val decayingTask = DecayingTask(it, 1, duration / 2)
            decayingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20 * 2)
        }
    }

    // Freezing
    fun freezingEffect(victimList: MutableCollection<LivingEntity>, duration: Int, amplifier: Int = 0) {
        val freezingPotionEffect = PotionEffect(PotionEffectType.SLOW, duration * 20, amplifier)
        victimList.forEach {
            if (OdysseyEffectTags.FREEZING !in it.scoreboardTags) {
                it.addPotionEffect(freezingPotionEffect)
                it.addScoreboardTag(OdysseyEffectTags.FREEZING)
                val freezingTask = FreezingTask(it, amplifier, duration)
                freezingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
            }
        }
    }

    // Doused
    fun dousedEffect(victimList: MutableCollection<LivingEntity>, duration: Int, amplifier: Int = 0) {
        victimList.forEach {
            if (OdysseyEffectTags.DOUSED !in it.scoreboardTags) {
                it.addScoreboardTag(OdysseyEffectTags.DOUSED)
                it.addScoreboardTag("Doused_Factor_$amplifier")
                val dousedTask = DousedTask(it, duration)
                dousedTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
            }
        }
    }

    // Ablaze
    fun ablazeEffect(victimList: MutableCollection<LivingEntity>, duration: Int, amplifier: Int = 0) {
        victimList.forEach {
            if (OdysseyEffectTags.ABLAZE !in it.scoreboardTags) {
                it.fireTicks = duration * 20
                it.addScoreboardTag(OdysseyEffectTags.ABLAZE)
                val blazingTask = BlazingTask(it, amplifier, duration)
                blazingTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
            }
        }
    }

    // Soul Damage
    fun soulDamageEffect(victimList: MutableCollection<LivingEntity>, amplifier: Int = 1) {
        victimList.forEach {
            // Souls will not kill only escape violently
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
            if (OdysseyEffectTags.THORNY !in it.scoreboardTags) {
                it.addScoreboardTag(OdysseyEffectTags.THORNY)
                val thornyTask = ThornsTask(it, duration)
                thornyTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
            }
        }
    }

    // Thorns
    fun puffyPricklyEffect(victimList: MutableCollection<LivingEntity>, duration: Int) {
        victimList.forEach {
            if (OdysseyEffectTags.PUFFY_PRICKLY !in it.scoreboardTags) {
                it.addScoreboardTag(OdysseyEffectTags.PUFFY_PRICKLY)
                val puffyPricklyTask = PuffyPricklyTask(it, duration)
                puffyPricklyTask.runTaskTimer(MinecraftOdyssey.instance, 0, 20)
            }
        }
    }



}