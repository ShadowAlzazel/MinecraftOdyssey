@file:Suppress("DEPRECATION")

package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.bosses.theAmbassador.AmbassadorBoss
import me.shadowalzazel.mcodyssey.commands.SpawnAmbassador
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.event.world.TimeSkipEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Repairable
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.sql.Time

object MinecraftOdysseyListeners : Listener {

    private var smithingCooldown: Long = 0

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

    // Gilded for ONLY smithing

    // Check for new enchants and repairs
    // FIX FOR ALL ENCHANTS
    // ANVIL OR SMITH
    /*
    @EventHandler
    fun onAnvilEnchant(event: PrepareAnvilEvent) {
        if (event.inventory.firstItem != null && event.inventory.secondItem != null) {
            // Check if Book
            val secondItem = event.inventory.secondItem!!
            val firstItem = event.inventory.firstItem!!
            println("Null Check!")
            if (secondItem.itemMeta.hasLore() || firstItem.itemMeta.hasLore()) {
                println("Passed lore OR")
                var hasOdysseyEnchant = false
                // Check if it has odyssey enchant
                val newEnchantments = firstItem.enchantments + secondItem.enchantments
                for (enchant in newEnchantments.keys) {
                    println("CHECKING...")
                    if (enchant in OdysseyEnchantments.enchantmentSet) {
                        hasOdysseyEnchant = true
                        break
                    }
                    // Check if it cannot be applied
                    if (!(enchant.canEnchantItem(firstItem)))
                    {
                        return
                    }
                }

                if (hasOdysseyEnchant) {
                    println("Passed Check")
                    // ADD DEBUG
                    var someLore = listOf<String>()

                    if (firstItem.itemMeta.hasLore() && secondItem.itemMeta.hasLore()) {
                        val firstLore = firstItem.itemMeta.lore
                        val secondLore = secondItem.itemMeta.lore
                        val newLore = firstLore!! + secondLore
                        someLore = newLore as List<String>
                    }
                    else if (firstItem.itemMeta.hasLore()) {
                        someLore = firstItem.itemMeta.lore!!
                    }
                    else if (secondItem.itemMeta.hasLore()) {
                        someLore = secondItem.itemMeta.lore!!
                    }

                    println("ADDED LORE")

                    if (event.result != null) {
                        val anvilResult = event.result
                        anvilResult!!.itemMeta = firstItem.itemMeta
                        anvilResult.addUnsafeEnchantments(newEnchantments)
                        anvilResult.itemMeta.lore = someLore
                        event.result = anvilResult
                        println("FINISHED")
                    }
                    println("HUH>")




                    // ADD NULL CHECK LATER
                    //val anvilItem = event.result

                }
            }
        }
    }
    */

    /*
    @EventHandler
    fun basicSmithingCraft(event: PrepareSmithingEvent) {
        val timeElapsed = System.currentTimeMillis() - smithingCooldown
        if (timeElapsed > 2) {
            smithingCooldown = System.currentTimeMillis()

            if (event.inventory.inputEquipment != null && event.inventory.inputMineral != null) {
                event.result = ItemStack(Material.AIR, 1)

                if (event.inventory.inputEquipment!!.type == Material.DIAMOND_SWORD && event.inventory.inputMineral!!.type == Material.ENCHANTED_BOOK) {
                    event.result = ItemStack(Material.GOLDEN_HOE)
                    event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                    println("${event.result}")
                }
                else {
                    event.result = ItemStack(Material.AIR, 1)
                    event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                    println("${event.result}")
                }

                event.setResult(event.result)
                event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                println("${event.result}")
            }
        }

    }
    */



