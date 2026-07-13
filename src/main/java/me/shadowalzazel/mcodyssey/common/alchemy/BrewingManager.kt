@file:Suppress("UnstableApiUsage")

package me.shadowalzazel.mcodyssey.common.alchemy

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.Consumable
import io.papermc.paper.datacomponent.item.CustomModelData
import io.papermc.paper.datacomponent.item.PotionContents
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.api.RegistryTagManager
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.event.inventory.BrewEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionType

interface BrewingManager : RegistryTagManager, DataTagManager {

    fun customBrewingHandler(event: BrewEvent) {
        event.contents.ingredient ?: return
        brewedCustomPotion(event)
    }

    // ──────────────────────────────────────────────────────────────────────────────
    // Ingredient reference:
    //   Prismarine Crystals  -> Potion Vials   (self-contained model + name + stack)
    //   Glow Berries         -> Upgraded+       (amplifier +1, shorter duration)
    //   Honey Bottle         -> Extended+       (longer duration)
    //   Experience Bottle    -> Aura
    //   Fire Charge          -> Blast
    //   Gunpowder            -> Splash          (type change, contents preserved)
    //   Dragon Breath        -> Lingering       (type change, contents preserved)
    //   Redstone / Glowstone -> model-only change on a plain potion
    //   Popped Chorus Fruit  -> Concoctions
    // ──────────────────────────────────────────────────────────────────────────────

    private fun brewedCustomPotion(event: BrewEvent) {
        val ingredient = event.contents.ingredient ?: return
        val bottleModel = getBottleModel(ingredient.type)
        val capModel = getCapModel(ingredient.type)
        val slots = event.contents.contents.toList()

        for (slot in 0..2) {
            val input = slots[slot]?.clone() ?: continue
            if (input.type == Material.AIR) continue
            if (input.getData(DataComponentTypes.POTION_CONTENTS) == null) continue

            // -----------------------------------------------------------------------------
            // 1) Custom ingredients. Vanilla has no recipe for these, so we generate the
            //    result ourselves FROM the input potion.
            // -----------------------------------------------------------------------------
            if (inputIsNotEnhanced(input)) {
                val enhanced = when (ingredient.type) {
                    Material.GLOW_BERRIES        -> createUpgradedPlusPotion(input)
                    Material.HONEY_BOTTLE        -> createExtendedPlusPotion(input)
                    Material.EXPERIENCE_BOTTLE   -> createAuraPotion(input)
                    Material.FIRE_CHARGE         -> createBlastPotion(input)
                    Material.PRISMARINE_CRYSTALS -> createPotionVials(input)
                    else                         -> null
                }
                if (enhanced != null) {
                    // Vials set their own full model/name/stack; don't restyle them.
                    if (ingredient.type != Material.PRISMARINE_CRYSTALS) {
                        styleModel(target = enhanced, modelSource = enhanced, bottle = bottleModel, cap = capModel)
                    }
                    event.results[slot] = enhanced
                    continue
                }
            }

            // -----------------------------------------------------------------------------
            // 2) Everything else. Vanilla already produced the correct result (contents +
            //    type, including splash/lingering). We ONLY restyle the model, seeding it
            //    from the input so the bottle the player has built up carries forward.
            //    We never touch the potion contents here -- that was the awkward-potion bug.
            // -----------------------------------------------------------------------------
            val result = event.results.getOrNull(slot)?.clone() ?: continue
            if (result.type == Material.AIR) continue
            styleModel(target = result, modelSource = input, bottle = bottleModel, cap = capModel)
            event.results[slot] = result
        }
    }

    /**
     * Restyles [target] for the current ingredient. When [modelSource] is a different item
     * (the vanilla result), its accumulated CUSTOM_MODEL_DATA is copied over first so the
     * player's built-up bottle/cap isn't lost, then this ingredient's parts are applied.
     */
    private fun styleModel(target: ItemStack, modelSource: ItemStack, bottle: String?, cap: String?) {
        if (target !== modelSource) {
            modelSource.getData(DataComponentTypes.CUSTOM_MODEL_DATA)?.let {
                target.setData(DataComponentTypes.CUSTOM_MODEL_DATA, it)
            }
        }
        target.updatePotionModel(bottle, cap, "alchemy_potion")
        target.setData(DataComponentTypes.MAX_STACK_SIZE, 8)
    }

