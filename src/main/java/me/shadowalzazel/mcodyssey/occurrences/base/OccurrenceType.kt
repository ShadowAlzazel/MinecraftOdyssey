package me.shadowalzazel.mcodyssey.occurrences.base

enum class OccurrenceType {

    FESTIVAL, // Occurs randomly on 'Villages' and 'Towns'
    INVASION, // Occurs randomly on 'player' base
    SEASONAL, // Occurs on season change
    WEATHER, // Occurs on change of 'weather' or 'climate'
    LUNAR, // Occurs on new 'Night'
    SOLAR, // Occurs on new 'Day'
    PHENOMENON, // Occurs randomly within a minecraft 'Daylight Cycle'
    MINOR, // Small local event. Meteor
    MAJOR, // Major world event. Dungeon growth (ie. trigger jigsaw)
    HUNT // Random Ancient Beast spawns, can be hunted WIP


    // NOTES: OdysseyOccurrenceCreator that 'grief' should always be countered by allay
    // NOTES: An occurrence can have multiple types, each with their own requirements (i.e. drought is weather, but seasonal)
    // NOTES: Read from COMMON Folder in datapack as JSON

    // TYPE: Refers to where they are stored. If Seasonal, only rolled once every 20 days. If Lunar rolled every night, etc.



}