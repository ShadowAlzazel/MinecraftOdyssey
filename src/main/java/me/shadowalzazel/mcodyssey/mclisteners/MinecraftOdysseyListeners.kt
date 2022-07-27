@file:Suppress("DEPRECATION")

package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.bosses.theAmbassador.AmbassadorBoss
import me.shadowalzazel.mcodyssey.enchantments.GildedPower
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.odysseyUtility.OdysseyItems
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.event.world.TimeSkipEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object MinecraftOdysseyListeners : Listener {

    private var smithingCooldown: Long = 0
    private var gildingCooldown: Long = 0

    // Check if Ender Dragon Dies
    @EventHandler
    fun onDefeatEnderDragon(event: EntityDeathEvent) {
        val dragon = event.entity
        val endWorld = event.entity.world
        if (dragon.type == EntityType.ENDER_DRAGON) {
            for (aPlayer in endWorld.players) {
                aPlayer.sendMessage("${ChatColor.DARK_PURPLE}The end has just begun ...")

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
    @EventHandler
    fun cancelAnvilGilding(event: PrepareAnvilEvent) {
        if (event.inventory.firstItem != null && event.inventory.secondItem != null) {

            //BASE CASE!
            if (event.inventory.firstItem!!.type == Material.ENCHANTED_GOLDEN_APPLE && event.inventory.secondItem!!.type == Material.GOLD_INGOT) {
                event.result = ItemStack(Material.GOLDEN_HOE)
                event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                println("${event.result}")
            }

            // Check if items have odyssey enchants
            else if (event.inventory.firstItem!!.hasItemMeta() || event.inventory.secondItem!!.hasItemMeta()) {
                val firstItem = event.inventory.firstItem
                val secondItem = event.inventory.secondItem
                if (firstItem!!.itemMeta.hasEnchants() || secondItem!!.itemMeta.hasEnchants()) {
                    val firstEnchants = firstItem.enchantments
                    val secondEnchants = secondItem?.enchantments

                    // Check if Odyssey Enchants
                    for (enchant in firstEnchants.keys) {
                        if (enchant in OdysseyEnchantments.enchantmentSet) {
                            event.result = ItemStack(Material.AIR, 1)
                            event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                        }
                    }

                    for (enchant in secondEnchants?.keys!!) {
                        if (enchant in OdysseyEnchantments.enchantmentSet) {
                            event.result = ItemStack(Material.AIR, 1)
                            event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                        }
                    }
                }
            }
        }
    }

    //Make BOOK + material for gilded later

    // Odyssey Smithing
    @EventHandler
    fun odysseySmithing(event: PrepareSmithingEvent) {
        if (event.inventory.inputEquipment != null && event.inventory.inputMineral != null) {
            val timeElapsed = System.currentTimeMillis() - smithingCooldown

            //BASE CASE!
            if (event.inventory.inputEquipment!!.type == Material.ENCHANTED_GOLDEN_APPLE && event.inventory.inputMineral!!.type == Material.GOLD_INGOT) {
                event.result = ItemStack(Material.GOLDEN_HOE)
                event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                println("${event.result}")
            }

            // Adding Gilded Power
            else if (event.inventory.inputMineral!!.type == Material.ENCHANTED_GOLDEN_APPLE) {
                val someItem = event.inventory.inputEquipment!!.clone()
                if (someItem.hasItemMeta()) {
                    if (someItem.itemMeta.hasEnchant(OdysseyEnchantments.GILDED_POWER)) {
                        val gildedPower = someItem.getEnchantmentLevel(OdysseyEnchantments.GILDED_POWER)
                        if (gildedPower < 3) {
                            val newItemMeta = someItem.itemMeta
                            newItemMeta.removeEnchant(GildedPower)
                            newItemMeta.addEnchant(OdysseyEnchantments.GILDED_POWER, gildedPower + 1, true)
                            someItem.itemMeta = newItemMeta
                            event.result = someItem
                            println("${event.result}")
                        }
                    }
                }
            }



            // Naming
            else if (event.inventory.inputMineral!!.type == Material.DIAMOND || event.inventory.inputMineral!!.type == Material.AMETHYST_SHARD) {
                val namedEquipment = event.inventory.inputEquipment!!.clone()
                val forgerName = mutableListOf("")
                val newItemMeta = namedEquipment.itemMeta
                for (viewer in event.viewers) {
                    if (viewer is Player) {
                        forgerName.add("${ChatColor.DARK_GRAY}${ChatColor.ITALIC}Created by ${viewer.name}")
                        println("$forgerName")
                        if (timeElapsed > 20) {
                            smithingCooldown = System.currentTimeMillis()
                            viewer.sendMessage("This is a permanent engraving. Once engraved this item can no longer be modified!")
                        }
                    }
                }
                if (namedEquipment.lore != null) {
                    if (namedEquipment.lore!!.contains("")) {
                        return
                    }
                    val newLore = namedEquipment.lore!! + forgerName
                    newItemMeta.lore = newLore
                    namedEquipment.itemMeta = newItemMeta


                }
                else {
                    newItemMeta.lore = forgerName
                    namedEquipment.itemMeta = newItemMeta
                }
                event.result = namedEquipment
                event.viewers.forEach { somePlayer -> (somePlayer as Player).updateInventory() }
                println("${event.result}")
            }

            // BOOK CAN NEVER HAVE GILDED POWER
            // Check if book
            else if (event.inventory.inputMineral!!.type == Material.ENCHANTED_BOOK && event.inventory.inputEquipment!!.type != Material.ENCHANTED_BOOK) {
                // Create values
                val gildedEquipment = event.inventory.inputEquipment!!.clone()
                val gildedEnchantmentBook = event.inventory.inputMineral
                // Check
                if (gildedEnchantmentBook != null) {
                    // Checking if it has odyssey enchant
                    val newEnchantments = gildedEnchantmentBook.itemMeta.enchants
                    var hasOdysseyEnchant = false
                    var someOdysseyEnchant: Enchantment? = null
                    for (enchant in newEnchantments.keys) {
                        println("CHECKING...")
                        // Check gilded power
                        if (enchant in OdysseyEnchantments.enchantmentSet) {
                            hasOdysseyEnchant = true
                            someOdysseyEnchant = enchant
                        }
                        // Check if it cannot be applied
                        if (!(enchant.canEnchantItem(gildedEquipment))) {
                            return
                        }
                    }
                    // Checks for enchantment counter
                    if (gildedEnchantmentBook.itemMeta.hasLore() && hasOdysseyEnchant) {
                        var odysseyEnchantCounter = 0
                        var gildedPower = 0
                        // Check if it has odyssey enchant
                        val equipmentEnchantments = gildedEquipment.itemMeta.enchants
                        for (enchant in equipmentEnchantments.keys) {
                            if (someOdysseyEnchant!!.conflictsWith(enchant)) {
                                return
                            }

                            if (enchant in OdysseyEnchantments.enchantmentSet) {
                                if (enchant != OdysseyEnchantments.GILDED_POWER) {
                                    odysseyEnchantCounter += 1
                                }
                            }
                        }
                        // Check gilded power
                        if (gildedEquipment.itemMeta.hasEnchant(OdysseyEnchantments.GILDED_POWER)) {
                            gildedPower = gildedEquipment.itemMeta.getEnchantLevel(OdysseyEnchantments.GILDED_POWER)
                        }
                        // Check if power allows new enchants
                        if (gildedPower > odysseyEnchantCounter || gildedPower == 0) {
                            println("Passed Check")
                            // Add Enchantments
                            gildedEquipment.addUnsafeEnchantments(newEnchantments)
                            if (gildedEquipment.itemMeta.hasEnchant(GildedPower)) {
                                gildedEquipment.removeEnchantment(GildedPower)
                            }
                            gildedEquipment.addEnchantment(GildedPower, odysseyEnchantCounter + 1)
                            // Viewer warning
                            for (viewer in event.viewers) {
                                if (viewer is Player) {
                                    println("Gilded Thing")
                                    if (timeElapsed > 20) {
                                        gildingCooldown = System.currentTimeMillis()
                                        viewer.sendMessage("This is a permanent gilded enchantment!")
                                    }
                                }
                            }
                            // Adds Lore
                            val equipmentMeta = gildedEquipment.itemMeta
                            if (equipmentMeta.hasLore()) {
                                val newLore = gildedEnchantmentBook.itemMeta.lore!!
                                newLore.addAll(equipmentMeta.lore!!)
                                equipmentMeta.lore = newLore
                                gildedEquipment.itemMeta = equipmentMeta

                            }
                            else {
                                val newLore = gildedEnchantmentBook.itemMeta.lore!!
                                equipmentMeta.lore = newLore
                                gildedEquipment.itemMeta = equipmentMeta
                            }
                            println("${gildedEquipment.itemMeta.lore}")
                            // Create new item
                            event.result = gildedEquipment
                            event.viewers.forEach { somePlayer -> (somePlayer as Player).updateInventory() }
                            println("${event.result}")
                        }
                        // Check
                        else {
                            event.result = ItemStack(Material.AIR, 1)
                            event.viewers.forEach { somePlayer -> (somePlayer as Player).updateInventory() }
                            println("${event.result}")
                        }
                    }
                    // Check
                    else {
                        event.result = ItemStack(Material.AIR, 1)
                        event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                        println("${event.result}")
                    }
                }
            }
            // Combine Similar gilded enchant
            else if (event.inventory.inputMineral!!.type == Material.ENCHANTED_BOOK && event.inventory.inputEquipment!!.type == Material.ENCHANTED_BOOK) {
                val gildedBook1 = event.inventory.inputMineral
                val gildedBook2 = event.inventory.inputEquipment
                var gildedLevel = 0
                var someEnchant: Enchantment? = null
                var canCombine = false
                if (gildedBook1!!.hasItemMeta() && gildedBook2!!.hasItemMeta()) {
                    for (enchant in gildedBook1.enchantments.keys) {
                        if (enchant in OdysseyEnchantments.enchantmentSet && enchant != OdysseyEnchantments.GILDED_POWER) {
                            if (gildedBook2.itemMeta.hasEnchant(enchant)) {
                                gildedLevel = gildedBook1.getEnchantmentLevel(enchant)
                                // Combine only same level and not max level
                                if (gildedBook2.getEnchantmentLevel(enchant) == gildedLevel && gildedLevel < enchant.maxLevel) {
                                    canCombine = true
                                    someEnchant = enchant
                                    break
                                }
                            }
                        }
                    }
                    // Check if similar enchant
                    if (canCombine) {
                        val newBook = OdysseyItems.GILDED_BOOK.createGildedBook(someEnchant!!, gildedLevel + 1)
                        event.result = newBook
                        event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                    }
                    // Check
                    else {
                        event.result = ItemStack(Material.AIR, 1)
                        event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                        println("${event.result}")
                    }
                }
            }


            // Check
            /*
            else {
                event.result = ItemStack(Material.AIR, 1)
                event.viewers.forEach {somePlayer -> (somePlayer as Player).updateInventory()}
                println("${event.result}")
            }
            */
        }
    }



    // Create new boss
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



}