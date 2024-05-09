package me.shadowalzazel.mcodyssey.listeners

import com.destroystokyo.paper.event.inventory.PrepareResultEvent
import me.shadowalzazel.mcodyssey.enchantments.api.TomeManager
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.items.Miscellaneous
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import me.shadowalzazel.mcodyssey.items.creators.ItemCreator
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.enchantment.PrepareItemEnchantEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.inventory.PrepareSmithingEvent
import org.bukkit.inventory.EnchantingInventory
import org.bukkit.inventory.GrindstoneInventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

object EnchantingListeners : Listener, TomeManager, ItemCreator {

    // IDEAS
    // BOOK that stores EXP points
    // can be read to take points
    // like mending steal away points and is 1st priority
    // PRISMATIC TOME (new tome book) converts to Tomes
    // MINI GAME
    // Books with click events
    // when clicked on thing -> changes the nbt data of that book
    // sculk related, need sculk and echo shards to craft
    // ARCANE BOOK (new gilded book)
    // GILDED ENCHANTS ARE LIKE CURSES (not removable)
    // When all clicks, transform Book?
    // Polymerization -> Promotion and Replication Fixes

    /*-----------------------------------------------------------------------------------------------*/
    @EventHandler
    fun grindstoneHandler(event: PrepareResultEvent) {
        if (event.inventory !is GrindstoneInventory) return
        val inventory: GrindstoneInventory = event.inventory as GrindstoneInventory
        if (inventory.result == null) return
        if (inventory.upperItem?.enchantments == null && inventory.lowerItem?.enchantments == null) return
        val result = inventory.result!!
        val slotted = result.isSlotted()
        if (!slotted) return
        // Sentries passed
        event.result = result.clone().also { it.updateSlotContainerLore() }
    }

