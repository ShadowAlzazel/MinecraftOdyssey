package me.shadowalzazel.mcodyssey.common.mobs

import me.shadowalzazel.mcodyssey.util.constants.EntityTags
import org.bukkit.entity.Illager
import org.bukkit.entity.PiglinBrute
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

internal fun cloneIllagerSanctum(illager: Illager) : Illager {
    return (illager.world.spawnEntity(
        illager.location,
        illager.type,
        CreatureSpawnEvent.SpawnReason.CUSTOM) as Illager).apply {
        // Set data
        isPersistent = true
        removeWhenFarAway = false
        scoreboardTags.addAll(illager.scoreboardTags)
        scoreboardTags.add(EntityTags.CLONED)
        // Copy equipment
        MobFactory.factory.copyAndSetEquipment(this, illager)
    }
}

internal fun clonePiglinBrute(brute: PiglinBrute) : PiglinBrute {
    return (brute.world.spawnEntity(
        brute.location,
        brute.type,
        CreatureSpawnEvent.SpawnReason.CUSTOM) as PiglinBrute).apply {
        // Set Data
        isPersistent = true
        removeWhenFarAway = false
        scoreboardTags.addAll(brute.scoreboardTags)
        scoreboardTags.add(EntityTags.CLONED)
        server.scoreboardManager.mainScoreboard.getEntityTeam(brute)?.addEntity(this)
        customName(brute.customName())

        // Copy equipment
        MobFactory.factory.copyAndSetEquipment(this, brute)

        // Roll for trim
        var promoted = true
        var rank = 0
        var trimMaterial: TrimMaterial? = null
        when((0..110).random()) {
            in 0..9 -> {
                trimMaterial = TrimMaterial.NETHERITE
                rank = 4
            }
            in 10..29 -> {
                trimMaterial = TrimMaterial.DIAMOND
                rank = 3
            }
            in 30..59 -> {
                trimMaterial = TrimMaterial.GOLD
                rank = 2
            }
            in 60..100 -> {
                trimMaterial = TrimMaterial.COPPER
                rank = 1
            }
            else -> promoted = false
        }
        if (promoted) {
            // TrimMaterials
            val newTrim = ArmorTrim(trimMaterial!!, TrimPattern.SNOUT)
            MobFactory.factory.trimMobArmor(this, newTrim)
            // New stats based on rank
            val bonusHealth = (rank * 4.0)
            val bonusDamage = (rank * 1.0)
            MobFactory.factory.setHealthAttribute(this, bonusHealth)
            MobFactory.factory.addAttackAttribute(this, bonusDamage)
        }
        // Random Enchant
        val eMax = maxOf((rank * 5), 1) // Clamp from 1..x
        MobFactory.factory.enchantMobWornArmorRandomly(this, 1..eMax)

    }
}