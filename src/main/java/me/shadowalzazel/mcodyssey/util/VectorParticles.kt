package me.shadowalzazel.mcodyssey.util

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.util.Vector
import java.util.*
import kotlin.math.*


interface VectorParticles {

    fun spawnLineParticles(particle: Particle, start: Location, end: Location, count: Int, color: Color?=null) {
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
        val random = Random()
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


    fun spawnArcParticles(
        particle: Particle, center: Location, directionVector: Vector, pitchAngle: Double,
        radius: Double, arcLength: Double, height: Double, count: Int, color: Color?=null) {
        // Normalize the direction vector
        val direction = directionVector.clone().normalize()
        // Pitch in radians
        val pitchRadians = Math.toRadians(pitchAngle)

        // Generate a perpendicular vector for the arc plane
        val perpendicular = getPerpendicularVector(direction, Vector(0, 1, 0)).normalize()
        val planarVector = perpendicular.clone()

        // Generate a second vector for height that is perpendicular to the direction and plane
        val heightVector = planarVector.clone().crossProduct(direction).clone()

        // Calculate angular spacing based on arc length and particle count
        val angleStep = arcLength / (count - 1)
        for (i in 0..count) {
            // Calculate the current angle in radians
            val angle = -arcLength / 2 + i * angleStep

            // Compute the X and Y components of the arc (circular motion)
            val x = cos(angle) * radius
            val y = sin(angle) * radius

            // Create the arc point in the perpendicular plane
            val pointVector = direction.clone().multiply(x).add(planarVector.clone().multiply(y))

            // Apply the pitch tilt by adjusting the Y component
            pointVector.setY(pointVector.y + tan(pitchRadians) * radius)

            val heightParticles = max(1.0, height * 5.0).toInt() // Number of particles across the thickness
            for (j in -heightParticles..heightParticles) {
                // Offset the arc point along the thickness vector
                val newPoint = pointVector.clone().add(heightVector.clone().multiply(j * (height / heightParticles)))
                // Translate the thickened point to the center location
                val particleLocation = center.clone().add(newPoint)

                // Spawn the particle
                if (color != null) {
                    center.world.spawnParticle(particle, particleLocation, 1, 0.0, 0.0, 0.0, 0.0, color)
                } else {
                    center.world.spawnParticle(particle, particleLocation, 1, 0.0, 0.0, 0.0, 0.0)
                }
            }
        }
    }

    fun spawnCircleParticles(
        particle: Particle, center: Location, upDirection: Vector,
        radius: Double, heightOffset: Double, count: Int, color: Color?=null) {
        // Normalize the facing vector
        val upVector = upDirection.clone().normalize()

        // Generate two planar perpendicular vectors for the circle's plane that form a basis
        val planeVector1 = if (abs(upVector.x) <= abs(upVector.x) && abs(upVector.x) <= abs(upVector.z)) {
            Vector(1.0, 0.0, 0.0) // x is the smallest component, pick something not aligned with x
        } else if (abs(upVector.y) <= abs(upVector.x) && abs(upVector.y) <= abs(upVector.z)) {
            Vector(0.0, 1.0, 0.0) // y is the smallest component
        } else {
            Vector(0.0, 0.0, 1.0) // z is the smallest component
        }
        val planeVector2 = getPerpendicularVector(upVector, planeVector1)

        // Angle step for placing particles evenly along the circumference
        val angleStep = 2 * Math.PI / count

        // Adjust the center location's height
        center.add(upVector.clone().multiply(heightOffset))

        for (i in 0 until count) {
            // Calculate the angle for this particle
            val angle = i * angleStep

            // Calculate the particle's position on the circle's circumference
            val x = cos(angle) * radius
            val z = sin(angle) * radius

            // Combine the perpendicular vectors to define the particle's position
            val circlePoint = planeVector1.clone().multiply(x).add(planeVector2.clone().multiply(z))

            // Translate the particle position to the center location
            val particleLocation = center.clone().add(circlePoint)

            // Spawn the particle
            if (color != null) {
                center.world.spawnParticle(particle, particleLocation, 1, 0.0, 0.0, 0.0, 0.0, color)
            } else {
                center.world.spawnParticle(particle, particleLocation, 1, 0.0, 0.0, 0.0, 0.0)
            }
        }
    }

    private fun getPerpendicularVector(vector: Vector, axis: Vector): Vector {
        // Find a perpendicular vector by picking a default axis and calculating the cross product
        return vector.clone().crossProduct(axis).normalize()
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