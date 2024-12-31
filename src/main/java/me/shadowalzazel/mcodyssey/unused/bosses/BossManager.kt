package me.shadowalzazel.mcodyssey.unused.bosses

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.unused.bosses.base.OdysseyBoss

class BossManager(val odyssey: Odyssey) {

    // Current Boss
    var currentBoss: OdysseyBoss? = null
    var hasBossActive: Boolean = false
    var timeSinceBoss: Long = System.currentTimeMillis()
    var timeUntilBossDespawn: Long = System.currentTimeMillis()

    // Config variables
    var isBossProgressionEnabled: Boolean = true

    // Boss Progression
    // Change This LATER to read from storage
    var isEnderDragonDefeated: Boolean = true
    var isAmbassadorDefeated: Boolean = true

}