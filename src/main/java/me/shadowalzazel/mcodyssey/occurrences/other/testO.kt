package me.shadowalzazel.mcodyssey.occurrences.other

import me.shadowalzazel.mcodyssey.occurrences.base.Condition
import me.shadowalzazel.mcodyssey.occurrences.base.OccurrenceAction
import me.shadowalzazel.mcodyssey.occurrences.base.OccurrenceType
import me.shadowalzazel.mcodyssey.occurrences.base.Requirement
import me.shadowalzazel.mcodyssey.seasons.SeasonType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

fun oMain() {

    //val chillingNight = listOf(Condition.BlockLight(1, 2, false),
    //    OccurrenceAction.PersistentPotionEffect(listOf(PotionEffect(PotionEffectType.HUNGER, 2, 1))))

    // for each json, create occurrence based on sealed classes v
    // then add to list corresponding to type
    //

    //val darkNight = Occurrence(
    //    "DarkNight",
    //    listOf(OccurrenceType.LUNAR),
    //    listOf(EntityType.PLAYER, EntityType.ENDERMAN),
    //    conditions = listOf(Condition.BlockLight(0, 6, false)),
    //    persistentActions = listOf(OccurrenceAction.PersistentPotionEffect(listOf(PotionEffect(PotionEffectType.HUNGER, 2, 1))))
    //)

    val oldTemplate = mapOf(
        "name" to "Hello",
        "type" to listOf(
            OccurrenceType.LUNAR
        ),
        "negatable_by_allay" to false,
        "requirements" to listOf(
            Requirement.IsSeason(listOf(SeasonType.AUTUMN)),
            Requirement.IsTime(1, 1121)
        ),
        "criteria" to mapOf(
            "is_high_and_wet" to listOf(
                Condition.WithinHeight(0, 222),
                Condition.IsWet(true, in_water = true)
            )
        ),
        "persistent_actions" to listOf(
            mapOf(
                "criteria_name" to "is_high_and_wet",
                "entries" to listOf(
                    OccurrenceAction.ApplyFreeze(7),
                    OccurrenceAction.ApplyPotionEffects(
                        listOf(
                            PotionEffect(PotionEffectType.HUNGER, 2, 1)
                        )
                    )
                )
            )
        )
    )

}