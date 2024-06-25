

package me.shadowalzazel.mcodyssey.items.utility

import org.bukkit.Material
import org.bukkit.Tag
import org.bukkit.inventory.meta.components.ToolComponent

@Suppress("UnstableApiUsage")
interface ToolMiningManager {

    fun getTypeMineableTags(toolName: String): Tag<Material>? {
        return when(toolName) {
            "pickaxe", "warhammer" -> Tag.MINEABLE_PICKAXE
            "axe", "longaxe", "poleaxe" -> Tag.MINEABLE_AXE
            "shovel", "spear", "halberd", "lance" -> Tag.MINEABLE_SHOVEL
            "hoe", "scythe" -> Tag.MINEABLE_HOE
            else -> null
        }
    }

   fun createMiningToolComponent(base: ToolComponent, toolMaterial: String, miningTags: Tag<Material>): ToolComponent? {
        val speed = when(toolMaterial) {
            "iridium" -> {
                8.5F
            }
            "titanium", "anodized_titanium" -> {
                9.0F
            }
            else -> {
                null
            }
        }
        if (speed != null) {
            base.addRule(miningTags, speed, true)
            return base
        }
        else {
            return null
        }
    }

}