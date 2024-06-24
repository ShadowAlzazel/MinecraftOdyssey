package me.shadowalzazel.mcodyssey.enchantments.api

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.enchantments.Enchantment
import kotlin.math.pow

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

    private fun Double.format(digits: Int) = "%.${digits}f".format(this)

    private fun getToolTipText(name: String, level: Int): List<String> {
        return when(name) {
            // Vanilla
            "curse_of_vanishing" -> listOf(
                "- Item disappears on death.")
            "curse_of_binding" -> listOf(
                "- Item cannot be removed from armor slots.")
            "lure" -> listOf(
                "- Decreases wait time for something to appear by ${level * 5}=[level x 5] seconds.")
            "luck_of_the_sea" -> listOf(
                "- Increases the chance of treasure catches by around ${level * 2.1}%=[level x 2.1].")
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
            "knockback" -> listOf(
                "- The target is knock backed further by around ${(1.6 + (2.6 * level)).format(2)}=[1.6 + 2.6 x level] blocks.")
            "sweeping_edge" -> listOf(
                "- Increases damage dealt by a sweep attack to ${level}/${level + 1}=[level / (level + 1)]")
            "fire_aspect" -> listOf(
                "- Set the target on fire for ${level * 4}=[level x 4] seconds.")
            "looting" -> listOf(
                "- Increases the max number of common drops by ${level}=[level].",
                "- Increases the chance of rare loot by ${level}%=[level].")
            "silk_touch" -> listOf(
                "- Causes blocks to drop themselves.")
            "fortune" -> listOf(
                "- Increases the number of drops on average by around ${(level + 1) / 2}=[(level + 1) / 2].")
            "efficiency" -> listOf(
                "- Increases mining speed when using the correct tool by ${(level * 1.0).pow(2)}=[level ^ 2].")
            "riptide" -> listOf(
                "- Throwing a trident in the rain or in the water launches the player",
                "for ${6 * level + 3}=[3 + (level x 6)] blocks.")
            "channeling" -> listOf(
                "- Throwing a trident summons a lightning bolt during a lightning storm.")
            "loyalty" -> listOf(
                "- Trident returns after being thrown")
            "impaling" -> listOf(
                "- Increase trident damage to aquatic mobs by ${2.5 * level}=[2.5 x level]")
            "breach" -> listOf(
                "- Reduce armor effectiveness on hit by ${level * 15}%=[level x 15].")
            "density" -> listOf(
                "- Increase damage dealt per block fallen by ${level * 0.5}=[level x 0.5].")
            "wind_burst" -> listOf(
                "- Launches the player upward on a smash attack for ${level * 8}=[level x 8] blocks.")
            "piercing" -> listOf(
                "- Projectiles can pass through a max of ${level + 1}=[level + 1] entities.")
            "multishot" -> listOf(
                "- Fires 3 arrows at the same time.")
            "quick_charge" -> listOf(
                "- Decrease crossbow charging time by ${0.25 * level}=[level x 0.25] seconds.")
            "infinity" -> listOf(
                "- Standard arrows are not consumed.")
            "flame" -> listOf(
                "- Arrows are ignited and deal fire damage.")
            "punch" -> listOf(
                "- Increase arrow knockback by ${level * 3}=[level x 3] blocks.")
            "power" -> listOf(
                "- Increase arrow damage by ${25 + level * 25}%=[25 + (level x 25)].")
            // Odyssey - Armor
            "analyze" -> listOf(
                "- Increase experience gained by ${level * 10}%=[level x 10].")
            "antibonk" -> listOf(
                "- Reduce critical hit damage by ${2.5 * level}=[2.5 x level].")
            "beastly" -> listOf(
                "- Receive ${level}=[level] less damage when ${2 * level}=[2 x level]",
                "or more enemies are within a 4 block radius.")
            "black_rose" -> listOf(
                "- Applies Wither II for ${level * 5}=[level x 5] seconds to enemies",
                "that attacked the wearer.")
            "blurcise" -> listOf(
                "- Take $level=[level] reduced damage while moving.")
            "brawler" -> listOf(
                "- Increase melee damage by ${level}=[level] when ${2 * level}=[2 x level]",
                "or more enemies are within a 4 block radius.")
            "bulwark" -> listOf(
                "- Increase armor by ${2 * level}%=[2 x level].")
            "claw_climbing" -> listOf(
                "- Increase automatic step height by $level=[level] blocks.")
            "cowardice" -> listOf(
                "- Get knock backed further and get speed $level=[level] for 6 seconds.")
            "devastating_drop" -> listOf(
                "- Converts fall damage to AOE damage at ${40 * level}%=[40 x level]")
            "fruitful_fare" -> listOf(
                "- Eating a fruit recovers $level=[level] Health. Adds a 3 second cooldown to the fruit.")
            "ignore_pain" -> listOf(
                "- Decrease Invulnerable time when hit by ${level * 0.1}=[level x 0.1] seconds",
                "but gain absorption for ${5 - level}=[5 - level] seconds.")
            "illumineye" -> listOf(
                "- Taking or dealing damage to an entity within line of sight applies",
                "Glowing I for ${3 + (level * 2)}=[3 + (level x 2)] seconds.")
            "impetus" -> listOf(
                "- Increase damage while moving by ${level}=[level].")
            "leap_frog" -> listOf(
                "- Increase jump height by ${level * 35}%=[level x 35]")
            "mandiblemania" -> listOf(
                "- Getting damaged by an entity or attacking an entity at a lower eye elevation",
                "decreases their immunity time by ${level * 0.1}=[level x 0.1] seconds.")
            "molten_core" -> listOf(
                "- Enemies that attack the wearer are set on fire for ${2 * level}=[2 x level] seconds;",
                "This effect is doubled when on fire or on lava.")
            "opticalization" -> listOf(
                "- Getting damaged by an entity or attacking an entity within",
                "${2 * level}=[2 x level] blocks forces the wearer and entity to lock on.")
            "pollen_guard" -> listOf(
                "WIP/REWORK")
            "heartened" -> listOf(
                "- Increase max health by ${level * 10}$=[level x 10].")
            "potion_barrier" -> listOf(
                "- Drinking a potion grants resistance for 10 seconds.",
                "- Negative effect timers are reduced by ${level * 0.25}%=[level x 0.25].")
            "reckless" -> listOf(
                "- Regenerate $level=[level] more health from satiation",
                "but take ${level * 0.5}=[level x 0.5] more damage.")
            "relentless" -> listOf(
                "- When damaged, gain ${0.25 * (level * 0.25)}=[0.25 + (level x 0.25)] saturation")
            "root_boots" -> listOf(
                "- Reduce knockback by ${30 * level}%=[30 x level] when standing still.")
            "sculk_sensitive" -> listOf(
                "- On sneak, sense moving entities within a ${5 + (level * 5)}=[5 + (level * 5)] block radius.")
            "speedy_spurs" -> listOf(
                "- Gives speed $level=[level] to ridden entities.")
            "sporeful" -> listOf(
                "- Getting hit applies Poison I for ${2 + (level * 2)}=[2 + (level x 2)] seconds",
                "and Nausea II for ${2 + (level * 2)}=[2 + (level x 2)] seconds.")
            "squidify" -> listOf(
                "- Getting hit applies Blindness I for ${level * 2}=[level x 2] seconds",
                "and Slowness II for ${level * 2}=[level x 2] seconds.")
            "sslither_ssight" -> listOf(
                "- Taking damage by an entity within line of sight disables",
                "their movement for ${0.5 * level}=[0.5 x level] seconds." )
            "static_socks" -> listOf(
                "- Gain a static charge every time you sneak maxed at ${level * 2}=[level x 2].",
                "Attacking an entity discharges all stacks for $level=[level] damage.")
            "untouchable" -> listOf(
                "- Increase invulnerable time to 1 second.")
            "veiled_in_shadow" -> listOf(
                "- Gain invulnerable time negatively proportional to light level.",
                "Reduce required light level by $level=[level].")
            "vigor" -> listOf(
                "- Deal $level=[level] more damage when below 40% health.")
            // Odyssey - Melee
            "agile" -> listOf(
                "- Increase attack speed by ${level * 5}%=[level x 5].")
            "arcane_cell" -> listOf(
                "- Create a circular zone with a radius of 5 for ${2 + (level * 2)}=[2 + (level x 2)] seconds;",
                "The target are teleported to the center if they leave.")
            "asphyxiate" -> listOf(
                "- Reduce target breath by ${2 * level}=[2 x level] seconds.",
                "- Deal $level=[level] damage to enemies without air.")
            "backstabber" -> listOf(
                "- Deal ${2 + (level * 2)}=[2 + (level x 2)] damage against targets",
                "that are looking away from you or while you are invisible.")
            "bane_of_the_illager" -> listOf(
                "- Increase Melee Damage to illagers by ${2.5 * level}=[2.5 x level]")
            "bane_of_the_sea" -> listOf(
                "- Increase Melee Damage to ocean mobs by ${2.5 * level}=[2.5 x level]")
            "bane_of_the_swine" -> listOf(
                "- Increase Melee Damage to piglins by ${2.5 * level}=[2.5 x level]")
            "swap" -> listOf(
                "- Directly attacking a target swaps location.")
            "budding" -> listOf(
                "- Applies Budding ${level}=[level] for 12 seconds.")
            "buzzy_bees" -> listOf(
                "- Summons an angered bee to attack most recent target.")
            "cleave" -> listOf(
                "- Deals ${level}=[level] item damage to armor.")
            "committed" -> listOf(
                "- Increase damage against enemies with less than 40% health by ${1 + level}=[1 + level].")
            "cull_the_weak" -> listOf(
                "- Deal $level=[level] bonus damage to enemies that are slowed,",
                "weak, or fatigued. These effects can stack.")
            "conflagrate" -> listOf(
                "- After 1.5 second delay, the target takes ${level * 20}%=[level x 20] fire damage",
                "based on the original damage amount.")
            "decay" -> listOf(
                "- Apply Wither I for ${level * 4}=[level x 4] seconds.")
            "douse" -> listOf(
                "- Deal bonus damage to enemies that are on fire, or susceptible to water.")
            "echo" -> listOf(
                "- Attacking has a ${20 * level}%=[20 x level] chance to attack again. (Can not re-apply).")
            "exploding" -> listOf(
                "- Killing an enemy creates an explosion that damages enemies",
                "for a maximum of ${(level * 1.0).pow(2)}=[level ^ 2] at the center.")
            "fearful_finisher" -> listOf(
                "- Killing an enemy causes nearby enemies to flee for ${level * 4}=[level x 4] blocks.")
            "freezing_aspect" -> listOf(
                "- Applies Freezing I for ${level * 4}=[level x 4] seconds.")
            "frog_fright" -> listOf(
                "- Attacking enemy pulls them back and damages them for ${level}=[level] after a 0.5 second delay.")
            "frosty_fuse" -> listOf(
                "- Applies a ticking frost bomb to the target that detonates after 5 seconds.")
            "grasp" -> listOf(
                "- Gain ${1 + level * 0.5}=[1 + (level x 0.5)] block interaction range.")
            "gravity_well" -> listOf(
                "- TODO ")
            "guarding_strike" -> listOf(
                "- Attacking while sneaking applies Resistance I for ${level * 3}=[level x 3] seconds.")
            "gust" -> listOf(
                "- Targets are knocked-up for ${level * 100}%=[level x 100] force.")
            "hemorrhage" -> listOf(
                "- Applies ${level}=[level] stacks of Hemorrhage to the target for 9 seconds.")
            "illucidation" -> listOf(
                "- Deal ${level}=[level] bonus damage to enemies that are glowing.",
                "If the attack is a crit, it is doubled, but the glowing is removed.")
            "invocative" -> listOf(
                "- Attacking a target damages the previous attacked target for ${level * 10}%=[level x 10] of the damage.")
            "pestilence" -> listOf(
                "- Killing an enemy that is poisoned, spreads the effect at ${level * 20}%=[level x 20] efficiency.",
                "- An enemy that is already poisoned takes damage based on ${level * 10}%=[level x 10] of the remaining time.")
            "rupture" -> listOf(
                "- Every third attack against a target deals ${level}=[level] true damage.")
            "vengeful" -> listOf(
                "- Deal $level=[level] more damage to enemies that have damaged the wearer.")
            "vital" -> listOf(
                "- Increase melee damage by ${level}=[level] if the attack is a crit.")
            "void_strike" -> listOf(
                "- Apply a stack of Void each attack; up to 10.",
                "- Attacks deal ${level}=[level] per Void stack. At 10, all the stacks are removed.")
            "whirlwind" -> listOf(
                "- Attacks knock nearby away in a cone. Enemies are damaged at ${level * 30}%=[level x 30] efficiency.")
            // Odyssey - Ranged
            "alchemy_artillery" -> listOf(
                "- Potion effect projectiles have their effect timers increased",
                "by ${0.2 * level}=[0.2 x level]%. These projectiles also have their",
                "speed increased by ${0.1 * level}%=[0.1 x level]")
            "ambush" -> listOf(
                "- The first projectile to hit an enemy deals${level * 2}=[level x 2] more damage.")
            "ballistics" -> listOf(
                "- Crossbow projectiles deal $level=[level] more damage.")
            "bola_shot" -> listOf(
                "- Applies Slowness I on the target for ${3 + level}=[3 + level] seconds",
                "- Places a cobweb at the target's location.")
            "burst_barrage" -> listOf(
                "- Shoot ${level}=[2 + level] consecutive arrows. (This does not bypass immunity).")
            "chain_reaction" -> listOf(
                "- On projectile hit, spawn an arrow that targets the closest",
                "enemy. This can happen ${2 + level}=[2 + level] amount of times")
            "cluster_shot" -> listOf(
                "TODO")
            "deadeye" -> listOf(
                "- Projectiles deal ${3 + level * 2}=[3 + (level x 2)] more damage if it hits the target's eye.")
            "death_from_above" -> listOf(
                "- Increase damage by ${1.5 + (level * 1.5)}=[1.5+ (level x 1.5)] if the projectile",
                "was launched from ${4 + level * 4}=[4 + (level x 4)] blocks high.")
            "double_tap" -> listOf(
                "- When you shoot a projectile, another is automatically created.")
            "entanglement" -> listOf(
                "- On projectile hit, the target is tagged. If another target is tagged",
                "within 10 blocks, they are pulled together and damaged for ${level}=[level].")
            "fan_fire" -> listOf(
                "- On projectile shoot, shoot ${level}=[level] extra projectiles",
                "at the nearest enemies within line of sight. (Velocity reduced by 50%).")
            "gale" -> listOf(
                "- After a 0.25 second delay, the shooter is blown in the facing direction.")
            "lucky_draw" -> listOf(
                "- There is a${7 + (10 * level)}=[7 + (level x 10)]% chance to not consume ammo.")
            "luxpose" -> listOf(
                "- Projectiles deal ${level}=[level] more damage to glowing targets.")
            "overcharge" -> listOf(
                "- Holding a fully drown applies a charge to a max of ${level}=[level].",
                "Each charge increase damage by ${level}=[level] and velocity.")
            "perpetual" -> listOf(
                "- Projectiles ignore gravity for ${10 + (level * 5)}=[10 + (level x 5)] seconds.")
            "rain_of_arrows" -> listOf(
                "TODO")
            "ricochet" -> listOf(
                "- Projectiles ricochet off entities/block up to a max of ${level}=[level].",
                "Each bounce increases damage by 2.")
            "sharpshooter" -> listOf(
                "- Critical arrows gain ${level * 10}%=[level * 0.1] speed",
                "and deal ${level * 0.5}=[level x 0.5] extra damage.")
            "single_out" -> listOf(
                "- Isolated targets more than 16 blocks away from another entity take ${level * 2}=[level x 2] more damage.")
            "singularity_shot" -> listOf(
                "TODO")
            "rend" -> listOf(
                "- Marks struck enemies with Rend",
                "- On item hand swap, damage all marked enemies based on how many",
                "arrows they have in them multiplied by ${level}=[level].")
            "steady_aim" -> listOf(
                "- Reduce bow sway and increase accuracy while standing still.")
            "temporal" -> listOf(
                "- Shot projectiles have speed reduced by ${level * 10}%=[level x 10].",
                "After a ${level * 0.2}=[level x 0.2] second delay, the speed is",
                "increased to ${level * 10}%=[level x 10] of the original velocity.")
            "vulnerocity" -> listOf(
                "- On projectile hit, reduce entity immunity time by ${level * 0.1}=[level * 0.1] seconds.")
            // Odyssey - Misc
            "moonpatch" -> listOf(
                "- Regenerates $level=[level] durability per second",
                "at night when the moon is visible.")
            "chitin" -> listOf(
                "- Regenerate durability alongside satiation regeneration.")
            "o_shiny" -> listOf(
                "- The item glistens when hold or worn.")
            "encumbering_curse" -> listOf(
                "- Increase gravity by ${level * 10}%=[level x 10].")
            "parasitic_curse" -> listOf(
                "- When the item takes damage, instead have a ${level * 10}%=[level x 10] chance",
                "to damage the user instead.")
            "fealty" -> listOf(
                "- When the wearer dies, this item is kept when respawning.")
            // Fallback
            else -> listOf(name)
        }
    }

}