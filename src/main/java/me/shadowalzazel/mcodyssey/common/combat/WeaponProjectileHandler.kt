package me.shadowalzazel.mcodyssey.common.combat

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ChargedProjectiles
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent
import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.util.VectorParticles
import me.shadowalzazel.mcodyssey.common.listeners.WeaponListeners
import me.shadowalzazel.mcodyssey.common.tasks.weapon_tasks.LoadAutoCrossbow
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.common.enchantments.EnchantmentManager
import me.shadowalzazel.mcodyssey.common.tasks.weapon_tasks.LoadCompactCrossbow
import me.shadowalzazel.mcodyssey.common.tasks.weapon_tasks.LoadCrossbolter
import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CrossbowMeta

@Suppress("UnstableApiUsage")
interface WeaponProjectileHandler : DataTagManager, EnchantmentManager, AttackHelper, VectorParticles {

    /*-----------------------------------------------------------------------------------------------*/
    // EXPLOSIVE ARROW
    fun explosiveArrowShootHandler(event: EntityShootBowEvent) {
        event.projectile.also {
            it.addScoreboardTag(EntityTags.EXPLOSIVE_ARROW)
        }
    }

    fun explosiveArrowHitHandler(event: ProjectileHitEvent) {
        val projectile = event.entity
        val location = projectile.location
        WeaponListeners.explosionHandler(location.getNearbyLivingEntities(2.0), location, 2.0)
    }

    /*-----------------------------------------------------------------------------------------------*/
    fun autoCrossbowShooting(event: EntityShootBowEvent) {
        val crossbow = event.bow ?: return
        // Offhand
        val offhand = event.entity.equipment?.itemInOffHand ?: return
        // Auto Crossbow
        if (crossbow.itemMeta is CrossbowMeta) {
            // Check if match
            //val crossbowMeta = crossbow.itemMeta as CrossbowMeta
            val item = event.consumable ?: return
            val itemMeta = item.itemMeta
            val loadedItems = mutableListOf<ItemStack>()
            // Matches
            val matchingArrows = offhand.type == Material.ARROW && item.type == Material.ARROW
            if (offhand.itemMeta == itemMeta || matchingArrows) {
                if (!crossbow.hasTag(ItemDataTags.AUTO_LOADER_LOADING)) {
                    loadedItems.add(item)
                    if (crossbow.hasEnchantment(Enchantment.MULTISHOT)) {
                        loadedItems.add(item.clone())
                        loadedItems.add(item.clone())
                    }
                    offhand.subtract(1)
                    val task = LoadAutoCrossbow(event.entity, crossbow, loadedItems)
                    task.runTaskLater(Odyssey.instance, 1)
                }
                crossbow.addTag(ItemDataTags.AUTO_LOADER_LOADING)
            }
        }
    }

    /*-----------------------------------------------------------------------------------------------*/
    fun crossbolterShooting(event: EntityShootBowEvent) {
        val crossbow = event.bow ?: return
        if (crossbow.type != Material.CROSSBOW) return
        val ammo = event.consumable ?: return
        // States
        // CURRENTLY multishot counts as 3
        val ammoCounter = crossbow.getIntTag(ItemDataTags.AMMO_COUNT) ?: return
        // Subtract ammo or remove count
        if (ammoCounter <= 1) {
            crossbow.removeTag(ItemDataTags.AMMO_COUNT)
        } else {
            crossbow.setIntTag(ItemDataTags.AMMO_COUNT, ammoCounter - 1)
        }
        // Load if possible
        if (!crossbow.hasTag(ItemDataTags.IS_LOADING_AMMO)) {
            // Load
            val loadedItems = mutableListOf(ammo.clone())
            crossbow.addTag(ItemDataTags.IS_LOADING_AMMO)
            val task = LoadCrossbolter(event.entity, crossbow, loadedItems)
            task.runTaskLater(Odyssey.instance, 8)
        }
    }