    /*-----------------------------------------------------------------------------------------------*/
    @EventHandler
    fun anvilHandler(event: PrepareAnvilEvent) {
        // Handled natively by class -> Conflicts / Adding E-Book to slotted / Renaming
        if (event.inventory.firstItem == null) return
        val first = event.inventory.firstItem ?: return
        val firstIsSlotted = first.isSlotted()
        val viewers = event.viewers
        val second = event.inventory.secondItem
        // Create Variables to detect conditions
        val firstHasOdysseyEnchantments = first.hasOdysseyEnchants()
        val secondHasOdysseyEnchantments = second?.hasOdysseyEnchants() == true
        val firstIsBook = first.type == Material.ENCHANTED_BOOK
        val firstBookIsOdyssey = firstHasOdysseyEnchantments && firstIsBook
        val secondIsBook = second?.type == Material.ENCHANTED_BOOK
        val secondBookIsOdyssey = secondHasOdysseyEnchantments && secondIsBook
        // Prevent Gilded Book + Enchanted Book [MAYBE make VOLUME tomes]
        if ((firstBookIsOdyssey && !secondBookIsOdyssey && secondIsBook) || (secondBookIsOdyssey && !firstBookIsOdyssey && firstIsBook)) {
            viewers.forEach { it.sendBarMessage("Can not combine Arcane Books and Enchanted Books!") }
            event.result = ItemStack(Material.AIR)
            return
        }
        // Skip Event if just two Enchanted Books
        if (!firstBookIsOdyssey && !secondBookIsOdyssey && (firstIsBook && secondIsBook)) {
            return
        }
        // Check Arcane Books
        if (firstBookIsOdyssey && secondBookIsOdyssey) {
            val firstStoredMeta = first.itemMeta as EnchantmentStorageMeta
            val secondStoredMeta = second!!.itemMeta as EnchantmentStorageMeta
            // Somehow the Arcane book got more than 1 enchant
            if (firstStoredMeta.storedEnchants.size > 1) {
                viewers.forEach { it.sendBarMessage("The first Arcane Book has more than 1 enchantment!") }
                event.result = ItemStack(Material.AIR)
                return
            }
            // Somehow the second Arcane book got more than 1 enchant
            if (secondStoredMeta.storedEnchants.size > 1) {
                viewers.forEach { it.sendBarMessage("The second Arcane Book has more than 1 enchantment!") }
                event.result = ItemStack(Material.AIR)
                return
            }
            val matching = firstStoredMeta.storedEnchants.keys.first() == secondStoredMeta.storedEnchants.keys.first()
            // If not matching set to air
            if (!matching) {
                viewers.forEach { it.sendBarMessage("Can not combine Arcane Books with different enchantments!") }
                event.result = ItemStack(Material.AIR)
                return
            }
            // If matching odyssey enchants, create new tool tip and lore
            else {
                val enchant = firstStoredMeta.storedEnchants.keys.first()
                event.result = event.result?.apply {
                    val meta = itemMeta as EnchantmentStorageMeta
                    val newLevel = meta.storedEnchants[enchant]!!
                    val newLoreName = enchant.displayName(newLevel)
                    //val newToolTip = enchant.convertToOdysseyEnchant().getDescriptionToolTip(newLevel)
                    //val changedLore = mutableListOf(newLoreName) + Component.text("") + newToolTip
                    //meta.lore(changedLore)
                    itemMeta = meta
                }
                return
            }
        }
        // EventResult Null Check
        if (event.result == null) return
        if (second == null) return
        // Assumes that NMS is present and transfer enchantments
        val result = event.result ?: return
        // Activating Slots First Time
        if (!firstIsSlotted) {
            event.result = event.result!!.apply {
                createNewEnchantSlots()
                addItemFlags(ItemFlag.HIDE_ENCHANTS)
                updateSlotContainerLore()
            }
            return
        }
        // If slotted
        else {
            val resultEnchants = result.enchantments
            val gildedEnchant = first.getGildedEnchantKey()
            // Check slot sizes
            val enchantSlots = result.getEnchantSlots()
            val nonGildedEnchants = result.enchantments.filter { it.key != gildedEnchant?.bukkitEnchant }
            // val nonGildedEnchants = result.enchantments.filter { it.key != gildedEnchant }
            if (enchantSlots < nonGildedEnchants.size) {
                viewers.forEach { it.sendBarMessage("There are not enough empty 'Enchant Slots' on the first item!") }
                event.result = ItemStack(Material.AIR)
                return
            }
            // Conflict Checker
            for (enchant in resultEnchants.keys) {
                var conflictingEnchant: Enchantment? = null
                val foundConflict = resultEnchants.keys.any {
                    conflictingEnchant = it
                    if (enchant.isOdysseyEnchant()) { // TODO !!!!!!!!!!! FIX
                        OdysseyEnchantments.VOID_STRIKE.checkBukkitConflict(enchant) && it != enchant
                    } else {
                        enchant.conflictsWith(it) && it != enchant
                    }
                }
                // If found conflict set to air
                if (foundConflict) {
                    viewers.forEach {
                        val enchantText = enchant.displayName(resultEnchants[enchant]!!)
                        val conflictText = conflictingEnchant!!.displayName(resultEnchants[conflictingEnchant]!!)
                        val fullText = "The enchantment [$enchantText}] conflicts with the enchantment [$conflictText]!"
                        it.sendBarMessage(fullText)
                    }
                    event.result = ItemStack(Material.AIR)
                    return
                }
            }
            // Current gilded
            val gildedSlots = result.getGildedSlots()
            val gildedCount = if (gildedEnchant != null) 1 else 0
            if (gildedEnchant != null && gildedSlots < gildedCount) {
                viewers.forEach { it.sendBarMessage("There are not enough empty 'Gilded Slots' on the first item!") }
                event.result = ItemStack(Material.AIR)
                return
            }
            event.result = event.result!!.apply {
                updateSlotContainerLore()
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    @EventHandler
    fun enchantPrepareHandler(event: PrepareItemEnchantEvent) {
        println(event.offers)
        println(event.enchantmentBonus)
        println(event)
    }

    @EventHandler
    fun enchantingTableHandler(event: EnchantItemEvent) {
        when (event.item.type) {
            Material.BOOK -> {
                if (event.item.itemMeta.hasCustomModelData()) {
                    enchantingBookHandler(event)
                }
            }
            else -> {
                enchantingItemHandler(event)
            }
        }
    }

    private fun enchantingItemHandler(event: EnchantItemEvent) {
        val item = event.item
        val newEnchants = event.enchantsToAdd
        getChiseledBookshelvesBonus(event) // Call this before slots
        // Get slots or create new slots
        val slotsPair = if (item.isSlotted()) { item.getPairSlots() } else { getMaterialEnchantSlots(item.type) }
        // New Enchant Slots
        if (!item.isSlotted()) {
            item.setPairSlots(slotsPair)
        }
        val enchantSlots = slotsPair.first
        val gildedSlots = slotsPair.second
        var usedEnchantSlots = 0
        var usedGildedSlots = 0
        // Get Hint
        val hint = event.enchantmentHint
        // Remove excess enchants over slot limit
        val enchantsToRemove = mutableListOf<Enchantment>()
        for (newEnchant in newEnchants.keys) {
            if (newEnchant == hint) {
                continue
            }
            // Append to removal list
            usedEnchantSlots += 1
            if (usedEnchantSlots > enchantSlots) {
                enchantsToRemove.add(newEnchant)
            }
        }
        enchantsToRemove.forEach { newEnchants.remove(it) }
        // Update New Slots
        val containerMap = createBukkitEnchantContainerMap(newEnchants).toMutableMap()
        item.updateSlotContainerLore(containerMap)
    }

    private fun enchantingBookHandler(event: EnchantItemEvent) {
        when (event.item.itemMeta.customModelData) {
            ItemModels.VOLUME_OF_BLUNTING -> {
                event.item.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_SWORD, 1)) }
            }
            ItemModels.VOLUME_OF_TILLING -> {
                event.item.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_AXE, 1)) }
            }
            ItemModels.VOLUME_OF_POKING -> {
                event.item.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_SWORD, 1)) }
            }
            ItemModels.VOLUME_OF_CLEAVING -> {
                event.item.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_HOE, 1)) }
            }
            ItemModels.VOLUME_OF_SLASHING -> {
                event.item.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_SHOVEL, 1)) }
            }
            ItemModels.VOLUME_OF_HELMETS -> {
                event.item.enchantments.filter { it.key.canEnchantItem(ItemStack(Material.WOODEN_PICKAXE, 1)) }
            }
            ItemModels.PRISMATIC_BOOK -> {
                enchantingTomeHandler(event)
            }
        }
    }

    private fun enchantingTomeHandler(event: EnchantItemEvent) {
        val randomTome: OdysseyItem
        val enchanterLevel = maxOf(1, event.enchanter.level)
        val tierCost: Int

        when ((0..40).random() + minOf(enchanterLevel, 100)) {
            in 0..10 -> {
                tierCost = 0
                randomTome = listOf(Miscellaneous.TOME_OF_BANISHMENT).random()
            }
            in 11..30 -> {
                tierCost = 1
                randomTome = listOf(Miscellaneous.TOME_OF_DISCHARGE, Miscellaneous.TOME_OF_EMBRACE).random()
            }
            in 31..60 -> {
                tierCost = 2
                randomTome = listOf(Miscellaneous.TOME_OF_PROMOTION, Miscellaneous.TOME_OF_IMITATION).random()
            }
            in 61..100 -> {
                tierCost = 3
                randomTome = listOf(Miscellaneous.TOME_OF_EXPENDITURE, Miscellaneous.TOME_OF_HARMONY).random()
            }
            in 101..200 -> {
                tierCost = 4
                randomTome = listOf(Miscellaneous.TOME_OF_AVARICE).random()
            }
            else -> {
                tierCost = 0
                randomTome = listOf(Miscellaneous.TOME_OF_BANISHMENT).random()
            }
        }

        // Particles and sounds
        with(event.enchantBlock.world) {
            val enchantLocation = event.enchantBlock.location.clone().toCenterLocation()
            spawnParticle(Particle.CRIT, enchantLocation, 125, 0.5, 0.5, 0.5)
            spawnParticle(Particle.HAPPY_VILLAGER, enchantLocation, 65, 0.5, 0.5, 0.5)
            spawnParticle(Particle.ELECTRIC_SPARK, enchantLocation, 65, 0.5, 0.5, 0.5)
            playSound(enchantLocation, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 2.5F, 0.9F)
            playSound(enchantLocation, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 2.5F, 1.3F)
            playSound(enchantLocation, Sound.ENTITY_ARROW_HIT_PLAYER, 2.5F, 1.6F)
        }

        val advancement = event.enchanter.server.getAdvancement(NamespacedKey.fromString("odyssey:odyssey/enchant_a_tome")!!)
        if (advancement != null) {
            event.enchanter.getAdvancementProgress(advancement).awardCriteria("requirement")
        }

        (event.inventory as EnchantingInventory).item = randomTome.newItemStack(1)
        event.enchanter.level -= minOf(tierCost + 1, event.enchanter.level)
        event.isCancelled = true
    }

    private fun getChiseledBookshelvesBonus(event: EnchantItemEvent) {
        // (Enchant, % chance)
        val shelfEnchantsChance = mutableMapOf<Enchantment, Int>()
        // Pairs of x,z offsets
        val ringList = listOf(
            Pair(2, -1), Pair(2, 0), Pair(2, 1),
            Pair(-2, -1), Pair(-2, 0), Pair(-2, 1),
            Pair(-1, 2), Pair(0, 2), Pair(1, 2),
            Pair(-1, -2), Pair(0, -2), Pair(1, -2),
        )
        for (pair in ringList) {
            val block = event.enchantBlock.location.clone().toCenterLocation().add(pair.first.toDouble(), 0.0, pair.second.toDouble()).block
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
                // Add value to chance
                bookMeta.storedEnchants.forEach {
                    if (it.key !in shelfEnchantsChance) { shelfEnchantsChance[it.key] = it.value }
                    else {
                        shelfEnchantsChance[it.key] = shelfEnchantsChance[it.key]!! + it.value
                    }
                }
            }
        }
        // Get final calculations (Enchant, lvl)
        val finalEnchants = mutableMapOf<Enchantment, Int>()
        for (rolled in shelfEnchantsChance) {
            if (rolled.value >= (1..100).random()) {
                // MAYBE add - negative modifier per unique enchants? OR positive
                finalEnchants[rolled.key] = rolled.value.floorDiv(100) + 1
            }
            println("Bookshelf Enchant: ${rolled.key} with ${rolled.value}%")
        }

        println("Bonus Enchants: $finalEnchants")
        // Chance
        // Lvl = Chance / 100 -> 500% = lvl5
        // MAYBE ADD MORE levels BASED ON BASE ENCHANTS TO ADD
        // SHARP 3 = 300%
        for (enchant in finalEnchants) {
            if (enchant.key !in event.enchantsToAdd.keys) {
                event.enchantsToAdd[enchant.key] = enchant.value
            }
        }
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
        if (!template.itemMeta.hasCustomModelData()) return
        if (!equipment.hasItemMeta()) return
        if (recipe.result.type != Material.ENCHANTED_BOOK) return
        if (event.result?.type == Material.ENCHANTED_BOOK) {
            event.result = ItemStack(Material.AIR)
        }
        // Variables
        val hasCrystals = (mineral.type == Material.PRISMARINE_CRYSTALS)
        val hasEquipment = equipment.type != Material.ENCHANTED_BOOK && equipment.type != Material.BOOK
        val hasBook = equipment.type == Material.ENCHANTED_BOOK
        val hasItem = hasBook || hasEquipment
        // Check for [TOME] + [EQUIPMENT] + [CRYSTALS]
        if (hasCrystals && hasItem) {
            val eventResult = when(template.itemMeta.customModelData) {
                ItemModels.TOME_OF_AVARICE -> tomeOfAvariceOnItem(equipment, event.viewers)
                ItemModels.TOME_OF_BANISHMENT -> tomeOfBanishmentOnItem(equipment, event.viewers)
                ItemModels.TOME_OF_DISCHARGE -> tomeOfDischargeOnItem(equipment, event.viewers)
                ItemModels.TOME_OF_EMBRACE -> tomeOfEmbraceOnItem(equipment, event.viewers)
                ItemModels.TOME_OF_EXPENDITURE -> tomeOfExpenditureOnItem(equipment, event.viewers)
                ItemModels.TOME_OF_HARMONY -> tomeOfHarmonyOnItem(equipment)
                ItemModels.TOME_OF_POLYMERIZATION -> tomeOfPolymerizationOnItem(template, equipment, event.viewers)
                ItemModels.TOME_OF_PROMOTION -> tomeOfPromotionOnItem(equipment, event.viewers)
                ItemModels.TOME_OF_IMITATION -> {
                    val result = tomeOfImitationOnItem(equipment, event.viewers) // TODO: Not consume extra book
                    result
                }
                ItemModels.TOME_OF_REPLICATION -> {
                    val result = tomeOfReplicationOnItem(equipment, event.viewers)
                    result
                }
                else -> {
                    null
                }
            }
            // Change result to air if null result
            event.result = eventResult ?: ItemStack(Material.AIR)
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Get slots based on material and return Enchant-Gilded Pair
    private fun getMaterialEnchantSlots(itemType: Material): Pair<Int, Int> {
        var gildedSlots = 0
        val enchantSlots = when (itemType) {
            Material.STONE_SWORD, Material.STONE_AXE, Material.STONE_PICKAXE, Material.STONE_SHOVEL, Material.STONE_HOE,
            Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET -> {
                3
            }
            Material.WOODEN_SWORD, Material.WOODEN_AXE, Material.WOODEN_PICKAXE, Material.WOODEN_SHOVEL, Material.WOODEN_HOE,
            Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET -> {
                3
            }
            Material.IRON_SWORD, Material.IRON_AXE, Material.IRON_PICKAXE, Material.IRON_SHOVEL, Material.IRON_HOE,
            Material.IRON_BOOTS, Material.IRON_LEGGINGS, Material.IRON_CHESTPLATE, Material.IRON_HELMET -> {
                4
            }
            Material.DIAMOND_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_PICKAXE, Material.DIAMOND_SHOVEL, Material.DIAMOND_HOE,
            Material.DIAMOND_BOOTS, Material.DIAMOND_LEGGINGS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET -> {
                4
            }
            Material.GOLDEN_SWORD, Material.GOLDEN_AXE, Material.GOLDEN_PICKAXE, Material.GOLDEN_SHOVEL, Material.GOLDEN_HOE,
            Material.GOLDEN_BOOTS, Material.GOLDEN_LEGGINGS, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_HELMET -> {
                5
            }
            Material.NETHERITE_SWORD, Material.NETHERITE_AXE, Material.NETHERITE_PICKAXE, Material.NETHERITE_SHOVEL, Material.NETHERITE_HOE,
            Material.NETHERITE_BOOTS, Material.NETHERITE_LEGGINGS, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_HELMET -> {
                4
            }
            Material.ELYTRA, Material.SHIELD, Material.FISHING_ROD, Material.TRIDENT -> {
                3
            }
            Material.BOW, Material.CROSSBOW -> {
                4
            }
            else -> {
                3
            }
        }
        return Pair(enchantSlots, gildedSlots)
    }

}