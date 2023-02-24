package me.shadowalzazel.mcodyssey.occurrences.utility

import me.shadowalzazel.mcodyssey.occurrences.base.*
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class LunarOccurrences {


    fun createAbyssalNight(): Occurrence {
        return Occurrence(
            "Abyssal Night",
            OccurrenceType.LUNAR,
            true,
            listOf(
                Requirement.IsTime(12500, 500)
            ),
            mapOf(
                "is_dark" to listOf(
                    Condition.BlockLighting(0, 8, true)
                )
            ),
            mapOf(
                "is_dark" to listOf(
                    OccurrenceAction.ApplyPotionEffects(
                        listOf(
                            PotionEffect(PotionEffectType.DARKNESS, 10 * 20, 1)
                        )
                    )
                )
            ),
            mapOf(),
            mapOf(),
        )
    }


    fun createBloodMoon(): Occurrence {
        return Occurrence(
            "Abyssal Night",
            OccurrenceType.LUNAR,
            true,
            listOf(
                Requirement.IsTime(12500, 500)
            ),
            mapOf(
                "is_dark" to listOf(
                    Condition.BlockLighting(0, 8, true)
                )
            ),
            mapOf(
                "is_dark" to listOf(
                    OccurrenceAction.ApplyPotionEffects(
                        listOf(
                            PotionEffect(PotionEffectType.DARKNESS, 10 * 20, 1)
                        )
                    )
                )
            ),
            mapOf(),
            mapOf(),
        )
    }




}