    fun crossbolterLoading(event: EntityLoadCrossbowEvent) {
        if (event.crossbow.hasTag(ItemDataTags.AMMO_COUNT)) return
        val crossbow = event.crossbow
        if (crossbow.hasTag(ItemDataTags.IS_LOADING_AMMO)) return
        // Need to load offhand
        if (crossbow == event.entity.equipment?.itemInOffHand) return
        // Add ammo tag
        val offhand = event.entity.equipment?.itemInOffHand ?: return
        //if (offhand.type != Material.ARROW) return
        val ammoCount = minOf(offhand.amount, 7)
        offhand.subtract(ammoCount - 1)
        crossbow.setIntTag(ItemDataTags.AMMO_COUNT, ammoCount)
    }

    /*-----------------------------------------------------------------------------------------------*/
    fun alchemicalWeaponShooting(event: EntityShootBowEvent, weaponType: String) {
        // Initial Sentries
        val crossbow = event.bow ?: return
        if (crossbow.type != Material.CROSSBOW) return
        if (!crossbow.hasTag(ItemDataTags.HAS_POTION_LOADED)) return
        // Potion Data
        val potionContents = crossbow.getData(DataComponentTypes.POTION_CONTENTS) ?: return
        val chargedProjectiles = crossbow.getData(DataComponentTypes.CHARGED_PROJECTILES) ?: return
        // Entity Data
        val shooter = event.entity
        val projectile = event.projectile
        var lastShot = false
        var hasAmmo = true
        //val potionItem = chargedPotions.projectiles().find { it.hasData(DataComponentTypes.POTION_CONTENTS) }
        // Run
        val multiCounter = crossbow.getIntTag(ItemDataTags.MULTISHOT_TRACKER) ?: 1
        val potionAmmoCount = crossbow.getIntTag(ItemDataTags.POTION_AMMO_AMOUNT) ?: 1
        when(weaponType) {
            "alchemical_driver" -> {
                var thrownPotion: ThrownPotion? = null
                if (multiCounter >= 1) {
                    // Set throwing potion
                    val potionItem = if (chargedProjectiles.projectiles().isNotEmpty()) chargedProjectiles.projectiles().first() else {
                        val potionType = crossbow.getStringTag(ItemDataTags.LOADED_POTION_TYPE) ?: "splash_potion"
                        val potionMaterial = if (potionType == "lingering_potion") Material.LINGERING_POTION else Material.SPLASH_POTION
                        ItemStack(potionMaterial, 1)
                    }
                    potionItem.setData(DataComponentTypes.POTION_CONTENTS, potionContents)
                    // Spawn Entity
                    thrownPotion = (shooter.world.spawnEntity(projectile.location, EntityType.POTION) as ThrownPotion).also {
                        it.item = potionItem
                        it.velocity = projectile.velocity.clone().multiply(0.6)
                        it.shooter = shooter
                    }
                    // Multishot compatibility
                    crossbow.setIntTag(ItemDataTags.MULTISHOT_TRACKER, multiCounter - 1)
                    if (multiCounter - 1 <= 0) {
                        lastShot = true
                        hasAmmo = false
                        crossbow.removeTag(ItemDataTags.LOADED_POTION_TYPE)
                    }
                }
                // Shoot if potion made
                if (thrownPotion != null) {
                    event.projectile = thrownPotion
                }
            }
            "alchemical_diffuser" -> {
                // Spray
                val rads = (45 * Math.PI) / 180 //60 degrees in front
                val entitiesInCone = getEntitiesInArc(shooter, rads, 6.5)
                val effects = potionContents.customEffects() + (potionContents.potion()?.potionEffects ?: mutableListOf())
                if (effects.isEmpty()) return
                // Particles
                val particle = Particle.ENTITY_EFFECT
                val color = potionContents.customColor() ?: effects.first().type.color
                spawnConeParticles(particle, shooter.location, 280, 45.0, 6.0, color)
                // Run
                for (entity in entitiesInCone) {
                    entity.addPotionEffects(effects)
                    val location = entity.location
                    location.world.spawnParticle(particle, location, 15, 0.03, 0.1, 0.03, color)
                    location.world.spawnParticle(particle, location, 5, 0.08, 0.15, 0.08, color)
                }
                // Multishot compatibility
                crossbow.setIntTag(ItemDataTags.MULTISHOT_TRACKER, multiCounter - 1)
                if (multiCounter - 1 <= 0) lastShot = true
                // Ammo tracker
                crossbow.setIntTag(ItemDataTags.POTION_AMMO_AMOUNT, potionAmmoCount - 1)
                if (potionAmmoCount - 1 <= 0) hasAmmo = false
                shooter.world.playSound(shooter.location, Sound.ITEM_GLOW_INK_SAC_USE, SoundCategory.MASTER, 2.3F, 1.3F)
                event.projectile.remove()
            }
            "alchemical_bolter" -> {
                // Add potions to arrow
                if (projectile is Arrow) {
                    projectile.clearCustomEffects()
                    val effects = potionContents.customEffects() + (potionContents.potion()?.potionEffects ?: mutableListOf())
                    for (effect in effects) {
                        projectile.addCustomEffect(effect, true)
                    }
                    // Ammo tracker
                    crossbow.setIntTag(ItemDataTags.POTION_AMMO_AMOUNT, potionAmmoCount - 1)
                    if (potionAmmoCount - 1 <= 0) hasAmmo = false
                }
                // Multishot compatibility
                crossbow.setIntTag(ItemDataTags.MULTISHOT_TRACKER, multiCounter - 1)
                if (multiCounter - 1 <= 0) lastShot = true
            }
        }
        // Reset Data conditions
        if (lastShot) {
            crossbow.resetData(DataComponentTypes.CHARGED_PROJECTILES) // have to load arrows again
        }
        if (!hasAmmo) { // Have to load a POTION again
            crossbow.removeTag(ItemDataTags.HAS_POTION_LOADED)
            crossbow.resetData(DataComponentTypes.POTION_CONTENTS)
        }
    }