    fun ItemStack.updatePotionModel(bottle: String?, cap: String?, newModel: String? = null) {
        if (newModel != null) {
            this.setData(DataComponentTypes.ITEM_MODEL, createOdysseyKey(newModel))
        }

        val oldModelData = this.getData(DataComponentTypes.CUSTOM_MODEL_DATA)
        // parts = [base, bottle, cap]; guarantee three slots so indexing never throws.
        val parts = oldModelData?.strings()?.toMutableList() ?: mutableListOf()
        while (parts.size < 3) {
            parts.add(when (parts.size) {
                0 -> "alchemy_potion"
                1 -> "bottle"
                else -> "cap"
            })
        }
        if (bottle != null) parts[1] = bottle
        if (cap != null) parts[2] = cap

        val customData = CustomModelData.customModelData().addStrings(parts)
        if (oldModelData != null) { // preserve any existing flags/floats/colors
            customData.addFlags(oldModelData.flags())
            customData.addFloats(oldModelData.floats())
            customData.addColors(oldModelData.colors())
        }
        this.setData(DataComponentTypes.CUSTOM_MODEL_DATA, customData)
    }


    /**
     * Rebuilds every effect on this potion onto a fresh THICK base, scaling duration and
     * offsetting amplifier. Returns false (leaving the item untouched) if there is nothing
     * to rebuild. Shared by all the custom-potion creators.
     */
    private fun ItemStack.applyRebuiltEffects(durationScale: Double, amplifierDelta: Int = 0): Boolean {
        val potionData = this.getData(DataComponentTypes.POTION_CONTENTS) ?: return false
        val effects = (potionData.potion()?.potionEffects ?: emptyList()) + potionData.customEffects()
        if (effects.isEmpty()) return false

        val builder = PotionContents.potionContents().potion(PotionType.THICK)
        for (effect in effects) {
            builder.addCustomEffect(
                PotionEffect(effect.type, (effect.duration * durationScale).toInt(), effect.amplifier + amplifierDelta)
            )
        }
        this.setData(DataComponentTypes.POTION_CONTENTS, builder)
        return true
    }


    // ──────────────────────────────────────────────────────────────────────────────
    // ───────────────────────────────── UTILITY ────────────────────────────────────
    // ──────────────────────────────────────────────────────────────────────────────

    private fun plainName(text: String): Component =
        Component.text(text)
            .decoration(TextDecoration.ITALIC, false)
            .decoration(TextDecoration.BOLD, false)


    private fun inputIsNotEnhanced(potion: ItemStack): Boolean {
        //val hasPotionTag: (String) -> Boolean = { tag: String -> potion.hasTag(tag)}
        if (potion.hasTag(ItemDataTags.IS_POTION_VIAL)) return false
        if (potion.hasTag(ItemDataTags.IS_EXTENDED_PLUS)) return false
        if (potion.hasTag(ItemDataTags.IS_UPGRADED_PLUS)) return false
        if (potion.hasTag(ItemDataTags.IS_AURA_POTION)) return false
        if (potion.hasTag(ItemDataTags.IS_BLAST_POTION)) return false
        return true
    }

    private fun getIngredientResult(ingredient: ItemStack): Material = when (ingredient.type) {
        Material.GUNPOWDER     -> Material.SPLASH_POTION
        Material.DRAGON_BREATH -> Material.LINGERING_POTION
        else                   -> Material.POTION
    }

    private fun getCapModel(material: Material): String? = when (material) {
        Material.REDSTONE          -> "tall_cap"
        Material.GLOWSTONE_DUST    -> "short_cap"
        Material.GLOW_BERRIES      -> "short_cap"
        Material.HONEY_BOTTLE      -> "tall_cap"
        Material.EXPERIENCE_BOTTLE -> "ring_cap"
        Material.GUNPOWDER         -> "splash_cap"
        Material.LINGERING_POTION  -> "lingering_cap"
        Material.FIRE_CHARGE       -> "fuse_cap"
        else                       -> null
    }

