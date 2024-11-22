package me.shadowalzazel.mcodyssey.common.items

import me.shadowalzazel.mcodyssey.common.items.OdysseyItem
import org.bukkit.block.Biome

enum class FishTypes(
    val biomes: List<Biome>,
    val times: IntRange,
    val rarity: Int,
    val item: OdysseyItem
) {


    //BLUEGILL(listOf(Biome.RIVER), 600..12000, 65, Foods.BLUEGILL),
   // RAINBOW_TROUT(listOf(Biome.RIVER), 600..12000, 65, Foods.RAINBOW_TROUT)


}