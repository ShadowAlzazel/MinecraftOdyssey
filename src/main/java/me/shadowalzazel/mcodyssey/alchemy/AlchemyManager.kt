package me.shadowalzazel.mcodyssey.alchemy

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.addIntTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.addStringTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.getIntTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.getStringTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.hasTag
import me.shadowalzazel.mcodyssey.effects.EffectColors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.entity.AreaEffectCloud
import org.bukkit.entity.ThrownPotion
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.persistence.PersistentDataType

interface AlchemyManager {

    /*-----------------------------------------------------------------------------------------------*/
    // Tags

    fun ItemStack.isCustomEffect(): Boolean {
        return hasTag(ItemTags.IS_CUSTOM_EFFECT)
    }

    // Get Effect
    fun ItemStack.getCustomEffectTag(): String {
        return getStringTag(ItemTags.ODYSSEY_EFFECT_TAG) ?: EffectTags.NO_EFFECT
    }

    fun ItemStack.setCustomEffectTag(effectTag: String) {
        addStringTag(ItemTags.ODYSSEY_EFFECT_TAG, effectTag)
    }

    // Default is Ticks
    fun ItemStack.getCustomEffectTimeInTicks(): Int {
        return getIntTag(ItemTags.ODYSSEY_EFFECT_TIME) ?: 0
    }

    fun ItemStack.getCustomEffectTimeInSeconds(): Int {
        return getIntTag(ItemTags.ODYSSEY_EFFECT_TIME)?.div(20) ?: 0
    }

    // Set Time in Ticks
    fun ItemStack.setCustomEffectTime(timeInTicks: Int) {
        addIntTag(ItemTags.ODYSSEY_EFFECT_TIME, timeInTicks)
    }

    // Get Amplifier
        fun ItemStack.getCustomEffectAmplifier(): Int {
        return getIntTag(ItemTags.ODYSSEY_EFFECT_AMPLIFIER) ?: 0
    }

    // Set Amplifier
    fun ItemStack.setCustomEffectAmplifier(amplifier: Int) {
        addIntTag(ItemTags.ODYSSEY_EFFECT_AMPLIFIER, amplifier)
    }

    // Get Cloud effect tag
    fun AreaEffectCloud.getCloudEffectTag(): String {
        return persistentDataContainer[NamespacedKey(Odyssey.instance, EntityTags.CUSTOM_EFFECT_TAG), PersistentDataType.STRING] ?: EffectTags.NO_EFFECT
    }

    // Set cloud effect tag
    fun AreaEffectCloud.setCloudEffectTag(effectTag: String) {
        persistentDataContainer.set(NamespacedKey(Odyssey.instance, EntityTags.CUSTOM_EFFECT_TAG), PersistentDataType.STRING, effectTag)
    }

    // Get Cloud time in ticks
    fun AreaEffectCloud.getCloudEffectTimeInTicks(): Int {
        return persistentDataContainer[NamespacedKey(Odyssey.instance, EntityTags.CUSTOM_EFFECT_TIME), PersistentDataType.INTEGER] ?: 1
    }

    // Set cloud time in ticks
    fun AreaEffectCloud.setCloudEffectTimeInTicks(timeInTicks: Int) {
        persistentDataContainer.set(NamespacedKey(Odyssey.instance, EntityTags.CUSTOM_EFFECT_TIME), PersistentDataType.INTEGER, timeInTicks)
    }

    // Get Cloud time in ticks
    fun AreaEffectCloud.getCloudEffectAmplifier(): Int {
        return persistentDataContainer[NamespacedKey(Odyssey.instance, EntityTags.CUSTOM_EFFECT_AMPLIFIER), PersistentDataType.INTEGER] ?: 0
    }

    // Set cloud time in ticks
    fun AreaEffectCloud.setCloudEffectAmplifier(amplifier: Int) {
        persistentDataContainer.set(NamespacedKey(Odyssey.instance, EntityTags.CUSTOM_EFFECT_AMPLIFIER), PersistentDataType.INTEGER, amplifier)
    }

    /*-----------------------------------------------------------------------------------------------*/
    // TODO: TIPPED ARROWS



    /*-----------------------------------------------------------------------------------------------*/
    // Components

    fun createTimeString(timeInSeconds: Int): CharSequence {
        val seconds = timeInSeconds % 60
        val minutes = timeInSeconds / 60
        return if (seconds < 9) { "($minutes:0$seconds)" } else { "($minutes:$seconds)" }
    }

    fun getEffectComponent(tag: String, timeInTicks: Int): Component {
        val color = getEffectColor(tag)
        return when (tag) {
            EffectTags.FREEZING -> {
                Component.text("Freezing ${createTimeString(timeInTicks / 20)}", color)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            }
            else -> {
                Component.text("No Effect")
            }
        }
    }

