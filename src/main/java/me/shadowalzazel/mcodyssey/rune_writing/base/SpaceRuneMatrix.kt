package me.shadowalzazel.mcodyssey.rune_writing.base

import org.bukkit.util.Vector
import org.joml.Matrix3d

class SpaceRuneMatrix(
    val colA: Vector,
    val colB: Vector,
    val colC: Vector
) {

    val asMatrix: Matrix3d

    init {
        asMatrix = Matrix3d(colA.toVector3d(), colB.toVector3d(), colC.toVector3d())
    }




}