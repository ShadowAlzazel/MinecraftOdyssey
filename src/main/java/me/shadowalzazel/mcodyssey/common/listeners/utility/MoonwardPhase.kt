package me.shadowalzazel.mcodyssey.common.listeners.utility

import io.papermc.paper.world.MoonPhase
import me.shadowalzazel.mcodyssey.util.EnchantmentsManager
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class MoonwardPhase(val player: Player) : BukkitRunnable(), EnchantmentsManager {

    // Maybe get access to moon phase player list?

    override fun run() {
        if (!player.isOnline) {
            this.cancel()
            return
        }
        if (player.isDead) {
            return
        }

        if (player.world.isDayTime) return
        if (!player.world.hasSkyLight()) return
        if (player.world.moonPhase == MoonPhase.NEW_MOON) return
        if (player.location.block.lightFromSky < 8) return

        // Regen every ten ticks
        // TODO :FIX!!!!!
        /*
        if (player.equipment.helmet.hasEnchantment()) {
            val armorMeta = player.equipment.helmet.itemMeta
            if (armorMeta is Damageable) {
                if (armorMeta.hasDamage()) {
                    armorMeta.damage -= 1
                }
            }
            player.equipment.helmet.itemMeta = armorMeta
        }
        if (player.equipment.chestplate.hasOdysseyEnchantment(OdysseyEnchantments.MOONPATCH)) {
            val armorMeta = player.equipment.chestplate.itemMeta
            if (armorMeta is Damageable) {
                if (armorMeta.hasDamage()) {
                    armorMeta.damage -= 1
                }
            }
            player.equipment.chestplate.itemMeta = armorMeta
        }
        if (player.equipment.leggings.hasOdysseyEnchantment(OdysseyEnchantments.MOONPATCH)) {
            val armorMeta = player.equipment.leggings.itemMeta
            if (armorMeta is Damageable) {
                if (armorMeta.hasDamage()) {
                    armorMeta.damage -= 1
                }
            }
            player.equipment.leggings.itemMeta = armorMeta
        }
        if (player.equipment.boots.hasOdysseyEnchantment(OdysseyEnchantments.MOONPATCH)) {
            val armorMeta = player.equipment.boots.itemMeta
            if (armorMeta is Damageable) {
                if (armorMeta.hasDamage()) {
                    armorMeta.damage -= 1
                }
            }
            player.equipment.boots.itemMeta = armorMeta
        }

         */
    }


}