    // For all alchemical weapons
    fun alchemicalWeaponLoading(loader: LivingEntity, crossbow: ItemStack, weaponType: String): Boolean {
        // Get Potion
        val offhandIsPotion = loader.equipment!!.itemInOffHand.type in listOf(
            Material.SPLASH_POTION, Material.LINGERING_POTION)
        val offhandIsAmmo = loader.equipment!!.itemInOffHand.type == Material.POTION
        if (!offhandIsPotion && !offhandIsAmmo) {
            return false
        }
        val potionOffHand = loader.equipment!!.itemInOffHand
        val potionContents = potionOffHand.getData(DataComponentTypes.POTION_CONTENTS) ?: return false
        // Check if potion loaded
        if (crossbow.hasTag(ItemDataTags.HAS_POTION_LOADED)) return false // Cancel loading logic - have to empty ammo first
        // Get vars
        val multiCounter = if (crossbow.itemMeta.hasEnchant(Enchantment.MULTISHOT)) 3 else 1
        val chargedProjectiles = ChargedProjectiles.chargedProjectiles(listOf(potionOffHand.clone()))
        // Set Components
        crossbow.setData(DataComponentTypes.POTION_CONTENTS, potionContents)
        crossbow.setData(DataComponentTypes.CHARGED_PROJECTILES, chargedProjectiles)
        crossbow.addTag(ItemDataTags.HAS_POTION_LOADED)
        // For Each
        when(weaponType) {
            "alchemical_driver" -> {
                crossbow.setIntTag(ItemDataTags.MULTISHOT_TRACKER, multiCounter)
                val potionType = if (potionOffHand.type == Material.LINGERING_POTION) "lingering_potion" else "splash_potion"
                crossbow.setStringTag(ItemDataTags.LOADED_POTION_TYPE, potionType)
                // Has no ammo slots as loads 1 potion at a time
            }
            "alchemical_diffuser" -> {
                crossbow.setIntTag(ItemDataTags.MULTISHOT_TRACKER, multiCounter)
                crossbow.setIntTag(ItemDataTags.POTION_AMMO_AMOUNT, 4) // Has 4 ammo slots
            }
            "alchemical_bolter" -> {
                crossbow.setIntTag(ItemDataTags.MULTISHOT_TRACKER, multiCounter)
                crossbow.setIntTag(ItemDataTags.POTION_AMMO_AMOUNT, 16)  // Has 16 ammo slots
            }
        }
        loader.world.playSound(loader.location, Sound.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.MASTER, 1.8F, 1.3F)
        loader.equipment!!.itemInOffHand.subtract()
        //val potionType = if (potionOffHand.type == Material.LINGERING_POTION) "lingering_potion" else "splash_potion"
        //crossbow.setStringTag(ItemDataTags.LOADED_POTION_TYPE, potionType)
        return false
    }

