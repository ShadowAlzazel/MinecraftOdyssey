package me.shadowalzazel.mcodyssey.enchantments.api

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.enchantments.util.EnchantContainer
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack as NmsStack
import net.minecraft.world.item.component.CustomData
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack as BukkitStack

interface EnchantmentDataManager : EnchantmentFinder {

    // Containers
    fun createEnchantContainer(name: String): EnchantContainer? {
        // org.bukkit.Registry.ENCHANTMENT.get(NamespacedKey(Odyssey.instance))
        val foundOdyssey = getOdysseyEnchantFromString(name)
        val foundBukkit = org.bukkit.Registry.ENCHANTMENT.get(NamespacedKey.minecraft(name))
        val container = if (foundBukkit != null) {
            createEnchantContainer(foundBukkit)
        } else if (foundOdyssey != null){
            createEnchantContainer(foundOdyssey)
        }
        else {
            null
        }
        return container
    }

    fun createEnchantContainer(bukkitEnchant: Enchantment): EnchantContainer {
        return EnchantContainer(bukkitEnchant, null)
    }

    fun createEnchantContainer(odysseyEnchantment: OdysseyEnchantment): EnchantContainer {
        return EnchantContainer(null, odysseyEnchantment)
    }

    fun createEnchantContainerList(enchantments: List<Enchantment>): List<EnchantContainer> {
        val containerList = mutableListOf<EnchantContainer>()
        for (x in enchantments) {
            containerList.add(createEnchantContainer(x))
        }
        return containerList.toList()
    }

    fun createEnchantContainerList(enchantments: List<OdysseyEnchantment>): List<EnchantContainer> {
        val containerList = mutableListOf<EnchantContainer>()
        for (x in enchantments) {
            containerList.add(createEnchantContainer(x))
        }
        return containerList.toList()
    }

    fun createEnchantContainerMap(enchantments: Map<Enchantment, Int>): Map<EnchantContainer, Int> {
        val containerMap = mutableMapOf<EnchantContainer, Int>()
        for (x in enchantments) {
            containerMap[createEnchantContainer(x.key)] = x.value
        }
        return containerMap
    }

    fun createEnchantContainerMap(enchantments: Map<OdysseyEnchantment, Int>): Map<EnchantContainer, Int> {
        val containerMap = mutableMapOf<EnchantContainer, Int>()
        for (x in enchantments) {
            containerMap[createEnchantContainer(x.key)] = x.value
        }
        return containerMap
    }

    // Tags
    fun getEnchantmentTag(nmsStack: NmsStack): Tag? {
        val components = nmsStack.components
        val customDataKey = ResourceLocation("minecraft", "custom_data")
        val dataType = BuiltInRegistries.DATA_COMPONENT_TYPE.get(customDataKey)!!
        val componentData = components.get(dataType)!!
        if (componentData !is CustomData) return null
        val customData = componentData as CustomData
        val dataTag = customData.copyTag()
        val enchantTag = dataTag.get("odyssey:enchantments")
        //println("Data Tag: $dataTag")
        //println("Odyssey Enchantments: $enchantContainer")
        return enchantTag
    }

    // Returns a list of odyssey enchantments or null if empty
    fun getEnchantmentMap(enchantmentTag: Tag): Map<OdysseyEnchantment, Int>? {
        val tagList = enchantmentTag as ListTag
        //println("tag type: ${tagList.type}")
        val enchantMap: MutableMap<OdysseyEnchantment, Int> = mutableMapOf()
        for (enchantTag in tagList) {
            if (enchantTag !is CompoundTag) continue
            val enchantName = enchantTag.tags.keys.first()
            val level = enchantTag[enchantName] ?: continue
            if (level !is IntTag) continue
            //println("Tag: $enchantTag")
            //println("Name: $enchantName, Level: $level")
            println("As String: $enchantName lvl: $level")
            val shortName = enchantName.removeRange(0,8) //odyssey:
            val odysseyEnchant = getOdysseyEnchantFromString(shortName) ?: continue
            println("Found Enchantment: $odysseyEnchant")
            enchantMap[odysseyEnchant] = level.asInt
        }
        // Return null if empty
        return if (enchantMap.isEmpty()) {
            null
        } else {
            enchantMap
        }
    }

    fun BukkitStack.getOdysseyEnchantments(): Map<OdysseyEnchantment, Int> {
        val itemAsNms = CraftItemStack.asNMSCopy(this)
        val enchantTag = getEnchantmentTag(itemAsNms) ?: return emptyMap()
        val enchantMap = getEnchantmentMap(enchantTag) ?: return emptyMap()
        return enchantMap
    }

}