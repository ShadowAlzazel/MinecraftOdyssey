package me.shadowalzazel.mcodyssey.enigmaticSorcery.recipes

import me.shadowalzazel.mcodyssey.enigmaticSorcery.utility.OdysseyEnigmaticRecipe
import me.shadowalzazel.mcodyssey.items.OdysseyItems
import me.shadowalzazel.mcodyssey.mobs.OdysseyMobs
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object SoulCrystalRecipe: OdysseyEnigmaticRecipe(
    OdysseyItems.SOUL_CRYSTAL,
    null,
    listOf(ItemStack(Material.QUARTZ, 1)),
    setOf(Material.SCULK),
    setOf(Pair(1.0, 0.0), Pair(-1.0, 0.0), Pair(0.0, 1.0), Pair(0.0, -1.0))
)

object RottingSummonRecipe: OdysseyEnigmaticRecipe(
    null,
    OdysseyMobs.ROTTING_SUMMON,
    listOf(OdysseyItems.SORROWING_SOUL.createItemStack(1)),
    setOf(Material.SCULK),
    setOf(Pair(1.0, 0.0), Pair(-1.0, 0.0), Pair(0.0, 1.0), Pair(0.0, -1.0))
)

object SoulSteelIngotRecipe: OdysseyEnigmaticRecipe(
    OdysseyItems.SOUL_STEEL_INGOT,
    null,
    listOf(OdysseyItems.SOUL_CATALYST.createItemStack(1)),
    setOf(Material.IRON_BLOCK),
    setOf(Pair(1.0, 0.0), Pair(-1.0, 0.0), Pair(0.0, 1.0), Pair(0.0, -1.0))
)