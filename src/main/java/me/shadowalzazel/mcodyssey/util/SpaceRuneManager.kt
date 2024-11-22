package me.shadowalzazel.mcodyssey.util

import me.shadowalzazel.mcodyssey.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.common.items.SpaceRuneMatrix
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import org.joml.Vector3d

interface SpaceRuneManager : DataTagManager {

    fun teleportFromSpaceRuneMatrix(entity: Entity, spaceRune: SpaceRuneMatrix) {
        // MATH
        val runeMatrix = spaceRune.asMatrix
        println(runeMatrix)
        val current3dVector = entity.location.toVector().toVector3d()
        val new3dVector = Vector3d()
        current3dVector.mul(runeMatrix, new3dVector)
        // Convert to Bukkit
        val newPos = Vector.fromJOML(new3dVector).toLocation(entity.world)
        entity.teleport(newPos)
    }

    // Switch to check
    fun spaceRuneTabletTeleport(entity: Entity, item: ItemStack): Boolean {
        if (!item.hasItemMeta()) return false
        if (!item.hasTag(ItemDataTags.IS_SPACERUNE)) return false
        val spaceRuneMatrix = item.getSpaceRuneMatrix() ?: return false
        // Call teleport
        teleportFromSpaceRuneMatrix(entity, spaceRuneMatrix)
        return true
    }

    fun ItemStack.createSpaceRuneComponents() {
        // m 'column' 'row'
        apply {
            setIntTag("m00", 0)
            setIntTag("m01", 0)
            setIntTag("m02", 0)
            setIntTag("m10", 0)
            setIntTag("m11", 0)
            setIntTag("m12", 0)
            setIntTag("m20", 0)
            setIntTag("m21", 0)
            setIntTag("m22", 0)
        }
        // Make sure can make spaceRuneMatrix
    }

    fun ItemStack.getSpaceRuneMatrix(): SpaceRuneMatrix? {
        // Make sure all values of matrix are available
        val m00 = getIntTag("m00") ?: return null
        val m01 = getIntTag("m01") ?: return null
        val m02 = getIntTag("m02") ?: return null
        val m10 = getIntTag("m10") ?: return null
        val m11 = getIntTag("m11") ?: return null
        val m12 = getIntTag("m12") ?: return null
        val m20 = getIntTag("m20") ?: return null
        val m21 = getIntTag("m21") ?: return null
        val m22 = getIntTag("m22") ?: return null
        // Make Vectors
        val c0 = Vector(m00, m01, m02)
        val c1 = Vector(m10, m11, m12)
        val c2 = Vector(m20, m21, m22)
        // Create Matrix
        val spaceRuneMatrix = SpaceRuneMatrix(c0, c1, c2)
        return spaceRuneMatrix
    }

    fun ItemStack.createSpaceRuneMatrixLore() {
        val matrix = getSpaceRuneMatrix()?.asMatrix ?: return
        val grayColor = TextColor.color(170, 170, 170)
        // Get strings
        val r0 = "[${matrix.m00.toInt()} ${matrix.m10.toInt()} ${matrix.m20.toInt()}]"
        val r1 = "[${matrix.m01.toInt()} ${matrix.m11.toInt()} ${matrix.m21.toInt()}]"
        val r2 = "[${matrix.m02.toInt()} ${matrix.m12.toInt()} ${matrix.m22.toInt()}]"
        val newLore = listOf(
            Component.text(r0, grayColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text(r1, grayColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            Component.text(r2, grayColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
        )
        lore(newLore)
    }


    // Crafting table grid
    // used to make tablets/scrolls of TP matrix transformations
    // Can rename in anvil to get positions?
    // can use elementary row operations using crafting table when placed in slot (if
    // in top left, add to top left value)

    // SPACE RUNE FRAGMENT
    // Can store 1 int by renaming in anvil (cost more levels)
    // made up of endstone, echoshard,

    // SPACE RUNE SCROLL
    // made up of 3 fragments, tps you to that location as it is just a vector but is 1 time use

    // SPACE RUNE TABLET
    // A linear transformation
    // maybe can put in lodestone block for the end

    // Maybe make the edge (i) perpendicular to overworld or use it to get from edge to end to overworld
}