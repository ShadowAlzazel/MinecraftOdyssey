package me.shadowalzazel.mcodyssey.alchemy.recipes

import me.shadowalzazel.mcodyssey.alchemy.base.SoulBraiseRecipe
import me.shadowalzazel.mcodyssey.items.OdysseyItems
import me.shadowalzazel.mcodyssey.mobs.OdysseyMobs
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

// SOUL_CRYSTAL_RECIPE
object SoulCrystalRecipe: SoulBraiseRecipe(
    OdysseyItems.SOUL_CRYSTAL,
    null,
    listOf(ItemStack(Material.QUARTZ, 1)),
    setOf(Material.SCULK),
    setOf(Pair(1.0, 0.0), Pair(-1.0, 0.0), Pair(0.0, 1.0), Pair(0.0, -1.0))
)

// SOUL_CRYSTAL_RECIPE
object RottingSummonRecipe: SoulBraiseRecipe(
    null,
    OdysseyMobs.ROTTING_SUMMON,
    listOf(OdysseyItems.SORROWING_SOUL.createItemStack(1)),
    setOf(Material.SCULK),
    setOf(Pair(1.0, 0.0), Pair(-1.0, 0.0), Pair(0.0, 1.0), Pair(0.0, -1.0))
)

// SOUL_STEEL_INGOT_RECIPE
object SoulSteelIngotRecipe: SoulBraiseRecipe(
    OdysseyItems.SOUL_STEEL_INGOT,
    null,
    listOf(OdysseyItems.SOUL_CATALYST.createItemStack(1)),
    setOf(Material.RAW_IRON_BLOCK),
    setOf(Pair(1.0, 0.0), Pair(-1.0, 0.0), Pair(0.0, 1.0), Pair(0.0, -1.0))
)

// SCULK_HEART_RECIPE
object SculkHeartRecipe: SoulBraiseRecipe(
    OdysseyItems.SCULK_HEART,
    null,
    listOf(OdysseyItems.WARDEN_ENTRAILS.createItemStack(1)),
    setOf(Material.SCULK_CATALYST),
    setOf(Pair(1.0, 0.0), Pair(-1.0, 0.0), Pair(0.0, 1.0), Pair(0.0, -1.0))
)