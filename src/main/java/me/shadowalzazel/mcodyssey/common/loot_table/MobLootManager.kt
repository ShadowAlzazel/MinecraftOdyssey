package me.shadowalzazel.mcodyssey.common.loot_table

import me.shadowalzazel.mcodyssey.common.enchantments.OdysseyEnchantments
import me.shadowalzazel.mcodyssey.api.RegistryTagManager
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity

interface MobLootManager : RegistryTagManager {


    fun getMobLootableEnchantments(mob: LivingEntity): List<Enchantment>? {
        return when(mob.type) {
            EntityType.SKELETON -> OdysseyEnchantments.rangedSet.toList()
            EntityType.ZOMBIE -> OdysseyEnchantments.meleeSet.toList()
            EntityType.SPIDER -> OdysseyEnchantments.armorSet.toList()
            EntityType.HUSK -> listOf(OdysseyEnchantments.CULL_THE_WEAK)
            EntityType.DROWNED -> listOf(OdysseyEnchantments.BANE_OF_THE_SEA)
            EntityType.PIGLIN_BRUTE -> listOf(OdysseyEnchantments.BANE_OF_THE_SWINE)
            EntityType.WITCH -> listOf(OdysseyEnchantments.ALCHEMY_ARTILLERY)
            EntityType.STRAY -> listOf(OdysseyEnchantments.FREEZING_ASPECT)
            EntityType.CREEPER -> listOf(OdysseyEnchantments.EXPLODING)
            EntityType.HOGLIN -> listOf(OdysseyEnchantments.HEMORRHAGE)
            EntityType.VINDICATOR -> listOf(OdysseyEnchantments.BANE_OF_THE_ILLAGER)
            EntityType.PILLAGER -> listOf(OdysseyEnchantments.BURST_BARRAGE)
            EntityType.RAVAGER -> listOf(OdysseyEnchantments.WHIRLWIND)
            EntityType.SQUID -> listOf(OdysseyEnchantments.SQUIDIFY)
            //EntityType.SHULKER -> listOf(OdysseyEnchantments.)
            //EntityType.ENDERMITE -> listOf()
            EntityType.ENDERMAN -> listOf(OdysseyEnchantments.VOID_STRIKE)
            EntityType.VEX -> listOf(OdysseyEnchantments.INVOCATIVE)
            EntityType.WARDEN -> listOf(OdysseyEnchantments.SCULK_SENSITIVE)
            EntityType.WITHER -> listOf(OdysseyEnchantments.STYX_ROSE)
            else -> null
        }
    }


}