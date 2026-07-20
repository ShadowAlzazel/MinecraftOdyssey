package me.shadowalzazel.mcodyssey.common.boss

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player


/**
 * A boss's whole voice in one editable object.
 *
 * Features that make it "robust":
 *   - each key can hold several variants; one is picked at random per send
 *   - `<placeholder>` tokens are replaced from a map (e.g. `<player>`, `<host>`)
 *   - a missing key fails silently instead of throwing
 *
 * Build one with the DSL and hand it to your boss:
 *
 *     val dialogue = Dialogue.of(prefix, WHITE) {
 *         line(AmbassadorLine.GREETING, "Hello, <player>.", "Ah... <player>.")
 *     }
 */
class Dialogue(
    private val prefix: Component,
    private val bodyColor: TextColor,
    private val entries: Map<String, List<String>>,
) {

    /**
     * A key into a dialogue pack. Have your per-boss enum implement this so lines
     * stay type-safe (`AmbassadorLine.PATIENCE_LOW`) while the text lives elsewhere.
     */

    private fun render(line: DialogueKey, vars: Map<String, String>): Component? {
        val variants = entries[line.id]?.takeIf { it.isNotEmpty() } ?: return null
        var text = variants.random()
        vars.forEach { (token, value) -> text = text.replace("<$token>", value) }
        return prefix.append(Component.text(text, bodyColor))
    }

    fun send(player: Player, line: DialogueKey, vars: Map<String, String> = emptyMap()) {
        render(line, vars)?.let { player.sendMessage(it) }
    }

    /** Sends the *same* rolled variant to everyone, so a group reads one message. */
    fun send(audience: Collection<Player>, line: DialogueKey, vars: Map<String, String> = emptyMap()) {
        val component = render(line, vars) ?: return
        audience.forEach { it.sendMessage(component) }
    }

    class Builder(private val prefix: Component, private val color: TextColor) {
        private val entries = mutableMapOf<String, MutableList<String>>()

        fun line(key: DialogueKey, vararg variants: String) = apply {
            entries.getOrPut(key.id) { mutableListOf() }.addAll(variants)
        }

        fun build() = Dialogue(prefix, color, entries)
    }

    companion object {
        inline fun of(prefix: Component, color: TextColor, block: Builder.() -> Unit): Dialogue =
            Builder(prefix, color).apply(block).build()
    }
}