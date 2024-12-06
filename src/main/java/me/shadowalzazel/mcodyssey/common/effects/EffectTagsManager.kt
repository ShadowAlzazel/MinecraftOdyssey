package me.shadowalzazel.mcodyssey.common.effects

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.EffectTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Color
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.entity.AreaEffectCloud
import org.bukkit.entity.ThrownPotion
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

interface EffectTagsManager : DataTagManager {

    /*-----------------------------------------------------------------------------------------------*/

    // Effects TODO: Finish maybe when official support for custom effects
    // Reapply effects again

    fun ItemStack.hasOdysseyEffectTag(): Boolean {
        return hasTag(ItemDataTags.ODYSSEY_EFFECT_TAG) || hasTag(ItemDataTags.IS_ODYSSEY_EFFECT)
    }

    fun ItemStack.hasCustomEffectTag(): Boolean {
        return hasTag(ItemDataTags.IS_CUSTOM_EFFECT)
    }

    // Get Effect
    fun ItemStack.getCustomEffectTag(): String {
        return getStringTag(ItemDataTags.ODYSSEY_EFFECT_TAG) ?: EffectTags.NO_EFFECT
    }

    fun ItemStack.setCustomEffectTag(effectTag: String) {
        setStringTag(ItemDataTags.ODYSSEY_EFFECT_TAG, effectTag)
    }

    // Default is Ticks
    fun ItemStack.getCustomEffectTimeInTicks(): Int {
        return getIntTag(ItemDataTags.ODYSSEY_EFFECT_TIME) ?: 0
    }

    fun ItemStack.getCustomEffectTimeInSeconds(): Int {
        return getIntTag(ItemDataTags.ODYSSEY_EFFECT_TIME)?.div(20) ?: 0
    }

    // Set Time in Ticks
    fun ItemStack.setCustomEffectTime(timeInTicks: Int) {
        setIntTag(ItemDataTags.ODYSSEY_EFFECT_TIME, timeInTicks)
    }

    // Get Amplifier
    fun ItemStack.getCustomEffectAmplifier(): Int {
        return getIntTag(ItemDataTags.ODYSSEY_EFFECT_AMPLIFIER) ?: 0
    }

    // Set Amplifier
    fun ItemStack.setCustomEffectAmplifier(amplifier: Int) {
        setIntTag(ItemDataTags.ODYSSEY_EFFECT_AMPLIFIER, amplifier)
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
    // Test and Lore

    fun getRomanNumeral(number: Int): String {
        val numeralList = mapOf(1 to "I", 2 to "II", 3 to "III", 4 to "IV", 5 to "V", 6 to "VI", 7 to "VII", 8 to "VIII", 9 to "IX", 10 to "X")
        return numeralList[number] ?: number.toString()
    }

    fun createTimeString(timeInSeconds: Int): CharSequence {
        val seconds = timeInSeconds % 60
        val minutes = timeInSeconds / 60
        return if (seconds < 9) { "($minutes:0$seconds)" } else { "($minutes:$seconds)" }
    }

    fun newEffectLoreComponent(tag: String, timeInTicks: Int, amplifier: Int=1): Component {
        val color = getEffectColor(tag)
        val numeral = getRomanNumeral(amplifier)
        val displayName = tag.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        return Component
            .text("$displayName $numeral ${createTimeString(timeInTicks / 20)}", color)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    }

    fun createEffectLore(tag: String, timeInTicks: Int, amplifier: Int = 1): Component {
        return newEffectLoreComponent(tag, timeInTicks, amplifier)
    }

    fun getEffectColor(tag: String): TextColor {
        return when (tag) {
            EffectTags.FREEZING -> EffectColors.FREEZING.color
            EffectTags.ROTTING ->  EffectColors.ROTTING.color
            EffectTags.ABLAZE -> EffectColors.ABLAZE.color
            EffectTags.IRRADIATED -> EffectColors.IRRADIATED.color
            EffectTags.CORRODING ->     EffectColors.CORROSION.color
            EffectTags.MIASMA -> EffectColors.MIASMA.color
            EffectTags.ACCURSED -> EffectColors.ACCURSED.color
            EffectTags.SOUL_DAMAGE ->   EffectColors.SOUL.color
            EffectTags.SHIMMER ->  EffectColors.SHIMMER.color
            else -> TextColor.color(255, 255, 255)
        }
    }

    // Helper function that converts a char sequence (M:SS) to time Int
    private fun loreToSeconds(timeSequence: CharSequence): Int {
        val minute = timeSequence[1].toString().toInt()
        val tensSecond = timeSequence[3].toString().toInt()
        val onesSecond = timeSequence[4].toString().toInt()
        return (minute * 60) + (tensSecond * 10) + (onesSecond)
    }

    fun createOdysseyEffectCloud(potion: ThrownPotion, cloud: AreaEffectCloud) {
        // Potion Cloud modifiers
        val cloudTag: String
        val effectTag = potion.item.getCustomEffectTag()
        val effectDuration = potion.item.getCustomEffectTimeInTicks() // PDT
        val effectAmplifier = potion.item.getCustomEffectAmplifier()
        val cloudColor = getEffectColor(effectTag).value()

        // Match names
        when (effectTag) {
            EffectTags.FREEZING -> {
                cloudTag = EntityTags.FREEZING_CLOUD
            }
            EffectTags.ROTTING -> {
                cloudTag = EntityTags.DECAYING_CLOUD
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
        cloud.apply {
            color = Color.fromRGB(cloudColor)
            //(potion.item.itemMeta as PotionMeta).addCustomEffect(effectToAdd, true)
            addScoreboardTag(cloudTag)
            addScoreboardTag(EntityTags.CUSTOM_EFFECT_CLOUD)
            setCloudEffectTag(effectTag)
            setCloudEffectTimeInTicks(effectDuration)
            setCloudEffectAmplifier(effectAmplifier)
        }
    }


}