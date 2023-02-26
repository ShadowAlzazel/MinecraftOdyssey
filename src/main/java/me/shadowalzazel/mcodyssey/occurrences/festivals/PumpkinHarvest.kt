package me.shadowalzazel.mcodyssey.occurrences.festivals

import me.shadowalzazel.mcodyssey.occurrences.base.OccurrenceType
import me.shadowalzazel.mcodyssey.occurrences.other.OldOccurrence
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.generator.structure.Structure

object PumpkinHarvest: OldOccurrence("Pumpkin Harvest",
    OccurrenceType.FESTIVAL,
    listOf<Structure>(Structure.VILLAGE_PLAINS, Structure.VILLAGE_SAVANNA, Structure.VILLAGE_TAIGA),
    Component.text("There is a pumpkin harvest going on in a nearby village...")) {


    override fun checkRequirements(structureLocation: Location): Boolean {
        // Checks if villager
        val livingVillagers = structureLocation.getNearbyLivingEntities(64.0).filter { it.type == EntityType.VILLAGER }
        var counter = 0
        livingVillagers.forEach {
            if ((it as Villager).profession == Villager.Profession.FARMER) {
                counter += 1
            }
        }
        // required farmers = 3
        return  counter >= 3
    }



    override fun situationEffects(structureLocation: Location) {
        super.situationEffects(structureLocation)

        val livingVillagers = structureLocation.getNearbyLivingEntities(64.0).filter { it.type == EntityType.VILLAGER }
        livingVillagers.forEach {
            if ((it as Villager).profession == Villager.Profession.FARMER) {
                it.recipes
                // Add recipe
                // Timer then remove after a while
            }
        }


    }






}