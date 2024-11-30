

package me.shadowalzazel.mcodyssey.util

import io.papermc.paper.datacomponent.item.Tool
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.keys.tags.BlockTypeTagKeys
import io.papermc.paper.registry.tag.TagKey
import net.kyori.adventure.util.TriState
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import io.papermc.paper.registry.tag.Tag
import org.bukkit.block.BlockType

@Suppress("UnstableApiUsage")
interface ToolComponentHelper : RegistryTagManager {


    fun getMiningTags(toolType: String): Tag<BlockType>? {
        return when(toolType) {
            "pickaxe", "warhammer" -> Registry.BLOCK.getTag(BlockTypeTagKeys.MINEABLE_PICKAXE)
            "axe", "longaxe", "poleaxe", "glaive" -> Registry.BLOCK.getTag(BlockTypeTagKeys.MINEABLE_PICKAXE)
            "shovel", "spear", "halberd", "lance" -> Registry.BLOCK.getTag(BlockTypeTagKeys.MINEABLE_PICKAXE)
            "hoe", "scythe" -> Registry.BLOCK.getTag(BlockTypeTagKeys.MINEABLE_PICKAXE)
            else -> null
        }
    }

    fun newToolComponent(toolMaterial: String, toolType: String): Tool? {
        val builder = Tool.tool()
        val speed = when (toolMaterial) {
            "copper" -> 4.5F
            "iridium" -> 8.5F
            "titanium", "anodized_titanium" -> 9.0F
            "mithril" -> 10.0F
            else -> null
        }
        if (speed != null) {
            //val oldTag = getTagFromRegistry(RegistryKey.BLOCK, "mineable/axe", "minecraft")
            val tag = getMiningTags(toolType) ?: return null
            val newRule = Tool.rule(tag, speed, TriState.TRUE)
            builder.addRule(newRule)
            builder.damagePerBlock(1)
            return builder.build()
        }
        else return null
    }


}