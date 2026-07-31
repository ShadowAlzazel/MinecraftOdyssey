package me.shadowalzazel.mcodyssey.common.listeners

import io.papermc.paper.datacomponent.DataComponentTypes
import me.shadowalzazel.mcodyssey.common.arcane.ArcaneSpellBuilder
import me.shadowalzazel.mcodyssey.common.arcane.ArcaneCaster
import me.shadowalzazel.mcodyssey.common.arcane.ArcaneSpellItems
import me.shadowalzazel.mcodyssey.common.arcane.CastingContext
import me.shadowalzazel.mcodyssey.util.DataTagManager
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
object ArcaneListeners: Listener, DataTagManager {

    @EventHandler(priority = EventPriority.LOW)
    fun mainWeaponInteractionHandler(event: PlayerInteractEvent) {
        if (event.action.isLeftClick) {
            leftClickHandler(event)
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    fun mainConsumingHandler(event: PlayerItemConsumeEvent) {
        val player = event.player
        val equipment = player.equipment ?: return
        val mainhand = equipment.itemInMainHand
        val item = event.item
        val itemName = item.getItemNameId()
        // Detect magic items
        when (itemName) {
            // TODO -> Disabled decryption for now
            "scroll" -> {
                //scrollConsumingHandler(event)
                ArcaneSpellItems.castScroll(player, item)
            }
            "spell_scroll" -> {
                // Scroll damage
                ArcaneSpellItems.castScroll(player, item)

                val scroll = item
                scroll.damage(1, player)
                event.replacement = scroll



                // do spell
                //spellScrollCastingHandler(player, mainhand)
            }
        }

    }

    //@EventHandler(priority = EventPriority.LOW)
    fun dispenserHandler(event: BlockDispenseEvent) {
        val dispensedItem = event.item
        val itemName = dispensedItem.getItemNameId()
        if (itemName == "spell_scroll") {
            // Triggered for block
            val direction = event.velocity
            val spellScroll = event.item
            // Check if we can build spell
            val spellBuilder = ArcaneSpellBuilder(arcaneItem = spellScroll)
            val canBuildSpell = spellBuilder.canBuildSpell()
            if (!canBuildSpell) return
            // Do stuff to item
            event.item = ItemStack(Material.AIR)
            val currentDamage = spellScroll.getData(DataComponentTypes.DAMAGE) ?: 0
            spellScroll.setData(DataComponentTypes.DAMAGE, currentDamage + 1)
            // Run caster
            val block = event.block
            val faceDirection = event.block.location.direction.clone().normalize()
            val normalizedDirection = direction.normalize().clone()
            val blockEdgeLocation = block.location.toCenterLocation().add(faceDirection.multiply(0.55))
            val spellContext = CastingContext(
                caster = ArcaneCaster(blockCaster = block),
                world = block.world,
                castingLocation = blockEdgeLocation,
                direction = direction,
                target = null,
                targetLocation = null
            )

            // Build Spell
            val spell = spellBuilder.buildSpell(spellContext)
            spell.castSpell()
        }

    }


    private fun leftClickHandler(event: PlayerInteractEvent) {
        val player = event.player
        val equipment = player.equipment
        val mainhand = event.item ?: return
        val itemName = mainhand.getItemNameId()
        // Sentries Passed
        when (itemName) {
            // Tools
            "arcane_wand" -> ArcaneSpellItems.castBuiltInWand(player)
            "arcane_scepter" -> ArcaneSpellItems.castBuiltInScepter(player)
            // Others
            "arcane_pen" -> ArcaneSpellItems.openPenWriter(player, mainhand)
            "arcane_stylus" -> null
            "arcane_blade" -> null //arcaneBladeHandler(event)
        }

    }



}