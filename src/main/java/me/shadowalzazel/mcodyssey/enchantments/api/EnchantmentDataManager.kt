package me.shadowalzazel.mcodyssey.enchantments.api

import me.shadowalzazel.mcodyssey.enchantments.OdysseyEnchantment
import me.shadowalzazel.mcodyssey.enchantments.util.EnchantContainer
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentType
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
        val customComponent = components.get(dataType)!!
        if (customComponent !is CustomData) return null
        val customTag = customComponent.copyTag()
        val enchantmentsTag = customTag.get("odyssey:enchantments")
        //println("Data Tag: $dataTag")
        //println("Odyssey Enchantments: $enchantContainer")
        if (enchantmentsTag !is ListTag) return null
        return enchantmentsTag // ListTag
    }

    // Returns a list of odyssey enchantments or null if empty
    fun getEnchantmentMap(enchantmentsTag: Tag): Map<OdysseyEnchantment, Int>? {
        if (enchantmentsTag !is ListTag) return null
        val enchantMap: MutableMap<OdysseyEnchantment, Int> = mutableMapOf()
        for (enchantTag in enchantmentsTag) {
            if (enchantTag !is CompoundTag) continue
            val enchantName = enchantTag.tags.keys.first()
            val level = enchantTag[enchantName] ?: continue
            if (level !is IntTag) continue
            //println("As String: $enchantName lvl: $level")
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

    fun createEnchantmentDataTag(nmsStack: NmsStack) {
        val itemCopy = nmsStack.copy()
        val components = nmsStack.components
        val customDataKey = ResourceLocation("minecraft", "custom_data")
        val dataType = BuiltInRegistries.DATA_COMPONENT_TYPE.get(customDataKey) as DataComponentType<CustomData>
        val newOdysseyEnchantTag = CompoundTag().put("odyssey:enchantments", ListTag())!!
        //CustomData.update(dataType, itemCopy, newOdysseyEnchantTag)

    }

    fun getOrCreateEnchantmentMap(bukkitStack: BukkitStack) {
        val itemAsNms = CraftItemStack.asNMSCopy(bukkitStack)
        val enchantTag = getEnchantmentTag(itemAsNms)
    }

    fun BukkitStack.addOdysseyEnchantment(enchant: OdysseyEnchantment) {
        //val enchantMap
    }

    fun BukkitStack.getOdysseyEnchantments(): Map<OdysseyEnchantment, Int> {
        val itemAsNms = CraftItemStack.asNMSCopy(this)
        val enchantTag = getEnchantmentTag(itemAsNms) ?: return emptyMap()
        val enchantMap = getEnchantmentMap(enchantTag) ?: return emptyMap()
        return enchantMap
    }

}