package me.shadowalzazel.mcodyssey.items.weapons

import me.shadowalzazel.mcodyssey.constants.OdysseyItemModels
import me.shadowalzazel.mcodyssey.constants.OdysseyUUIDs
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import me.shadowalzazel.mcodyssey.items.utility.OdysseyWeapon
import me.shadowalzazel.mcodyssey.items.utility.WeaponTypes
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta

// NEUTRONIUM_BARK_SWORD
/*
object NeutroniumBarkSword : OdysseyItem("Neutronium-Bark Sword", Material.NETHERITE_SWORD) {
    override val odysseyDisplayName: String = "${ChatColor.AQUA}${ChatColor.ITALIC}$name"
    override val odysseyLore = listOf("${ChatColor.GOLD}${ChatColor.ITALIC}A sword made from neutronium bark")

    //
    override fun createItemStack(amount: Int): ItemStack {
        val newItem = super.createItemStack(amount)
        val newItemMeta = newItem.itemMeta

        val odysseyAttackDamageUUID: UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A28DB5CF")
        val odysseyAttackSpeedUUID: UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A29DB5CF")
        val someAttackDamageStat = AttributeModifier(odysseyAttackDamageUUID, "generic.attack_damage", 9.42, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
        val someAttackSpeedStat = AttributeModifier(odysseyAttackSpeedUUID, "generic.attack_speed", 1.62, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)

        newItemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, someAttackSpeedStat)
        newItemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, someAttackDamageStat)
        newItem.itemMeta = newItemMeta
        return newItem
    }

}

 */


object WolfsGravestone: OdysseyWeapon("Wolf's Gravestone", Material.NETHERITE_SWORD, OdysseyItemModels.ABZU_BLADE, 18.75, 0.7, WeaponTypes.SWORD)

object SoulSteelKatana: OdysseyWeapon("Soul Steel Katana", Material.IRON_SWORD, OdysseyItemModels.SOUL_STEEL_KATANA, 7.0, 1.5, WeaponTypes.SWORD) {

    override fun createItemStack(amount: Int): ItemStack {
        val newKatana = super.createItemStack(amount).also {
            val odysseyDurabilityStat = AttributeModifier(OdysseyUUIDs.DURABILITY_UUID, "odyssey.max_durability", 899.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
            it.itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, odysseyDurabilityStat)
        }
        // Assign item meta
        return newKatana
    }

}

object KineticBlaster : OdysseyItem("Kinetic Blaster",
    Material.BOW,
    odysseyDisplayName = Component.text("Kinetic Blaster", TextColor.color(39, 79, 152)),
    odysseyLore =  listOf(Component.text("A weapon commissioned to shoot kinetic projectiles", TextColor.color(39, 79, 152)).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)),
    customModel = OdysseyItemModels.KINETIC_BLASTER) {

    override fun createItemStack(amount: Int): ItemStack {
        val newKineticBlaster = super.createItemStack(amount)
        newKineticBlaster.itemMeta.also {
            it.addEnchant(Enchantment.ARROW_DAMAGE, 5, true)
            it.addEnchant(Enchantment.ARROW_INFINITE, 1, true)
            it.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true)
            it.addEnchant(Enchantment.DURABILITY, 3, true)
        }
        return newKineticBlaster

    }

}

// TODO: Make Pages that can add to personal necronomicon using components
// Original from Vail is to powerful to use
object Necronomicon : OdysseyItem("Necronomicon",
    Material.WRITTEN_BOOK,
    odysseyDisplayName = Component.text("Necronomicon", TextColor.color(39, 19, 92)),
    odysseyLore = null,
    customModel = OdysseyItemModels.NECRONOMICON_BOOK) {

    override fun createItemStack(amount: Int): ItemStack {
        val necronomiconBook = super.createItemStack(amount)
        val necronomiconBookMeta: BookMeta = necronomiconBook.itemMeta.clone() as BookMeta
        val necronomiconBookBuilder = necronomiconBookMeta.toBuilder()

        // Author component
        val authorComponent: TextComponent = Component.text("Vail", TextColor.color(255, 170, 0)).decorate(TextDecoration.OBFUSCATED)
        necronomiconBookBuilder.author(authorComponent)

        // Title component
        val titleComponent: TextComponent = Component.text("Necronomicon", TextColor.color(39, 19, 92)).decorate(TextDecoration.ITALIC)
        necronomiconBookBuilder.title(titleComponent)

        // First Page
        val firstPage: TextComponent = Component.text("Summon Undead", TextColor.color(115, 13, 59)).decorate(TextDecoration.UNDERLINED)
            .append(Component.text("A spell capable of summoning the dead"))
            .append(Component.text("At a significant cost to the caster..."))
            .append(Component.text(""))
            .append(Component.text("CAST")
                .color(TextColor.color(115, 13, 59))
                .decorate(TextDecoration.STRIKETHROUGH, TextDecoration.BOLD)
                .clickEvent(ClickEvent.runCommand("/necronomicon summon"))
            )
        necronomiconBookBuilder.addPage(firstPage)

        // Build
        val newBookMeta = necronomiconBookBuilder.build()
        newBookMeta.setCustomModelData(OdysseyItemModels.NECRONOMICON_BOOK)
        newBookMeta.generation = BookMeta.Generation.TATTERED
        necronomiconBook.itemMeta = newBookMeta
        return necronomiconBook
    }
}