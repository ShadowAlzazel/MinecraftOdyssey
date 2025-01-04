package me.shadowalzazel.mcodyssey

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.RegistryTagManager
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
    private const val RESOURCE_PACK_HASH = "9648192d7b73af98290a7f0ed43a0a661d455ffd"

    private fun resourcePackHandler(player: Player) {
        // PROD
        //https://www.dropbox.com/s/2sdfqy76ym39h44/odyssey-resource-pack.zip?dl=1
        val packString = "https://www.dropbox.com/s/2sdfqy76ym39h44/odyssey-resource-pack.zip?dl=1"
        // TEST
        //val packString = "https://www.dropbox.com/s/6hob8iad499vz95/odyssey-resource-pack-test.zip?dl=1"
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