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
import org.bukkit.inventory.ItemStack as BukkitStack

interface EnchantmentDataManager : EnchantmentFinder {

    fun createEnchantContainer(name: String): EnchantContainer {
        // org.bukkit.Registry.ENCHANTMENT.get(NamespacedKey(Odyssey.instance))
        val foundOdyssey = getOdysseyEnchantFromString(name)
        val foundBukkit = org.bukkit.Registry.ENCHANTMENT.get(NamespacedKey.minecraft(name))
        val container = if (foundBukkit != null) {
            EnchantContainer(bukkitEnchant = foundBukkit)
        } else {
            EnchantContainer(odysseyEnchant = foundOdyssey)
        }
        return container
    }

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
    fun getEnchantmentList(enchantmentTag: Tag): List<OdysseyEnchantment>? {
        val tagList = enchantmentTag as ListTag
        //println("tag type: ${tagList.type}")
        val enchantList: MutableList<OdysseyEnchantment> = mutableListOf()
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
            enchantList.add(odysseyEnchant)
        }
        // Return null if empty
        return if (enchantList.isEmpty()) {
            null
        } else {
            enchantList
        }
    }

    fun BukkitStack.getOdysseyEnchantments(): List<OdysseyEnchantment> {
        val itemAsNms = CraftItemStack.asNMSCopy(this)
        val enchantContainer = getEnchantmentTag(itemAsNms) ?: return emptyList()
        val enchantList = getEnchantmentList(enchantContainer) ?: return emptyList()
        return enchantList
    }

}