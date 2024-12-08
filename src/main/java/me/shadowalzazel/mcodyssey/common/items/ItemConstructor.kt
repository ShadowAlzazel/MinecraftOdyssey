package me.shadowalzazel.mcodyssey.common.items

import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.CustomModelData
import io.papermc.paper.datacomponent.item.FoodProperties
import io.papermc.paper.datacomponent.item.ItemLore
import io.papermc.paper.datacomponent.item.PotionContents
import me.shadowalzazel.mcodyssey.util.AttributeManager
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.RegistryTagManager
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionType
import java.util.*

@Suppress("UnstableApiUsage")
sealed class ItemConstructor(
    val material: Material,
    val itemModel: String? = null,
    val lore: List<Component>? = null
) : DataTagManager, RegistryTagManager, AttributeManager {

    class GenericItemConstructor(material: Material, itemModel: String?, lore: List<Component>?):
        ItemConstructor(material, itemModel, lore)
    class PotionConstructor(
        material: Material,
        itemModel: String?,
        val effects: List<PotionEffect>?=null,
        val capModel: String?="potion_cap",
        val bottleModel: String?="bottle_cap",
        val color: Color?=null,
        val potionType: PotionType=PotionType.THICK): ItemConstructor(material, itemModel=itemModel)
    class FoodConstructor(
        material: Material,
        itemModel: String?,
        val saturation: Float,
        val nutrition: Int,
        val alwaysEat: Boolean): ItemConstructor(material, itemModel=itemModel)
    class GlyphsherdConstructor(
        material: Material,
        itemModel: String?,
        val attribute: Attribute,
        val value: Double,
        val slotGroup: EquipmentSlotGroup): ItemConstructor(material, itemModel=itemModel)

    /*-----------------------------------------------------------------------------------------------*/

    fun createItemStack(name: String, amount: Int=1, withBukkitId: Boolean=true): ItemStack {
        return when(this) {
            is GenericItemConstructor -> newItem(name, amount, withBukkitId)
            is PotionConstructor -> newItemPotion(name, amount, withBukkitId)
            is FoodConstructor -> newItemFood(name, amount, withBukkitId)
            is GlyphsherdConstructor -> newItemGlyphsherd(name, amount, withBukkitId)
        }
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun newItem(name: String, amount: Int=1, withBukkitId: Boolean=true) : ItemStack {
        val item = ItemStack(this.material)
        if (withBukkitId) item.setStringTag("item", name) // ID Tag is for Recipe Choice for crafting
        item.setData(DataComponentTypes.ITEM_NAME, Component.text(name).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, false))
        val customName = Component.text(name.toTitleCase()).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, false)
        item.setData(DataComponentTypes.CUSTOM_NAME, customName)
        if (itemModel != null) item.setData(DataComponentTypes.ITEM_MODEL, createOdysseyKey(this.itemModel))
        if (lore != null) item.setData(DataComponentTypes.LORE, ItemLore.lore(this.lore))
        item.amount = amount
        return item
    }

    private fun FoodConstructor.newItemFood(name: String, amount: Int=1, withIdTag: Boolean=true): ItemStack {
        val item = newItem(name, amount, withIdTag)
        val foodComponent = FoodProperties.food()
            .nutrition(this.nutrition)
            .saturation(this.saturation)
            .canAlwaysEat(this.alwaysEat)
        item.setData(DataComponentTypes.FOOD, foodComponent)
        return item
    }

    private fun PotionConstructor.newItemPotion(name: String, amount: Int=1, withIdTag: Boolean=true): ItemStack {
        val item = newItem(name, amount, withIdTag)
        val potionComponent = PotionContents.potionContents().potion(this.potionType)
        if (effects != null) potionComponent.addCustomEffects(this.effects)
        if (color != null) potionComponent.customColor(this.color)
        val potionParts = mutableListOf("alchemy_potion", "bottle", "cap")
        if (bottleModel != null) potionParts[1] = this.bottleModel
        if (capModel != null) potionParts[2] = this.capModel
        val customData = CustomModelData.customModelData().addStrings(potionParts)
        item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, customData)
        item.setData(DataComponentTypes.POTION_CONTENTS, potionComponent)
        item.setData(DataComponentTypes.MAX_STACK_SIZE, 16)
        return item
    }

    private fun GlyphsherdConstructor.newItemGlyphsherd(name: String, amount: Int=1, withIdTag: Boolean=true): ItemStack {
        val item = newItem(name, amount, withIdTag)
        item.addTag(ItemDataTags.IS_GLYPHSHERD)
        item.setGenericAttribute(value, AttributeTags.GLYPH_SLOT, attribute, null, slotGroup)
        return item
    }

    /*-----------------------------------------------------------------------------------------------*/

    private fun String.toTitleCase(): String {
        return this.replace("_", " ").trim().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

}