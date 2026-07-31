package me.shadowalzazel.mcodyssey.common.arcane

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.ActionButton
import io.papermc.paper.registry.data.dialog.DialogBase
import io.papermc.paper.registry.data.dialog.action.DialogAction
import io.papermc.paper.registry.data.dialog.body.DialogBody
import io.papermc.paper.registry.data.dialog.type.DialogType
import me.shadowalzazel.mcodyssey.common.arcane.runes.ArcaneRune
import me.shadowalzazel.mcodyssey.common.items.Item
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ItemType
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * The pen's R&D writer. It reads the runes in the pen's bundle as a READ-ONLY palette and lets
 * the player assemble a working sequence, which is inscribed onto a spell scroll on save.
 *
 * It never consumes or edits the pen's runes — the palette is just a view. Physical runes are
 * only spent when the final spell is inscribed (see the consumption hook in [inscribe]).
 *
 * Dialogs are static once shown, so every edit rebuilds and reopens the dialog with fresh
 * state. The working sequence lives in a per-player session for the duration of editing.
 */
@Suppress("UnstableApiUsage")
object SpellPenDialog : RuneDataManager {

    private data class EditSession(
        val paletteRunes: List<ArcaneRune>,   // one entry per distinct rune in the pen
        val paletteItems: List<ItemStack>,    // matching item stacks, for their icons
        val sequence: MutableList<ArcaneRune> = mutableListOf(),
        var source: ArcaneSource = ArcaneSource.Magic
    )

    private val sessions = ConcurrentHashMap<UUID, EditSession>()

    private val sourcePalette = listOf(
        ArcaneSource.Fire, ArcaneSource.Frost, ArcaneSource.Magic,
        ArcaneSource.Void, ArcaneSource.Radiant, ArcaneSource.Soul, ArcaneSource.Aero
    )

    /** Open the writer for the pen in the player's hand. */
    internal fun open(player: Player, pen: ItemStack) {
        val contents = pen.getData(DataComponentTypes.BUNDLE_CONTENTS)?.contents() ?: emptyList()

        // Build a de-duplicated palette from the pen's contents.
        val runes = mutableListOf<ArcaneRune>()
        val items = mutableListOf<ItemStack>()
        val seen = mutableSetOf<String>()
        for (item in contents) {
            val rune = ArcaneRune.getRuneFromItem(item) ?: continue
            if (seen.add(rune.name)) {
                runes.add(rune)
                items.add(item)
            }
        }

        val session = EditSession(runes, items)
        // Resume any draft already written on the pen.
        readSpell(pen)?.let { (src, drafted) ->
            session.sequence.addAll(drafted)
            if (src != null) session.source = src
        }
        sessions[player.uniqueId] = session
        show(player)
    }

    private fun show(player: Player) {
        val session = sessions[player.uniqueId] ?: return
        val dialog = Dialog.create { factory ->
            factory.empty()
                .base(
                    DialogBase.builder(Component.text("Spell Writer", NamedTextColor.LIGHT_PURPLE))
                        .canCloseWithEscape(true)
                        .body(buildBody(session))
                        .build()
                )
                .type(DialogType.multiAction(buildButtons(player, session)).build())
        }
        player.showDialog(dialog)
    }

    private fun buildBody(session: EditSession): List<DialogBody> {
        val body = mutableListOf<DialogBody>()
        val summary = if (session.sequence.isEmpty()) "(empty)"
        else session.sequence.joinToString("  \u2192  ") { it.displayName }

        body.add(DialogBody.plainMessage(Component.text("Element: ${session.source.name}", NamedTextColor.GOLD)))
        body.add(DialogBody.plainMessage(Component.text(summary, NamedTextColor.WHITE)))
        // Show the actual rune items from the pen as icons.
        for (item in session.paletteItems) body.add(DialogBody.item(item).build())
        return body
    }

    private fun buildButtons(player: Player, session: EditSession): List<ActionButton> {
        val buttons = mutableListOf<ActionButton>()

        // Cycle the element.
        buttons.add(button("Element: ${session.source.name} \u25B8", "Cycle the spell's element") {
            val i = sourcePalette.indexOf(session.source)
            session.source = sourcePalette[(i + 1) % sourcePalette.size]
            show(player)
        })

        // One append button per palette rune.
        for (rune in session.paletteRunes) {
            buttons.add(button("+ ${rune.displayName}", "Append ${rune.displayName}") {
                session.sequence.add(rune)
                show(player)
            })
        }

        // Editing controls.
        buttons.add(button("Remove Last", "Delete the final rune") {
            if (session.sequence.isNotEmpty()) session.sequence.removeAt(session.sequence.lastIndex)
            show(player)
        })
        buttons.add(button("Clear", "Empty the sequence") {
            session.sequence.clear()
            show(player)
        })
        buttons.add(button("Inscribe to Scroll", "Write this spell onto the scroll in your main hand") {
            inscribe(player, session)
        })

        return buttons
    }

    private fun button(label: String, tooltip: String, onClick: () -> Unit): ActionButton =
        ActionButton.builder(Component.text(label))
            .tooltip(Component.text(tooltip))
            .action(
                DialogAction.customClick(
                    { _, audience -> if (audience is Player) onClick() },
                    ClickCallback.Options.builder().uses(1).build()
                )
            )
            .build()

    private fun inscribe(player: Player, session: EditSession) {
        if (session.sequence.isEmpty()) {
            player.sendMessage(Component.text("Nothing to inscribe.", NamedTextColor.RED))
            return
        }
        val scroll = player.inventory.itemInOffHand
        if (scroll.getItemNameId() != "scroll") {
            player.sendMessage(Component.text("Hold ane empty scroll in your main hand to inscribe.", NamedTextColor.RED))
            return
        }

        val newSpellScroll = Item.SPELL_SCROLL.newItemStack(1)
        storeSpell(newSpellScroll, session.sequence, session.source)
        player.inventory.addItem(newSpellScroll)

        //storeSpell(scroll, session.sequence, session.source)
        // TODO(consume): spend the physical rune items from the pen bundle here, per your
        // economy (how many of each, partial matches, etc.). Casting/editing never consume;
        // this final inscribe is the one place runes are spent.

        player.closeDialog()
        sessions.remove(player.uniqueId)
        player.sendMessage(
            Component.text("Inscribed ${session.sequence.size} runes (${session.source.name}).", NamedTextColor.GREEN)
        )
    }
}