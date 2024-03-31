package me.shadowalzazel.mcodyssey.listeners.utility

import me.shadowalzazel.mcodyssey.constants.EntityTags
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

class LootLogic(
    private val raw: Double,
    mob: LivingEntity,
    player: Player) {

    private var lootLuck: Double = 0.0 // Rename

    init {
        if (player.equipment.itemInMainHand.itemMeta != null) {
            if (player.equipment.itemInMainHand.itemMeta.hasEnchant(Enchantment.LOOT_BONUS_MOBS)) {
                lootLuck += player.activeItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) * 1.0
            }
        }
        if (player.hasPotionEffect(PotionEffectType.LUCK)) {
            lootLuck += 1.25
        }
        if (player.hasPotionEffect(PotionEffectType.UNLUCK)) {
            lootLuck -= 0.75
        }
        if (EntityTags.BLOOD_MOON_MOB in mob.scoreboardTags) {
            lootLuck += 8.0
        }

    }

    fun roll(modifier: Double = 0.0): Boolean {
        return (raw + modifier + lootLuck) * 10 > (0..1000).random()
    }


}