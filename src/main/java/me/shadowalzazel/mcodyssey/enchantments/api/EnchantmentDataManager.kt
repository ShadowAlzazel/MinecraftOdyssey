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

    fun createBukkitEnchantContainerList(bukkitEnchants: List<Enchantment>): List<EnchantContainer> {
        val containerList = mutableListOf<EnchantContainer>()
        for (x in bukkitEnchants) {
            containerList.add(createEnchantContainer(x))
        }
        return containerList.toList()
    }

    fun createOdysseyEnchantContainerList(odysseyEnchants: List<OdysseyEnchantment>): List<EnchantContainer> {
        val containerList = mutableListOf<EnchantContainer>()
        for (x in odysseyEnchants) {
            containerList.add(createEnchantContainer(x))
        }
        return containerList.toList()
    }

    fun createBukkitEnchantContainerMap(bukkitEnchants: Map<Enchantment, Int>): Map<EnchantContainer, Int> {
        val containerMap = mutableMapOf<EnchantContainer, Int>()
        for (x in bukkitEnchants) {
            containerMap[createEnchantContainer(x.key)] = x.value
        }
        return containerMap
    }

    fun createOdysseyEnchantContainerMap(odysseyEnchants: Map<OdysseyEnchantment, Int>): Map<EnchantContainer, Int> {
        val containerMap = mutableMapOf<EnchantContainer, Int>()
        for (x in odysseyEnchants) {
            containerMap[createEnchantContainer(x.key)] = x.value
        }
        return containerMap
    }

    fun BukkitStack.hasOdysseyEnchants(): Boolean {
        return getOdysseyEnchantments().isNotEmpty()
    }

    fun BukkitStack.hasOdysseyEnchantment(enchant: OdysseyEnchantment): Boolean {
        return getOdysseyEnchantments().contains(enchant)
    }

    fun BukkitStack.removeOdysseyEnchantment(enchant: OdysseyEnchantment) {
        val enchantMap = getOdysseyEnchantments().toMutableMap()
        val contains = enchantMap.keys.contains(enchant)
        if (!contains) return
        this.updateEnchantmentsNBT(enchantMap)
    }

    // Tries to get an enchantment Map if not
    fun BukkitStack.getOdysseyEnchantments(): Map<OdysseyEnchantment, Int> {
        val itemAsNms = CraftItemStack.asNMSCopy(this)
        val enchantmentsTag = getEnchantmentsDataTag(itemAsNms) ?: return emptyMap()
        val enchantMap = getEnchantmentsMap(enchantmentsTag) ?: return emptyMap()
        return enchantMap
    }

    // This method sets and odyssey enchantment/overriding existing enchant
    fun BukkitStack.setOdysseyEnchantment(enchant: OdysseyEnchantment, level: Int, pastMax: Boolean = false) {
        val enchantMap = getOdysseyEnchantments().toMutableMap()
        val checkedLevel = if (pastMax && enchant.maximumLevel <= level) level else enchant.maximumLevel
        enchantMap[enchant] = checkedLevel
        this.updateEnchantmentsNBT(enchantMap)
        // Change Glint
        val newMeta = itemMeta
        newMeta.setEnchantmentGlintOverride(true)
        itemMeta = newMeta
    }

    // This method is to add an enchantment via survival like anvil
    fun BukkitStack.addOdysseyEnchantment(enchant: OdysseyEnchantment, level: Int, pastMax: Boolean = false) {
        val enchantMap = getOdysseyEnchantments().toMutableMap()
        val hasMatching = enchantMap.keys.contains(enchant)
        if (hasMatching) {
            val oldLevel = enchantMap[enchant]!!
            val combinedLevel = if (oldLevel == level) { maxOf(level + 1, enchant.maximumLevel) } else { maxOf(level, oldLevel) }
            enchantMap[enchant] = combinedLevel
        } else {
            enchantMap[enchant] = level
        }
        this.updateEnchantmentsNBT(enchantMap)
        // Change Glint
        val newMeta = itemMeta
        newMeta.setEnchantmentGlintOverride(true)
        itemMeta = newMeta
    }

    // -----------------------------------------------------
    // Internal Methods DO NOT USE outside

    // Primary method to update item NBT tag pertaining to enchantments
    private fun BukkitStack.updateEnchantmentsNBT(newEnchants: Map<OdysseyEnchantment, Int>) {
        this.overrideEnchantmentsTag(newEnchantmentsDataTag(newEnchants))
    }

    // Overrides/Updates the entire "odyssey:enchantments" tag with a new list
    private fun BukkitStack.overrideEnchantmentsTag(enchantmentsTag: ListTag) {
        val nmsStack = CraftItemStack.asNMSCopy(this)
        // Get custom_data component
        val customDataKey = ResourceLocation("minecraft", "custom_data")
        val dataType = BuiltInRegistries.DATA_COMPONENT_TYPE.get(customDataKey) ?: return // DataComponentType<CustomData>
        // Create new compound tag
        val updatedTag = CompoundTag()
        updatedTag.put("odyssey:enchantments", enchantmentsTag)
        // Create new custom_data
        val updatedData = CustomData.of(updatedTag)
        // Build and apply new custom_data
        val builder = DataComponentMap.builder()
        builder.set(dataType as DataComponentType<CustomData>, updatedData)
        nmsStack.applyComponents(builder.build())
        // set item meta
        this.itemMeta = CraftItemStack.asBukkitCopy(nmsStack).itemMeta
    }

    // Create a new "odyssey:enchantments" listTag
    private fun createEnchantmentsDataTag(nmsStack: NmsStack) {
        // Create new tag
        val newOdysseyEnchantTag = CompoundTag()
        newOdysseyEnchantTag.put("odyssey:enchantments", ListTag())
        // Get custom_data component
        val customDataKey = ResourceLocation("minecraft", "custom_data")
        val dataType = BuiltInRegistries.DATA_COMPONENT_TYPE.get(customDataKey) ?: return // DataComponentType<CustomData>
        // Builder
        val newCustomData = CustomData.of(newOdysseyEnchantTag)
        val builder = DataComponentMap.builder()
        builder.set(dataType as DataComponentType<CustomData>, newCustomData)
        val customDataMap = builder.build()
        nmsStack.applyComponents(customDataMap)
        // Passing in function called consumer which takes in custom_data component
        // val tagConsumer = Consumer<CompoundTag> {
        //    it.put("odyssey:enchantments", ListTag())!!
        //}
    }

    // New Enchantment NBT Data Tag from map of enchantments
    private fun newEnchantmentsDataTag(enchantments: Map<OdysseyEnchantment, Int>): ListTag {
        // Create new tag list
        val enchantmentTag = ListTag()
        // Iterate and create new enchantTag
        for (enchant in enchantments) {
            val newEnchantTag = CompoundTag()
            val namesKey = "odyssey:${enchant.key.name}"
            newEnchantTag.putInt(namesKey, enchant.value)
            enchantmentTag.add(newEnchantTag)
        }
        return enchantmentTag
    }

    // Get enchant tags under "odyssey:enchantments"
    private fun getEnchantmentsDataTag(nmsStack: NmsStack): ListTag? {
        // Get custom_data component
        val itemComponents = nmsStack.components
        val customDataKey = ResourceLocation("minecraft", "custom_data")
        val dataType = BuiltInRegistries.DATA_COMPONENT_TYPE.get(customDataKey) ?: return null
        val customDataComponent = itemComponents.get(dataType) ?: return null
        if (customDataComponent !is CustomData) return null
        val customDataTag = customDataComponent.copyTag()
        // Get enchantment root tag
        val enchantmentsTag = customDataTag.get("odyssey:enchantments")
        if (enchantmentsTag !is ListTag) return null
        return enchantmentsTag
    }

    // Returns a list of odyssey enchantments or null if empty
    private fun getEnchantmentsMap(enchantmentsTag: Tag): Map<OdysseyEnchantment, Int>? {
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
            //println("Found Enchantment: $odysseyEnchant")
            enchantMap[odysseyEnchant] = level.asInt
        }
        // Return null if empty
        return if (enchantMap.isEmpty()) {
            null
        } else {
            enchantMap
        }
    }

    private fun getOrCreateEnchantmentsTag(bukkitStack: BukkitStack): ListTag {
        val itemAsNms = CraftItemStack.asNMSCopy(bukkitStack)
        var enchantmentsTag = getEnchantmentsDataTag(itemAsNms)
        if (enchantmentsTag == null) {
            createEnchantmentsDataTag(itemAsNms)
            enchantmentsTag = getEnchantmentsDataTag(itemAsNms)!!
            bukkitStack.itemMeta = CraftItemStack.asBukkitCopy(itemAsNms).itemMeta
        }
        return enchantmentsTag
    }
    // -----------------------------------------------------
}