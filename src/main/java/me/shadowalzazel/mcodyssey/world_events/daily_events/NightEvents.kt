package me.shadowalzazel.mcodyssey.world_events.daily_events

import me.shadowalzazel.mcodyssey.world_events.daily_events.night_events.BloodMoon

object NightEvents {

    //ABYSS, // Darkness at night
    //ACIDIC_RAIN, // Poison Rain
    //BLOOD_MOON, // Ominous Event, more mob spawns
    //BLUE_MOON, // No mob spawns
    //FALLING_STARS, // Fireworks that drop loot
    //FREEZING_NIGHT, // Freezing at night

    val BLOOD_MOON = BloodMoon()

    //  livingVillagers.forEach {
    //            if ((it as Villager).profession == Villager.Profession.FARMER) {
    //                counter += 1
    //            }

    val eventList = listOf(BLOOD_MOON)

}