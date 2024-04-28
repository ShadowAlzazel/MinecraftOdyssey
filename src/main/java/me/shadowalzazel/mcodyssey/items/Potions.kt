package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.alchemy.AlchemyManager
import me.shadowalzazel.mcodyssey.alchemy.base.OdysseyPotion
import me.shadowalzazel.mcodyssey.constants.EffectTags
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.setIntTag
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.addStringTag
import me.shadowalzazel.mcodyssey.constants.ItemDataTags.addTag
import me.shadowalzazel.mcodyssey.effects.EffectColors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

object Potions : AlchemyManager {

    fun OdysseyPotion.createPotionStack(): ItemStack {
        return createItemStack(1).apply {
            val potionMeta = itemMeta as PotionMeta
            if (potionColor != null) potionMeta.color = potionColor
            if (!potionEffects.isNullOrEmpty()) {
                for (effect in potionEffects) { potionMeta.addCustomEffect(effect, true) }
                potionMeta.basePotionType = PotionType.THICK
                // For Cauldron usage
                if (this@createPotionStack == CRYSTALLINE_POTION) {
                    potionMeta.basePotionType = PotionType.MUNDANE
                }
            }
            itemMeta = potionMeta
            if (isOdysseyEffect) {
                potionMeta.basePotionType = PotionType.MUNDANE
                itemMeta = potionMeta
                addTag(ItemDataTags.IS_ODYSSEY_EFFECT)
                addStringTag(ItemDataTags.ODYSSEY_EFFECT_TAG, odysseyEffectTag)
                setIntTag(ItemDataTags.ODYSSEY_EFFECT_TIME, odysseyEffectTimeInTicks) // Int
                setIntTag(ItemDataTags.ODYSSEY_EFFECT_AMPLIFIER, odysseyEffectAmplifier) // Int
                addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS)
                val component = createEffectComponent(odysseyEffectTag, odysseyEffectTimeInTicks, odysseyEffectAmplifier)
                val newLore = lore() ?: mutableListOf()
                newLore.add(component)
                lore(newLore)
            }
        }
    }

    private fun EffectColors.itemColor(): Color {
        return Color.fromRGB(color.value())
    }

    val CRYSTALLINE_POTION = OdysseyPotion(
        name = "crystalline_potion",
        material = Material.POTION,
        customName =  "Crystalline Potion",
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        lore = listOf(Component.text("A crystalline liquid...", TextColor.color(170, 0, 255)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
        potionEffects = listOf(PotionEffect(PotionEffectType.LUCK, 0 , 0)),
        potionColor = Color.fromRGB(147, 84, 255))

    val POTION_OF_LEVITATION = OdysseyPotion(
        name = "potion_of_levitation",
        material = Material.POTION,
        customName = "Potion of Levitation",
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.LEVITATION, 30 * 20 , 0)),
        potionColor = Color.fromRGB(85, 255, 255))

    val POTION_OF_WITHERING = OdysseyPotion(
        name = "potion_of_withering",
        material = Material.POTION,
        customName = "Potion of Decay",
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.WITHER, 30 * 20 , 0)),
        potionColor = Color.fromRGB(55, 55, 55))

    val POTION_OF_LUCK = OdysseyPotion(
        name = "potion_of_luck",
        material = Material.POTION,
        customName = "Potion of Luck",
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.LUCK, 5 * 60 * 20 , 0)),
        potionColor = Color.fromRGB(255, 157, 0))

    val POTION_OF_RESISTANCE = OdysseyPotion(
        name = "potion_of_resistance",
        material = Material.POTION,
        customName = "Potion of Resistance",
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.RESISTANCE, 3 * 60 * 20 , 0)),
        potionColor = Color.fromRGB(65, 97, 122))

    val POTION_OF_HASTE = OdysseyPotion(
        name = "potion_of_resistance",
        material = Material.POTION,
        customName = "Potion of Haste",
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.HASTE, 3 * 60 * 20 , 0)),
        potionColor = Color.fromRGB(255, 233, 133))

    val POTION_OF_BIOLUMINESCENCE = OdysseyPotion(
        name = "potion_of_bioluminescence",
        material = Material.POTION,
        customName = "Potion of Bioluminescence",
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.GLOWING, 3 * 60 * 20 , 0)),
        potionColor = Color.fromRGB(0, 255, 179))

    val POTION_OF_CONSTITUTION = OdysseyPotion(
        name = "potion_of_constitution",
        material = Material.POTION,
        customName = "Potion of Constitution",
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(PotionEffect(PotionEffectType.HEALTH_BOOST, 3 * 60 * 20 , 0)),
        potionColor = Color.fromRGB(209, 0, 49))

    /*-----------------------------------------------------------------------------------------------*/

    val POTION_OF_STONE_SKIN = OdysseyPotion(
        name = "potion_of_stone_skin",
        material = Material.POTION,
        customName = "Potion of Stone Skin",
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(
            PotionEffect(PotionEffectType.ABSORPTION, 2 * 60 * 20 , 2),
            PotionEffect(PotionEffectType.SLOWNESS, 2 * 60 * 20 , 0)),
        potionColor = Color.fromRGB(42, 51, 38))

    val POTION_OF_WRATH = OdysseyPotion(
        name = "potion_of_wrath",
        material = Material.POTION,
        customName = "Potion of Wrath",
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(
            PotionEffect(PotionEffectType.STRENGTH, 2 * 60 * 20 , 2),
            PotionEffect(PotionEffectType.HUNGER, 2 * 60 * 20 , 1)),
        potionColor = Color.fromRGB(250, 60, 17))

    val POTION_OF_WHIZ = OdysseyPotion(
        name = "potion_of_whiz",
        material = Material.POTION,
        customName = "Whiz Potion",
        customModel = ItemModels.PEARSHAPE_BOTTLE,
        potionEffects = listOf(
            PotionEffect(PotionEffectType.SPEED, 2 * 60 * 20 , 2),
            PotionEffect(PotionEffectType.HUNGER, 2 * 60 * 20 , 1),
            PotionEffect(PotionEffectType.NAUSEA, 10 * 20 , 0)),
        potionColor = Color.fromRGB(220, 210, 217))


    /*-----------------------------------------------------------------------------------------------*/

    val FLASK_OF_ROT = OdysseyPotion( // 1:00
        name = "flask_of_rot",
        material = Material.POTION,
        customName = "Flask o' Rot",
        customModel = ItemModels.CONICAL_BOTTLE,
        isOdysseyEffect = true,
        odysseyEffectTag = EffectTags.BUDDING,
        odysseyEffectTimeInTicks = 60 * 20,
        odysseyEffectAmplifier = 1,
        potionColor = Color.fromRGB(114, 227, 154))

    val FLASK_OF_FROST = OdysseyPotion( // 0:30
        name = "flask_of_frost",
        material = Material.POTION,
        customName = "Flask o' Frost",
        customModel = ItemModels.CONICAL_BOTTLE,
        isOdysseyEffect = true,
        odysseyEffectTag = EffectTags.FREEZING,
        odysseyEffectTimeInTicks = 30 * 20,
        odysseyEffectAmplifier = 1,
        potionColor = EffectColors.FREEZING.itemColor())

    val FLASK_OF_TAR = OdysseyPotion( // 3:00
        name = "flask_of_tar",
        material = Material.POTION,
        customName = "Flask o' Tar",
        customModel = ItemModels.CONICAL_BOTTLE,
        isOdysseyEffect = true,
        odysseyEffectTag = EffectTags.TARRED,
        odysseyEffectTimeInTicks = 3 * 60 * 20,
        odysseyEffectAmplifier = 1,
        potionColor = EffectColors.TARRED.itemColor())

    val FLASK_OF_ABLAZE = OdysseyPotion( // 0:30
        name = "flask_of_ablaze",
        material = Material.POTION,
        customName = "Flask o' Ablaze",
        customModel = ItemModels.CONICAL_BOTTLE,
        isOdysseyEffect = true,
        odysseyEffectTag = EffectTags.ABLAZE,
        odysseyEffectTimeInTicks = 30 * 20,
        odysseyEffectAmplifier = 1,
        potionColor = EffectColors.ABLAZE.itemColor())

    val FLASK_OF_IRRADIATION = OdysseyPotion( // 3:00
        name = "flask_of_irradiation",
        material = Material.POTION,
        customName = "Flask o' Irradiation",
        customModel = ItemModels.CONICAL_BOTTLE,
        isOdysseyEffect = true,
        odysseyEffectTag = EffectTags.IRRADIATED,
        odysseyEffectTimeInTicks = 30 * 20,
        odysseyEffectAmplifier = 1,
        potionColor = EffectColors.IRRADIATED.itemColor())

    val FLASK_OF_CORROSION = OdysseyPotion( // 0:30
        name = "flask_of_corrosion",
        material = Material.POTION,
        customName = "Flask o' Corrosion",
        customModel = ItemModels.CONICAL_BOTTLE,
        isOdysseyEffect = true,
        odysseyEffectTag = EffectTags.CORRODING,
        odysseyEffectTimeInTicks = 30 * 20,
        odysseyEffectAmplifier = 1,
        potionColor = EffectColors.CORROSION.itemColor())

    val FLASK_OF_MIASMA = OdysseyPotion( // 3:00
        name = "flask_of_miasma",
        material = Material.POTION,
        customName = "Flask o' Miasma",
        customModel = ItemModels.CONICAL_BOTTLE,
        isOdysseyEffect = true,
        odysseyEffectTag = EffectTags.MIASMA,
        odysseyEffectTimeInTicks = 30 * 20,
        odysseyEffectAmplifier = 1,
        potionColor = EffectColors.MIASMA.itemColor())

    // TONIC OF INVINCIBILITY
    // MAYBE POTION OF KNOCK UP?
    // POTION OF PHASING? reduce collision
    // INFERNO POTION??? fire up nearby enemies

    val ACCURSED_BREW = OdysseyPotion(
        name = "accursed_brew",
        material = Material.POTION,
        customName = "Accursed Brew",
        customModel = ItemModels.ROUND_BOTTLE,
        isOdysseyEffect = true,
        odysseyEffectTag = EffectTags.ACCURSED,
        odysseyEffectAmplifier = 1,
        potionColor = EffectColors.ACCURSED.itemColor())

    val BOTTLE_OF_SOULS = OdysseyPotion(
        name = "bottle_of_souls",
        material = Material.POTION,
        customName = "Bottled Souls",
        customModel = ItemModels.ROUND_BOTTLE,
        isOdysseyEffect = true,
        odysseyEffectTag = EffectTags.ABLAZE,
        odysseyEffectAmplifier = 1,
        potionColor = EffectColors.SOUL.itemColor())

    val BOTTLE_OF_SHIMMER = OdysseyPotion(
        name = "bottle_of_shimmer",
        material = Material.POTION,
        customName = "Bottle o' Shimmer",
        customModel = ItemModels.DAIRYSHAPE_BOTTLE,
        potionEffects = listOf(
            PotionEffect(PotionEffectType.RESISTANCE, 120 * 20, 1),
            PotionEffect(PotionEffectType.STRENGTH, 120 * 20, 1),
            PotionEffect(PotionEffectType.GLOWING, 120 * 20, 1),
            PotionEffect(PotionEffectType.HUNGER, 120 * 20, 0),
            PotionEffect(PotionEffectType.NAUSEA, 120 * 20, 0)),
        potionColor = EffectColors.SHIMMER.itemColor())

    /*-----------------------------------------------------------------------------------------------*/

    // Anglers' Potion
    // Night vision + water breathing + 2 Kelp
    // (10 minutes)
    val ANGLERS_CONCOCTION = OdysseyPotion(
        name = "anglers_concoction",
        material = Material.POTION,
        customName = "Angler's Concoction",
        customModel = ItemModels.CONCOCTION_BOTTLE,
        potionEffects = listOf(
            PotionEffect(PotionEffectType.NIGHT_VISION, 3 * 60 * 20 , 0),
            PotionEffect(PotionEffectType.WATER_BREATHING, 3 * 60 * 20, 0)),
        potionColor = Color.fromRGB(115, 215, 233))

    // Spelunkers' Potion
    // Haste + Glowing + 1 Honeycomb
    // (7 minutes)
    val SPELUNKERS_CONCOCTION = OdysseyPotion(
        name = "spelunkers_concoction",
        material = Material.POTION,
        customName = "Spelunker's Concoction",
        customModel = ItemModels.CONCOCTION_BOTTLE,
        potionEffects = listOf(
            PotionEffect(PotionEffectType.HASTE, 3 * 60 * 20 , 0),
            PotionEffect(PotionEffectType.GLOWING, 3 * 60 * 20, 0)),
        potionColor = Color.fromRGB(115, 215, 233))

    // Nether Owl Potion
    // Night Vision + Fire Resistance + 1 Weeping Vine
    // (10 minutes)
    val NETHER_OWL_CONCOCTION = OdysseyPotion(
        name = "nether_owl_concoction",
        material = Material.POTION,
        customName = "Nether Owl Concoction",
        customModel = ItemModels.CONCOCTION_BOTTLE,
        potionEffects = listOf(
            PotionEffect(PotionEffectType.NIGHT_VISION, 3 * 60 * 20 , 0),
            PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3 * 60 * 20, 0)),
        potionColor = Color.fromRGB(255, 105, 78))

    /*-----------------------------------------------------------------------------------------------*/

    // CUSTOM DECOCTION
    // Potion + Potion + Thick Potion + Popped Chorus Fruit
    val CUSTOM_CONCOCTION = OdysseyPotion(
        name = "custom_concoction",
        material = Material.POTION,
        customName = "Concoction",
        customModel = ItemModels.CONCOCTION_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(185, 255, 155))

    /*
    val LARGE_POTION = OdysseyPotion( // TODO: Thick Potion
        name = "large_potion",
        material = Material.POTION,
        customName = "Large Potion of TODO",
        customModel = ItemModels.LARGE_BOTTLE,
        potionEffects = emptyList(),
        potionColor = Color.fromRGB(185, 255, 155))
     */

}