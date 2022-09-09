package me.shadowalzazel.mcodyssey.phenomenon.suenPhenomena

import me.shadowalzazel.mcodyssey.mobs.OdysseyMobs
import me.shadowalzazel.mcodyssey.phenomenon.utility.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.utility.PhenomenonTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Skeleton
import org.bukkit.entity.Zombie
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object BloodMoon : OdysseyPhenomenon("Blood Moon",
    PhenomenonTypes.SUEN,
    12,
    3,
    10) {

    private val bloodMoonMobEffects = listOf(
        PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300 * 20,1),
        PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300 * 20,1),
        PotionEffect(PotionEffectType.HEALTH_BOOST, 300 * 20,4)
    )


    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("A blood moon is happening at ${someWorld.name}!")

        // Blood Moon Effects
        val bloodMoonEffect = PotionEffect(PotionEffectType.UNLUCK, 12000, 0)
        for (somePlayer in someWorld.players) {
            with(somePlayer) {
                addPotionEffect(bloodMoonEffect)
                sendMessage(Component.text("The blood moon rises...", TextColor.color(96, 17, 12)))
            }
            with(somePlayer.world) {
                val someLocation = somePlayer.location
                spawnParticle(Particle.SPELL_MOB_AMBIENT, someLocation, 15, 0.5, 0.5, 0.5)
                val bloodBlockBreak = Material.REDSTONE_BLOCK.createBlockData()
                spawnParticle(Particle.BLOCK_CRACK, someLocation, 95, 0.95, 0.8, 0.95, bloodBlockBreak)
                playSound(someLocation, Sound.ENTITY_WITHER_AMBIENT, 2.5F, 0.5F)
            }
        }
    }

    override fun persistentSpawningActives(someEntity: LivingEntity) {
        someEntity.also {
            // Naming and Tagging
            if (it.customName() != null) { it.customName(Component.text("Blood Moon ${(it.customName() as TextComponent).content()}", TextColor.color(96, 17, 12))) }
            else { it.customName(Component.text("Blood Moon ${it.name}", TextColor.color(96, 17, 12))) }
            it.scoreboardTags.add("Blood_Moon_Mob")
            // Effects and Health
            it.addPotionEffects(bloodMoonMobEffects)
            it.health += 20
            // Random Upgrade
            val randomUpgrade = (0..100).random()
            if (40 < randomUpgrade) {
                val spawnLocation = it.location.clone()
                val someWorld = it.world
                if (spawnLocation.y > 68.0) {
                    var someMob: LivingEntity? = null
                    when (it) {
                        is Zombie -> {
                            it.remove()
                            someMob = if (90 < randomUpgrade) { OdysseyMobs.SAVAGE.createKnight(someWorld, spawnLocation).first } else { OdysseyMobs.SAVAGE.createMob(someWorld, spawnLocation) }
                        }
                        is Skeleton -> {
                            it.remove()
                            someMob = if (90 < randomUpgrade) { OdysseyMobs.VANGUARD.createKnight(someWorld, spawnLocation).first } else { OdysseyMobs.VANGUARD.createMob(someWorld, spawnLocation) }
                        }
                        else -> {
                        }
                    }
                    someMob?.scoreboardTags?.add("Blood_Moon_Mob")
                }
            }
        }
    }





}