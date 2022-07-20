@file:Suppress("DEPRECATION")

package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object MinecraftOdysseyListeners : Listener {

    // Check if Ender Dragon Dies
    @EventHandler
    fun onDefeatEnderDragon(event: EntityDeathEvent) {
        val dragon = event.entity
        val endWorld = event.entity.world
        if (dragon.type == EntityType.ENDER_DRAGON) {
            for (aPlayer in endWorld.players) {
                aPlayer.sendMessage("${ChatColor.DARK_PURPLE}The end as just begun ...")

            }
        }
    }

    @EventHandler
    fun snowManDamage(event: CreatureSpawnEvent) {
        val snowSpawn = event.spawnReason
        val anEntity = event.entity
        if (snowSpawn == CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN) {
            val anEntityLocationX = anEntity.location.blockX
            val anEntityLocationY = anEntity.location.blockY
            val anEntityLocationZ = anEntity.location.blockZ
            //val anEntityBlock = anEntity.world.getBlockAt(anEntity.location.blockX, anEntity.location.blockY, anEntity.location.blockZ)
            if (anEntity.world.getTemperature(anEntityLocationX, anEntityLocationY, anEntityLocationZ) >= 1) {
                //change to powder snow
                if (true) {
                    val snowSkin = PotionEffect(PotionEffectType.FIRE_RESISTANCE, 9999999, 1)
                    anEntity.addPotionEffect(snowSkin)
                }
            }
        }
    }


    // Smiting table logic

    // Check for new enchants and repairs
    // FIX FOR ALL ENCHANTS
    @EventHandler
    fun onAnvilEnchant(event: PrepareAnvilEvent) {
        if (event.inventory.firstItem != null && event.inventory.secondItem != null) {
            // Check if Book
            val secondItem = event.inventory.secondItem!!
            val firstItem = event.inventory.firstItem!!
            if (secondItem.hasItemMeta()) {
                if (secondItem.type == Material.ENCHANTED_BOOK) {
                    if (secondItem.itemMeta.hasEnchants()) {
                        // Check if odyssey enchant in set
                        var hasOdysseyEnchant = false
                        val enchantmentsAppliedBook = secondItem.enchantments
                        for (enchant in enchantmentsAppliedBook.keys) {
                            if (enchant in OdysseyEnchantments.enchantmentSet) {
                                hasOdysseyEnchant = true
                                break
                            }
                            // Check of item applicable
                            if (!(enchant.canEnchantItem(firstItem)))
                            {
                                return
                            }
                            // Check Conflicts ADD LATER
                        }
                        // Apply Odyssey Enchant Stuff
                        if (hasOdysseyEnchant) {
                            //val enchantmentsAppliedItem = firstItem.enchantments
                            //val newEnchantments = enchantmentsAppliedItem + enchantmentsAppliedBook
                            var someLore = mutableListOf<String?>()
                            if (firstItem.itemMeta.hasLore()) {
                                someLore = ((firstItem.lore!! + secondItem.lore!!) as MutableList<String?>)
                            }
                            someLore = secondItem.lore!!

                            // ADD DEBUG
                            val anvilResult = firstItem.clone()
                            anvilResult.addUnsafeEnchantments(enchantmentsAppliedBook)
                            anvilResult.itemMeta.lore = someLore
                            event.result = anvilResult
                        }
                    }
                }
            }
        }
    }

}