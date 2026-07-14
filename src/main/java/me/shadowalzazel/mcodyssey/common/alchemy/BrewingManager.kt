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

    // ──────────────────────────────────────────────────────────────────────────────
    // Ingredient reference:
    //   Prismarine Crystals  -> Potion Vials    (self-contained model + name + stack)
    //   Diamond         -> Upgraded+       (amplifier +1, shorter duration)
    //   Honey Bottle         -> Extended+       (longer duration)
    //   Experience Bottle    -> Aura
    //   Fire Charge          -> Blast
    //   Gunpowder            -> Splash          (type change, contents preserved)
    //   Dragon Breath        -> Lingering       (type change, contents preserved)
    //   Redstone / Glowstone -> model-only change on a plain potion
    //   Popped Chorus Fruit  -> Concoctions
    // ──────────────────────────────────────────────────────────────────────────────

    fun customBrewingHandler(event: BrewEvent) {
        if (event.contents.ingredient == null) return
        brewedCustomPotion(event)
    }

    private fun brewedCustomPotion(event: BrewEvent) {
        val ingredient = event.contents.ingredient ?: return
        val slots = event.contents.contents.toList()

        for (slot in 0..2) {
            val input = slots[slot]?.clone() ?: continue
            if (input.type == Material.AIR) continue
            if (input.getData(DataComponentTypes.POTION_CONTENTS) == null) continue

            // -----------------------------------------------------------------------------
            // 1) Custom effect ingredients. Vanilla has no recipe for these, so we generate
            //    the result ourselves FROM the input potion.
            // -----------------------------------------------------------------------------
            if (inputIsNotEnhanced(input)) {
                val enhanced = when (ingredient.type) {
                    Material.DIAMOND             -> createUpgradedPlusPotion(input)
                    Material.HONEY_BOTTLE        -> createExtendedPlusPotion(input)
                    Material.EXPERIENCE_BOTTLE   -> createAuraPotion(input)
                    Material.FIRE_CHARGE         -> createBlastPotion(input)
                    Material.PRISMARINE_CRYSTALS -> createPotionVials(input)
                    else                         -> null
                }
                if (enhanced != null) {
                    // Vials set their own full model/name/stack; don't restyle them.
                    if (ingredient.type != Material.PRISMARINE_CRYSTALS) {
                        styleModel(target = enhanced, input = enhanced, ingredient = ingredient.type)
                    }
                    event.results[slot] = enhanced
                    continue
                }
            }

            // -----------------------------------------------------------------------------
            // 2) Type-changing ingredients (gunpowder -> splash, dragon breath -> lingering).
            //    Plain vanilla potions: vanilla already produced the correct splash/lingering,
            //    so use that result (this is what makes Harming II / Infestation convert).
            //    Custom potions (vials / concoctions / enhanced): vanilla won't convert them
            //    correctly, so we force the type ourselves and carry the input's contents.
            // -----------------------------------------------------------------------------
            val forcedType = when (ingredient.type) {
                Material.GUNPOWDER     -> Material.SPLASH_POTION
                Material.DRAGON_BREATH -> Material.LINGERING_POTION
                else                   -> null
            }
            if (forcedType != null) {
                val vanilla = event.results.getOrNull(slot)
                val converted: ItemStack = if (!isCustomPotion(input) && vanilla != null && vanilla.type == forcedType) {
                    vanilla.clone()
                } else {
                    val forced = if (input.type != forcedType) input.withType(forcedType) else input.clone()
                    // Re-apply contents so custom effects can't be lost in the conversion.
                    input.getData(DataComponentTypes.POTION_CONTENTS)?.let {
                        forced.setData(DataComponentTypes.POTION_CONTENTS, it)
                    }
                    forced
                }
                styleModel(target = converted, input = input, ingredient = ingredient.type)
                event.results[slot] = converted
                continue
            }

            // -----------------------------------------------------------------------------
            // 3) Everything else (nether wart, sugar, redstone, glowstone, ...). Vanilla
            //    computed the correct contents; we ONLY restyle the model, seeding it from
            //    the input so the built-up bottle carries forward. Contents are left alone.
            // -----------------------------------------------------------------------------
            val result = event.results.getOrNull(slot)?.clone() ?: continue
            if (result.type == Material.AIR) continue
            styleModel(target = result, input = input, ingredient = ingredient.type)
            event.results[slot] = result
        }
    }

    /*-----------------------------------------------------------------------------------------------*/

    /**
     * Restyles [target] for the current [ingredient], seeding from [input] (the potion the
     * player brewed) so the built-up model isn't lost.
     *
     * BASE MODEL: whatever the input already used is kept (vials stay "potion_vial",
     * concoctions/normal potions stay "alchemy_potion", etc). A brand-new potion with no
     * model defaults to "alchemy_potion". This stops a vial from being flattened into a
     * plain composite potion when it's turned into a splash.
     *
     * CAP: driven by the RESULTING potion type -- a splash is always splash_cap, a lingering
     * always lingering_cap, and only a normal potion falls back to the ingredient's cap.
     *
     * BOTTLE: set by the ingredient when it defines one, otherwise the input's existing bottle
     * is re-applied explicitly so type conversions (e.g. -> lingering) can't drop it.
     */
    private fun styleModel(target: ItemStack, input: ItemStack, ingredient: Material) {
        // Vials use a dedicated model, not the bottle/cap composite. Preserve their identity
        // so a splash vial still reads as a vial (just thrown), rather than becoming a
        // composite splash potion.
        if (input.hasTag(ItemDataTags.IS_POTION_VIAL)) {
            target.setData(DataComponentTypes.ITEM_MODEL, createOdysseyKey("potion_vial"))
            input.getData(DataComponentTypes.CUSTOM_MODEL_DATA)?.let { target.setData(DataComponentTypes.CUSTOM_MODEL_DATA, it) }
            input.getData(DataComponentTypes.ITEM_NAME)?.let { target.setData(DataComponentTypes.ITEM_NAME, it) }
            target.setData(DataComponentTypes.MAX_STACK_SIZE, 64)
            if (!target.hasTag(ItemDataTags.IS_POTION_VIAL)) target.addTag(ItemDataTags.IS_POTION_VIAL)

            // Only a drinkable (POTION) vial keeps the drink behaviour. A thrown vial
            // (splash / lingering) is used by throwing, so strip any consumable that the
            // original vial carried or that withType() copied across.
            if (target.type == Material.POTION) {
                input.getData(DataComponentTypes.CONSUMABLE)?.let { target.setData(DataComponentTypes.CONSUMABLE, it) }
            } else {
                target.unsetData(DataComponentTypes.CONSUMABLE)
            }
            return
        }

        // Composite model (normal potions + multi-effect concoctions): this is the ONLY
        // model non-vial potions ever use, so just force it — don't try to "preserve" it
        // by reading ITEM_MODEL back off the input, since Paper returns an implicit vanilla
        // default there (e.g. minecraft:potion) even when it was never explicitly set, which
        // silently defeated the old ?: fallback and left potions looking vanilla.
        target.setData(DataComponentTypes.ITEM_MODEL, createOdysseyKey("alchemy_potion"))

        if (target !== input) {
            input.getData(DataComponentTypes.CUSTOM_MODEL_DATA)?.let {
                target.setData(DataComponentTypes.CUSTOM_MODEL_DATA, it)
            }
        }

        val prevParts = input.getData(DataComponentTypes.CUSTOM_MODEL_DATA)?.strings().orEmpty()
        val bottle = getBottleModel(ingredient) ?: prevParts.getOrNull(1)
        val cap = when (target.type) {
            Material.SPLASH_POTION    -> "splash_cap"
            Material.LINGERING_POTION -> "lingering_cap"
            else                      -> getCapModel(ingredient) ?: prevParts.getOrNull(2)
        }

        target.updatePotionModel(bottle, cap)
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

    private fun plainName(text: String): Component =
        Component.text(text)
            .decoration(TextDecoration.ITALIC, false)
            .decoration(TextDecoration.BOLD, false)

    private fun inputIsNotEnhanced(potion: ItemStack): Boolean {
        val enhancedTags = listOf(
            ItemDataTags.IS_POTION_VIAL,
            ItemDataTags.IS_EXTENDED_PLUS,
            ItemDataTags.IS_UPGRADED_PLUS,
            ItemDataTags.IS_AURA_POTION,
            ItemDataTags.IS_BLAST_POTION,
        )
        return enhancedTags.none { potion.hasTag(it) }
    }

    /**
     * A "custom" potion is one vanilla cannot faithfully re-brew: our enhanced/vial potions,
     * or anything carrying custom effects (concoctions). For these, vanilla's brewing result
     * is unreliable (no-effect or unconverted), so we build the result from the input instead.
     * Plain vanilla potions (base type, no custom effects) are NOT custom.
     */
    private fun isCustomPotion(potion: ItemStack): Boolean {
        if (!inputIsNotEnhanced(potion)) return true // vials + enhanced tags
        val contents = potion.getData(DataComponentTypes.POTION_CONTENTS) ?: return false
        return contents.customEffects().isNotEmpty()
    }

    // Cap for NORMAL potions only. splash_cap / lingering_cap are handled in styleModel
    // by potion type, so gunpowder / dragon breath deliberately have no entry here.
    private fun getCapModel(material: Material): String? = when (material) {
        Material.REDSTONE          -> "tall_cap"
        Material.GLOWSTONE_DUST    -> "short_cap"
        Material.DIAMOND           -> "diamond_cap"
        Material.HONEY_BOTTLE      -> "tall_cap"
        Material.EXPERIENCE_BOTTLE -> "ring_cap"
        Material.FIRE_CHARGE       -> "fuse_cap"
        else                       -> null
    }

    private fun getBottleModel(material: Material): String? = when (material) {
        Material.REDSTONE          -> "volumetric_bottle"
        Material.GLOWSTONE_DUST    -> "square_bottle"
        Material.DIAMOND           -> "square_bottle"
        Material.HONEY_BOTTLE      -> "volumetric_bottle"
        Material.EXPERIENCE_BOTTLE -> "aura_bottle"
        else                       -> null
    }

    /*-----------------------------------------------------------------------------------------------*/

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

    private fun createUpgradedPlusPotion(potion: ItemStack): ItemStack {
        if (potion.hasTag(ItemDataTags.IS_UPGRADED_PLUS) || potion.hasTag(ItemDataTags.IS_EXTENDED_PLUS)) return potion
        if (!potion.applyRebuiltEffects(durationScale = 0.6, amplifierDelta = 1)) return potion
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 8)
        potion.addTag(ItemDataTags.IS_UPGRADED_PLUS)
        return potion
    }

    private fun createExtendedPlusPotion(potion: ItemStack): ItemStack {
        if (potion.hasTag(ItemDataTags.IS_UPGRADED_PLUS) || potion.hasTag(ItemDataTags.IS_EXTENDED_PLUS)) return potion
        if (!potion.applyRebuiltEffects(durationScale = 1.5)) return potion
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 8)
        potion.addTag(ItemDataTags.IS_EXTENDED_PLUS)
        return potion
    }

    private fun createPotionVials(potion: ItemStack): ItemStack {
        if (potion.hasTag(ItemDataTags.IS_POTION_VIAL)) return potion
        if (!potion.applyRebuiltEffects(durationScale = 0.4)) return potion

        val consumable = Consumable.consumable().consumeSeconds(0.8f)
        consumable.sound(Key.key("entity.generic.drink"))

        potion.setData(DataComponentTypes.CONSUMABLE, consumable)
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 64)
        potion.setData(DataComponentTypes.ITEM_MODEL, createOdysseyKey("potion_vial"))
        potion.setData(DataComponentTypes.ITEM_NAME, plainName("potion_vial"))
        potion.addTag(ItemDataTags.IS_POTION_VIAL)
        potion.amount = 4
        return potion
    }

    // Aura: mini-beacon style, applies effects to nearby entities.
    private fun createAuraPotion(potion: ItemStack): ItemStack {
        if (potion.hasTag(ItemDataTags.IS_AURA_POTION)) return potion
        if (!potion.applyRebuiltEffects(durationScale = 0.6)) return potion
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 8)
        potion.setData(DataComponentTypes.ITEM_NAME, plainName("aura_potion"))
        potion.addTag(ItemDataTags.IS_AURA_POTION)
        return potion
    }

    // Blast: timed potion that explodes after a delay or on contact.
    private fun createBlastPotion(potion: ItemStack): ItemStack {
        if (potion.hasTag(ItemDataTags.IS_BLAST_POTION)) return potion
        if (!potion.applyRebuiltEffects(durationScale = 1.0)) return potion
        potion.setData(DataComponentTypes.MAX_STACK_SIZE, 8)
        potion.setData(DataComponentTypes.ITEM_NAME, plainName("blast_potion"))
        potion.addTag(ItemDataTags.IS_BLAST_POTION)
        return potion
    }

}