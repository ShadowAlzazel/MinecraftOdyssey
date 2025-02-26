package me.shadowalzazel.mcodyssey.common.combat

import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

interface WeaponCombatHandler : AttackHelper, DataTagManager {

    //println("Cooldown Charge: " + player.attackCooldown)
    //println("Cooldown Period: " + player.cooldownPeriod)
    //println("Event Damage: ${event.damage}")
    //println("Final Damage: ${event.finalDamage}")

    // MAYBE ADD CHARGING MECHANIC
    // Moving in one direction check -> then multiply attack by 50%

    // PARRY MECHANIC -> if block within < 1 seconds of taking damage negate?

    fun weaponBonusEffectsHandler(event: EntityDamageByEntityEvent) {
        val player = event.damager as Player
        val victim = event.entity as LivingEntity
        // Further checks
        val mainWeapon = player.equipment!!.itemInMainHand
        val offHandWeapon = player.equipment!!.itemInOffHand
        // Get weapon type
        val mainWeaponType = mainWeapon.getStringTag(ItemDataTags.TOOL_TYPE)
        //val mainWeaponMaterial = mainWeapon.getStringTag(ItemDataTags.MATERIAL_TYPE)
        // Conditions
        val twoHanded = offHandWeapon.type == Material.AIR
        val hasShield = offHandWeapon.type == Material.SHIELD
        val isMounted = (player.vehicle != null) && (player in player.vehicle!!.passengers)
        val isSneaking = player.isSneaking
        val isCrit = event.isCritical
        val fullAttack = player.attackCooldown > 0.99
        // Get bonus/special effects
        when(mainWeaponType) {    // ??? If Crouching more damage
            "sickle" -> {
                val rads = (100 * Math.PI) / 180 // Always hits in a circle around player
                doWeaponAOESweep(player, victim, event.damage, rads, 2.0)
            }
            "dagger" -> {
                val rads = (80 * Math.PI) / 180 // Always hits in a circle around player
                doWeaponAOESweep(player, victim, event.damage, rads, 1.75)
            }
            "katana" -> {
                if (isCrit) event.damage += 3
                if (twoHanded) {
                    val rads = (80 * Math.PI) / 180 // Hits enemies near contact
                    doWeaponAOESweep(player, victim, event.damage * 0.75, rads)
                }
            }
            "claymore" -> {
                if (isSneaking) {
                    val rads = (90 * Math.PI) / 180 // Hits enemies near contact
                    doWeaponAOESweep(player, victim, event.damage, rads)
                }
            }
            "saber" -> {
                if (isMounted) event.damage += 3
            }
            "scythe" -> {
                val rads = (150 * Math.PI) / 180 // Always hits in a cone
                doWeaponAOESweep(player, victim, event.damage, rads, 4.0)
            }
            "chakram" -> {
                val rads = (175 * Math.PI) / 180 // Always hits in a circle around player
                doWeaponAOESweep(player, victim, event.damage, rads, 1.75)
            }
            "zweihander" -> {
                val rads = (120 * Math.PI) / 180 // Always hits in a circle around player
                doWeaponAOESweep(player, victim, event.damage, rads, 3.0)
            }
            "poleaxe" -> {
                val rads = (25 * Math.PI) / 180 // Hits enemies near contact
                doWeaponAOESweep(player, victim, event.damage, rads)
            }
            "glaive" -> {
                val rads = (65 * Math.PI) / 180   // Hits enemies near contact
                doWeaponAOESweep(player, victim, event.damage, rads)
            }
            "warhammer" -> {
                if (twoHanded) victim.shieldBlockingDelay = 60
            }
            "spear" -> {
                if (isSneaking) event.damage += 2.0
            }
            "halberd" -> {
                if (isSneaking) event.damage += 2.0
                if (hasShield) event.damage += 1.0
            }
            "lance" -> {
                if (isMounted && fullAttack) event.damage *= 2.50
            }
            "longaxe" -> {
                if (twoHanded && isCrit) {
                    event.damage += 0.5 // Weird bug
                    val extraCrit = (7.0 / 6.0) // Translates to 1.75 Damage
                    event.damage *= extraCrit
                }
            }

        }

    }

    fun weaponBonusAttributesHandler(event: EntityDamageByEntityEvent, weapon: String?) {
        if (weapon == null) return
        val attackPower = minOf((event.damager as Player).attackCooldown, 1.0F)
        val victim = event.entity
        if (victim.isDead) return
        if (victim !is LivingEntity) return
        // Get victim attributes
        val armor = victim.getAttribute(Attribute.ARMOR)?.value ?: 0.0
        val health = victim.health
        // Get bonuses from maps
        val bludgeoningDamage = WeaponMaps.BLUDGEON_MAP[weapon]?.let { minOf(it, armor.div(2))} ?: 0.0 // Dmg = x < armor / 2
        val laceratingDamage = WeaponMaps.LACERATE_MAP[weapon]?.let { maxOf(it - armor, 0.0) } ?: 0.0 // Dmg = x - armor
        val piercingDamage = WeaponMaps.PIERCE_MAP[weapon]?.let { minOf(armor, it) } ?: 0.0
        val cleavingDamage = WeaponMaps.CLEAVE_MAP[weapon] ?: 0.0
        // Piercing
        val trueDamage = piercingDamage * attackPower
        if (health < trueDamage) {
            victim.health = 0.0
        } else {
            victim.health -= trueDamage
        }
        event.damage -= trueDamage
        // Lacerate + Bludgeon
        val bonusDamage = attackPower * (laceratingDamage + bludgeoningDamage)
        event.damage += bonusDamage
    }

    // THROWABLE
    /*-----------------------------------------------------------------------------------------------*/


}