    /*-----------------------------------------------------------------------------------------------*/
    fun compactCrossbowLoading(event: EntityLoadCrossbowEvent) {
        val player = event.entity
        if (player !is Player) return
        if (player.inventory.itemInMainHand.type != Material.CROSSBOW) return
        if (player.inventory.itemInOffHand.type != Material.CROSSBOW) return
        if (player.inventory.itemInMainHand.getItemIdentifier() != "compact_crossbow") return
        if (player.inventory.itemInOffHand.getItemIdentifier() != "compact_crossbow") return
        // Call runnable
        val handToLoad = if (event.hand == EquipmentSlot.HAND) EquipmentSlot.OFF_HAND else EquipmentSlot.HAND
        LoadCompactCrossbow(player, handToLoad).runTask(Odyssey.instance)
    }

    /*-----------------------------------------------------------------------------------------------*/
    fun tinkeredMusketLoading(event: EntityLoadCrossbowEvent) {
        val player = event.entity
        if (player !is Player) return
        if (player.inventory.itemInMainHand.type != Material.CROSSBOW) return
        if (player.inventory.itemInMainHand.getItemIdentifier() != "tinkered_musket") return
        if (player.inventory.itemInOffHand.getItemIdentifier() == "tinkered_musket") {
            event.isCancelled =true
            return
        } // Can not load this way
        val musket = event.crossbow
        val offhand = player.inventory.itemInOffHand
        if (musket.hasTag(ItemDataTags.MUSKET_LOADED_STAGE_2)) {
            event.isCancelled = true // Prevent
            return
        }
        // Stage 1 -> Gunpowder
        if (!musket.hasTag(ItemDataTags.MUSKET_LOADED_STAGE_1)) {
            if (offhand.type != Material.GUNPOWDER) {
                event.isCancelled = true // Prevent
            } else {
                musket.addTag(ItemDataTags.MUSKET_LOADED_STAGE_1)
                offhand.subtract()
                event.isCancelled = true // Stop
            }
            return
        }
        // Stage 2 -> Iron nugget
        if (musket.hasTag(ItemDataTags.MUSKET_LOADED_STAGE_1)) {
            if (offhand.type != Material.IRON_NUGGET) {
                event.isCancelled = true // Prevent
            } else {
                musket.removeTag(ItemDataTags.MUSKET_LOADED_STAGE_1)
                musket.addTag(ItemDataTags.MUSKET_LOADED_STAGE_2)
                offhand.subtract()
            }
            return
        }
    }

    fun tinkeredMusketShooting(event: EntityShootBowEvent) {
        val entity = event.entity
        val musket = event.bow ?: return
        if (!musket.hasTag(ItemDataTags.MUSKET_LOADED_STAGE_2)) {
            event.isCancelled = true
            return
        }
        else {
            musket.removeTag(ItemDataTags.MUSKET_LOADED_STAGE_2)
            // Projectile Stats
            with(event.projectile) {
                val eyeDirection = entity.eyeLocation.direction.clone().normalize().multiply( 1.125)
                val speed = 7.75F
                val aimVector = eyeDirection.add(velocity).normalize().multiply(speed)
                velocity = aimVector
                if (this is Arrow) {
                    damage = 4.0
                }
            }
            // Recoil
            val recoilVector = event.projectile.velocity.clone().normalize().multiply(-0.4)
            entity.velocity = entity.velocity.add(recoilVector)
            val location = entity.location
            location.world.playSound(location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.BLOCKS, 0.8F, 1.7F)
        }


    }


}