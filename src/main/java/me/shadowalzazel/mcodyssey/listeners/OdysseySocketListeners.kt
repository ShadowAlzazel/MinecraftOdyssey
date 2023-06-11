package me.shadowalzazel.mcodyssey.listeners


import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.event.Listener

object OdysseySocketListeners : Listener {

    private val statColor = TextColor.color(167, 125, 255)
    private val createdBySeparator = Component.text("Socket: + Empty" , statColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
    // TODO: Ornament, Socket,

    // Get First space, then next space, if first set (quality) has then proceed
    // Then get the gem.


    // Percentages %
    // -----------
    // Diamond -> Increases durability (1 - 20)
    // Amethyst -> Increases damages against elites and bosses (5 - 30)
    // Emerald -> Increases Crit Damage (50 - 400)
    // Quartz -> Increases Base Damage (10 - 200)

    // Neptunian Diamond
    // Iojovian Emerald
    // Soul Quartz



    // Tiers
    // -----------
    // Regular
    // Flawless
    // IDK

    // Each item has ONE AND ONLY ONE socket MAX
    //

    // TODO: Maybe if kill with soul catalyst, takes Entity Type (if not custom) as name,
    // Soul Gem -> Increases Damage to that Entity Type (Custom Name Tag) by (10 - 60)


    // TODO: Add tags for gilded and blood moon "Elites"

    fun socketDamageHandler() {

        // IF WEAPON HAS SOCKET GEM/RUNE/SOUL

        // DO stuff


        // OR ---

        // IF has special DUngeon gem
        // DO effects

        // Diamond (Armor: 10% more resistance) (DeprecatedWeapon: )
        // Emerald
        // Amethyst
        // Soul Quartz
        // Ruby

        // Make gem refiner table/merchant


        // TODO: Smith weapons reset attack, hide attack, and speed, add custom Lore
    }

}