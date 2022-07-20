package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

object EnchantmentListeners : Listener {

    // UNBIDDEN
    @EventHandler
    fun onEntityDisintegrate(event: EntityDeathEvent) {
        if (event.entity.killer is Player) {
            if (event.entity !is Player) {
                val somePlayer = event.entity.killer!!
                if (somePlayer.inventory.itemInMainHand.hasItemMeta()) {
                    val someWeapon = somePlayer.inventory.itemInMainHand
                    if (someWeapon.itemMeta.hasEnchant(OdysseyEnchantments.BANE_OF_THE_SWINE)) {
                        if (somePlayer.gameMode != GameMode.CREATIVE || somePlayer.gameMode != GameMode.SPECTATOR) {
                            somePlayer.sendMessage("TEST!SEA")
                            somePlayer.giveExpLevels(10)
                        }
                    }
                }
            }
        }
    }

}