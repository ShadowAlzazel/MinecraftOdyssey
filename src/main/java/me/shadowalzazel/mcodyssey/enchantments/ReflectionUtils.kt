package me.shadowalzazel.mcodyssey.enchantments

// Code Provided by Pluggg

// ReflectionUtils
import net.minecraft.core.Holder
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import java.lang.reflect.Field

// Gracefully borrowed from Coll1234567

@Deprecated(message = "No Longer Using Registry")
object ReflectionUtils {
    private fun getField(clazz: Class<*>, type: Class<*>, index: Int): Field? {
        var i = 0
        for (field in clazz.declaredFields) {
            if (field.type == type) {
                if (i == index) {
                    field.isAccessible = true
                    return field
                }
                i++
            }
        }
        return null
    }

    fun <T> unfreezeRegistry(registry: Registry<T>) {
        val intrusiveHolderCache = getField(MappedRegistry::class.java, Map::class.java, 5) ?: return
        intrusiveHolderCache.set(registry, mutableMapOf<T, Holder.Reference<T>>())

        val frozen = getField(MappedRegistry::class.java, Boolean::class.java, 0) ?: return
        frozen.setBoolean(registry, false)
    }

    fun <T> freezeRegistry(registry: Registry<T>) {
        registry.freeze()
    }

    // Old Register
    /*
    fun registerAll() {
        for (odysseyEnchant in REGISTERED_SET) {
            var registered = false
            try {
                net.minecraft.core.Registry.register( // Using own namespace for safety and support
                    BuiltInRegistries.ENCHANTMENT,
                    ResourceLocation("odyssey", odysseyEnchant.name),
                    //odysseyEnchant.name,
                    odysseyEnchant
                )
                registered = true
            }
            catch (exception: Exception) {
                exception.printStackTrace()
            }
            // SUCCESS!!
            if (registered) {
                Odyssey.instance.logger.info("Registered: $odysseyEnchant")
            }
        }
    }

    fun registerTest() {
        val testEnchantment = net.minecraft.world.item.enchantment.Enchantment(
            net.minecraft.world.item.enchantment.Enchantment.definition(
                ItemTags.WEAPON_ENCHANTABLE,
                ItemTags.WEAPON_ENCHANTABLE,
                5, // Weight
                3,
                net.minecraft.world.item.enchantment.Enchantment.constantCost(10),
                net.minecraft.world.item.enchantment.Enchantment.constantCost(15),
                3,
                EquipmentSlot.MAINHAND // Array to varargs
            )
        )
        try {
            net.minecraft.core.Registry.register( // Using own namespace for safety and support
                BuiltInRegistries.ENCHANTMENT,
                "test_enchant",
                testEnchantment
            )
        }
        catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

     */


}

