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
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.time.Duration
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * The pen's R&D writer. It reads the runes in the pen's bundle as a READ-ONLY palette and lets
 * the player assemble a working sequence, which is inscribed onto a spell scroll on save.
 *
 * It never consumes or edits the pen's runes — the palette is just a view. Physical runes are
 * only spent when the final spell is inscribed (see the consumption hook in [inscribe]).
 *
 * ---------------------------------------------------------------------------------------------
 * LAYOUT NOTES (vanilla dialog constraints, see https://minecraft.wiki/w/Dialog)
 *
 *  - `multi_action` arranges its ACTION BUTTONS in a grid via `columns` (default 2). That is the
 *    fix for the "everything is one tall column" problem: [COLUMNS] below.
 *  - BODY elements are always stacked one per row. There is no `columns` for the body, and there
 *    is no item-type action button, so an item icon CANNOT be made clickable. Merging the icon
 *    and the button is not expressible in the format today (there's an open Mojang feedback
 *    request for exactly this). The closest vanilla allows is `minecraft:item` with a
 *    `description`, which draws the icon with text to its right — used on the Review screen.
 *  - Because of that, the palette is a compact button grid and the icons live on the Review
 *    screen, where the list is short and seeing the actual rune textures is worth the rows.
 * ---------------------------------------------------------------------------------------------
 *
 * Dialogs are static once shown, so every edit rebuilds and re-sends the dialog with fresh state.
 * The working sequence lives in a per-player session for the duration of editing.
 */
@Suppress("UnstableApiUsage")
object SpellPenDialog : RuneDataManager {

    // --- Layout / behaviour tuning ---------------------------------------------------------

    /** Rune buttons per row. 3 is a good balance for default GUI scale; 4 gets cramped. */
    private const val COLUMNS = 3

    /** Rune rows per page before paging kicks in. COLUMNS * ROWS_PER_PAGE runes per page. */
    private const val ROWS_PER_PAGE = 5

    /** Button width in pixels (vanilla allows 1..1024, default 150). Narrower = tighter grid. */
    private const val BUTTON_WIDTH = 104

    /** Max width for body text lines (1..1024, default 200). */
    private const val BODY_WIDTH = 340

    /** Hard cap on spell length. Raise/remove to taste — it only exists to keep the UI sane. */
    private const val MAX_SPELL_LENGTH = 64

    /** Item icons shown on the Review screen before it collapses into "+N more". */
    private const val MAX_REVIEW_ICONS = 6

    /**
     * Keep the dialog open between clicks instead of close→reopen. Vanilla only permits
     * `after_action: none` when `pause` is false, so both are set together. Flip to false if
     * you'd rather have the old behaviour.
     */
    private const val KEEP_OPEN = true

    /** Write the working draft back onto the pen after every edit, so Esc never loses work. */
    private const val SAVE_DRAFT_TO_PEN = true

    /** Consume the blank scroll when inscribing (the old code added a written one without it). */
    private const val CONSUME_BLANK_SCROLL = true

    private const val RUNES_PER_PAGE = COLUMNS * ROWS_PER_PAGE
    private const val BLANK_SCROLL_ID = "scroll"

    private enum class Mode { ADD, REVIEW }

    private data class EditSession(
        val pen: ItemStack,                   // kept for the draft save + rune consumption hook
        val paletteRunes: List<ArcaneRune>,   // one entry per distinct rune in the pen
        val paletteItems: List<ItemStack>,    // matching item stacks, for their icons
        val paletteCounts: List<Int>,         // how many of each the pen actually holds
        val sequence: MutableList<ArcaneRune> = mutableListOf(),
        var source: ArcaneSource = ArcaneSource.Magic,
        var page: Int = 0,
        var mode: Mode = Mode.ADD,
        var status: Component? = null         // one-line feedback shown INSIDE the dialog
    ) {
        val pageCount: Int
            get() = if (paletteRunes.isEmpty()) 1
            else (paletteRunes.size + RUNES_PER_PAGE - 1) / RUNES_PER_PAGE
    }

    private val sessions = ConcurrentHashMap<UUID, EditSession>()

    private val sourcePalette = listOf(
        ArcaneSource.Fire, ArcaneSource.Frost, ArcaneSource.Magic,
        ArcaneSource.Void, ArcaneSource.Radiant, ArcaneSource.Soul, ArcaneSource.Aero
    )

    // --- Entry points -----------------------------------------------------------------------

    /** Open the writer for the pen in the player's hand. */
    internal fun open(player: Player, pen: ItemStack) {
        val contents = pen.getData(DataComponentTypes.BUNDLE_CONTENTS)?.contents() ?: emptyList()

        // Build a de-duplicated palette from the pen's contents, keeping a running count.
        val runes = mutableListOf<ArcaneRune>()
        val items = mutableListOf<ItemStack>()
        val counts = mutableListOf<Int>()
        val indexByName = mutableMapOf<String, Int>()
        for (item in contents) {
            val rune = ArcaneRune.getRuneFromItem(item) ?: continue
            val existing = indexByName[rune.name]
            if (existing == null) {
                indexByName[rune.name] = runes.size
                runes.add(rune)
                items.add(item)
                counts.add(item.amount)
            } else {
                counts[existing] = counts[existing] + item.amount
            }
        }

        val session = EditSession(pen, runes, items, counts)
        // Resume any draft already written on the pen.
        readSpell(pen)?.let { (src, drafted) ->
            session.sequence.addAll(drafted.take(MAX_SPELL_LENGTH))
            if (src != null) session.source = src
        }
        sessions[player.uniqueId] = session
        show(player)
    }

    /** Call from your PlayerQuitEvent listener so sessions don't linger. */
    internal fun clearSession(uuid: UUID) {
        sessions.remove(uuid)
    }

    // --- Rendering --------------------------------------------------------------------------

    private fun show(player: Player) {
        val session = sessions[player.uniqueId] ?: return
        val dialog = Dialog.create { factory ->
            val base = DialogBase.builder(Component.text("Spell Writer", NamedTextColor.LIGHT_PURPLE))
                .externalTitle(Component.text("Spell Writer"))
                .canCloseWithEscape(true)
                .body(buildBody(session))
            if (KEEP_OPEN) {
                base.pause(false).afterAction(DialogBase.DialogAfterAction.NONE)
            }
            factory.empty()
                .base(base.build())
                .type(
                    DialogType.multiAction(buildButtons(player, session))
                        .columns(COLUMNS)
                        .exitAction(exitButton(player))
                        .build()
                )
        }
        player.showDialog(dialog)
    }

    private fun buildBody(session: EditSession): List<DialogBody> {
        val body = mutableListOf<DialogBody>()

        // 1. What this screen wants you to do. One short line, mode-specific.
        val hint = when {
            session.paletteRunes.isEmpty() -> "This pen holds no runes. Put some in the bundle first."
            session.mode == Mode.REVIEW -> "Click a slot to remove that rune."
            else -> "Click a rune to add it to the end of the spell."
        }
        body.add(DialogBody.plainMessage(Component.text(hint, NamedTextColor.GRAY), BODY_WIDTH))

        // 2. Element + length, on one line so it doesn't eat two rows.
        body.add(
            DialogBody.plainMessage(
                Component.text()
                    .append(Component.text("Element ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(session.source.name, colorOf(session.source)))
                    .append(Component.text("   Length ", NamedTextColor.DARK_GRAY))
                    .append(
                        Component.text(
                            "${session.sequence.size}/$MAX_SPELL_LENGTH",
                            if (session.sequence.size >= MAX_SPELL_LENGTH) NamedTextColor.RED else NamedTextColor.WHITE
                        )
                    )
                    .build(),
                BODY_WIDTH
            )
        )

        // 3. The sequence, numbered so it reads like a recipe rather than a blob.
        val summary = if (session.sequence.isEmpty()) {
            Component.text("(empty spell)", NamedTextColor.DARK_GRAY)
        } else {
            Component.text(
                session.sequence
                    .mapIndexed { i, rune -> "${i + 1}. ${rune.displayName}" }
                    .joinToString("  \u2192  "),
                NamedTextColor.WHITE
            )
        }
        body.add(DialogBody.plainMessage(summary, BODY_WIDTH))

        // 4. Review screen: show the real rune items, icon + label, in spell order.
        if (session.mode == Mode.REVIEW && session.sequence.isNotEmpty()) {
            session.sequence.take(MAX_REVIEW_ICONS).forEachIndexed { i, rune ->
                val icon = iconFor(session, rune) ?: return@forEachIndexed
                body.add(
                    DialogBody.item(icon.asQuantity(1))
                        .description(
                            DialogBody.plainMessage(
                                Component.text("${i + 1} \u00B7 ${rune.displayName}", NamedTextColor.WHITE),
                                BODY_WIDTH - 40
                            )
                        )
                        .showDecorations(false)  // no stack count over the icon; the label carries it
                        .showTooltip(true)
                        .build()
                )
            }
            val hidden = session.sequence.size - MAX_REVIEW_ICONS
            if (hidden > 0) {
                body.add(
                    DialogBody.plainMessage(
                        Component.text("+$hidden more below", NamedTextColor.DARK_GRAY), BODY_WIDTH
                    )
                )
            }
        }

        // 5. Feedback. Chat is not readable while a dialog is open, so errors must live here.
        session.status?.let { body.add(DialogBody.plainMessage(it, BODY_WIDTH)) }

        return body
    }

    private fun buildButtons(player: Player, session: EditSession): List<ActionButton> {
        val buttons = mutableListOf<ActionButton>()

        if (session.mode == Mode.REVIEW) {
            // One button per slot: click to remove that rune.
            session.sequence.forEachIndexed { i, rune ->
                buttons.add(
                    button("${i + 1} \u2715 ${rune.displayName}", "Remove ${rune.displayName} from slot ${i + 1}") {
                        if (i < session.sequence.size) session.sequence.removeAt(i)
                        if (session.sequence.isEmpty()) session.mode = Mode.ADD
                        session.status = null
                        commit(player, session)
                    }
                )
            }
            buttons.padToGrid(player)

            buttons.add(button("\u2190 Back", "Return to the rune palette") {
                session.mode = Mode.ADD
                session.status = null
                show(player)
            })
            buttons.add(button("\u2715 Clear All", "Empty the sequence") {
                session.sequence.clear()
                session.mode = Mode.ADD
                session.status = null
                commit(player, session)
            })
            buttons.add(inscribeButton(player, session))
            return buttons
        }

        // --- ADD mode ---
        val start = session.page * RUNES_PER_PAGE
        val end = minOf(start + RUNES_PER_PAGE, session.paletteRunes.size)
        for (i in start until end) {
            val rune = session.paletteRunes[i]
            val used = session.sequence.count { it.name == rune.name }
            val label = if (used > 0) "${rune.displayName} \u00D7$used" else rune.displayName
            buttons.add(
                button(label, "Append ${rune.displayName}  (pen holds ${session.paletteCounts[i]})") {
                    if (session.sequence.size >= MAX_SPELL_LENGTH) {
                        session.status = Component.text(
                            "Spell is full ($MAX_SPELL_LENGTH runes).", NamedTextColor.RED
                        )
                        show(player)
                    } else {
                        session.sequence.add(rune)
                        session.status = null
                        commit(player, session)
                    }
                }
            )
        }
        buttons.padToGrid(player)

        // Paging, only when it's actually needed.
        if (session.pageCount > 1) {
            buttons.add(button("\u25C0 Prev", "Previous page of runes") {
                session.page = (session.page - 1 + session.pageCount) % session.pageCount
                show(player)
            })
            buttons.add(
                button(
                    "Page ${session.page + 1}/${session.pageCount}",
                    "Jump back to the first page"
                ) {
                    session.page = 0
                    show(player)
                }
            )
            buttons.add(button("Next \u25B6", "Next page of runes") {
                session.page = (session.page + 1) % session.pageCount
                show(player)
            })
        }

        // Controls, always a clean row of COLUMNS = 3.
        buttons.add(button("Element: ${session.source.name} \u25B8", "Cycle the spell's element") {
            val i = sourcePalette.indexOf(session.source)
            session.source = sourcePalette[(i + 1) % sourcePalette.size]
            session.status = null
            commit(player, session)
        })
        buttons.add(button("\u21A9 Undo", "Delete the final rune") {
            if (session.sequence.isNotEmpty()) session.sequence.removeAt(session.sequence.lastIndex)
            session.status = null
            commit(player, session)
        })
        buttons.add(button("\u270E Edit Slots", "Review the spell and remove individual runes") {
            if (session.sequence.isEmpty()) {
                session.status = Component.text("Nothing to edit yet.", NamedTextColor.RED)
            } else {
                session.mode = Mode.REVIEW
                session.status = null
            }
            show(player)
        })

        buttons.add(inscribeButton(player, session))
        buttons.padToGrid(player)

        return buttons
    }

    // --- Buttons ----------------------------------------------------------------------------

    private fun inscribeButton(player: Player, session: EditSession): ActionButton =
        ActionButton.builder(Component.text("\u2712 Inscribe", NamedTextColor.GREEN))
            .tooltip(Component.text("Write this spell onto the blank scroll in your other hand"))
            .width(BUTTON_WIDTH)
            .action(callback { inscribe(player, session) })
            .build()

    private fun exitButton(player: Player): ActionButton =
        ActionButton.builder(Component.text("Close"))
            .tooltip(Component.text("Close the writer — your draft stays on the pen"))
            .action(
                callback {
                    sessions[player.uniqueId]?.let { saveDraft(it) }
                    sessions.remove(player.uniqueId)
                    player.closeDialog()
                }
            )
            .build()

    private fun button(label: String, tooltip: String, onClick: () -> Unit): ActionButton =
        ActionButton.builder(Component.text(label))
            .tooltip(Component.text(tooltip))
            .width(BUTTON_WIDTH)
            .action(callback(onClick))
            .build()

    /**
     * Blank filler so a partly-filled rune page doesn't push the control row out of alignment.
     * It re-sends the current dialog so a stray click is a no-op under either KEEP_OPEN setting.
     */
    private fun MutableList<ActionButton>.padToGrid(player: Player) {
        while (size % COLUMNS != 0) {
            add(
                ActionButton.builder(Component.text(" "))
                    .width(BUTTON_WIDTH)
                    .action(callback { show(player) })
                    .build()
            )
        }
    }

    private fun callback(onClick: () -> Unit): DialogAction =
        DialogAction.customClick(
            { _, audience -> if (audience is Player) onClick() },
            // The dialog is re-sent after every edit, but under KEEP_OPEN a button can legitimately
            // be clicked more than once, so single-use callbacks would go dead mid-session.
            ClickCallback.Options.builder()
                .uses(ClickCallback.UNLIMITED_USES)
                .lifetime(Duration.ofMinutes(15))
                .build()
        )

    // --- State ------------------------------------------------------------------------------

    /** Persist the draft (if enabled) and re-render. */
    private fun commit(player: Player, session: EditSession) {
        saveDraft(session)
        show(player)
    }

    /**
     * Writes the working sequence back onto the pen so closing with Esc doesn't lose it.
     * This mutates the ItemStack handed to [open] — Paper's `getItemInMainHand()` returns a live
     * mirror, so that lands on the real item. If your call site passes a copy, set the stack back
     * into the player's hand here instead.
     */
    private fun saveDraft(session: EditSession) {
        if (!SAVE_DRAFT_TO_PEN) return
        storeSpell(session.pen, session.sequence, session.source)
    }

    private fun inscribe(player: Player, session: EditSession) {
        if (session.sequence.isEmpty()) {
            session.status = Component.text("Nothing to inscribe.", NamedTextColor.RED)
            show(player)
            return
        }

        val slot = findBlankScroll(player)
        if (slot == null) {
            session.status = Component.text(
                "Hold an empty scroll in your other hand to inscribe.", NamedTextColor.RED
            )
            show(player)
            return
        }

        if (CONSUME_BLANK_SCROLL) {
            val held = player.inventory.getItem(slot)
            val left = held.amount - 1
            player.inventory.setItem(slot, if (left > 0) held.asQuantity(left) else ItemStack.empty())
        }

        val writtenScroll = Item.SCROLL.newItemStack(1)
        // val writtenScroll = Item.SPELL_SCROLL.newItemStack(1)  // when the dedicated item exists
        storeSpell(writtenScroll, session.sequence, session.source)
        player.inventory.addItem(writtenScroll).values.forEach { overflow ->
            player.world.dropItem(player.location, overflow)
        }

        // TODO(consume): spend the physical rune items from `session.pen`'s bundle here, per your
        // economy (how many of each, partial matches, etc.). Casting/editing never consume;
        // this final inscribe is the one place runes are spent.

        player.closeDialog()
        sessions.remove(player.uniqueId)
        player.sendMessage(
            Component.text(
                "Inscribed ${session.sequence.size} runes (${session.source.name}).",
                NamedTextColor.GREEN
            )
        )
    }

    // --- Helpers ----------------------------------------------------------------------------

    /** Off hand first (the pen is normally in the main hand), then main hand as a fallback. */
    private fun findBlankScroll(player: Player): EquipmentSlot? = when {
        player.inventory.itemInOffHand.getItemNameId() == BLANK_SCROLL_ID -> EquipmentSlot.OFF_HAND
        player.inventory.itemInMainHand.getItemNameId() == BLANK_SCROLL_ID -> EquipmentSlot.HAND
        else -> null
    }

    private fun iconFor(session: EditSession, rune: ArcaneRune): ItemStack? {
        val index = session.paletteRunes.indexOfFirst { it.name == rune.name }
        return if (index >= 0) session.paletteItems[index] else null
    }

    private fun colorOf(source: ArcaneSource): NamedTextColor = when (source) {
        ArcaneSource.Fire -> NamedTextColor.RED
        ArcaneSource.Frost -> NamedTextColor.AQUA
        ArcaneSource.Magic -> NamedTextColor.LIGHT_PURPLE
        ArcaneSource.Void -> NamedTextColor.DARK_PURPLE
        ArcaneSource.Radiant -> NamedTextColor.YELLOW
        ArcaneSource.Soul -> NamedTextColor.BLUE
        ArcaneSource.Aero -> NamedTextColor.WHITE
        else -> NamedTextColor.GRAY
    }
}