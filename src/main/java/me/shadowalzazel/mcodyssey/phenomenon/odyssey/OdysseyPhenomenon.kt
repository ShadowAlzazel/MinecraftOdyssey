package me.shadowalzazel.mcodyssey.phenomenon.odyssey

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import net.kyori.adventure.text.Component
import org.bukkit.World
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

open class OdysseyPhenomenon(
    private val phenomenonName: String,
    val phenomenonType: PhenomenonTypes,
    private var occurrenceRate: Int,
    private val growthRate: Int,
    private val resetRate: Int,
    private val warningThreshold: Int? = null,
    private val warningMessage: Component? = null) {

    private val failMessages: Map<PhenomenonTypes, List<String>> = mutableMapOf(
        PhenomenonTypes.UTU to listOf("An uneventful day proceeds...",
            "Looks like nothing is happening today...", "MULTIPLE HOSTILES INCOM.. False alarm. Nothing is going on.", "The day continues...",
            "What is next...", "Hello World!", "Just another ordinary day...", "The forecast predicts... Nothing.", "There are no planned disturbances for today...",
            "No distinct events are Schedu-Predicted for today.", "The standard cycle was not disturbed...", "A normal day follows...", "Just plain Today...", "The only thing that happens is nothing at all"),
        PhenomenonTypes.SUEN to listOf("The night remains bleak...", "No monsters will come out... Hopefully.", "The abyss stares and watches but does not act...", "Nothing happens tonight...",
            "Another uneventful night...", "The orientation of the world does not allow for any tests at this period.", "The night remains untouched."),
        PhenomenonTypes.ANKI to listOf("No events follow..."),
        PhenomenonTypes.ABZU to listOf("No events follow...")
    )

    fun criticalWarning(): Boolean {
        return if (warningThreshold != null) { occurrenceRate > warningThreshold } else { false }
    }


    fun rollActivation(someWorld: World, modifier: Int = 0): Boolean {
        return if (occurrenceRate > ((0..100).random() + modifier)) {
            println("$phenomenonName ${phenomenonType}-Phenomenon activated at $someWorld")
            successfulActivation(someWorld)
            true
        } else {
            failedActivation(someWorld)
            false
        }

    }

    open fun successfulActivation(someWorld: World) {
        occurrenceRate = resetRate
        return
    }

    open fun failedActivation(someWorld: World) {
        MinecraftOdyssey.instance.logger.info("$phenomenonName ${phenomenonType}-Phenomenon did not occur")
        // Send fail message
        val randomMessage = failMessages[phenomenonType]!!.random()
        for (somePlayer in someWorld.players) { somePlayer.sendMessage(Component.text(randomMessage)) }
        occurrenceRate += growthRate

        // Check if event has great chance of occurring
        if (criticalWarning()) { criticalityActivation(someWorld.players) }

    }

    open fun criticalityActivation(somePlayers: MutableList<Player>) {
        somePlayers.forEach { it.sendMessage(warningMessage!!) }
    }

    open fun persistentPlayerActives() {
        // IF persistent call this
        // timer delay var and tick speed
    }

    open fun persistentSpawningActives(someEntity: LivingEntity) {
        // IF persistent call this
        // timer delay var and tick speed
    }

    open fun timedActives() {

    }



}
