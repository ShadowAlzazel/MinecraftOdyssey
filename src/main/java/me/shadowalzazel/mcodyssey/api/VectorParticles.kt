package me.shadowalzazel.mcodyssey.api

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


interface VectorParticles {

    fun spawnLineParticles(particle: Particle, start: Location, end: Location, count: Int, color: Color?) {
        // Calculate the vector from start to end
        val vector = end.toVector().subtract(start.toVector())
        val length = vector.length()
        vector.normalize() // Normalize the vector to unit length

        // Calculate the spacing between particles
        val spacing = length / count

        // Iterate along the line and spawn particles
        for (i in 0..count) {
            // Calculate the current point on the line
            val offset = vector.clone().multiply(i * spacing)
            val particleLocation = start.clone().add(offset)

            // Spawn the particle at the current location
            if (color != null) {
                start.world.spawnParticle(particle, particleLocation, 1, 0.0, 0.0, 0.0, color)
            } else {
                start.world.spawnParticle(particle, particleLocation, 1, 0.0, 0.0, 0.0)
            }
        }
    }

    fun spawnConeParticles(particle: Particle, location: Location, count: Int, coneAngle: Double, coneLength: Double, color: Color?) {
        val random: Random = Random()
        val angleRadians = Math.toRadians(coneAngle) // Convert to radians

        // Direction vector for the cone axis (can be customized)
        val coneDirection: Vector = location.direction.normalize()

        // Minimum distance from center
        val epsilon = 0.11

        for (i in 0 until count) {
            // Generate random spherical coordinates
            val phi: Double = random.nextDouble() * 2 * Math.PI // Azimuthal angle (0 to 360 degrees)
            val cosTheta: Double = 1 - random.nextDouble() * (1 - cos(angleRadians)) // Constrain by cone angle
            val sinTheta = sqrt(1 - cosTheta * cosTheta)

            // Convert spherical coordinates to Cartesian coordinates
            val randomDirection = Vector(
                sinTheta * cos(phi),
                sinTheta * sin(phi),
                cosTheta
            )

            // Scale by random distance within the cone length
            val distance: Double = (random.nextDouble() + epsilon) * coneLength
            var particleOffset = randomDirection.multiply(distance)

            // Align the random direction to the cone's central axis
            particleOffset = rotateVectorToDirection(particleOffset, coneDirection)

            // Add the offset to the base location
            val particleLocation: Location = location.clone().add(particleOffset).add(0.0, 1.0, 0.0)

            // Spawn the particle at the calculated location
            if (color != null) {
                location.world.spawnParticle(particle, particleLocation, 1, 0.0, 0.0, 0.0, 0.0, color)
            } else {
                location.world.spawnParticle(particle, particleLocation, 1, 0.0, 0.0, 0.0, 0.0)
            }
        }
    }

    private fun rotateAroundAxis(vector: Vector, axis: Vector, angle: Double): Vector {
        val cos = cos(angle)
        val sin = sin(angle)
        val dot: Double = axis.dot(vector)

        return vector.clone().multiply(cos)
            .add(axis.clone().crossProduct(vector).multiply(sin))
            .add(axis.clone().multiply(dot * (1 - cos)))
    }

    private fun rotateVectorToDirection(vector: Vector, direction: Vector): Vector {
        // Use quaternion or matrix rotation to align the vector with the desired direction
        var rotatedVector: Vector? = null
        val axis = Vector(0, 0, 1) // Default "up" direction
        if (direction != axis) {
            val cross = axis.clone().crossProduct(direction).normalize()
            val dot = axis.clone().dot(direction)
            val angle = acos(dot)

            rotatedVector = rotateAroundAxis(vector, cross, angle)
        }
        return rotatedVector ?: vector
    }

}