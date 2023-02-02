package me.shadowalzazel.mcodyssey.bosses.base

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

open class OdysseyBoss(val bossName: String,
                       val entityType: String) {

    protected val vailPrefix = Component.text("[Vail] ", TextColor.color(255, 170, 0), TextDecoration.OBFUSCATED)

    internal open var bossActive: Boolean = false
    internal open var removeBoss: Boolean = false

}
// FOR BIG BOSSES INCREASE BOUNDING BOX