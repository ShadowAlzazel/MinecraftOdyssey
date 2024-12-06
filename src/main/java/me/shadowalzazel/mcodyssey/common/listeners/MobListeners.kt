package me.shadowalzazel.mcodyssey.common.listeners

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.tasks.mob_tasks.PiglinRallyTask
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import org.bukkit.Material
import org.bukkit.MusicInstrument
import org.bukkit.Sound
import org.bukkit.entity.Piglin
import org.bukkit.entity.PiglinBrute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityTargetLivingEntityEvent
import org.bukkit.event.entity.PiglinBarterEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MusicInstrumentMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object MobListeners : Listener, DataTagManager {

    // Scarecrow Mob pumpkin and stand
    // weeping angels

    @EventHandler
    fun piglinTargetHandler(event: EntityTargetLivingEntityEvent) {
        // MAYBE STILL REQUIRES GOLD ARMOR, CULTURE THING
        if (event.target == null) { return }
        if (event.entity !is Piglin) { return }
        if (event.target !is Player) { return }
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
        if (!event.input.matchItem("crying_gold")) { return }
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

    @EventHandler
    fun piglinDifficulty(event: EntityDamageByEntityEvent) {
        if (event.entity !is PiglinBrute) return
        if (event.damager !is Player) return
        if (event.cause != EntityDamageEvent.DamageCause.PROJECTILE) return
        val brute = event.entity as PiglinBrute
        brute.target = event.damager as Player
        if (brute.scoreboardTags.contains("in.knight")) {
            val top = event.damager.location.clone().add(0.0, 10.0, 0.0)
            top.getNearbyLivingEntities(5.0).forEach { if (it is PiglinBrute) { it.target = brute.target } }
        }
    }


    // Bastions and Castles
    @EventHandler
    private fun runningPig(event: EntityTargetLivingEntityEvent) {
        if (event.entity !is PiglinBrute) return
        if (event.entity.scoreboardTags.contains(EntityTags.RUNNING_PIG)) { event.isCancelled = true }
    }

    @EventHandler
    private fun piglinHelp(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player) return
        if (event.entity !is PiglinBrute) return
        val brute = event.entity as PiglinBrute
        if (brute.scoreboardTags.contains(EntityTags.STARTED_RALLYING)) return
        if (event.cause == EntityDamageEvent.DamageCause.PROJECTILE) return

        // RNG HERE
        val roll = (1 > (0..40).random())
        if (!roll) { return }

        brute.also {
            // FIND
            var far = 0.0
            var helper: PiglinBrute? = null
            it.getNearbyEntities(16.0, 6.0, 16.0).filterIsInstance<PiglinBrute>().forEach { piglin ->
                val distance = piglin.location.distance(brute.location)
                if (distance > far) {
                    far = distance
                    helper = piglin
                }
            }
            if (helper == null) return
            // SOUNDS
            it.world.playSound(it.location, Sound.ITEM_GOAT_HORN_SOUND_5, 5.5F, 0.95F)
            val horn = ItemStack(Material.GOAT_HORN)
            val musicMeta = horn.itemMeta as MusicInstrumentMeta
            musicMeta.instrument = MusicInstrument.CALL_GOAT_HORN
            horn.itemMeta = musicMeta
            it.equipment.setItemInOffHand(horn)
            it.swingOffHand()
            // SCORE
            it.addScoreboardTag(EntityTags.STARTED_RALLYING)
            it.addScoreboardTag(EntityTags.RUNNING_PIG)
            // MOVEMENT
            val newLocation = helper!!.location.clone()
            it.target = null
            it.pathfinder.stopPathfinding()
            it.pathfinder.moveTo(newLocation, 1.2)
            it.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 20 * 8, 0))
        }

        val rallyTask = PiglinRallyTask(brute)
        rallyTask.runTaskLater(Odyssey.instance, 20 * 8)
    }


}