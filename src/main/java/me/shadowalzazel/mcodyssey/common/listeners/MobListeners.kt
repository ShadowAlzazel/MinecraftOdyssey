package me.shadowalzazel.mcodyssey.common.listeners

import io.papermc.paper.entity.LookAnchor
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.common.tasks.mob_tasks.PiglinRallyTask
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import org.bukkit.Material
import org.bukkit.MusicInstrument
import org.bukkit.Sound
import org.bukkit.entity.Boat
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Monster
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

@Suppress("UnstableApiUsage")
object MobListeners : Listener, DataTagManager {

    // Scarecrow Mob pumpkin and stand
    // weeping angels

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


    // ──────────────────────────────────────────────────────────────────────────────
    // ──────────────────────────────── TARGETING ───────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    @EventHandler
    fun entityTargetHandler(event: EntityDamageByEntityEvent) {
        // Get tags
        if (event.damager !is Player) return
        val entity = event.entity as? LivingEntity ?: return
        if (entity.scoreboardTags.isEmpty()) return

        for (tag in entity.scoreboardTags) {
            when (tag) {
                "in.knight" -> {
                    if (event.entity !is PiglinBrute) return
                    if (event.cause != EntityDamageEvent.DamageCause.PROJECTILE) return
                    // Get brute
                    val brute = event.entity as PiglinBrute
                    val damager = event.damager as? LivingEntity ?: return
                    brute.target = damager

                    val top = event.damager.location.clone().add(0.0, 10.0, 0.0)
                    // Get all brutes to target player
                    top.getNearbyLivingEntities(5.0).forEach { if (it is PiglinBrute) { it.target = brute.target } }
                }

                EntityTags.SHADOW_MOB -> {
                    val damager = event.damager as? LivingEntity ?: return
                    val mob = event.entity as? LivingEntity ?: return
                    val vehicle = mob.vehicle
                    val inBoat = vehicle is Boat
                    //val target = entity.getTargetEntity(16) ?: return
                    //if (target !is LivingEntity) return
                    mob.location.getNearbyLivingEntities(6.0, 4.0, 6.0).filter {
                        it != mob &&
                        it.scoreboardTags.contains(EntityTags.SHADOW_MOB) &&
                        it is Monster
                    }.forEach { it as Monster
                        val distance = mob.location.distance(it.location)
                        // Have mobs attack the boat
                        if (inBoat && distance <= 2.0) {
                            it.lookAt(vehicle.location, LookAnchor.EYES)
                            it.attack(vehicle)
                        }
                        else if (!inBoat) {
                            it.target = damager
                        }
                    }

                }

            }
        }

    }


    // Bastions and Castles
    @EventHandler
    private fun runningPig(event: EntityTargetLivingEntityEvent) {
        if (event.entity !is PiglinBrute) return
        if (event.entity.scoreboardTags.contains(EntityTags.RUNNING_PIG)) { event.isCancelled = true }
    }

    @EventHandler
    private fun piglinReinforcements(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player) return
        if (event.entity !is PiglinBrute) return
        val brute = event.entity as PiglinBrute
        if (brute.scoreboardTags.contains(EntityTags.STARTED_RALLYING)) return
        if (event.cause == EntityDamageEvent.DamageCause.PROJECTILE) return

        // 2% Chance to call Reinforcements
        val roll = (2 > (0..100).random())
        if (!roll) { return }

        brute.also {
            // Find reinforcements
            val maxDistance = 16.0
            var foundHelp = false
            var retreatLocation = brute.location
            it.getNearbyEntities(22.0, 6.0, 22.0).filterIsInstance<PiglinBrute>().forEach { helper ->
                val helperDistance = helper.location.distance(it.location)
                // If in range, set target to piglin target
                if (helperDistance < maxDistance) {
                    foundHelp = true
                    helper.target = brute.target
                }
                // if farthest set new retreat there
                if (retreatLocation.distance(it.location) < helperDistance) {
                    retreatLocation = helper.location
                }
            }
            if (!foundHelp) return
            // Create Horn
            it.world.playSound(it.location, Sound.ITEM_GOAT_HORN_SOUND_5, 5.5F, 0.95F)
            val horn = ItemStack(Material.GOAT_HORN)
            val musicMeta = horn.itemMeta as MusicInstrumentMeta
            musicMeta.instrument = MusicInstrument.CALL_GOAT_HORN
            horn.itemMeta = musicMeta
            it.equipment.setItemInOffHand(horn)
            it.swingOffHand()
            // Add scoreboard tags and data
            it.addScoreboardTag(EntityTags.STARTED_RALLYING)
            it.addScoreboardTag(EntityTags.RUNNING_PIG)
            // Pathfinder and retreating
            it.target = null
            it.pathfinder.stopPathfinding()
            it.pathfinder.moveTo(retreatLocation, 1.2)
            it.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, 20 * 8, 0))
        }

        val rallyTask = PiglinRallyTask(brute)
        rallyTask.runTaskLater(Odyssey.instance, 20 * 8)
    }


}