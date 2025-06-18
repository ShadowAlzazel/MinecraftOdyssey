package me.shadowalzazel.mcodyssey

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.api.RegistryTagManager
import net.kyori.adventure.resource.ResourcePackInfo
import net.kyori.adventure.resource.ResourcePackRequest
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import java.net.URI

// -------------------- READ THIS --------------------------
// Put hash into resource-pack-sha1 WITHIN server.properties

@Suppress("UnstableApiUsage")
object OdysseyAssets : Listener, RegistryTagManager, DataTagManager {

    // Put hash into resource-pack-sha1 WITHIN server.properties
    private const val RESOURCE_PACK_HASH = "990e197c33f1e0e64f8f2f3a7680df58285a9889"

    private fun resourcePackHandler(player: Player) {
        // ----------------------------------------------------------------------------------------
        val packString = "https://www.dropbox.com/scl/fi/jo1myyt0fih0qk13qoh6u/OdysseyResourcePack-1.21.6.zip?dl=1"

        // Request to add resource pack
        val text = Component.text("Please allow Odyssey to download a server side resource pack!", TextColor.color(255, 170, 0))
        val packURI = URI.create(packString)
        val packInfoBuilder = ResourcePackInfo.resourcePackInfo().apply {
            uri(packURI)
            hash(RESOURCE_PACK_HASH)
        }

        val requestBuilder = ResourcePackRequest.resourcePackRequest().apply {
            packs(packInfoBuilder)
            prompt(text)
            replace(false)
            required(true)
        }

        player.sendResourcePacks(requestBuilder)
    }

    private fun ItemStack.updateItemModel() {
        val oldModel = this.getData(DataComponentTypes.ITEM_MODEL)
        val itemId = this.getItemIdentifier() ?: return
        // Set
        if (oldModel == null || oldModel.key().namespace() == "minecraft") {
            this.resetData(DataComponentTypes.CUSTOM_MODEL_DATA) // Reset old override data
        }
        val newModel = createOdysseyKey(itemId)
        this.setData(DataComponentTypes.ITEM_MODEL, newModel)
    }

    // Prompt player with resource pack
    @EventHandler
    fun playerJoinHandler(event: PlayerJoinEvent) {
        resourcePackHandler(event.player)
        // Update on join old items
        event.player.inventory.contents.forEach { it?.updateItemModel() }

    }

    @EventHandler
    fun playerLeaveHandler(event: PlayerQuitEvent) {
    }

}