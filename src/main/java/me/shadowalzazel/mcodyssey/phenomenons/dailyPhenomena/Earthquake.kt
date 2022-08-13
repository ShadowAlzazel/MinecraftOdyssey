package me.shadowalzazel.mcodyssey.phenomenons.dailyPhenomena

import me.shadowalzazel.mcodyssey.phenomenons.dailyPhenomena.utilty.DailyPhenomenon
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object Earthquake : DailyPhenomenon("Earthquake", 20, 4, 0) {

    // No Warning
    override val hasWarning: Boolean = false

    override fun phenomenonEffect(phenomenonWorld: World) {
        println("A large tremor has hit ${phenomenonWorld.name}!")
        // Earthquake Effects
        val trembleConfusionEffect = PotionEffect(PotionEffectType.CONFUSION, 150, 2)
        val trembleSlowEffect = PotionEffect(PotionEffectType.SLOW, 190, 2)
        val trembleEffects = listOf<PotionEffect>(trembleConfusionEffect, trembleSlowEffect)
        val shakeEffect = PotionEffect(PotionEffectType.SLOW, 100, 1)

        for (aPlayer in phenomenonWorld.players) {
            when ((0..3).random()) {
                3 -> {
                    if (!aPlayer.isFlying){
                        aPlayer.addPotionEffects(trembleEffects)
                        aPlayer.sendMessage("${ChatColor.BOLD}The ground trembles profoundly under you!!!")
                    }
                }
                2 -> {
                    if (!aPlayer.isFlying){
                        aPlayer.addPotionEffect(shakeEffect)
                        aPlayer.sendMessage("${ChatColor.WHITE}The ground shakes under you!")
                    }
                }
                else -> {
                    aPlayer.sendMessage("${ChatColor.ITALIC}Tremors are heard from afar...")
                }
            }
        }
    }

}

//someOdysseyRunnable = BukkitRunnable {

//     @override
//    fun tremblePlayers() {
//        val trembleEffect = PotionEffect(PotionEffectType.SLOW, 80, 1)
//        for (tPlayers in worldPlayers) {
//            tPlayers.addPotionEffect(trembleEffect)
//        }
//    }
//}
//open class TrembleTask : BukkitRunnable() {
//    override fun run() {
//        //val trembleEffect = PotionEffect(PotionEffectType.SLOW, 80, 1)
//        //for (tPlayers in worldPlayers) {
//        //    tPlayers.addPotionEffect(trembleEffect)
//         //}
//        println("Hello World!")



//}


//val scheduler = Bukkit.getScheduler()
//.runTaskLater(this, TrembleTask(), 60L)
//scheduler.runTaskLaterAsynchronously(this, TrembleTask(), 60L)