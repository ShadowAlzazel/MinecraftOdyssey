package me.shadowalzazel.mcodyssey.phenomenon.suenPhenomena

import me.shadowalzazel.mcodyssey.mobs.OdysseyMobs
import me.shadowalzazel.mcodyssey.phenomenon.odyssey.OdysseyPhenomenon
import me.shadowalzazel.mcodyssey.phenomenon.odyssey.PhenomenonTypes
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
    13,
    4,
    10) {

    private val bloodMoonMobEffects = listOf(
        PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300 * 20,1),
        PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 300 * 20,0),
        PotionEffect(PotionEffectType.HEALTH_BOOST, 300 * 20,4)
    )


    override fun successfulActivation(someWorld: World) {
        super.successfulActivation(someWorld)
        println("A blood moon is happening at ${someWorld.name}!")

        // Blood Moon Effects
        val bloodMoonEffect = PotionEffect(PotionEffectType.UNLUCK, 12000, 0)

        someWorld.players.forEach {
            it.addPotionEffect(bloodMoonEffect)
            it.sendMessage(Component.text("The blood moon rises...", TextColor.color(96, 17, 12)))
            it.spawnParticle(Particle.SPELL_MOB_AMBIENT, it.location, 15, 0.5, 0.5, 0.5)
            it.playSound(it.location, Sound.ENTITY_WITHER_AMBIENT, 2.5F, 0.5F)
            it.spawnParticle(Particle.BLOCK_CRACK, it.location, 95, 0.95, 0.8, 0.95, Material.REDSTONE_BLOCK.createBlockData())
        }
    }

    override fun persistentSpawningActives(someEntity: LivingEntity) {
        someEntity.also {
            // Do not blood moon if not exposed to night sky
            if (it.location.block.lightFromSky < 7) { return }
            // Naming and Tagging
            if (it.customName() != null) { it.customName(Component.text("Blood Moon ${(it.customName() as TextComponent).content()}", TextColor.color(96, 17, 12))) }
            else { it.customName(Component.text("Blood Moon ${it.name}", TextColor.color(96, 17, 12))) }
            it.scoreboardTags.add("Blood_Moon_Mob")
            // Effects and Health
            it.addPotionEffects(bloodMoonMobEffects)
            it.health += 20
            // Random Upgrade
            val randomUpgrade = (0..100).random()
            if (randomUpgrade > 40) {
                if (it.location.y > 68.0) {
                    var someMob: LivingEntity? = null
                    when (it) {
                        is Zombie -> {
                            it.remove()
                            someMob = if (randomUpgrade > 90) { OdysseyMobs.SAVAGE.createKnight(it.world, it.location).first } else { OdysseyMobs.SAVAGE.createMob(it.world, it.location) }
                        }
                        is Skeleton -> {
                            it.remove()
                            someMob = if (randomUpgrade > 90) { OdysseyMobs.VANGUARD.createKnight(it.world, it.location).first } else { OdysseyMobs.VANGUARD.createMob(it.world, it.location) }
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