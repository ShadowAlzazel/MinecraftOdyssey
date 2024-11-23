package me.shadowalzazel.mcodyssey.common.alchemy

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.util.constants.EffectTags
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.ItemModels
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.common.effects.EffectColors
import me.shadowalzazel.mcodyssey.util.DataTagManager
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
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionType

interface PotionEffectsManager : DataTagManager {

    /*-----------------------------------------------------------------------------------------------*/
    // Tags

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
        addStringTag(ItemDataTags.ODYSSEY_EFFECT_TAG, effectTag)
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
    // TIPPED ARROWS



    /*-----------------------------------------------------------------------------------------------*/
    // Components

    fun getRomanNumeral(number: Int): String {
        val numeralList = mapOf(1 to "I", 2 to "II", 3 to "III", 4 to "IV", 5 to "V", 6 to "VI", 7 to "VII", 8 to "VIII", 9 to "IX", 10 to "X")
        return numeralList[number] ?: number.toString()
    }

    fun createTimeString(timeInSeconds: Int): CharSequence {
        val seconds = timeInSeconds % 60
        val minutes = timeInSeconds / 60
        return if (seconds < 9) { "($minutes:0$seconds)" } else { "($minutes:$seconds)" }
    }

    fun createEffectComponent(tag: String, timeInTicks: Int, amplifier: Int = 1): Component {
        val color = getEffectColor(tag)
        val numeral = getRomanNumeral(amplifier)
        return when (tag) {
            EffectTags.FREEZING -> {
                Component.text("Freezing $numeral ${createTimeString(timeInTicks / 20)}", color)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            }
            EffectTags.BUDDING -> {
                Component.text("Rotting $numeral ${createTimeString(timeInTicks / 20)}", color)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            }
            EffectTags.ABLAZE -> {
                Component.text("Blazing $numeral ${createTimeString(timeInTicks / 20)}", color)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            }
            EffectTags.IRRADIATED -> {
                Component.text("Irradiated $numeral ${createTimeString(timeInTicks / 20)}", color)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            }
            EffectTags.CORRODING -> {
                Component.text("Corrosion $numeral ${createTimeString(timeInTicks / 20)}", color)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            }
            EffectTags.MIASMA -> {
                Component.text("Miasma $numeral ${createTimeString(timeInTicks / 20)}", color)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            }
            EffectTags.ACCURSED -> {
                Component.text("Accursed $numeral", color)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            }
            EffectTags.SOUL_DAMAGE -> {
                Component.text("Soul Damage $numeral", color)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            }
            EffectTags.SHIMMER -> {
                Component.text("Shimmer $numeral ${createTimeString(timeInTicks / 20)}", color)
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
            EffectTags.BUDDING -> {
                EffectColors.ROTTING.color
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
    // Potion Creators
    fun createPotionVialStack(potion: ItemStack): ItemStack {
        val vial = potion.clone()
        val meta = (vial.itemMeta as PotionMeta)
        if (meta.basePotionType == null) return potion
        // Do net Detect Water
        if (meta.basePotionType != PotionType.WATER) {
            val newEffects = mutableListOf<PotionEffect>()
            // Base Effects
            for (effect in meta.basePotionType!!.potionEffects) {
                val duration = (effect.duration * 0.4).toInt()
                newEffects.add(PotionEffect(effect.type, duration, effect.amplifier))
            }
            // Custom Effects
            for (effect in meta.customEffects) {
                val duration = (effect.duration * 0.4).toInt()
                newEffects.add(PotionEffect(effect.type, duration, effect.amplifier))
            }
            // Override effects
            for (effect in newEffects) {
                meta.addCustomEffect(effect, true)
            }
        }
        // Meta Apply
        meta.setCustomModelData(ItemModels.VIAL_CHARGE_1)
        meta.basePotionType = PotionType.THICK
        meta.setMaxStackSize(64)
        vial.itemMeta = meta
        // Return
        vial.addTag(ItemDataTags.IS_POTION_VIAL)
        vial.amount = 4
        return vial
        // TODO: For Odyssey Effects
    }

    fun createOdysseyLingeringPotion(item: ItemStack): ItemStack {
        val potion = ItemStack(Material.LINGERING_POTION, 1)
        val oldTime = item.getCustomEffectTimeInTicks()
        val newTime = oldTime / 4
        // Logic
        potion.itemMeta = (item.itemMeta as PotionMeta)
        // Lore
        return potion.apply {
            setCustomEffectTime(newTime)
            val newLore = lore() ?: mutableListOf()
            val effectTag = item.getCustomEffectTag()
            val amplifier = item.getCustomEffectAmplifier()
            val index = newLore.indexOf(createEffectComponent(effectTag, oldTime, amplifier))
            val foundLore = newLore.find { it.color() == getEffectColor(effectTag) }
            if (foundLore != null) {
                val foundIndex = newLore.indexOf(foundLore)
                newLore[foundIndex] = createEffectComponent(effectTag, newTime, amplifier)
            } else if (index != -1) {
                newLore[index] = createEffectComponent(effectTag, newTime, amplifier)
            } else {
                newLore[0] = createEffectComponent(effectTag, newTime, amplifier)
            }
            lore(newLore)
        }
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
            EffectTags.BUDDING -> {
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