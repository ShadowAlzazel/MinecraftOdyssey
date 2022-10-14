package me.shadowalzazel.mcodyssey.listeners


import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.event.Listener

object OdysseySocketListeners : Listener {

    private val statColor = TextColor.color(167, 125, 255)
    private val createdBySeparator = Component.text("Socket: + Empty" , statColor).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)

    fun socketDamageHandler() {

        // IF WEAPON HAS SOCKET GEM/RUNE/SOUL

        // DO stuff


        // OR ---

        // IF has special DUngeon gem
        // DO effects

        // Diamond (Armor: 10% more resistance) (Weapon: )
        // Emerald
        // Amethyst
        // Soul Quartz
        // Ruby

        // Make gem refiner table/merchant


        // TODO: Smith weapons reset attack, hide attack, and speed, add custom Lore
    }

}