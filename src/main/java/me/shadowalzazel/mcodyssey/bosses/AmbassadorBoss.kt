package me.shadowalzazel.mcodyssey.bosses

import me.shadowalzazel.mcodyssey.bosses.utility.OdysseyBoss
import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Illusioner
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

@Suppress("DEPRECATION")
open class AmbassadorBoss : OdysseyBoss("The Ambassador", "Illusioner") {

    var patienceMeter: Double = 75.0
    var appeasementMeter: Double = 0.0
    var ambassadorBossEntity: Illusioner? = null
    var ambassadorActive: Boolean = false

    // spawner for boss entity
    private fun spawnBoss(odysseyWorld: World): Illusioner {
        //random
        val villageLocation =  Location(odysseyWorld, (-50).toDouble(), 100.toDouble(), (-50).toDouble())
        val randomPlayers = odysseyWorld.getNearbyPlayers(villageLocation, 100.0)
        val randomPlayer = randomPlayers.random()
        val playerLoc = randomPlayer.location
        playerLoc.y += 150
        println("Spawned the Ambassador at ${randomPlayer.location.x}, ${randomPlayer.location.z}")
        for (aPlayer in odysseyWorld.players) {
            aPlayer.sendMessage("${ChatColor.GOLD}${ChatColor.MAGIC}[Vail]${ChatColor.RESET}${ChatColor.YELLOW} My Ambassador has arrived!")
            aPlayer.playSound(aPlayer.location, Sound.ENTITY_EVOKER_PREPARE_SUMMON, 2.5F, 0.9F)
        }
        return odysseyWorld.spawnEntity(playerLoc, EntityType.ILLUSIONER) as Illusioner

    }

    fun spawnDummy() {
        val currentLocation = ambassadorBossEntity!!.location
        val randomXZLoc = (-5..5).random()
        currentLocation.x += randomXZLoc
        currentLocation.z += randomXZLoc
        currentLocation.y += 3
        val ambassadorDummy = ambassadorBossEntity!!.world.spawnEntity(currentLocation, EntityType.ILLUSIONER) as Illusioner
        ambassadorDummy.customName = "${ChatColor.LIGHT_PURPLE}$bossName"
        ambassadorDummy.isCustomNameVisible = true
        ambassadorDummy.isCanJoinRaid = false
        ambassadorDummy.isAware = true
        ambassadorDummy.lootTable = null
        ambassadorDummy.health = 25.0

        // Add Item
        val ambassadorWeapon: ItemStack = createAmbassadorWeapon()
        ambassadorDummy.clearActiveItem()
        ambassadorDummy.equipment.setItemInMainHand(ambassadorWeapon)

    }

    // Create Kinetic Weapon
    private fun createAmbassadorWeapon(): ItemStack {
        val kineticBlaster = ItemStack(Material.BOW, 1)
        val kineticBlasterMeta: ItemMeta = kineticBlaster.itemMeta
        kineticBlasterMeta.setDisplayName("${ChatColor.LIGHT_PURPLE}Kinetic Blaster")
        val kineticBlasterLore = listOf("A weapon commissioned to shoot kinetic darts", "Designed by the current natives")
        kineticBlasterMeta.lore = kineticBlasterLore
        kineticBlasterMeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true)
        kineticBlasterMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true)
        kineticBlasterMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true)
        kineticBlaster.itemMeta = kineticBlasterMeta
        return kineticBlaster

    }

    //REPURPOSE LATER
    private fun createSuperFirework(){
        // Create Firework Effects
        //: ItemStack

        // Add Effects, Sound, and Power
        return
    }

    fun createBoss(odysseyWorld: World) {
        var odysseyBossEntity: Illusioner = spawnBoss(odysseyWorld)
        // 600 tks = 30 sec
        // Add Potion Effects
        val voidFall = PotionEffect(PotionEffectType.SLOW_FALLING, 1200, 1)
        val voidGlow = PotionEffect(PotionEffectType.GLOWING, 1200, 1)
        val voidSolar = PotionEffect(PotionEffectType.FIRE_RESISTANCE, 99999, 3)
        val enhancedHealth = PotionEffect(PotionEffectType.HEALTH_BOOST, 99999, 235)
        val ankiRainEffects = listOf<PotionEffect>(enhancedHealth, voidFall, voidSolar, voidGlow)
        odysseyBossEntity.addPotionEffects(ankiRainEffects)

        // Change Default Behaviour
        odysseyBossEntity.customName = "${ChatColor.LIGHT_PURPLE}$bossName"
        odysseyBossEntity.isCustomNameVisible = true
        odysseyBossEntity.removeWhenFarAway = false
        odysseyBossEntity.isCanJoinRaid = false
        odysseyBossEntity.isAware = false
        odysseyBossEntity.canPickupItems = true
        odysseyBossEntity.health = 950.0

        // Add Item
        val ambassadorWeapon: ItemStack = createAmbassadorWeapon()
        odysseyBossEntity.clearActiveItem()
        odysseyBossEntity.equipment.setItemInMainHand(ambassadorWeapon)

        ambassadorBossEntity = odysseyBossEntity
    }

    // MOVE HERE LATER
    fun gravityAttack() {
    }

}

