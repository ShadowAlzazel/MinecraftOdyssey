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

@Suppress("UnstableApiUsage")
object OdysseyAssets : Listener, RegistryTagManager, DataTagManager {

    private const val ODYSSEY_RESOURCE_PACK_HASH = "fabcf0b092baf83f4b3a170645ab125331d2c082"
    private const val ODYSSEY_RESOURCE_PACK_LINK = "https://www.dropbox.com/scl/fi/aiclr8b58ew5bzeb36w76/odyssey-resource-pack-v26.2.zip?rlkey=5fe7yn32f49nwuyowhs8k263n&st=zehi4f01&dl=1"

    // For the Vailcraft Server
    private const val VAILCRAFT_RESOURCE_PACK_HASH = "2368f44985eb5c93eb8a33a7fdaebbee9d27c448"
    private const val VAILCRAFT_RESOURCE_PACK_LINK = "https://www.dropbox.com/scl/fi/lyjdqmgpgpya84brop36z/vailcraft-resourece-pack-v26.2.zip?rlkey=qndn6b1hl2sfzyuevpgvcgrvq&st=lgx0om56&dl=1"

    private fun resourcePackHandler(
        player: Player
    ) {
        // Odyssey Resource Pack
        val odysseyPrompt = Component.text("Please allow Odyssey to download a server side resource pack!", TextColor.color(255, 170, 0))
        val odysseyPackURI = URI.create(ODYSSEY_RESOURCE_PACK_LINK)
        val odysseyPackInfoBuilder = ResourcePackInfo.resourcePackInfo().apply {
            uri(odysseyPackURI)
            hash(ODYSSEY_RESOURCE_PACK_HASH)
        }
        // Vailcraft Resource Pack
        val vailcraftPackURI = URI.create(VAILCRAFT_RESOURCE_PACK_LINK)
        val vailcraftPackInfoBuilder = ResourcePackInfo.resourcePackInfo().apply {
            uri(vailcraftPackURI)
            hash(VAILCRAFT_RESOURCE_PACK_HASH)
        }

        // Request to add resource packs
        val requestBuilder = ResourcePackRequest.resourcePackRequest().apply {
            packs(odysseyPackInfoBuilder, vailcraftPackInfoBuilder)
            prompt(odysseyPrompt)
            replace(false)
            required(true)
        }

        player.sendResourcePacks(requestBuilder)
    }


    // Prompt player with resource pack
    @EventHandler
    fun playerJoinHandler(event: PlayerJoinEvent) {
        resourcePackHandler(event.player)
    }

    @EventHandler
    fun playerLeaveHandler(event: PlayerQuitEvent) {
    }

}