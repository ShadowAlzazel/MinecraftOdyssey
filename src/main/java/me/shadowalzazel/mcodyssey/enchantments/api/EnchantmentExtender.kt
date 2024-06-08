package me.shadowalzazel.mcodyssey.enchantments.api

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.enchantments.Enchantment

interface EnchantmentExtender {

    /*-----------------------------------------------------------------------------------------------*/
    // Helper Functions
    private fun getGrayTextComponent(text: String): TextComponent {
        return Component
            .text(text)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            .color(SlotColors.DARK_GRAY.color)
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Extension Helper
    fun Enchantment.getDescriptionTooltip(level: Int): List<Component>  {
        val name = this.key.key
        val textList = getToolTipText(name, level)
        val description: MutableList<Component> = mutableListOf()
        for (t in textList) {
            description.add(getGrayTextComponent(t))
        }
        return description
    }

    private fun getToolTipText(name: String, level: Int): List<String> {
        return when(name) {
            // Vanilla
            "mending" -> listOf(
                "- Repairs the item using experience.")
            "unbreaking" -> listOf(
                "- Increases effective Durability by ${level * 100}%=[level x 100].")
            "protection" -> listOf(
                "- Reduce post mitigated damage by ${level * 4}%=[level x 4].")
            "projectile_protection" -> listOf(
                "- Reduce post mitigated projectile damage by ${level * 8}%=[level x 8].")
            "fire_protection" -> listOf(
                "- Reduce post mitigated fire damage by ${level * 8}%=[level x 8].",
                "- Reduce burn time by ${level * 15}%=[level x 15].")
            "blast_protection" -> listOf(
                "- Reduce post mitigated explosion damage by ${level * 8}%=[level x 8].",
                "- Reduce explosion knockback by ${level * 15}%=[level x 15].")
            "thorns" -> listOf(
                "- Attackers have a ${level * 15}%=[level x 15] to take 1-4 damage.")
            "aqua_affinity" -> listOf(
                "- Increases under water mining speed to surface levels.")
            "respiration" -> listOf(
                "- Extends underwater breathing time by ${level * 15}=[level x 15] seconds.",
                "- Adds a ${level}/${level + 1}=[level / (level + 1)] chance to not take drowning damage.")
            "swift_sneak" -> listOf(
                "- Increase player crouching speed by ${level * 15}%=[level x 15].")
            "feather_falling" -> listOf(
                "- Reduce fall damage taken by ${level * 12}%=[level x 12].")
            "depth_strider" -> listOf(
                "- Increase underwater swimming speed by ${level * 33}%=[level x 33].")
            "frost_walker" -> listOf(
                "- While moving on ground, water blocks are turned to frosted ice within",
                "a ${2 + level}=[2 + level] block circle radius.")
            "soul_speed" -> listOf(
                "- Increase player speed on soul sand and soul soil by ${30 + (level * 10.5)}%=[30 + (level x 10.5)].")
            "sharpness" -> listOf(
                "- Increase Melee Damage by ${0.5 * level + 0.5}=[0.5 + (0.5 x level)].")
            "smite" -> listOf(
                "- Increase Melee Damage to undead mobs by ${2.5 * level}=[2.5 x level]")
            "bane_of_arthropods" -> listOf(
                "- Increase Melee Damage to arthropod mobs by ${2.5 * level}=[2.5 x level]",
                "- Inflicts Slowness IV to an arthropod for around 1-${1 + (0.5 * level)}=[1 + 0.5 x level] seconds.")
            "fire_aspect" -> listOf(
                "- Set the target on fire for ${level * 4}=[level x 4] seconds.")
            "looting" -> listOf(
                "- Increases the number of common drops by ${level}=[level].")
            // Odyssey - Armor
            "antibonk" -> listOf(
                "Reduce critical hit damage by ${2.5 * level}=[2.5 x level].")
            "beastly" -> listOf(
                "Receive ${0.5 * level}=[0.5 x level] less damage when ${2 * level}=[2 x level]",
                "or more enemies are within a 4 block radius.")
            "black_rose" -> listOf(
                "Applies Wither $level=[level] for 5 seconds to enemies", "that attacked the wearer.")
            "blurcise" -> listOf(
                "Take $level=[level] reduced damage while moving.")
            "brawler" -> listOf(
                "Increase melee damage by ${0.5 * level}=[0.5 x level] when ${2 * level}=[2 x level]",
                "or more enemies are within a 4 block radius.")
            "brewful_breath" -> listOf(
                "REMOVED")
            "chitin" -> listOf(
                "Regenerate armor durability alongside health regeneration.")
            "cowardice" -> listOf(
                "Get knock backed further and get speed $level=[level] for 6 seconds.")
            "devastating_drop" -> listOf(
                "Converts fall damage to AOE damage at ${40 * level}%=[40 x level]")
            "dreadful_shriek" -> listOf(
                "REMOVED")
            "fruitful_fare" -> listOf(
                "Eating a fruit recovers $level=[level] Health. Adds a 3 second cooldown to the fruit.")
            "ignore_pain" -> listOf(
                "Decrease Invulnerable time when hit by ${level * 0.1}=[level x 0.1] seconds.",
                "but gain absorption for ${5 - level}=[5 - level] seconds.")
            "illumineye" -> listOf(
                "Taking damage by an entity within line of sight applies",
                "glowing for ${3 + (level * 2)}=[3 + (level x 2)] seconds.")
            "leap_frog" -> listOf(
                "TODO: WIP")
            "mandiblemania" -> listOf(
                "Getting damaged by an entity or attacking an entity at a lower elevation",
                "decreases their immunity time by ${level * 0.1}=[level x 0.1] seconds.")
            "molten_core" -> listOf(
                "Enemies that attack the wearer are set on fire for ${2 * level}=[2 x level] seconds;",
                "This effect is doubled when on fire or on lava.")
            "moonpatch" -> listOf(
                "Regenerates $level=[level] durability per second",
                "at night when the moon is visible.")
            "opticalization" -> listOf(
                "Getting damaged by an entity or attacking an entity within",
                "${2 * level}=[2 x level] blocks forces the wearer and entity to lock on.")
            "pollen_guard" -> listOf(
                "WIP/REWORK")
            "potion_barrier" -> listOf(
                "REMOVED")
            "raging_roar" -> listOf(
                "REMOVED")
            "reckless" -> listOf(
                "Regenerate $level=[level] more health from satiation",
                "but take ${level * 0.5}=[level x 0.5] more damage.")
            "relentless" -> listOf(
                "When damaged, gain ${0.25 * (level * 0.25)}=[0.25 + (level x 0.25)] saturation")
            "root_boots" -> listOf(
                "WIP")
            "sculk_sensitive" -> listOf(
                "On sneak, sense moving entities within a ${5 + (level * 5)}=[5 + (level * 5)] block radius.")
            "speedy_spurs" -> listOf(
                "Gives speed $level=[level] to ridden entities.")
            "sporeful" -> listOf(
                "Getting hit applies Poison 1 for ${2 + (level * 2)}=[2 + (level x 2)] seconds",
                "and Nausea 2 for ${2 + (level * 2)}=[2 + (level x 2)] seconds.")
            "squidify" -> listOf(
                "Getting hit applies Poison 1 for ${level * 2}=[level x 2] seconds",
                "and Slowness 1 for ${level * 2}=[level x 2] seconds.")
            "sslither_ssight" -> listOf(
                "Taking damage by an entity within line of sight disables",
                "their movement for ${0.5 * level}=[0.5 x level] seconds." )
            "static_socks" -> listOf(
                "Gain a static charge every time you sneak maxed at ${level * 2}=[level x 2].",
                "Attacking an entity discharges all stacks for $level=[level] damage.")
            "untouchable" -> listOf(
                "Increase invulnerable time to 1 second.")
            "veiled_in_shadow" -> listOf(
                "Gain invulnerable time negatively proportional to light level.",
                "Reduce required light level by $level=[level].")
            "vigor" -> listOf(
                "Deal $level=[level] more damage when below 40% health.")
            "warcry" -> listOf(
                "REMOVED")
            // Odyssey - Melee
            "arcane_cell" -> listOf(
                "Create a circular zone with a radius of 5 for ${2 + (level * 2)}=[2 + (level x 2)] seconds.",
                "The target can not leave, and are teleported inside if they try.")
            "asphyxiate" -> listOf(
                "TODO")
            "backstabber" -> listOf(
                "TODO")
            "bane_of_the_illager" -> listOf(
                "TODO")
            "bane_of_the_sea" -> listOf(
                "TODO")
            "bane_of_the_swine" -> listOf(
                "TODO")
            "blitz_shift" -> listOf(
                "TODO")
            "buzzy_bees" -> listOf(
                "TODO")
            "budding" -> listOf(
                "TODO")
            "committed" -> listOf(
                "TODO")
            "cull_the_weak" -> listOf(
                "TODO")
            "decaying_touch" -> listOf(
                "REMOVED")
            "douse" -> listOf(
                "TODO")
            "echo" -> listOf(
                "TODO")
            "exploding" -> listOf(
                "TODO")
            "fearful_finisher" -> listOf(
                "TODO")
            "freezing_aspect" -> listOf(
                "TODO")
            "frog_fright" -> listOf(
                "TODO")
            "frosty_fuse" -> listOf(
                "TODO")
            "grasp" -> listOf(
                "TODO")
            "gravity_well" -> listOf(
                "TODO")
            "guarding_strike" -> listOf(
                "TODO")
            "gust" -> listOf(
                "TODO")
            "hemorrhage" -> listOf(
                "TODO")
            "illucidation" -> listOf(
                "TODO")
            "rupturing_strike" -> listOf(
                "TODO")
            "tar_n_dip" -> listOf(
                "REMOVED")
            "vengeful" -> listOf(
                "Deal $level=[level] more damage to enemies that have damaged the wearer.")
            "vital" -> listOf(
                "TODO")
            "void_strike" -> listOf(
                "TODO")
            "whirlwind" -> listOf(
                "TODO")
            // Odyssey - Ranged
            // Fallback
            else -> listOf(name)
        }
    }

}