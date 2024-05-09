package me.shadowalzazel.mcodyssey.occurrences

import me.shadowalzazel.mcodyssey.occurrences.base.*
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class OdysseyOccurrenceCreator {


    // CURRENTLY DOES NOT READ FROM JSON FIX LATER

    /*

    fun createLunarOccurrences(): List<Occurrence> {

        val lunarList = mutableListOf<Occurrence>()





        return lunarList.filter { it.mainType == OccurrenceType.LUNAR }
    }



    // TODO: Moonlight Festival
    // TODO: Cookie Carnival
    // TODO: Decay Plague
    // TODO: Pumpkin Harvest - golem
    // TODO: Fairy Fair!
    // TODO: Skeleton Invasion


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
                    EntityConditions.BlockLighting(0, 8, true),
                    EntityConditions.IsMobType(EntityType.PLAYER)
                ),
                "on_activation" to listOf(
                    EntityConditions.AlwaysTrue
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
            mapOf(
                "on_activation" to listOf(
                    OccurrenceAction.SendActionBar(Component.text("Is it as someone turned off the stars...", TextColor.color(22, 22, 22)))
                )
            )
        )
    }


    fun createChillingNight(): Occurrence {
        return Occurrence(
            "Chilling Night",
            OccurrenceType.LUNAR,
            true,
            listOf(
                Requirement.IsTime(12500, 500)
            ),
            mapOf(
                "is_dark" to listOf(
                    EntityConditions.BlockLighting(0, 8, true),
                ),
                "is_wet" to listOf(
                    EntityConditions.IsWet(in_rain = true, true)
                )
            ),
            mapOf(
                "is_dark" to listOf(
                    OccurrenceAction.ApplyPotionEffects(
                        listOf(
                            PotionEffect(PotionEffectType.SLOWNESS, 10 * 20, 1)
                        )
                    ),
                    OccurrenceAction.ApplyFreeze(20 * 7)
                ),
                "is_wet" to listOf(
                    OccurrenceAction.ApplyFreeze(20 * 11)
                )
            ),
            mapOf(),
            mapOf(
                "on_activation" to listOf(
                    OccurrenceAction.SendActionBar(Component.text("The air becomes freezing cold...", TextColor.color(106, 177, 242))),
                    OccurrenceAction.ChillingNightPreset
                )
            )
        )
    }








     */





}