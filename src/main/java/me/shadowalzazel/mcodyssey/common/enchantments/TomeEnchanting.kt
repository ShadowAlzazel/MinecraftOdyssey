package me.shadowalzazel.mcodyssey.common.enchantments

import io.papermc.paper.datacomponent.DataComponentType
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.ItemEnchantments
import me.shadowalzazel.mcodyssey.api.AdvancementManager
import me.shadowalzazel.mcodyssey.util.ItemToolTipManager
import me.shadowalzazel.mcodyssey.util.constants.CustomColors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import org.bukkit.inventory.meta.Repairable

@Suppress("UnstableApiUsage")
internal interface TomeEnchanting : EnchantabilityHandler, AdvancementManager, ItemToolTipManager {

    /*-- Shared component helpers ------------------------------------------------------------*/

    /** Writes the map back to the correct component and refreshes the tooltip. */
    private fun ItemStack.writeEnchants(enchants: Map<Enchantment, Int>) {
        val component = enchantComponent()
        if (enchants.isEmpty()) unsetData(component)
        else setData(component, ItemEnchantments.itemEnchantments().addAll(enchants))
        updateToolTip()
    }

    private fun awardAdvancement(viewers: List<HumanEntity>, key: String) {
        val namespaced = NamespacedKey.fromString(key) ?: return
        for (v in viewers) {
            if (v !is Player) continue
            val advancement = v.server.getAdvancement(namespaced) ?: continue
            v.getAdvancementProgress(advancement).awardCriteria("requirement")
        }
    }

    /*-- Tomes -------------------------------------------------------------------------------*/

    fun tomeOfDischargeOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        val enchants = item.readEnchants()
        if (enchants.isEmpty()) {
            viewers.forEach { it.broadcastBarMessage("This item needs to have enchantments to use this tome.") }
            return null
        }

        val dischargeEnchant = enchants.keys.random()
        val remaining = enchants - dischargeEnchant

        // An enchanted book with zero stored enchants is a broken item — hand back a plain book.
        val isBook = item.type == Material.ENCHANTED_BOOK
        val result = if (isBook && remaining.isEmpty()) {
            ItemStack(Material.BOOK, 1)
        } else {
            item.writeEnchants(remaining)
            item
        }

