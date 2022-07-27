package me.shadowalzazel.mcodyssey.bosses.hogRider

import me.shadowalzazel.mcodyssey.bosses.utility.OdysseyBoss
import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantments
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class HogRiderBoss : OdysseyBoss("Hog Rider", "Piglin") {

    // Boss Spawning Logic
    var bossEntityRider: PiglinBrute? = null
    var bossEntityMount: Hoglin? = null
    var despawnTimer: Long = 1

    private fun createHogRiderWeapon(): ItemStack {
        val smokyWarHammer = ItemStack(Material.NETHERITE_AXE, 1)

        // Add lore and name
        val smokyWarHammerMeta: ItemMeta = smokyWarHammer.itemMeta
        smokyWarHammerMeta.setDisplayName("${ChatColor.GOLD}Puny Smoky War-hammer")
        val smokyWarHammerLore = listOf("${ChatColor.GOLD}${OdysseyEnchantments.BANE_OF_THE_SWINE.name} II",
            "${ChatColor.GOLD}${OdysseyEnchantments.FREEZING_ASPECT.name} V",
            "${ChatColor.GOLD}${OdysseyEnchantments.GUARDING_STRIKE.name} II",
            "A weapon ironic in name", "That has slaughtered many")
        smokyWarHammerMeta.lore = smokyWarHammerLore

        // Add Enchantments
        smokyWarHammerMeta.addEnchant(Enchantment.DAMAGE_ALL, 5, true)
        smokyWarHammerMeta.addEnchant(Enchantment.KNOCKBACK, 3, true)
        smokyWarHammerMeta.addEnchant(Enchantment.FIRE_ASPECT, 2, true)
        smokyWarHammerMeta.addEnchant(Enchantment.DURABILITY, 3, true)
        smokyWarHammerMeta.addEnchant(OdysseyEnchantments.GILDED_POWER, 3, true)
        smokyWarHammerMeta.addEnchant(OdysseyEnchantments.BANE_OF_THE_SWINE, 5, true)
        smokyWarHammerMeta.addEnchant(OdysseyEnchantments.FREEZING_ASPECT, 2, true)
        smokyWarHammerMeta.addEnchant(OdysseyEnchantments.GUARDING_STRIKE, 2, true)

        // Create weapon
        smokyWarHammer.itemMeta = smokyWarHammerMeta
        return smokyWarHammer
    }



    private fun spawnRider(odysseyWorld: World, someLocation: Location): PiglinBrute {
        val warRider: PiglinBrute = odysseyWorld.spawnEntity(someLocation, EntityType.PIGLIN_BRUTE) as PiglinBrute
        return warRider
    }


    private fun spawnMount(odysseyWorld: World, someLocation: Location): Hoglin {
        val warthog: Hoglin = odysseyWorld.spawnEntity(someLocation, EntityType.HOGLIN) as Hoglin
        return warthog
    }



    fun createBoss(odysseyWorld: World, someLocation: Location) {
        // 1200 tks = 60 sec
        val hogRider = spawnRider(odysseyWorld, someLocation) as PiglinBrute
        val warthog = spawnMount(odysseyWorld, someLocation) as Hoglin
        // Crate mount
        warthog.addPassenger(hogRider)
        // Add Potion Effects
        val flameResistance = PotionEffect(PotionEffectType.FIRE_RESISTANCE, 99999, 3)
        val enhancedHealth = PotionEffect(PotionEffectType.HEALTH_BOOST, 99999, 235)
        val abnormalStrength = PotionEffect(PotionEffectType.INCREASE_DAMAGE, 99999, 5)
        val abnormalSpeed = PotionEffect(PotionEffectType.SPEED, 99999, 2)
        //
        val hogRiderEffects = listOf(enhancedHealth, flameResistance, abnormalStrength)
        val warthogEffects = listOf(enhancedHealth, flameResistance, abnormalSpeed)
        hogRider.addPotionEffects(hogRiderEffects)
        warthog.addPotionEffects(warthogEffects)

        despawnTimer = System.currentTimeMillis()
        // Change Default Behaviour
        hogRider.customName = "${ChatColor.GOLD}$bossName"
        hogRider.isCustomNameVisible = true
        hogRider.removeWhenFarAway = false
        hogRider.isAware = true
        hogRider.isImmuneToZombification = true
        hogRider.canPickupItems = true
        hogRider.health = 950.0
        //
        warthog.setIsAbleToBeHunted(false)
        warthog.isImmuneToZombification = true
        warthog.ageLock = true
        warthog.setBaby()
        warthog.health = 950.0
        warthog.removeWhenFarAway = false


        // Add Item
        val hogRiderWeapon: ItemStack = createHogRiderWeapon()
        hogRider.clearActiveItem()
        hogRider.equipment.setItemInMainHand(hogRiderWeapon)
        hogRider.equipment.boots = ItemStack(Material.NETHERITE_BOOTS, 1)
        hogRider.equipment.helmet = ItemStack(Material.NETHERITE_HELMET, 1)
        hogRider.equipment.leggings = ItemStack(Material.NETHERITE_LEGGINGS, 1)
        hogRider.equipment.chestplate = ItemStack(Material.NETHERITE_CHESTPLATE, 1)

        // Change boss class
        bossEntityRider = hogRider
        bossEntityMount = warthog
    }


}