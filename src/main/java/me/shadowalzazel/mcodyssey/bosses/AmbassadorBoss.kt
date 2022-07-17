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

    var patienceMeter: Double = 50.0
    var appeasementMeter: Double = 0.0
    var ambassadorBossEntity: Illusioner? = null
    var ambassadorActive: Boolean = false

    // spawner for boss entity
    private fun spawnBoss(odysseyWorld: World): Illusioner {
        //test XYZ
        val randomZLoc = (-10..10).random()
        val randomXLoc = (-10..10).random()
        val spawnBossLocation = Location(odysseyWorld, randomXLoc.toDouble(), 250.toDouble(), randomZLoc.toDouble())
        println("Spawned the Ambassador at $randomXLoc, $randomZLoc")
        for (aPlayer in odysseyWorld.players) {
            aPlayer.sendMessage("${ChatColor.GOLD}[${ChatColor.MAGIC}Vail${ChatColor.RESET}${ChatColor.GOLD}]${ChatColor.YELLOW} My Ambassador has arrived!")
            aPlayer.playSound(aPlayer.location, Sound.ENTITY_EVOKER_PREPARE_SUMMON, 2.0F, 1.0F)
        }
        return odysseyWorld.spawnEntity(spawnBossLocation, EntityType.ILLUSIONER) as Illusioner

    }

    private fun createAmbassadorWeapon(): ItemStack {
        val rocketBlaster = ItemStack(Material.BOW, 1)
        val rocketBlasterMeta: ItemMeta = rocketBlaster.itemMeta
        rocketBlasterMeta.setDisplayName("${ChatColor.LIGHT_PURPLE}Kinetic Blaster")
        val rocketBlasterLore = listOf("A weapon commissioned to shoot kinetic darts", "Designed by the current natives")
        rocketBlasterMeta.lore = rocketBlasterLore
        rocketBlasterMeta.addEnchant(Enchantment.ARROW_DAMAGE, 5, true)
        rocketBlasterMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true)
        rocketBlasterMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true)
        rocketBlaster.itemMeta = rocketBlasterMeta
        return rocketBlaster

    }

    private fun createSuperFirework(): ItemStack {
        // Create Firework Effects

        //var superFirework = ItemStack(Material.FIREWORK_ROCKET)
        var superFirework = ItemStack(Material.FIREWORK_ROCKET, 64)
        var superFireworkEffectsBuilder = FireworkEffect.builder()
        superFireworkEffectsBuilder.withColor(Color.BLUE)
        superFireworkEffectsBuilder.with(FireworkEffect.Type.BALL_LARGE)
        superFireworkEffectsBuilder.withFade(Color.PURPLE)
        superFireworkEffectsBuilder.trail(true)
        superFireworkEffectsBuilder.flicker(true)
        superFireworkEffectsBuilder.withFlicker()
        superFireworkEffectsBuilder.withTrail()
        val superFireworkEffects = superFireworkEffectsBuilder.build()

        // Add Effects, Sound, and Power

        return superFirework
    }

    fun createBoss(odysseyWorld: World) {
        var odysseyBossEntity: Illusioner = spawnBoss(odysseyWorld)
        // 600 tks = 30 sec
        // Add Potion Effects
        val voidFall = PotionEffect(PotionEffectType.SLOW_FALLING, 1200, 1)
        val voidGlow = PotionEffect(PotionEffectType.GLOWING, 1200, 1)
        val enhancedHealth = PotionEffect(PotionEffectType.HEALTH_BOOST, 99999, 175)
        val ankiRainEffects = listOf<PotionEffect>(enhancedHealth, voidFall, voidGlow)
        odysseyBossEntity.addPotionEffects(ankiRainEffects)

        // Change Default Behaviour
        odysseyBossEntity.customName = "${ChatColor.LIGHT_PURPLE}$bossName"
        odysseyBossEntity.isCustomNameVisible = true
        odysseyBossEntity.removeWhenFarAway = false
        odysseyBossEntity.isCanJoinRaid = false
        odysseyBossEntity.isAware = false
        odysseyBossEntity.canPickupItems = true
        odysseyBossEntity.health = 600.0

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

