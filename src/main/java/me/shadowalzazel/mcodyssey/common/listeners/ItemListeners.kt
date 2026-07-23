@file:Suppress("UnstableApiUsage")

package me.shadowalzazel.mcodyssey.common.listeners

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemEnchantments
import io.papermc.paper.registry.RegistryKey
import me.shadowalzazel.mcodyssey.api.RegistryTagManager
import me.shadowalzazel.mcodyssey.common.enchantments.EnchantabilityHandler
import me.shadowalzazel.mcodyssey.common.items.Item
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.data.Directional
import org.bukkit.block.Dispenser   // BlockState — NOT the block.data.type one
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.ItemFrame
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object ItemListeners : Listener, EnchantabilityHandler, RegistryTagManager {

    private fun ItemStack.hasMaterialType(type: String): Boolean =
        getStringTag(ItemDataTags.MATERIAL_TYPE) == type || getItemNameId().contains(type)

    // ──────────────────────────────────────────────────────────────────────────────
    // ──────────────────────────────── HANDLERS ────────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────


    @EventHandler
    fun playerItemInteractEntityHandler(event: PlayerInteractAtEntityEvent) {
        val item = event.player.inventory.itemInMainHand
        val entity = event.rightClicked
        val customItemId = item.getItemNameFromData() ?: return
        // When block to match
        val success = when (customItemId) {
            "crystal_alloy_trimmers" -> crystalAlloyTrimmersItemUse(entity)
            else -> false
        }

        if (success) {
            val hasDurability = item.getData(DataComponentTypes.MAX_DAMAGE) != null
            // Damage if durability, else remove 1
            if (hasDurability) {
                item.damage(1, event.player)
            } else {
                item.subtract(1)
            }
        }
    }

    @EventHandler
    fun playerItemInteractHandler(event: PlayerInteractEvent) {
        val item = event.item ?: return
        val block = event.clickedBlock
        val customItemId = item.getItemNameFromData() ?: return
        // When block to match
        val success = when (customItemId) {
            "crystalline_compost" -> crystallineCompostItemUse(block)
            else -> false
        }

        if (success) {
            item.subtract(1)
        }

    }

    @EventHandler
    fun dispenseItemUseHandler(event: BlockDispenseEvent) {
        val dispenser = event.block
        val dispensedItem = event.item
        val customItemId = dispensedItem.getItemNameFromData() ?: return

        // A dispenser's facing is stored in its BlockData, not its location
        val data = dispenser.blockData as? Directional ?: return
        val facing = data.facing            // BlockFace — can be UP, DOWN, NORTH, etc.
        val blockInFront = dispenser.getRelative(facing)

        // If the custom item has no dispense-use, bail out and let the
        // dispenser/dropper drop it exactly like vanilla.
        val success = when (customItemId) {
            "crystalline_compost" -> crystallineCompostItemUse(blockInFront)
            else -> return
        }

        if (success) {
            //dispensedItem.subtract(1)
            // (no item entity in the world, no auto-decrement fight)
            event.isCancelled = true

            // Manually consume one from the dispenser's container
            val inv = (dispenser.state as Dispenser).inventory
            inv.removeItem(dispensedItem.clone().apply { amount = 1 })
        }
        else {
            event.item = ItemStack(Material.AIR)
            event.isCancelled = true
        }
    }

    @EventHandler
    fun consumeItemHandler(event: PlayerItemConsumeEvent) {
        // Buffed Golden Apples
        if (event.item.type == Material.ENCHANTED_GOLDEN_APPLE) {
            event.player.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 20 * 20, 3))
            event.player.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 120 * 20, 4))
            return
        }

        val item = event.item
        val customItemId = item.getItemNameFromData() ?: return

        val success = when (customItemId) {
            "scroll" -> scrollItemUse(event.player)
            else -> return
        }
    }

    @EventHandler
    fun deathHandler(event: EntityDeathEvent) {
        if (event.entity.killer != null && event.droppedExp > 0) {
            val killer = event.entity.killer ?: return
            val inventory = killer.inventory
            // Check each slot for soul steel
            var bonusSoulSteelExp = 0.0
            inventory.also {
                // Helmet
                if (it.boots?.getStringTag(ItemDataTags.MATERIAL_TYPE) == "soul_steel"
                    || it.boots?.getItemNameId()?.contains("soul_steel") == true) {
                    bonusSoulSteelExp += 0.1
                }
                // Chestplate
                if (it.chestplate?.getStringTag(ItemDataTags.MATERIAL_TYPE) == "soul_steel"
                    || it.chestplate?.getItemNameId()?.contains("soul_steel") == true) {
                    bonusSoulSteelExp += 0.1
                }
                // Leggings
                if (it.leggings?.getStringTag(ItemDataTags.MATERIAL_TYPE) == "soul_steel"
                    || it.leggings?.getItemNameId()?.contains("soul_steel") == true) {
                    bonusSoulSteelExp += 0.1
                }
                // Boots
                if (it.boots?.getStringTag(ItemDataTags.MATERIAL_TYPE) == "soul_steel"
                    || it.boots?.getItemNameId()?.contains("soul_steel") == true) {
                    bonusSoulSteelExp += 0.1
                }
                // Mainhand
                if (it.itemInMainHand.getStringTag(ItemDataTags.MATERIAL_TYPE) == "soul_steel"
                    || it.itemInMainHand.getItemNameId().contains("soul_steel")) {
                    bonusSoulSteelExp += 0.15
                }
            }
            // Dont care if no soul steel
            if (bonusSoulSteelExp <= 0.0) return
            // Soul steel bonus
            var expBonusPercent = 0.0
            expBonusPercent += bonusSoulSteelExp
            event.droppedExp += (event.droppedExp * (1 + expBonusPercent)).toInt()
            with(killer.world) {
                spawnParticle(Particle.SOUL, killer.location, 2, 0.05, 0.25, 0.05)
                spawnParticle(Particle.SCULK_SOUL, killer.location, 3, 0.15, 0.25, 0.15)
            }
            return
        }
    }


    // ──────────────────────────────────────────────────────────────────────────────
    // ────────────────────────────── ITEM FUNCTIONS ────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    /*
     * Returns true if successful, otherwise returns false
     */
    private fun crystallineCompostItemUse(block: Block?): Boolean {
        if (block == null) return false
        val blockType = block.type.asBlockType() ?: return false
        // Small Flowers
        /*
        val smallFlowerTags = getTagFromRegistry(
            RegistryKey.BLOCK,
            "small_flowers",
            "minecraft")
        val smallFlowerBlocks = getCollectionFromTag(
            RegistryKey.BLOCK,
            smallFlowerTags).toList()
        // Saplings
        val saplingsTags = getTagFromRegistry(
            RegistryKey.BLOCK,
            "saplings",
            "minecraft")
        val saplingsBlocks = getCollectionFromTag(
            RegistryKey.BLOCK,
            saplingsTags).toList()
         */
        val crystallineGrowableTags = getTagFromRegistry(
            RegistryKey.BLOCK,
            "crystalline_compost_growable",
            "odyssey")
        val crystallineGrowableBlocks = getCollectionFromTag(
            RegistryKey.BLOCK,
            crystallineGrowableTags).toList()

        // Check if small flower
        val center = block.location.toCenterLocation()
        if (blockType in crystallineGrowableBlocks) {
            block.world.spawnParticle(Particle.WITCH, center, 10, 0.15, 0.15, 0.15)
            // Spread flowers
            block.world.dropItemNaturally(center, ItemStack(block.type, 1))
            return true
        }
        return false
    }

    private fun crystalAlloyTrimmersItemUse(entity: Entity): Boolean {
        val type = entity.type
        if (type != EntityType.ITEM_FRAME && type != EntityType.GLOW_ITEM_FRAME) return false
        // Item Frame
        entity.world.spawnParticle(Particle.WITCH, entity.location, 8, 0.15, 0.15, 0.15)

        if (entity is ItemFrame) {
            entity.isInvisible = !entity.isInvisible
        }
        return true
    }


    private fun scrollItemUse(player: Player): Boolean {
        val offhand = player.equipment.itemInOffHand
        val type = offhand.type
        // For Books
        if (type != Material.ENCHANTED_BOOK) return false
        // Get levels on book or fail if none
        var enchantabilityCost = 0
        val enchantments = offhand.readEnchants()
        enchantments.forEach { it -> enchantabilityCost += getEnchantabilityCost(it.toPair())}
        if (enchantabilityCost <= 0) return false
        // Check exp levels
        if (enchantabilityCost <= player.level) {
            // Add stored enchants to scroll
            val newScroll = Item.SCROLL.newItemStack(1).apply {
                setData(DataComponentTypes.MAX_STACK_SIZE, 1)
                val itemEnchantments = ItemEnchantments.itemEnchantments().addAll(enchantments)
                setData(DataComponentTypes.STORED_ENCHANTMENTS, itemEnchantments)
                setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
            }
            // Modify player
            player.inventory.addItem(newScroll)
            player.level -= enchantabilityCost

            // VFX
            with(player.world) {
                spawnParticle(Particle.CRIT, player.location, 75, 0.5, 0.5, 0.5)
                spawnParticle(Particle.WITCH, player.location, 35, 0.5, 0.5, 0.5)
                playSound(player.location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.5F, 1.3F)
                playSound(player.location, Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.5F, 1.4F)
            }
            // Exit
            return true
        }

        return true
    }



    private fun soulSpiceItemHandler(event: PlayerDropItemEvent) {
        val item = event.itemDrop
        if (item.itemStack.amount != 1) return
        val player = event.player
        if (player.hasCooldown(item.itemStack.type)) return
        // Give Nearby entities glowing
        val nearbyEntities = player.location.getNearbyLivingEntities(20.0).filter {
            (it != player) &&  (!it.isDead)
        }
        nearbyEntities.forEach {
            val glowingEffect = PotionEffect(PotionEffectType.GLOWING, (2 * 20) + 10, 0)
            it.addPotionEffect(glowingEffect)
        }
        player.setCooldown(event.itemDrop.itemStack.type, 5 * 20)
        // World Particles
        with(player.location.world) {
            spawnParticle(Particle.SOUL, player.location, 10, 0.05, 0.35, 0.05)
            spawnParticle(Particle.SCULK_SOUL, player.location, 5, 0.25, 0.35, 0.25)
            playSound(player.location, Sound.PARTICLE_SOUL_ESCAPE, 2.5F, 1.4F)
            playSound(player.location, Sound.BLOCK_SAND_BREAK, 1.0F, 1.6F)
        }
        event.itemDrop.remove()
    }

}