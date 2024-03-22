package me.shadowalzazel.mcodyssey.enchantments

// Code Provided by Pluggg

// ReflectionUtils
import net.minecraft.core.Holder
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import java.lang.reflect.Field

// Gracefully borrowed from Coll1234567
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
}

