package me.shadowalzazel.mcodyssey.alchemy

import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.effects.OdysseyEffectsHandler
import me.shadowalzazel.mcodyssey.items.Potions
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.entity.AreaEffectCloud
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.ThrownPotion
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

interface AlchemyManager {

    // Helper function that converts a char sequence (M:SS) to time Int
    private fun loreToSeconds(timeSequence: CharSequence): Int {
        val minute = timeSequence[1].toString().toInt()
        val tensSecond = timeSequence[3].toString().toInt()
        val onesSecond = timeSequence[4].toString().toInt()
        return (minute * 60) + (tensSecond * 10) + (onesSecond)
    }


    private fun timeToLore(time: Int): CharSequence {
        val seconds = time % 60
        val minutes = time / 60
        return if (seconds < 9) { "($minutes:0$seconds)" } else { "($minutes:$seconds)" }
    }

    private fun getPotionDuration(potionLore: MutableList<Component>): Int {
        val timerLoreContent = (potionLore.first() as TextComponent).content()
        val i = timerLoreContent.lastIndex
        val potionLoreTimer = timerLoreContent.subSequence((i - 5)..i)
        return loreToSeconds(potionLoreTimer)
    }


    // Helper function to create lingering timed potions
    fun createLingeringPotion(potionMaterial: Material, oldPotion: ItemStack): ItemStack {
        val newPotion = ItemStack(potionMaterial, 1)
        // Modify potion meta
        newPotion.itemMeta = (oldPotion.itemMeta as PotionMeta).also {
            val oldTextComponent = it.lore()!!.first() as TextComponent
            val oldColor = oldTextComponent.color()
            val timerLore = oldTextComponent.content()
            val i = timerLore.lastIndex
            // Get the time in seconds form String in "(M:SS)" format
            val oldTime = loreToSeconds(timerLore.subSequence((i - 5)..i))
            // Create lore by getting old effect and adding new time
            val newTimerString = timerLore.subSequence(0..(i - 6)).toString() + timeToLore(oldTime / 4)
            val newText = Component.text(newTimerString, oldColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            it.lore(listOf(newText))
        }
        return newPotion
    }

    fun createCustomPotion(potionMaterial: Material, oldPotion: ItemStack, modelBottle: Int = 0): ItemStack {
        val newPotion = ItemStack(potionMaterial, 1)
        newPotion.itemMeta = (oldPotion.itemMeta as PotionMeta)
        if (modelBottle != 0) { newPotion.itemMeta.setCustomModelData(modelBottle) }
        return newPotion
    }


    fun createEffectCloud(somePotion: ThrownPotion, someCloud: AreaEffectCloud, potionName: String) {
        // Potion Cloud modifiers
        val tagToAdd: String
        var applicationDelay = 20
        val customEffectToAdd: PotionEffect
        val potionDuration = getPotionDuration(somePotion.item.lore()!!) // -> Can Make When?
        // Match names
        when (potionName) {
            Potions.FLASK_OF_FROST.name -> {
                customEffectToAdd = PotionEffect(PotionEffectType.HUNGER, potionDuration * 20, 0)
                applicationDelay = 40
                tagToAdd = EntityTags.FREEZING_CLOUD
            }
            Potions.FLASK_OF_DECAY.name -> {
                customEffectToAdd = PotionEffect(PotionEffectType.SLOW, potionDuration * 20, 0)
                tagToAdd = EntityTags.DECAYING_CLOUD
            }
            Potions.FLASK_OF_DOUSE.name -> {
                customEffectToAdd = PotionEffect(PotionEffectType.UNLUCK, potionDuration * 20, 0)
                tagToAdd = EntityTags.DOUSE_CLOUD
            }
            Potions.FLASK_OF_ABLAZE.name -> {
                customEffectToAdd = PotionEffect(PotionEffectType.WEAKNESS, potionDuration * 20, 0)
                tagToAdd = EntityTags.BLAZING_CLOUD
            }
            Potions.FLASK_OF_ROSE.name -> {
                customEffectToAdd = PotionEffect(PotionEffectType.UNLUCK, potionDuration * 20, 0)
                tagToAdd = EntityTags.ROSE_CLOUD
            }
            Potions.FLASK_OF_MIASMA.name -> {
                customEffectToAdd = PotionEffect(PotionEffectType.UNLUCK, potionDuration * 20, 0)
                tagToAdd = EntityTags.MIASMA_CLOUD
            }
            Potions.FLASK_OF_PUFFJUICE.name -> {
                customEffectToAdd = PotionEffect(PotionEffectType.UNLUCK, potionDuration * 20, 0)
                tagToAdd = EntityTags.PUFFJUICE_CLOUD
            }
            Potions.POLTERGEIST_BREW.name -> {
                customEffectToAdd = PotionEffect(PotionEffectType.UNLUCK, potionDuration * 20, 0)
                tagToAdd = EntityTags.ACCURSED_CLOUD
            }
            else -> { // SHOULD NOT TRIGGER!
                customEffectToAdd = PotionEffect(PotionEffectType.UNLUCK, potionDuration * 20, 0)
                tagToAdd = EntityTags.TIMED_CLOUD
            }
        }
        // Add Tags and Effects to potion cloud
        someCloud.also {
            (somePotion.item.itemMeta as PotionMeta).addCustomEffect(customEffectToAdd, true)
            it.addCustomEffect(customEffectToAdd, true)
            it.duration = 20 * 20
            it.reapplicationDelay = applicationDelay
            it.addScoreboardTag(tagToAdd)
            it.addScoreboardTag(EntityTags.TIMED_CLOUD)
        }
    }

    fun potionEffectAssigner(affectedEntities: MutableCollection<LivingEntity>, effectApplierNames: List<String>, isCloud: Boolean) {
        var duration: Int
        val durationModifier: Int = if (isCloud) 4 else 1

        for (someName in effectApplierNames) {
            when (someName) {
                EntityTags.FREEZING_CLOUD, Potions.FLASK_OF_FROST.name -> {
                    duration = 30 / durationModifier
                    OdysseyEffectsHandler.freezingEffect(affectedEntities, duration, 1)
                }
                EntityTags.DECAYING_CLOUD, Potions.FLASK_OF_DECAY.name -> {
                    duration = 30 / durationModifier
                    OdysseyEffectsHandler.decayingEffect(affectedEntities, duration, 1)
                }
                EntityTags.DOUSE_CLOUD, Potions.FLASK_OF_DOUSE.name -> {
                    duration = 40 / durationModifier
                    OdysseyEffectsHandler.dousedEffect(affectedEntities, duration, 2)
                }
                EntityTags.BLAZING_CLOUD, Potions.FLASK_OF_ABLAZE.name -> {
                    duration = 30 / durationModifier
                    OdysseyEffectsHandler.ablazeEffect(affectedEntities, duration, 2)
                }
                EntityTags.ROSE_CLOUD, Potions.FLASK_OF_ROSE.name -> {
                    duration = 30 / durationModifier
                    OdysseyEffectsHandler.thornsEffect(affectedEntities, duration)
                }
                EntityTags.MIASMA_CLOUD, Potions.FLASK_OF_MIASMA.name -> {
                    duration = 30 / durationModifier
                    OdysseyEffectsHandler.miasmaEffect(affectedEntities, duration)
                }
                EntityTags.PUFFJUICE_CLOUD, Potions.FLASK_OF_PUFFJUICE.name -> {
                    duration = 30 / durationModifier
                    OdysseyEffectsHandler.decayingEffect(affectedEntities, duration, 1)
                }
                EntityTags.ACCURSED_CLOUD, Potions.POLTERGEIST_BREW.name -> {
                    duration = 90 / durationModifier
                    OdysseyEffectsHandler.accursedEffect(affectedEntities, duration)
                }
                EntityTags.SOUL_DAMAGE_CLOUD, Potions.BOTTLE_OF_SOULS.name -> {
                    OdysseyEffectsHandler.soulDamageEffect(affectedEntities, 1)
                }
                else -> {
                }
            }
        }
    }


}