package me.shadowalzazel.mcodyssey.alchemy

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.arcane.SlotColors
import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.ItemTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.setIntTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.addStringTag
import me.shadowalzazel.mcodyssey.constants.ItemTags.addTag
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
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionType

interface AlchemyManager {

    /*-----------------------------------------------------------------------------------------------*/
    // Tags

    fun ItemStack.hasOdysseyEffectTag(): Boolean {
        return hasTag(ItemTags.ODYSSEY_EFFECT_TAG) || hasTag(ItemTags.IS_ODYSSEY_EFFECT)
    }

    fun ItemStack.hasCustomEffectTag(): Boolean {
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
        setIntTag(ItemTags.ODYSSEY_EFFECT_TIME, timeInTicks)
    }

    // Get Amplifier
    fun ItemStack.getCustomEffectAmplifier(): Int {
        return getIntTag(ItemTags.ODYSSEY_EFFECT_AMPLIFIER) ?: 0
    }

    // Set Amplifier
    fun ItemStack.setCustomEffectAmplifier(amplifier: Int) {
        setIntTag(ItemTags.ODYSSEY_EFFECT_AMPLIFIER, amplifier)
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
            EffectTags.ROTTING -> {
                Component.text("Rotting $numeral ${createTimeString(timeInTicks / 20)}", color)
                    .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            }
            EffectTags.TARRED -> {
                Component.text("Tarred $numeral ${createTimeString(timeInTicks / 20)}", color)
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
    // TEMPORARY Data
    fun ItemStack.getEffectFromData(): PotionEffect {
        val potionData = (itemMeta as PotionMeta).basePotionData
        val baseTime: Int
        var extendedMultiplier: Double = 8.0 / 3.0
        var upgradedMultiplier: Double = 1.0 / 2.0
        var amplifier = if (potionData.isUpgraded) { 1 } else { 0 }
        when (potionData.type) {
            PotionType.TURTLE_MASTER -> {
                baseTime = 20 * 20
                upgradedMultiplier = 1.0
                extendedMultiplier = 2.0
            }
            PotionType.POISON, PotionType.REGEN -> {
                baseTime = 45 * 20
                extendedMultiplier = 2.0
            }
            PotionType.WEAKNESS, PotionType.SLOW_FALLING -> {
                baseTime = 90 * 20
            }
            PotionType.SLOWNESS -> {
                baseTime = 90 * 20
                upgradedMultiplier = 2.0 / 9.0
                if (potionData.isUpgraded) { amplifier = 3 }
            }
            PotionType.INSTANT_HEAL, PotionType.INSTANT_DAMAGE -> {
                baseTime = 0
            }
            else -> {
                baseTime = 3 * 60 * 20
            }
        }

        var time = baseTime.toDouble()
        if (potionData.isUpgraded) { time *= upgradedMultiplier }
        else if (potionData.isExtended) { time *= extendedMultiplier }
        return PotionEffect(potionData.type.effectType!!, time.toInt(), amplifier)
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Potion Creators
    fun createPotionVials(potion: ItemStack): ItemStack {
        val meta = (potion.itemMeta as PotionMeta).clone()

        // Do net Detect Water
        if (meta.basePotionData.type != PotionType.WATER) {
            // Base Effects
            val baseEffect = potion.getEffectFromData() // BREAKS WITH WATER!!!
            meta.addCustomEffect(baseEffect, true)
            // Custom Effects
            val newEffects = mutableListOf<PotionEffect>()
            for (effect in meta.customEffects) {
                val duration = (effect.duration * 0.4).toInt()
                newEffects.add(PotionEffect(effect.type, duration, effect.amplifier))
            }
            for (effect in newEffects) {
                meta.addCustomEffect(effect, true)
            }
        }
        // Meta Apply
        meta.setCustomModelData(ItemModels.VIAL_CHARGE_5)
        meta.basePotionData = PotionData(PotionType.THICK)
        return potion.clone().apply {
            itemMeta = meta
            setIntTag(ItemTags.POTION_CHARGES_LEFT, 5)
            addTag(ItemTags.IS_POTION_VIAL)
            createPotionVialLore()
        }
        // TODO: For Odyssey Effects
    }

    fun ItemStack.createPotionVialLore() {
        val charges = getIntTag(ItemTags.POTION_CHARGES_LEFT) ?: return
        if (!hasTag(ItemTags.IS_POTION_VIAL)) return
        val meta = itemMeta as PotionMeta
        val newLore = meta.lore() ?: mutableListOf()
        val text = "Uses Left: [$charges/5]"
        val component = Component.text(text, SlotColors.GRAY.color).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        newLore.add(component)
    }

    fun createModeledPotion(material: Material, item: ItemStack, model: Int? = null): ItemStack {
        return ItemStack(material, 1).apply {
            itemMeta = item.itemMeta as PotionMeta
            if (model != null) { itemMeta.setCustomModelData(model) }
        }
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