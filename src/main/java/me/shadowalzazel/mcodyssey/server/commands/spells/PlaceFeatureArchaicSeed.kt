package me.shadowalzazel.mcodyssey.server.commands.spells

import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object PlaceFeatureArchaicSeed : CommandExecutor, DataTagManager{

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        if (sender.equipment.itemInMainHand.type != Material.WHEAT_SEEDS) return false
        val archaicSeed = sender.equipment.itemInMainHand
        if (!archaicSeed.hasItemMeta()) return false
        if (!archaicSeed.hasTag(ItemDataTags.IS_ARCHAIC_SEED)) return false
        // Plant

        println("RUN PLACE COMMAND")

        val seedNamespaces = listOf(
            "terralith:highlands/forest/trees_maple", // Forested Highlands
            "terralith:highlands/temperate/cloud_trees", // Temperate Highland
            "terralith:flower/lavender/main_tree", // Lavender Forest
            "terralith:flower/blue/birch_tree", // Moonlight Grove
            "odyssey:aspen_forest/tree_aspen", // Aspen Forest
            "odyssey:bayou/tree_tall", // Bayou
            "odyssey:pantanal/tree_blob" // Pantanal
        )

        // TEMP ON SNIFFER DIG SET NAMESPACE ON ITEM
        val seedNamespace = if (archaicSeed.hasTag(ItemDataTags.ARCHAIC_NAMESPACE)) {
            archaicSeed.getStringTag(ItemDataTags.ARCHAIC_NAMESPACE)
        } else {
            seedNamespaces.random()
        }
        var coordinates = "~ ~ ~"

        val block = sender.rayTraceBlocks(6.0)?.hitBlock
        if (block != null) {
            println("Block ${block.type} at ${block.location}")
            coordinates = "${block.x} ${block.y} ${block.z}"
            block.type = Material.AIR
        }

        with(sender.world) {
            spawnParticle(Particle.CRIT, sender.location, 30, 0.35, 0.5, 0.35)
        }
        sender.performCommand("place feature $seedNamespace $coordinates")

        return true
    }

}