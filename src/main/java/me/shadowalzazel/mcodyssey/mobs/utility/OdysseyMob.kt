package me.shadowalzazel.mcodyssey.mobs.utility

import me.shadowalzazel.mcodyssey.constants.OdysseyUUIDs
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity


open class OdysseyMob(internal val odysseyName: String, private val odysseyEntityType: EntityType, private val odysseyHealth: Double) {

    // Custom Entity Type -> custom model
    open fun createMob(someWorld: World, spawningLocation: Location): Entity {
        val newMob = someWorld.spawnEntity(spawningLocation, odysseyEntityType).apply {
            customName(Component.text(odysseyName))
            addScoreboardTag("Odyssey_Mob")
            // Health
            if (this is LivingEntity) {
                // CUSTOM KEY HEALTH PAIRS
                val mobHealth = AttributeModifier(OdysseyUUIDs.ODYSSEY_MOB_HEALTH_UUID, "odyssey_mob_health", odysseyHealth, AttributeModifier.Operation.ADD_NUMBER)
                val healthAttribute = getAttribute(Attribute.GENERIC_MAX_HEALTH)
                healthAttribute!!.addModifier(mobHealth)
                health += odysseyHealth
            }
        }
        return newMob
    }

}