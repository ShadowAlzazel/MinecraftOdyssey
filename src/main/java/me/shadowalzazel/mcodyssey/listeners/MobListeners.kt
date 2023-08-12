package me.shadowalzazel.mcodyssey.listeners

import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.ItemTags.isThisItem
import org.bukkit.entity.Piglin
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTargetLivingEntityEvent
import org.bukkit.event.entity.PiglinBarterEvent

object MobListeners : Listener{

    // Scarecrow Mob pumpkin and stand
    // weeping angels

    @EventHandler
    fun piglinTargetHandler(event: EntityTargetLivingEntityEvent) {
        // MAYBE STILL REQUIRES GOLD ARMOR, CULTURE THING
        if (event.target == null) { return }
        if (event.entity !is Piglin) { return }
        if (event.target !is Player) { return }
        println(event.reason)
        if (event.entity.scoreboardTags.contains("${EntityTags.HIRED_BY}${event.target!!.uniqueId}")) {
            //event.isCancelled = true
            //event.target = null
            //println("Cancelled Target")
        }
        (event.entity as Piglin)


    }

    @EventHandler
    fun piglinHireHandler(event: PiglinBarterEvent) {
        if (!event.input.itemMeta.hasCustomModelData()) { return }
        if (!event.input.isThisItem("crying_gold")) { return }
        // NOT KIDS
        if (event.entity.scoreboardTags.contains(EntityTags.IS_HIRED)) return
        //println(event.entity.target)
        //if (event.entity.target == null) { return }

        // EITHER IMPRINT GOLD WITH UUID OF PLAYER -> different NBTs
        // OR DETECT NEAREST PLAYER
        val first = event.entity.getNearbyEntities(5.0, 3.0, 5.0).first { it is Player }
        //println("FIRST: ${first.uniqueId}")

        if (1 >= (1..4).random()) {
            //println("HIRED")
            event.outcome.removeAll(event.outcome)
            event.entity.addScoreboardTag("${EntityTags.HIRED_BY}${first.uniqueId}")
            event.entity.addScoreboardTag(EntityTags.IS_HIRED)
            event.entity.pathfinder.moveTo(first as Player)
        }
    }

}