    private fun getBottleModel(material: Material): String? = when (material) {
        Material.REDSTONE          -> "volumetric_bottle"
        Material.GLOWSTONE_DUST    -> "square_bottle"
        Material.GLOW_BERRIES      -> "square_bottle"
        Material.HONEY_BOTTLE      -> "volumetric_bottle"
        Material.EXPERIENCE_BOTTLE -> "aura_bottle"
        else                       -> null
    }

    private fun createUpgradedPlusPotion(potion: ItemStack): ItemStack {
        if (potion.hasTag(ItemDataTags.IS_EXTENDED_PLUS)) return potion
        if (potion.hasTag(ItemDataTags.IS_UPGRADED_PLUS)) return potion
        val potionData = potion.getData(DataComponentTypes.POTION_CONTENTS) ?: return potion
        // Get all effects
        val rawEffects = potionData.potion()?.potionEffects ?: mutableListOf()
        val customEffects = potionData.customEffects()
        val allEffects = rawEffects + customEffects
        if (allEffects.isEmpty()) return potion
        // Create new effect list
        val newPotionData = PotionContents.potionContents().potion(PotionType.THICK)
        for (effect in allEffects) {
            newPotionData.addCustomEffect(PotionEffect(effect.type, (effect.duration * 0.6).toInt(), effect.amplifier + 1))
        }
        potion.setData(DataComponentTypes.POTION_CONTENTS, newPotionData)
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 8)
        potion.addTag(ItemDataTags.IS_UPGRADED_PLUS)
        return potion
    }

    private fun createExtendedPlusPotion(potion: ItemStack): ItemStack {
        if (potion.hasTag(ItemDataTags.IS_EXTENDED_PLUS)) return potion
        if (potion.hasTag(ItemDataTags.IS_UPGRADED_PLUS)) return potion
        val potionData = potion.getData(DataComponentTypes.POTION_CONTENTS) ?: return potion
        // Get all effects
        val allEffects = (potionData.potion()?.potionEffects ?: mutableListOf()) + potionData.customEffects()
        if (allEffects.isEmpty()) return potion
        // Create new effect list
        val newPotionData = PotionContents.potionContents().potion(PotionType.THICK)
        for (effect in allEffects) {
            newPotionData.addCustomEffect(PotionEffect(effect.type, (effect.duration * 1.5).toInt(), effect.amplifier))
        }
        potion.setData(DataComponentTypes.POTION_CONTENTS, newPotionData)
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 8)
        potion.addTag(ItemDataTags.IS_EXTENDED_PLUS)
        return potion
    }

    private fun createPotionVials(potion: ItemStack): ItemStack {
        if (potion.hasTag(ItemDataTags.IS_POTION_VIAL)) return potion
        if (!potion.applyRebuiltEffects(durationScale = 0.4)) return potion

        val consumable = Consumable.consumable()
            .consumeSeconds(0.8f)
        consumable.sound(Key.key("entity.generic.drink"))

        potion.setData(DataComponentTypes.CONSUMABLE, consumable)
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 64)
        potion.setData(DataComponentTypes.ITEM_MODEL, createOdysseyKey("potion_vial"))
        potion.setData(DataComponentTypes.ITEM_NAME, plainName("potion_vial"))
        potion.addTag(ItemDataTags.IS_POTION_VIAL)
        potion.amount = 4
        return potion
    }

    private fun createAuraPotion(potion: ItemStack): ItemStack {
        // Create AURA POTION when near, applies effects, GOOD or BAD (mini-beacon)
        if (potion.hasTag(ItemDataTags.IS_AURA_POTION)) return potion
        if (!potion.applyRebuiltEffects(durationScale = 0.6)) return potion
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 8)
        potion.setData(DataComponentTypes.ITEM_NAME, plainName("aura_potion"))
        potion.addTag(ItemDataTags.IS_AURA_POTION)
        return potion
    }

    private fun createBlastPotion(potion: ItemStack): ItemStack {
        // Create BLAST POTION, timed potion that explodes after a 10 sec delay or something contacts it
        if (potion.hasTag(ItemDataTags.IS_BLAST_POTION)) return potion
        if (!potion.applyRebuiltEffects(durationScale = 1.0)) return potion
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 8)
        potion.setData(DataComponentTypes.ITEM_NAME, plainName("blast_potion"))
        potion.addTag(ItemDataTags.IS_BLAST_POTION)
        return potion
    }

}