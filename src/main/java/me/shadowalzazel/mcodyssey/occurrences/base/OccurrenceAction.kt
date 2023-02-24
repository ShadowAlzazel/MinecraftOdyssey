package me.shadowalzazel.mcodyssey.occurrences.base

import me.shadowalzazel.mcodyssey.constants.OdysseyUUIDs
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect

sealed class OccurrenceAction {

    data class ApplyFreeze(var amountInTicks: Int): OccurrenceAction()
    data class ApplyFire(var amountInTicks: Int): OccurrenceAction()
    data class ApplyPotionEffects(var potionList: List<PotionEffect>): OccurrenceAction()
    data class ApplyOdysseyEffect(var potionList: List<PotionEffect>): OccurrenceAction()
    data class ApplyTag(var tag: String): OccurrenceAction()
    data class ApplyTags(var tag_list: List<String>): OccurrenceAction()
    data class ChangeHealth(var addedHealth: Double): OccurrenceAction()
    data class SendActionBar(var message: Component): OccurrenceAction()
    data class SendMessage(var message: Component): OccurrenceAction()
    data class SendTitle(var message: Component, var subMessage: Component): OccurrenceAction()
    data class PlaySound(var sound: Sound, var pitch: Float, var range: Float): OccurrenceAction()
    data class SpawnParticle(var particle: Particle, var amount: Int, var offsetA: Double, var offsetB: Double, var offsetC: Double): OccurrenceAction()


    object ChillingNightPreset : OccurrenceAction()

    object NoOccurrenceAction: OccurrenceAction()


    fun applyAction(someEntity: LivingEntity) {
        when (this) {
            is ApplyFreeze -> someEntity.freezeTicks += amountInTicks
            is ApplyFire -> someEntity.fireTicks += amountInTicks
            is ApplyPotionEffects -> someEntity.addPotionEffects(potionList)
            is ApplyOdysseyEffect -> {
            }
            is ApplyTag -> someEntity.addScoreboardTag(tag)
            is ApplyTags -> tag_list.forEach { tag -> someEntity.addScoreboardTag(tag) }
            is ChangeHealth -> modifyMobHealth(someEntity)
            is SendMessage -> someEntity.sendMessage(message)
            is SendActionBar -> someEntity.sendActionBar(message)
            is SendTitle -> someEntity.showTitle(Title.title(message, subMessage))
            is PlaySound -> someEntity.world.playSound(someEntity, sound, pitch, range)
            is SpawnParticle -> someEntity.world.spawnParticle(particle, someEntity.location, amount, offsetA, offsetB, offsetC)
            is NoOccurrenceAction -> { return }
            is ChillingNightPreset -> presetAction(someEntity)
        }
    }


    private fun ChillingNightPreset.presetAction(someEntity: LivingEntity) {
        with(someEntity.world) {
            spawnParticle(Particle.SNOWFLAKE, someEntity.location, 35, 0.5, 0.5, 0.5)
            spawnParticle(Particle.ELECTRIC_SPARK, someEntity.location, 14, 0.5, 0.5, 0.5)
            playSound(someEntity.location, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 2.5F, 1.5F)
            playSound(someEntity.location, Sound.ENTITY_PLAYER_HURT_FREEZE, 2.5F, 0.5F)
        }

    }


    private fun ChangeHealth.modifyMobHealth(someEntity: LivingEntity) {
        val oldHealth = someEntity.health
        val mobHealthModifier = AttributeModifier(OdysseyUUIDs.ODYSSEY_MOB_HEALTH_UUID, "odyssey_mob_health", addedHealth, AttributeModifier.Operation.ADD_NUMBER)
        val healthAttribute = someEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)
        healthAttribute!!.addModifier(mobHealthModifier)
        someEntity.health = oldHealth + addedHealth
    }

}