        if (dischargeEnchant.isCursed) {
            awardAdvancement(viewers, "odyssey:odyssey/discharge_a_curse")
        }
        viewers.forEach { it.broadcastBarMessage("Removing Enchantment: ${dischargeEnchant.key.key}") }
        return result
    }

    fun tomeOfHarmonyOnItem(item: ItemStack): ItemStack {
        // Repair cost is no longer a stacking penalty, so it resets to 0, not 1.
        item.setData(DataComponentTypes.REPAIR_COST, 0)
        if (item.getData(DataComponentTypes.MAX_DAMAGE) != null) {
            item.setData(DataComponentTypes.DAMAGE, 0)
        }
        item.updateToolTip()
        return item
    }

    fun tomeOfPromotionOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        val enchants = item.readEnchants()
        if (enchants.isEmpty()) {
            viewers.forEach { it.broadcastBarMessage("This item needs to be enchanted to use this tome.") }
            return null
        }

        val upgradeable = enchants.toList().filter { (ench, lvl) -> lvl < ench.maxLevel }
        if (upgradeable.isEmpty()) {
            viewers.forEach { it.broadcastBarMessage("This item already has max level enchants!") }
            return null
        }

        val (ench, currentLevel) = upgradeable.random()
        val newLevel = minOf(ench.maxLevel, currentLevel + 1)

        // Books ignore the enchantability budget; gear does not.
        val isBook = item.enchantComponent() == DataComponentTypes.STORED_ENCHANTMENTS
        if (!isBook) {
            val projected = item.getUsedEnchantabilityPoints() -
                    getEnchantabilityCost(ench to currentLevel) +
                    getEnchantabilityCost(ench to newLevel)
            if (projected > item.getMaxEnchantabilityPoints()) {
                viewers.forEach { it.broadcastBarMessage("The enchantment ${ench.key.key} would be too expensive!") }
                return null
            }
        }

        item.writeEnchants(enchants + (ench to newLevel))

        if (newLevel >= ench.maxLevel) {
            awardAdvancement(viewers, "odyssey:odyssey/use_promotion_tome")
        }
        return item
    }

    fun tomeOfReplicationOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        val stored = item.getData(DataComponentTypes.STORED_ENCHANTMENTS)?.enchantments()
        if (stored.isNullOrEmpty()) {
            viewers.forEach { it.broadcastBarMessage("This tome can only be used on enchanted books with enchants!") }
            return null
        }
        return item.clone()
    }

    fun tomeOfImitationOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        val stored = item.getData(DataComponentTypes.STORED_ENCHANTMENTS)?.enchantments()
        if (stored.isNullOrEmpty()) {
            viewers.forEach { it.broadcastBarMessage("This tome can only be used on enchanted books with enchants!") }
            return null
        }

        val (ench, level) = stored.toList().random()
        val checkedLevel = minOf(ench.maxLevel, level)

        val imitatedBook = ItemStack(Material.ENCHANTED_BOOK, 1)
        imitatedBook.writeEnchants(mapOf(ench to checkedLevel))

        if (checkedLevel >= ench.maxLevel) {
            awardAdvancement(viewers, "odyssey:odyssey/use_imitation_tome")
        }
        return imitatedBook
    }

    /** Extracts one enchantment onto a book, without destroying the source. */
    fun tomeOfExtractionOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        val enchants = item.readEnchants()
        if (enchants.isEmpty()) {
            viewers.forEach { it.broadcastBarMessage("This item needs to be enchanted to use this tome.") }
            return null
        }

        val (ench, level) = enchants.toList().random()
        val extractedBook = ItemStack(Material.ENCHANTED_BOOK, 1)
        extractedBook.writeEnchants(mapOf(ench to minOf(ench.maxLevel, level)))
        return extractedBook
    }

    fun tomeOfExtractionPostEffect(item: ItemStack, book: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        val extracted = book.getData(DataComponentTypes.STORED_ENCHANTMENTS)
            ?.enchantments()?.keys?.firstOrNull() ?: return null

        item.writeEnchants(item.readEnchants() - extracted)
        return item
    }

    fun tomeOfExpenditureOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        val enchants = item.readEnchants()
        if (enchants.isEmpty()) {
            viewers.forEach { it.broadcastBarMessage("This item needs to be enchanted to use this tome.") }
            return null
        }

        val (ench, level) = enchants.toList().random()
        val extractedBook = ItemStack(Material.ENCHANTED_BOOK, 1)
        extractedBook.writeEnchants(mapOf(ench to minOf(ench.maxLevel, level)))
        return extractedBook
    }

    /** Removes all enchants and gives XP. Destroys the item. */
    fun tomeOfAvariceOnItem(item: ItemStack, viewers: List<HumanEntity>): ItemStack? {
        if (item.readEnchants().isEmpty()) {
            viewers.forEach { it.broadcastBarMessage("This item needs to be enchanted to use this tome.") }
            return null
        }
        return ItemStack(Material.BOOK, 1)
    }

    fun tomeOfAvaricePostEffect(item: ItemStack, viewers: List<HumanEntity>) {
        val totalLevels = item.readEnchants().values.sum()
        if (totalLevels <= 0) return

        val reward = totalLevels * 50
        viewers.forEach {
            if (it is Player) {
                it.giveExp(reward)
                it.playSound(it.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.5F, 0.9F)
            }
        }
    }


    /*-----------------------------------------------------------------------------------------------*/
    // Fail Message
    private fun LivingEntity.broadcastBarMessage(reason: String, color: TextColor = CustomColors.ENCHANT.color) {
        this.sendActionBar(
            Component.text(
                reason,
                color
            )
        )
    }

}