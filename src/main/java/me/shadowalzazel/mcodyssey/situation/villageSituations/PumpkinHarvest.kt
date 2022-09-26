package me.shadowalzazel.mcodyssey.situation.villageSituations

import me.shadowalzazel.mcodyssey.situation.odyssey.OdysseySituation
import me.shadowalzazel.mcodyssey.situation.odyssey.SituationTypes
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager
import org.bukkit.generator.structure.Structure

object PumpkinHarvest: OdysseySituation("Pumpkin Harvest",
    SituationTypes.MASTO,
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

        println("LOLOL")
        val livingVillagers = structureLocation.getNearbyLivingEntities(64.0).filter { it.type == EntityType.VILLAGER }
        livingVillagers.forEach {
            if ((it as Villager).profession == Villager.Profession.FARMER) {
                it.recipes
                // Add recipe
                // Timer then remove after a while
                println(it)
            }
        }


    }





}