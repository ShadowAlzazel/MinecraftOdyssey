package me.shadowalzazel.mcodyssey.bosses.the_ambassador

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GiftManager(internal val ambassador: TheAmbassador) {

    fun getGift(player: Player, item: ItemStack) {

    }

    // Appeasement Mechanic
    internal fun appeasementHandler(player: Player, item: ItemStack) {
        // Check if player in gift cooldown map
        var cooldown = ambassador.playersGiftCooldown[player.uniqueId]
        if (cooldown == null) {
            ambassador.playersGiftCooldown[player.uniqueId] = 0
            cooldown = 0
        }
        // Time since last gift
        val timeElapsed = System.currentTimeMillis() - cooldown
        if (timeElapsed >= 4 * 1000) {
            ambassador.playersGiftCooldown[player.uniqueId] = System.currentTimeMillis()
        }
    }


}