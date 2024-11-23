 package me.shadowalzazel.mcodyssey.tasks.mob_tasks

 import me.shadowalzazel.mcodyssey.util.constants.EntityTags
 import me.shadowalzazel.mcodyssey.common.listeners.SpawningListeners
 import org.bukkit.entity.PiglinBrute
 import org.bukkit.scheduler.BukkitRunnable

 class PiglinRallyTask(private val brute: PiglinBrute) : BukkitRunnable() {

     override fun run() {
         if (brute.isDead) {
             this.cancel()
             return
         }

         brute.getNearbyEntities(7.0, 6.0, 7.0).forEach {
             if (it is PiglinBrute && it != brute) {
                 val new = SpawningListeners.handlePiglinBruteSpawn(it)
                 new.addScoreboardTag(EntityTags.STARTED_RALLYING)
             }
             it.addScoreboardTag(EntityTags.STARTED_RALLYING)
         }
        brute.removeScoreboardTag(EntityTags.RUNNING_PIG)

     }

 }