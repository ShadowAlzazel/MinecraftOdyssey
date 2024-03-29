package me.shadowalzazel.mcodyssey.occurrences.other

import me.shadowalzazel.mcodyssey.occurrences.base.Condition
import me.shadowalzazel.mcodyssey.occurrences.base.OccurrenceAction
import me.shadowalzazel.mcodyssey.occurrences.base.OccurrenceType
import me.shadowalzazel.mcodyssey.occurrences.base.Requirement
import me.shadowalzazel.mcodyssey.seasons.SeasonType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

interface TemplateCreator {

    fun temp() {

        // CREATE TEMPLATE
        // THEN -> READ
        // THEN -> CREATE NEW OCCURRENCE
        // THEN -> MATCH TEMP VALUES TO NEW OCCURRENCE

        val jsonTemplate = mapOf(
            "name" to "Hello",
            "type" to OccurrenceType.LUNAR,
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
            "persistent" to listOf(
                mapOf(
                    "criteria_name" to "is_high_and_wet",
                    "entries" to listOf(
                        OccurrenceAction.ApplyFreeze(7),
                        OccurrenceAction.ApplyPotionEffects(listOf(PotionEffect(PotionEffectType.HUNGER, 2, 1)))
                    )
                )
            ),
            "spawning" to listOf("EMPTY"),
            "one_time" to listOf("ALSO EMPTY")
        )
    }

    // 1. Create Conditions
    // 2. Create Criteria
    // 3. Create Actions link to criteria

}