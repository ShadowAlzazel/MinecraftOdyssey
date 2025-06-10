package me.shadowalzazel.mcodyssey.util.constants

import me.shadowalzazel.mcodyssey.Odyssey
import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.persistence.PersistentDataType

object EntityTags {

    // PIGLINS
    const val IS_HIRED: String = "odyssey.is_hired"
    const val HIRED_BY: String = "odyssey.hired_by."
    const val STARTED_RALLYING: String = "odyssey.started_rallying"
    const val RUNNING_PIG: String = "odyssey.running_pig"
    const val CLONED: String = "odyssey.cloned"

    // MINESHAFT
    const val IN_MINESHAFT: String = "odyssey.in_mineshaft"
    const val IN_SUPERSHAFT: String = "odyssey.in_supershaft"
    const val IN_LINE_MINE: String = "odyssey.in_line_mine"

    // SNIFFERS
    const val CAN_DIG_BIOME_SEEDS: String = "odyssey.can_dig_biome_seeds"

    // MOBS

    // AREA_EFFECT_CLOUD
    const val FREEZING_CLOUD: String = "odyssey.freezing_cloud"
    const val DECAYING_CLOUD: String = "odyssey.decaying_cloud"
    const val TARRED_CLOUD: String = "odyssey.douse_cloud"
    const val BLAZING_CLOUD: String = "odyssey.blazing_cloud"
    const val IRRADIATED_CLOUD: String = "odyssey.rose_cloud"
    const val MIASMA_CLOUD: String = "odyssey.miasma_cloud"
    const val CORROSION_CLOUD: String = "odyssey.puffjuice_cloud"
    const val ACCURSED_CLOUD: String = "odyssey.accursed_cloud"
    const val SOUL_DAMAGE_CLOUD: String = "odyssey.soul_damage_cloud"

    // CLOUD UTILITY
    const val TIMED_CLOUD: String = "odyssey.timed_cloud"
    const val CUSTOM_EFFECT_CLOUD: String = "odyssey.custom_effect_cloud"

    // ALCHEMY - PERSISTENT DATA CONTAINER
    const val CUSTOM_EFFECT_TIME: String = "odyssey.custom_effect_time" // Stores Int for ticks [PDT]
    const val CUSTOM_EFFECT_TAG: String = "odyssey.custom_effect_tag" // Stores String USE EffectTags [PDT]
    const val CUSTOM_EFFECT_AMPLIFIER: String = "odyssey.custom_effect_amplifier" // Stores Int [PDT]

    // BOSS
    const val DRAGON_BOMB: String = "odyssey.dragon_bomb"
    const val LEFT_PORTAL: String = "odyssey.left_portal"
    const val LIGHTNING_BALL: String = "odyssey.lightning_ball"

    const val SUPER_FIREWORK: String = "odyssey.super_firework"

    // MOBS
    const val ODYSSEY_MOB: String = "odyssey.mob"
    const val CUSTOMIZED_MOB: String = "odyssey.customized_mob"
    const val GILDED_MOB: String = "odyssey.gilded_mob"
    const val ELITE_MOB: String = "odyssey.elite_mob"
    const val BLOOD_MOON_MOB: String = "odyssey.blood_moon_mob"
    const val SHADOW_MOB: String = "odyssey.shadow_mob"

    const val TRIAL_ELITE: String = "odyssey.trial_elite"
    const val GIANT_MOB: String = "odyssey.giant"

    const val HANDLED: String = "odyssey._handled" // Used to check and not trigger spawn events

    // ARROWS
    const val AMBUSH_ARROW: String = "odyssey.ambush_arrow"
    const val BALLISTICS_ARROW: String = "odyssey.ballistics_arrow"
    const val DEADEYE_ARROW: String = "odyssey.deadeye_arrow"
    const val DEATH_FROM_ABOVE_ARROW: String = "odyssey.death_from_above_arrow"
    const val BOLA_SHOT_ARROW: String = "odyssey.bola_shot_arrow"
    const val CHAIN_REACTION_ARROW: String = "odyssey.chain_reaction_arrow"
    const val CLUSTER_SHOT_ARROW: String = "odyssey.cluster_shot_arrow"
    const val ENTANGLEMENT_ARROW: String = "odyssey.entanglement_arrow"
    const val LUXPOSE_ARROW: String = "odyssey.luxpose_arrow"
    const val OVERCHARGE_ARROW: String = "odyssey.overcharge_arrow"
    const val PERPETUAL_ARROW: String = "odyssey.perpetual_arrow"
    const val RICOCHET_ARROW: String = "odyssey.ricochet_arrow"
    const val SHARPSHOOTER_ARROW: String = "odyssey.sharpshooter_arrow"
    const val SINGLE_OUT_ARROW: String = "odyssey.single_out_arrow"
    const val SOUL_REND_ARROW: String = "odyssey.soul_rend_arrow"
    const val TEMPORAL_ARROW: String = "odyssey.temporal_arrow"
    const val VULNEROCITY_ARROW: String = "odyssey.vulnerocity_arrow"

    const val ORIGINAL_ARROW: String = "odyssey.original_arrow"
    const val REPLICATED_ARROW: String = "odyssey.replicated_arrow"
    const val REPLICATED_BURST_ARROW: String = "odyssey.replicated_burst_arrow"

