package me.shadowalzazel.mcodyssey.common.listeners

import com.destroystokyo.paper.event.inventory.PrepareResultEvent
import me.shadowalzazel.mcodyssey.common.enchantments.TomeEnchanting
import me.shadowalzazel.mcodyssey.common.items.Item
import me.shadowalzazel.mcodyssey.util.constants.CustomColors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.event.inventory.SmithItemEvent
import org.bukkit.inventory.EnchantingInventory
import org.bukkit.inventory.GrindstoneInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.Repairable
import java.util.*

@Suppress("UnstableApiUsage")
object EnchantingListeners : Listener, TomeEnchanting {

    // IDEAS
    // BOOK that stores EXP points
    // can be read to take points
    // like mending steal away points and is 1st priority

    // MINI GAME
    // Books with click events
    // when clicked on thing -> changes the nbt data of that book
    // When all clicks, transform Book?

    // TOME OF INFUSION?
    // sculk related, need sculk and echo shards to craft

    // GILDED ENCHANTS ARE LIKE CURSES (not removable)


    /*-----------------------------------------------------------------------------------------------*/
    @EventHandler
    fun anvilHandler(event: PrepareAnvilEvent) {
        // Handled natively by class -> Conflicts / Adding E-Book to slotted / Renaming
        if (event.inventory.firstItem == null) return
        val first = event.inventory.firstItem ?: return
        val second = event.inventory.secondItem
        val viewers = event.viewers
        val result = event.result ?: return
        val anvilView = event.view
        val resultIsRepairable = result.itemMeta is Repairable
        // Create Variables to detect conditions
        // CHANGE Anvil cost from being cringe mechanic
        if (second != null && second.itemMeta is EnchantmentStorageMeta && result.itemMeta is Repairable) {
            val booKMeta = second.itemMeta as EnchantmentStorageMeta
            val resultMeta = result.itemMeta as Repairable
            val storedEnchants = booKMeta.storedEnchants
            // If found enchants get cost and set
            if (storedEnchants.isNotEmpty()) {
                var bookPoints = 0
                for (e in storedEnchants) {
                    bookPoints += getEnchantabilityCost(Pair(e.key, e.value))
                }
                // Reset to prevent cringe
                anvilView.repairCost = bookPoints // Current Anvil Cost
                resultMeta.repairCost = result.enchantments.size + 1
            }
            result.itemMeta = resultMeta
        }
        // CHANGE combine cost when combining enchantments
        else if (resultIsRepairable && second?.hasItemMeta() == true) {
            if (second.itemMeta.hasEnchants()) {
                val itemPoints = first.getUsedEnchantabilityPoints()
                val newPoints = result.getUsedEnchantabilityPoints()
                val pointDiff = minOf(1, newPoints - itemPoints)
                val cost = pointDiff + result.enchantments.size
                val resultMeta = result.itemMeta as Repairable
                // Set Mew Combine Cost
                anvilView.repairCost = cost
                resultMeta.repairCost = cost
                result.itemMeta = resultMeta
            }
        }
        else if (resultIsRepairable && second != null) {
            val resultMeta = result.itemMeta as Repairable
            val cost = result.enchantments.size + 1
            // New Cost
            anvilView.repairCost = cost
            resultMeta.repairCost = cost
            result.itemMeta = resultMeta
        }
        // Check if over points max
        val usedPoints = result.getUsedEnchantabilityPoints()
        val maxPoints = result.getMaxEnchantabilityPoints()
        if (usedPoints > maxPoints && result.type != Material.ENCHANTED_BOOK) {
            event.result = null
            viewers.forEach { it.sendBarMessage("The item is maxed out on enchantability points.") }
            return
        }
        // Update
        if (result.hasItemMeta()) {
            result.updatePoints()
        }
        // IF empty name
        if (second == null) {
            anvilView.repairCost = 1
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    @EventHandler
    fun grindstoneHandler(event: PrepareResultEvent) {
        if (event.inventory !is GrindstoneInventory) return
        val inventory: GrindstoneInventory = event.inventory as GrindstoneInventory
        if (inventory.result == null) return
        val item = inventory.upperItem ?: inventory.lowerItem ?: return
        val result = inventory.result ?: return
        // Change name for arcane book
        if (result.type == Material.BOOK) {
            val itemName = item.getItemIdentifier()
            if (itemName == "arcane_book") {
                val newMeta = result.itemMeta
                val newName = Component.text("Arcane Book").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                newMeta.displayName(newName)
                result.itemMeta = newMeta
            }
        }
        event.result = result.clone().also { it.updatePoints() }
    }

    /*-----------------------------------------------------------------------------------------------*/
    @EventHandler(priority = EventPriority.HIGH)
    fun smithingEnchantHandler(event: PrepareSmithingEvent) {
        val recipe = event.inventory.recipe ?: return
        if (event.inventory.inputMineral == null) return
        if (event.inventory.inputEquipment == null) return
        if (event.inventory.inputTemplate == null) return
        val mineral = event.inventory.inputMineral!!
        val equipment = event.inventory.inputEquipment!!.clone()
        val template = event.inventory.inputTemplate!!
        // Avoid Conflict with other smithing, using (Enchanted Book)
        val templateId = template.getItemIdentifier() ?: return
        if (recipe.result.type != Material.ENCHANTED_BOOK) return
        if (event.result?.type == Material.ENCHANTED_BOOK) {
            event.result = ItemStack(Material.AIR)
        }
        // Variables
        val hasLapis = (mineral.type == Material.LAPIS_LAZULI)
        val hasEquipment = equipment.type != Material.ENCHANTED_BOOK && equipment.type != Material.BOOK
        val hasBook = equipment.type == Material.ENCHANTED_BOOK
        val hasItem = hasBook || hasEquipment
        // Check for [TOME] + [EQUIPMENT] + [LAPIS]
        if (hasLapis && hasItem) {
            val result = when(templateId) {
                "tome_of_avarice" -> tomeOfAvariceOnItem(equipment, event.viewers)
                "tome_of_discharge" -> tomeOfDischargeOnItem(equipment, event.viewers)
                "tome_of_expenditure" -> tomeOfExpenditureOnItem(equipment, event.viewers)
                "tome_of_harmony" -> tomeOfHarmonyOnItem(equipment)
                //"tome_of_polymerization" -> tomeOfPolymerizationOnItem(template, equipment, event.viewers)
                "tome_of_promotion" -> tomeOfPromotionOnItem(equipment, event.viewers)
                "tome_of_imitation" -> tomeOfImitationOnItem(equipment, event.viewers)
                "tome_of_replication" -> tomeOfReplicationOnItem(equipment, event.viewers)
                else -> {
                    null
                }
            }
            // Change result to air if null result
            result?.updatePoints()
            event.result = result ?: ItemStack(Material.AIR)
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun smithingDoneHandler(event: SmithItemEvent) {
        val smithingInventory = event.inventory
        if (smithingInventory.result == null) return
        val template = smithingInventory.inputTemplate ?: return
        val mineral = smithingInventory.inputMineral ?: return
        val equipment = smithingInventory.inputEquipment ?: return
        //val result = smithingInventory.result ?: return
        val player = event.whoClicked

        val hasLapis = (mineral.type == Material.LAPIS_LAZULI)
        val isTome = (template.type == Material.ENCHANTED_BOOK)
        // When for tomes
        if (hasLapis && isTome) {
            when(template.getItemIdentifier()) {
                "tome_of_avarice" -> {
                    tomeOfAvaricePostEffect(equipment, event.viewers)
                }
                "tome_of_imitation" -> {
                    val overflow = player.inventory.addItem(equipment.clone())
                    if (overflow.isNotEmpty()) {
                        player.world.dropItem(player.location, overflow[0]!!)
                    }
                }
                "tome_of_replication" -> {
                    val overflow = player.inventory.addItem(equipment.clone())
                    if (overflow.isNotEmpty()) {
                        player.world.dropItem(player.location, overflow[0]!!)
                    }
                }
            }
        }


    }

    /*-----------------------------------------------------------------------------------------------*/

    @EventHandler
    fun enchantingTableHandler(event: EnchantItemEvent) {
        when (event.item.type) {
            Material.BOOK -> {
                enchantingBookHandler(event)
            }
            else -> {
                enchantingItemHandler(event)
            }
        }
    }


    /*-----------------------------------------------------------------------------------------------*/
    private fun enchantingBookHandler(event: EnchantItemEvent) {
        val itemID = event.item.getItemIdentifier()
        when (itemID) {
            "arcane_book" -> {
                arcaneBookHandler(event)
            }
            "blank_tome" -> {
                enchantingTomeHandler(event)
            }
            else -> { // Update Points
                val book = event.item
                book.updatePoints(resetLore = true, toggleToolTip = true, newEnchants = event.enchantsToAdd)
                event.item = book
            }
        }
    }

    private fun enchantingTomeHandler(event: EnchantItemEvent) {
        val randomTome: Item
        //val enchanterLevel = maxOf(1, event.enchanter.level)
        val tierCost: Int
        //val tableCost = event.expLevelCost
        // Scale of Tomes
        when (event.whichButton()) {
            0 -> {
                tierCost = 1
                randomTome = listOf(Item.TOME_OF_DISCHARGE, Item.TOME_OF_PROMOTION).random()
            }
            1 -> {
                tierCost = 2
                randomTome = listOf(Item.TOME_OF_IMITATION, Item.TOME_OF_EXPENDITURE).random()
            }
            2 -> {
                tierCost = 3
                randomTome = listOf(Item.TOME_OF_AVARICE, Item.TOME_OF_HARMONY).random()
            }
            else -> {
                tierCost = 0
                randomTome = listOf(Item.TOME_OF_HARMONY).random()
            }
        }
        // Particles and sounds
        with(event.enchantBlock.world) {
            val enchantLocation = event.enchantBlock.location.clone().toCenterLocation()
            spawnParticle(Particle.CRIT, enchantLocation, 75, 0.5, 0.5, 0.5)
            spawnParticle(Particle.HAPPY_VILLAGER, enchantLocation, 35, 0.5, 0.5, 0.5)
            playSound(enchantLocation, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 2.5F, 0.9F)
            playSound(enchantLocation, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 2.5F, 1.3F)
        }
        // Set advancement from datapack
        val advancement = event.enchanter.server.getAdvancement(NamespacedKey.fromString("odyssey:odyssey/enchant_a_tome")!!)
        if (advancement != null) {
            event.enchanter.getAdvancementProgress(advancement).awardCriteria("requirement")
        }
        // Set tome and XP
        (event.inventory as EnchantingInventory).item = randomTome.newItemStack(1)
        event.enchanter.level -= minOf(tierCost, event.enchanter.level)
        event.isCancelled = true
    }


    private fun arcaneBookHandler(event: EnchantItemEvent) {
        // Create more enchants to add
        addChiseledBookshelfBonus(event)
        val book = event.item
        val newEnchants = event.enchantsToAdd
        val hint = event.enchantmentHint
        val tableCost = event.expLevelCost
        // Create a copy of enchantments
        val arcaneBonusEnchants = (book.clone().enchantWithLevels(tableCost, false, Random()).itemMeta as EnchantmentStorageMeta).storedEnchants
        for (enchant in arcaneBonusEnchants) {
            if (enchant.key.conflictsWith(hint)) continue
            if (enchant.key == hint) continue
            newEnchants[enchant.key] = enchant.value
        }
        // update
        book.updatePoints(resetLore = true, toggleToolTip = true, newEnchants = newEnchants) // Not full trigger since its passed as a book
        event.item = book
    }

    /*-----------------------------------------------------------------------------------------------*/
    private fun enchantingItemHandler(event: EnchantItemEvent) {
        // Create more enchants to add
        addChiseledBookshelfBonus(event)
        val item = event.item
        val newEnchants = event.enchantsToAdd
        // Can get OVER-MAXED ITEMS if lucky
        item.updateEnchantabilityPoints(newEnchants, toggleToolTip = true)
        event.item = item
    }

    private fun addChiseledBookshelfBonus(event: EnchantItemEvent) {
        // (Enchant, % chance)
        val shelfEnchantsMap = mutableMapOf<Enchantment, Int>()
        // Pairs of x,z offsets
        val ringList = listOf(
            Pair(2, -1), Pair(2, 0), Pair(2, 1),
            Pair(-2, -1), Pair(-2, 0), Pair(-2, 1),
            Pair(-1, 2), Pair(0, 2), Pair(1, 2),
            Pair(-1, -2), Pair(0, -2), Pair(1, -2),
        )
        for (pair in ringList) {
            // Above y+1 above enchanting table
            val block = event.enchantBlock.location.clone().toCenterLocation().add(pair.first.toDouble(), 1.0, pair.second.toDouble()).block
            if (block.type != Material.CHISELED_BOOKSHELF) continue
            val shelfData = block.blockData
            val blockState = block.state
            if (shelfData !is org.bukkit.block.data.type.ChiseledBookshelf) continue
            if (blockState !is org.bukkit.block.ChiseledBookshelf) continue
            for (book in blockState.inventory.storageContents) {
                if (book == null) continue
                if (!book.hasItemMeta()) continue
                if (book.itemMeta !is EnchantmentStorageMeta) continue
                val bookMeta = book.itemMeta as EnchantmentStorageMeta
                if (bookMeta.storedEnchants.isEmpty()) continue
                // Add enchantability point to chance
                bookMeta.storedEnchants.forEach {
                    val enchantabilityCost = it.key.enchantabilityCost(it.value)
                    if (it.key !in shelfEnchantsMap) {
                        shelfEnchantsMap[it.key] = enchantabilityCost
                    }
                    else {
                        shelfEnchantsMap[it.key] = shelfEnchantsMap[it.key]!! + enchantabilityCost
                    }
                }
            }
        }
        // Get final calculations (Enchant, lvl)
        val bonusEnchants = mutableMapOf<Enchantment, Int>()
        for (rolled in shelfEnchantsMap) {
            // MAYBE add - negative modifier per unique enchants? OR positive bonus
            if (rolled.value >= (1..100).random()) {
                val enchantment = rolled.key
                val level = enchantment.maxLevel
                bonusEnchants[rolled.key] = level
                //bonusEnchants[rolled.key] = rolled.value.floorDiv(100) + 1
            }
            //println("Bookshelf Enchant: ${rolled.key} with ${rolled.value}%")
        }
        //println("Bonus Enchants: $bonusEnchants")
        // Add to events list
        for (enchant in bonusEnchants) {
            if (enchant.key !in event.enchantsToAdd.keys) {
                event.enchantsToAdd[enchant.key] = enchant.value
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Bar Message
    private fun LivingEntity.sendBarMessage(reason: String, color: TextColor = CustomColors.ENCHANT.color) {
        this.sendActionBar(
            Component.text(
                reason,
                color
            )
        )
    }

}