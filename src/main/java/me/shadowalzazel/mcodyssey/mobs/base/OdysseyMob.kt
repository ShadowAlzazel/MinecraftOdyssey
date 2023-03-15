package me.shadowalzazel.mcodyssey.mobs.base

import me.shadowalzazel.mcodyssey.constants.EntityTags
import me.shadowalzazel.mcodyssey.constants.Identifiers
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity


open class OdysseyMob(
    internal val odysseyName: String,
    private val entityType: EntityType,
    private val mobHealth: Double) {

    // Custom Entity Type -> custom model
    open fun createMob(someWorld: World, spawningLocation: Location): Entity {
        val newMob = someWorld.spawnEntity(spawningLocation, entityType).apply {
            customName(Component.text(odysseyName))
            addScoreboardTag(EntityTags.ODYSSEY_MOB)
            // Health
            if (this@apply is LivingEntity) {
                // CUSTOM KEY HEALTH PAIRS
                val mobHealth = AttributeModifier(Identifiers.ODYSSEY_MOB_HEALTH_UUID, "odyssey_mob_health", mobHealth, AttributeModifier.Operation.ADD_NUMBER)
                val healthAttribute = getAttribute(Attribute.GENERIC_MAX_HEALTH)
                healthAttribute!!.addModifier(mobHealth)
                health += this@OdysseyMob.mobHealth
            }
        }
        return newMob
    }

}