    // ARROW UTILITY
    const val AMBUSH_MODIFIER: String = "odyssey.ambush_modifier"
    const val BALLISTICS_MODIFIER: String = "odyssey.ballistics_modifier"
    const val BOLA_SHOT_MODIFIER: String = "odyssey.bola_shot_modifier"
    const val DEADEYE_MODIFIER: String = "odyssey.deadeye_modifier"
    const val DEATH_FROM_ABOVE_MODIFIER: String = "odyssey.death_from_above_modifier"
    const val DOUBLE_TAP_SHOT: String = "odyssey.double_tap_shot"
    const val ENTANGLEMENT_MODIFIER: String = "odyssey.entanglement_modifier"
    const val ENTANGLED: String = "odyssey.entangled"
    const val FAN_FIRE_SHOT: String = "odyssey.fan_fire_shot"
    const val CHAIN_REACTION_MODIFIER: String = "odyssey.chain_reaction_modifier"
    const val CHAIN_REACTION_SPAWNED: String = "odyssey.chain_reaction_spawned"
    const val CHAIN_REACTION_LAST_HIT: String = "odyssey.chain_reaction_last_hit"
    const val CLUSTER_SHOT_MODIFIER: String = "odyssey.cluster_shot_modifier"
    const val LUXPOSE_MODIFIER: String = "odyssey.luxpose_modifier"
    const val OVERCHARGE_MODIFIER: String = "odyssey.overcharge_modifier"
    const val OVERCHARGING: String = "odyssey.bow_overcharging"
    const val PERPETUAL_MODIFIER: String = "odyssey.perpetual_modifier"
    const val RICOCHET_BOUNCE: String = "odyssey.ricochet_bounce"
    const val RICOCHET_MODIFIER: String = "odyssey.ricochet_modifier"
    const val SHARPSHOOTER_MODIFIER: String = "odyssey.sharpshooter_modifier"
    const val SINGLE_OUT_MODIFIER: String = "odyssey.single_out_modifier"
    const val SOUL_REND_MODIFIER: String = "odyssey.soul_rend_modifier"
    const val SOUL_RENDED_BY: String = "odyssey.soul_rended_by"
    const val TORRENT_MODIFIER: String = "odyssey.temporal_modifier"
    const val VULNEROCITY_MODIFIER: String = "odyssey.vulnerocity_modifier"

    // CONSUMABLE ARROWS
    const val EXPLOSIVE_ARROW: String = "odyssey.explosive_arrow"

    // Grappling
    const val GRAPPLE_HOOK: String = "odyssey.grapple_hook" // For the projectile
    const val HAS_SHOT_GRAPPLE: String = "odyssey.has_shot_grapple" // for when shooter started the shot
    const val IS_GRAPPLING: String = "odyssey.is_grappling" // WHen entity is grappling
    const val CANCEL_GRAPPLE: String = "odyssey.cancel_grapple" // For canceling grapple midway pull

    // ARROW TASKS
    const val IS_BURST_BARRAGING: String = "odyssey.is_burst_barraging"

    // ENCHANTS
    const val FALLING_SINGULARITY: String = "odyssey.falling_singularity"
    const val MOVING_SINGULARITY: String = "odyssey.moving_singularity"
    const val MARKED_FOR_VENGEANCE: String = "odyssey.marked_for_vengeance"
    const val VENGEFUL_MODIFIER: String = "odyssey.vengeful_modifier"
    const val MARKED_BY: String = "odyssey.marked_by."
    const val ECHO_STRUCK: String = "odyssey.echo_struck"
    const val BREATH_CLOUD: String = "odyssey.breath_cloud"
    const val BREATH_BY: String = "odyssey.breath_by."
    const val ROOT_BOOTS_ROOTED: String = "odyssey.root_boots_rooted"
    const val STATIC_SOCKS_CHARGING: String = "odyssey.static_socks_charging"
    const val STATIC_SOCKS_CHARGE: String = "odyssey.static_socks_charge"
    const val POLLEN_GUARD_STACKS: String = "odyssey.pollen_guard_stacks"
    const val LEAP_FROG_READY: String = "odyssey.leap_frog_ready"
    const val VOID_TOUCHED: String = "odyssey.void_touched"
    const val VOID_STRUCK_BY: String = "odyssey.void_struck_by"
    const val VOID_STRIKE_MODIFIER: String = "odyssey.void_strike_modifier"
    const val AMBUSH_MARKED: String = "odyssey.ambush_marked"
    const val CLOUD_STRIDER_IS_JUMPING: String = "odyssey.cloud_strider_is_jumping"
    const val CLOUD_STRIDER_JUMPS: String = "odyssey.cloud_strider_jumps"
    const val LODESIGHT_BLOCK: String = "odyssey.lodesight_block"

    // WEAPONS
    const val COMBOED: String = "odyssey.comboed"
    const val HIT_BY_AOE_SWEEP: String = "odyssey.melee_aoe_hit"
    const val THROWN_KUNAI: String = "odyssey.thrown_kunai"
    const val THROWN_CHAKRAM: String = "odyssey.thrown_chakram"
    const val THROWN_SHURIKEN: String = "odyssey.thrown_shuriken"
    const val CHAKRAM_HAS_BOUNCED: String = "odyssey.chakram_has_bounced"
    const val THROWABLE_DAMAGE: String = "odyssey.throwable_damage"
    const val THROWABLE_ATTACK_HIT: String = "odyssey.throwable_attack_hit"
    const val MAGIC_MISSILE: String = "odyssey.magic_missile"


    /*-----------------------------------------------------------------------------------------------*/
    /*--------------------------------------------FUNCTIONS------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/

    fun Entity.setIntTag(tag: String, count: Int) {
        val tagKey = NamespacedKey(Odyssey.instance, tag)
        persistentDataContainer.set(tagKey, PersistentDataType.INTEGER, count)
    }

    fun Entity.getIntTag(tag: String): Int? {
        val tagKey = NamespacedKey(Odyssey.instance, tag)
        return persistentDataContainer[tagKey, PersistentDataType.INTEGER]
    }

    fun Entity.removeTag(tag: String) {
        val tagKey = NamespacedKey(Odyssey.instance, tag)
        persistentDataContainer.remove(tagKey)
    }

}