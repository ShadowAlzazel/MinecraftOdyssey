package me.shadowalzazel.mcodyssey.occurrences.base

import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect

sealed class OccurrenceAction {

    data class ApplyFreeze(var amount: Int): OccurrenceAction()
    data class ApplyFire(var amount: Int): OccurrenceAction()
    data class ApplyPotionEffect(var potionList: List<PotionEffect>): OccurrenceAction()
    data class ApplyOdysseyEffect(var potionList: List<PotionEffect>): OccurrenceAction()
    data class ApplyTag(var tag: String): OccurrenceAction()
    data class ApplyTags(var tag_list: List<String>): OccurrenceAction()

    object NoOccurrenceAction: OccurrenceAction()


    lateinit var criteriaName: String

    // REWORD
    fun applyAction(someEntity: LivingEntity) {
        when (this) {
            is ApplyFreeze -> {
                someEntity.freezeTicks += 20 * amount
            }
            is ApplyFire -> {
                someEntity.fireTicks += 20 * amount
            }
            is ApplyPotionEffect -> {
               someEntity.addPotionEffects(potionList)
            }
            is ApplyOdysseyEffect -> {

            }
            is ApplyTag -> {

            }
            is ApplyTags -> {

            }
            NoOccurrenceAction -> {
                return
            }
        }
    }



}
