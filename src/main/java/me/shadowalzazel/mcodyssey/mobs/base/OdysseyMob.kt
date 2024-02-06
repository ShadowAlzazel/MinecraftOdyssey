package me.shadowalzazel.mcodyssey.mobs.base

import me.shadowalzazel.mcodyssey.constants.AttributeIDs
import me.shadowalzazel.mcodyssey.constants.AttributeTags
import me.shadowalzazel.mcodyssey.constants.EntityTags
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity


open class OdysseyMob(
    internal val displayName: String,
    private val tagName: String,
    private val type: EntityType,
    private val health: Double) {

    // Custom Entity Type -> custom model
    open fun createMob(world: World, location: Location): Entity {
        val newMob = world.spawnEntity(location, type).apply {
            customName(Component.text(displayName))
            addScoreboardTag(EntityTags.ODYSSEY_MOB)
            addScoreboardTag(tagName)
            // Health
            if (this@apply is LivingEntity) {
                // CUSTOM KEY HEALTH PAIRS
                val mobHealth = AttributeModifier(AttributeIDs.ODYSSEY_MOB_HEALTH_UUID, AttributeTags.MOB_HEALTH, health, AttributeModifier.Operation.ADD_NUMBER)
                val healthAttribute = getAttribute(Attribute.GENERIC_MAX_HEALTH)
                healthAttribute!!.addModifier(mobHealth)
                health += this@OdysseyMob.health
            }
        }
        return newMob
    }

}