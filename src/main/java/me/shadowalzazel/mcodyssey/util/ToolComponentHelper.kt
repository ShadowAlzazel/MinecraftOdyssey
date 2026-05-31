

package me.shadowalzazel.mcodyssey.util

import io.papermc.paper.datacomponent.item.Tool
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.tag.Tag
import io.papermc.paper.registry.tag.TagKey
import me.shadowalzazel.mcodyssey.api.RegistryTagManager
import net.kyori.adventure.util.TriState
import org.bukkit.block.BlockType


@Suppress("UnstableApiUsage")
interface ToolComponentHelper : RegistryTagManager {


    fun getMiningTags(toolType: String): Tag<BlockType>? {
        val registryKey = RegistryKey.BLOCK
        /*
        val blockTagRegistry = RegistryAccess.registryAccess().getRegistry(registryKey)
        val tagKey = TagKey.create(registryKey, createMinecraftKey("mineable/pickaxe"))
        val pickAxeMineableTag = blockTagRegistry.getTag(tagKey)
        "warhammer" -> pickAxeMineableTag
         */

        return when(toolType) {
            "pickaxe", "warhammer" -> getTagFromMinecraftRegistry(registryKey, "mineable/pickaxe")
            "axe", "longaxe", -> getTagFromMinecraftRegistry(registryKey, "mineable/axe")
            "shovel" -> getTagFromMinecraftRegistry(registryKey, "mineable/shovel")
            "hoe", "scythe" -> getTagFromMinecraftRegistry(registryKey, "mineable/hoe")
            else -> null
        }
    }

    fun newToolComponent(toolMaterial: String, toolType: String): Tool? {
        val builder = Tool.tool()
        val speed = when (toolMaterial) {
            "iridium" -> 8.0F
            "titanium", "anodized_titanium" -> 9.0F
            "silver" -> 10.0F
            "mithril", "crystal_alloy" -> 13.0F
            else -> null
        }
        if (speed != null) {
            //val oldTag = getTagFromRegistry(RegistryKey.BLOCK, "mineable/axe", "minecraft")
            val tag = getMiningTags(toolType) ?: return null
            val newRule = Tool.rule(
                tag,
                speed,
                TriState.TRUE)
            builder.addRule(newRule)
            builder.damagePerBlock(1)
            return builder.build()
        }
        else return null
    }


}