    @EventHandler
    // MAKE ALL HERE!!
    fun basicSmithingCraft(event: PrepareSmithingEvent) {
        // WORKS!!!
        if (event.inventory.inputEquipment != null && event.inventory.inputMineral != null) {
            val timeElapsed = System.currentTimeMillis() - smithingCooldown


            //BASE CASE!
            if (event.inventory.inputEquipment!!.type == Material.DIAMOND_SWORD && event.inventory.inputMineral!!.type == Material.ENCHANTED_BOOK) {
                event.result = ItemStack(Material.GOLDEN_HOE)
                event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                println("${event.result}")
            }

            // Naming
            else if (event.inventory.inputMineral!!.type == Material.DIAMOND || event.inventory.inputMineral!!.type == Material.AMETHYST_SHARD) {
                //event.result = event.inventory.inputEquipment!!
                //val namedSword = ItemStack(event.inventory.inputEquipment!!.type, 1)
                val namedSword = event.inventory.inputEquipment!!.clone()
                var forgerName = mutableListOf("")
                for (viewer in event.viewers) {
                    if (viewer is Player) {
                        forgerName += "${ChatColor.DARK_GRAY}${ChatColor.ITALIC}Created by ${viewer.name}"
                        println("$forgerName")
                        if (timeElapsed > 20) {
                            smithingCooldown = System.currentTimeMillis()
                            viewer.sendMessage("This is a permanent engraving. Once engraved this item can no longer be modified!")
                        }
                    }
                }
                if (namedSword.lore != null) {
                    namedSword.lore!! + forgerName
                }
                else {
                    namedSword.lore = forgerName
                }
                event.result = namedSword
                event.viewers.forEach { somePlayer -> (somePlayer as Player).updateInventory() }
                println("${event.result}")
            }
            // Check
            else {
                event.result = ItemStack(Material.AIR, 1)
                event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                println("${event.result}")
            }
        }
    }



    @EventHandler
    fun newBoss(event: TimeSkipEvent) {
        if (!MinecraftOdyssey.instance.activeBoss) {
            val timeElapsed = System.currentTimeMillis() - MinecraftOdyssey.instance.timeSinceBoss
            if (timeElapsed >= 90000000) {
                when ((0..4).random()) {
                    //for all bosses later 1, 2 ,3
                    0 -> {
                        MinecraftOdyssey.instance.currentBoss = AmbassadorBoss()
                        MinecraftOdyssey.instance.activeBoss = true
                        val ambassadorBoss = MinecraftOdyssey.instance.currentBoss as AmbassadorBoss
                        ambassadorBoss.createBoss(event.world)
                        println("${event.world.name}Spawned the Ambassador")
                    }
                    else -> {

                    }
                }
            }
        }
    }





    // Feature
    /*
    @EventHandler
    fun onAnvilEnchant(event: PrepareSmithingEvent) {
        if (event.inventory.inputEquipment != null && event.inventory.inputMineral != null) {
            // Check if Book
            val firstItem = event.inventory.inputEquipment!!
            val secondItem = event.inventory.inputMineral!!
            println("Null Check!")
            if (secondItem.itemMeta.hasLore() || firstItem.itemMeta.hasLore()) {
                println("Passed lore OR")
                var hasOdysseyEnchant = false
                // Check if it has odyssey enchant
                val newEnchantments = firstItem.enchantments + secondItem.enchantments
                println("$newEnchantments")
                for (enchant in newEnchantments.keys) {
                    println("CHECKING...")
                    if (enchant in OdysseyEnchantments.enchantmentSet) {
                        hasOdysseyEnchant = true
                        break
                    }
                    // Check if it cannot be applied
                    if (!(enchant.canEnchantItem(firstItem)))
                    {
                        return
                    }
                }

                if (hasOdysseyEnchant) {
                    println("Passed Check")
                    // ADD DEBUG
                    var someLore = listOf<String>()

                    if (firstItem.itemMeta.hasLore() && secondItem.itemMeta.hasLore()) {
                        val firstLore = firstItem.itemMeta.lore
                        val secondLore = secondItem.itemMeta.lore
                        val newLore = firstLore!! + secondLore!!
                        someLore = newLore
                        println("L1")
                    }
                    else if (firstItem.itemMeta.hasLore()) {
                        someLore = firstItem.itemMeta.lore!!
                        println("L2")
                    }
                    else if (secondItem.itemMeta.hasLore()) {
                        someLore = secondItem.itemMeta.lore!!
                        println("L3")
                    }

                    println("ADDED LORE")

                    /*
                    val smithingResult = firstItem.clone()
                    smithingResult!!.itemMeta = firstItem.itemMeta
                    smithingResult.addUnsafeEnchantments(secondItem.enchantments)
                    smithingResult.itemMeta.lore = someLore
                    event.result = smithingResult
                     */
                    event.result = ItemStack(Material.DIAMOND_AXE, 1)
                    event.view.player as Player

                    if (event.result != null) {
                        for (viewer in event.viewers)
                            if (viewer is Player) {
                                viewer.updateInventory()
                                println("UPDATE")
                            }
                        println("FINISHED")
                    }

                    // ADD NULL CHECK LATER
                    //val anvilItem = event.result

                }
            }
        }
    }

     */


}