    fun getEffectColor(tag: String): TextColor {
        return when (tag) {
            EffectTags.FREEZING -> {
                EffectColors.FREEZING.color
            }
            EffectTags.ROTTING -> {
                EffectColors.ROTTING.color
            }
            EffectTags.TARRED -> {
                EffectColors.TARRED.color
            }
            EffectTags.ABLAZE -> {
                EffectColors.ABLAZE.color
            }
            EffectTags.IRRADIATED -> {
                EffectColors.IRRADIATED.color
            }
            EffectTags.CORRODING -> {
                EffectColors.CORROSION.color
            }
            EffectTags.MIASMA -> {
                EffectColors.MIASMA.color
            }
            EffectTags.ACCURSED -> {
                EffectColors.ACCURSED.color
            }
            EffectTags.SOUL_DAMAGE -> {
                EffectColors.SOUL.color
            }
            EffectTags.SHIMMER -> {
                EffectColors.SHIMMER.color
            }
            else -> {
                TextColor.color(255, 255, 255)
            }
        }
    }

    // Helper function that converts a char sequence (M:SS) to time Int
    private fun loreToSeconds(timeSequence: CharSequence): Int {
        val minute = timeSequence[1].toString().toInt()
        val tensSecond = timeSequence[3].toString().toInt()
        val onesSecond = timeSequence[4].toString().toInt()
        return (minute * 60) + (tensSecond * 10) + (onesSecond)
    }

    /*-----------------------------------------------------------------------------------------------*/

    fun createModeledPotion(material: Material, item: ItemStack, model: Int? = null): ItemStack {
        return ItemStack(material, 1).apply {
            itemMeta = item.itemMeta as PotionMeta
            if (model != null) { itemMeta.setCustomModelData(model) }
        }
    }

    fun createLingeringPotion(item: ItemStack): ItemStack {
        val potion = ItemStack(Material.LINGERING_POTION, 1)
        val isCustom = potion.isCustomEffect()
        val oldTime = potion.getCustomEffectTimeInTicks()
        // Logic
        potion.itemMeta = (item.itemMeta as PotionMeta).also {
            if (isCustom) {
                potion.setCustomEffectTime(oldTime / 4)
            }
        }
        // Lore
        return potion.apply {
            val newLore = lore() ?: mutableListOf()
            val effectTag = getCustomEffectTag()
            val index = newLore.indexOf(getEffectComponent(effectTag, oldTime))
            if (index != -1) { newLore[index] = getEffectComponent(effectTag, oldTime / 4) }
            lore(newLore)
        }
    }

    fun createCustomEffectCloud(potion: ThrownPotion, cloud: AreaEffectCloud) {
        // Potion Cloud modifiers
        val cloudTag: String
        var applicationDelay = 20
        val effectTag = potion.item.getCustomEffectTag()
        val effectDuration = potion.item.getCustomEffectTimeInTicks() / 4 // PDT
        val effectAmplifier = potion.item.getCustomEffectAmplifier()
        val cloudColor = getEffectColor(effectTag).value()

        // Match names
        when (effectTag) {
            EffectTags.FREEZING -> {
                applicationDelay = 40
                cloudTag = EntityTags.FREEZING_CLOUD
            }
            EffectTags.ROTTING -> {
                cloudTag = EntityTags.DECAYING_CLOUD
            }
            EffectTags.TARRED -> {
                cloudTag = EntityTags.TARRED_CLOUD
            }
            EffectTags.ABLAZE -> {
                cloudTag = EntityTags.BLAZING_CLOUD
                cloud.particle = Particle.SMALL_FLAME
            }
            EffectTags.IRRADIATED -> {
                cloudTag = EntityTags.IRRADIATED_CLOUD
            }
            EffectTags.MIASMA -> {
                cloudTag = EntityTags.MIASMA_CLOUD
            }
            EffectTags.CORRODING -> {
                cloudTag = EntityTags.CORROSION_CLOUD
            }
            EffectTags.ACCURSED -> {
                cloudTag = EntityTags.ACCURSED_CLOUD
            }
            else -> {
                cloudTag = EntityTags.TIMED_CLOUD
            }
        }
        // Add Tags, PDT and Effects to potion cloud
        cloud.also {
            //(potion.item.itemMeta as PotionMeta).addCustomEffect(effectToAdd, true)
            it.particle = Particle.SPELL
            it.color = Color.fromRGB(cloudColor)
            it.duration = 20 * 20
            it.reapplicationDelay = applicationDelay
            it.addScoreboardTag(cloudTag)
            it.addScoreboardTag(EntityTags.CUSTOM_EFFECT_CLOUD)
            it.setCloudEffectTag(effectTag)
            it.setCloudEffectTimeInTicks(effectDuration)
            it.setCloudEffectAmplifier(effectAmplifier)
        }
    }


}