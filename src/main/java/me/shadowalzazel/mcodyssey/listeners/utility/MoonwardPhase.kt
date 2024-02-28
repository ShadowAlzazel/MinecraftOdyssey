package me.shadowalzazel.mcodyssey.listeners.utility

import io.papermc.paper.world.MoonPhase
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.Damageable
import org.bukkit.scheduler.BukkitRunnable

class MoonwardPhase (val player: Player) : BukkitRunnable() {

    // Maybe get access to moonphaseplayer list?

    override fun run() {
        if (!player.isOnline) {
            //this.cancel()
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
        if (player.equipment.helmet.itemMeta.hasEnchant(OdysseyEnchantments.MOONWARD)) {
            val armorMeta = player.equipment.helmet.itemMeta
            if (armorMeta is Damageable) {
                if (armorMeta.hasDamage()) {
                    armorMeta.damage -= 1
                }
            }
            player.equipment.helmet.itemMeta = armorMeta
        }
        if (player.equipment.chestplate.itemMeta.hasEnchant(OdysseyEnchantments.MOONWARD)) {
            val armorMeta = player.equipment.chestplate.itemMeta
            if (armorMeta is Damageable) {
                if (armorMeta.hasDamage()) {
                    armorMeta.damage -= 1
                }
            }
            player.equipment.chestplate.itemMeta = armorMeta
        }
        if (player.equipment.leggings.itemMeta.hasEnchant(OdysseyEnchantments.MOONWARD)) {
            val armorMeta = player.equipment.leggings.itemMeta
            if (armorMeta is Damageable) {
                if (armorMeta.hasDamage()) {
                    armorMeta.damage -= 1
                }
            }
            player.equipment.leggings.itemMeta = armorMeta
        }
        if (player.equipment.boots.itemMeta.hasEnchant(OdysseyEnchantments.MOONWARD)) {
            val armorMeta = player.equipment.boots.itemMeta
            if (armorMeta is Damageable) {
                if (armorMeta.hasDamage()) {
                    armorMeta.damage -= 1
                }
            }
            player.equipment.boots.itemMeta = armorMeta
        }

    }

}