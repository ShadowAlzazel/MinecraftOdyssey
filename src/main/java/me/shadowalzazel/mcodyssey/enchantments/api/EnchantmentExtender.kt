package me.shadowalzazel.mcodyssey.enchantments.api

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.enchantments.Enchantment

interface EnchantmentExtender {

    /*-----------------------------------------------------------------------------------------------*/
    // Helper Functions
    private fun getGrayTextComponent(text: String): TextComponent {
        return Component
            .text(text)
            .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            .color(TextColor.color(170, 170, 170))
    }

    /*-----------------------------------------------------------------------------------------------*/
    // Extension Helper
    fun Enchantment.getDescriptionTooltip(level: Int): List<Component>  {
        val name = this.key.key
        val textList = getToolTipText(name, level)
        val description: MutableList<Component> = mutableListOf()
        for (t in textList) {
            description.add(getGrayTextComponent(t))
        }
        return description
    }

    private fun getToolTipText(name: String, level: Int): List<String> {
        return when(name) {
            // Vanilla
            "sharpness" -> listOf("Increase Melee Damage by ${0.5 * level + 0.5}=[0.5 * level + 0.5]")
            "looting" -> listOf("Increases the number of common drops by ${level}=[level]")
            "unbreaking" -> listOf("Increases Durability by ${level * 100}%=[level * 100]")
            "smite" -> listOf("Increase Melee Damage to undead mobs by ${2.5 * level}=[2.5 * level]")
            // Odyssey
            "antibonk" -> listOf("Reduce critical hit damage by ${2.5 * level}=[2.5 x level].")
            else -> listOf(name)
        }
    }

}