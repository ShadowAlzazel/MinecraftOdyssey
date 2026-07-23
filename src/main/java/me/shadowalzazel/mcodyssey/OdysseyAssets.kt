package me.shadowalzazel.mcodyssey

import me.shadowalzazel.mcodyssey.api.RegistryTagManager
import net.kyori.adventure.resource.ResourcePackInfo
import net.kyori.adventure.resource.ResourcePackRequest
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import java.net.URI


object OdysseyAssets : RegistryTagManager {

    private const val ODYSSEY_RESOURCE_PACK_HASH = "a251db541f513a417228dd3a2a199c6959170836"
    private const val ODYSSEY_RESOURCE_PACK_LINK = "https://www.dropbox.com/scl/fi/aiclr8b58ew5bzeb36w76/odyssey-resource-pack-v26.2.zip?rlkey=5fe7yn32f49nwuyowhs8k263n&st=zehi4f01&dl=1"

    // For the Vailcraft Server
    private const val VAILCRAFT_RESOURCE_PACK_HASH = "2368f44985eb5c93eb8a33a7fdaebbee9d27c448"
    private const val VAILCRAFT_RESOURCE_PACK_LINK = "https://www.dropbox.com/scl/fi/lyjdqmgpgpya84brop36z/vailcraft-resourece-pack-v26.2.zip?rlkey=qndn6b1hl2sfzyuevpgvcgrvq&st=lgx0om56&dl=1"

    internal fun resourcePackHandler(
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

}