package me.shadowalzazel.mcodyssey.unused

import org.bukkit.util.Vector
import org.joml.Matrix3d

@Deprecated("Not in use")
class SpaceRuneMatrix(
    private val colA: Vector,
    private val colB: Vector,
    private val colC: Vector
) {

    val asMatrix: Matrix3d = Matrix3d(colA.toVector3d(), colB.toVector3d(), colC.toVector3d())


}