package me.shadowalzazel.mcodyssey.mobs.utility

import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import java.util.*


open class OdysseyMob(internal val odysseyName: String, private val odysseyEntityType: EntityType, private val odysseyHealth: Double) {

    // Custom Entity Type -> custom model
    open fun createMob(someWorld: World, spawningLocation: Location): Entity {
        val newMob = someWorld.spawnEntity(spawningLocation, odysseyEntityType).apply {
            customName(Component.text(odysseyName))
            addScoreboardTag("Odyssey_Mob")
            // Health
            if (this is LivingEntity) {
                val mobHealth = AttributeModifier(UUID.fromString("c994412e-9e72-4881-a55f-1f2d1c95f454"), "odyssey_mob_health", odysseyHealth, AttributeModifier.Operation.ADD_NUMBER)
                val healthAttribute = getAttribute(Attribute.GENERIC_MAX_HEALTH)
                healthAttribute!!.addModifier(mobHealth)
                health += odysseyHealth
            }
        }
        return newMob
    }

}