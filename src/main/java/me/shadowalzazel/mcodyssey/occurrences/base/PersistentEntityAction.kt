package me.shadowalzazel.mcodyssey.occurrences.base

import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect

sealed class PersistentEntityAction {

    class PersistentPotionEffect(val potionList: List<PotionEffect>): PersistentEntityAction()
    class SpawningPotion(val potionList: List<PotionEffect>): PersistentEntityAction()
    object NoPersistentAction: PersistentEntityAction()


    fun applyAction(someEntities: List<LivingEntity>, affectedTypes: List<EntityType>) {

        when (this) {
            is PersistentPotionEffect -> {
                applyPotionEffects(someEntities, affectedTypes, this.potionList)
            }
            is SpawningPotion -> {
                applyPotionEffects(someEntities, affectedTypes, this.potionList)
            }
            is NoPersistentAction -> {

            }

        }


    }



    private fun applyPotionEffects(someEntities: List<LivingEntity>, affectedTypes: List<EntityType>, passedPotionList: List<PotionEffect>) {
        someEntities.forEach {
            if (it.type in affectedTypes) it.addPotionEffects(passedPotionList)
        }
    }

}
