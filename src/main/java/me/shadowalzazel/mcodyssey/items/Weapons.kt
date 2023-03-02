package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.Identifiers
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import me.shadowalzazel.mcodyssey.items.utility.WeaponMaterial
import me.shadowalzazel.mcodyssey.items.utility.WeaponType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

object Weapons {

    // Weapon (Odyssey Item) is a holder for enums
    // Used to create multiple weapons of different materials from the same type
    fun OdysseyItem.createWeapon(damageOverride: Double = 0.0): ItemStack {
        val newWeapon = this.createItemStack(1)
        newWeapon.itemMeta = newWeapon.itemMeta.also {
            if (weaponMaterial != null && weaponType != null) {
                // For Damage
                val newDamage = weaponType.baseDamage + weaponMaterial.damage + damageOverride
                val newDamageModifier = AttributeModifier(Identifiers.ATTACK_DAMAGE_UUID, "odyssey.attack_damage", newDamage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
                it.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, newDamageModifier)
                // For Attack Speed
                val resetSpeedModifier = AttributeModifier(Identifiers.ATTACK_SPEED_RESET_UUID, "odyssey.attack_speed", -4.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
                val newSpeedModifier = AttributeModifier(Identifiers.ATTACK_SPEED_UUID, "odyssey.attack_speed", weaponType.baseAttackSpeed, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND)
                it.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, resetSpeedModifier)
                it.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, newSpeedModifier)
                // Set Appearances
                it.displayName(Component.text(weaponMaterial.namePrefix + " " + weaponType.baseName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                if (customModel != null) { it.setCustomModelData(customModel) } else { it.setCustomModelData(weaponType.model) }
            }
        }
        return newWeapon
    }

    // ---------------------------------------SWORDS---------------------------------------------

    // Katanas
    val WOODEN_KATANA = OdysseyItem(
        name = "wooden_katana",
        material = Material.WOODEN_SWORD,
        weaponMaterial = WeaponMaterial.WOOD,
        weaponType = WeaponType.KATANA)

    val GOLDEN_KATANA = OdysseyItem(
        name = "golden_katana",
        material = Material.GOLDEN_SWORD,
        weaponMaterial = WeaponMaterial.GOLD,
        weaponType = WeaponType.KATANA)

    val STONE_KATANA = OdysseyItem(
        name = "stone_katana",
        material = Material.STONE_SWORD,
        weaponMaterial = WeaponMaterial.STONE,
        weaponType = WeaponType.KATANA)

    val IRON_KATANA = OdysseyItem(
        name = "iron_katana",
        material = Material.IRON_SWORD,
        weaponMaterial = WeaponMaterial.IRON,
        weaponType = WeaponType.KATANA)

    val DIAMOND_KATANA = OdysseyItem(
        name = "diamond_katana",
        material = Material.DIAMOND_SWORD,
        weaponMaterial = WeaponMaterial.DIAMOND,
        weaponType = WeaponType.KATANA)

    val SOUL_STEEL_KATANA = OdysseyItem(
        name = "soul_steel_katana",
        material = Material.IRON_SWORD,
        customModel = ItemModels.SOUL_STEEL_KATANA,
        weaponMaterial = WeaponMaterial.SOUL_STEEL,
        weaponType = WeaponType.KATANA)

    // Claymores
    val WOODEN_CLAYMORE = OdysseyItem(
        name = "wooden_claymore",
        material = Material.WOODEN_SWORD,
        weaponMaterial = WeaponMaterial.WOOD,
        weaponType = WeaponType.CLAYMORE)

    val GOLDEN_CLAYMORE = OdysseyItem(
        name = "golden_claymore",
        material = Material.GOLDEN_SWORD,
        weaponMaterial = WeaponMaterial.GOLD,
        weaponType = WeaponType.CLAYMORE)

    val STONE_CLAYMORE = OdysseyItem(
        name = "stone_claymore",
        material = Material.STONE_SWORD,
        weaponMaterial = WeaponMaterial.STONE,
        weaponType = WeaponType.CLAYMORE)

    val IRON_CLAYMORE = OdysseyItem(
        name = "iron_claymore",
        material = Material.IRON_SWORD,
        weaponMaterial = WeaponMaterial.IRON,
        weaponType = WeaponType.CLAYMORE)

    val DIAMOND_CLAYMORE = OdysseyItem(
        name = "diamond_claymore",
        material = Material.DIAMOND_SWORD,
        weaponMaterial = WeaponMaterial.DIAMOND,
        weaponType = WeaponType.CLAYMORE)

    // Rapiers
    val WOODEN_RAPIER = OdysseyItem(
        name = "wooden_rapier",
        material = Material.WOODEN_SWORD,
        weaponMaterial = WeaponMaterial.WOOD,
        weaponType = WeaponType.RAPIER)

    val GOLDEN_RAPIER= OdysseyItem(
        name = "golden_rapier",
        material = Material.GOLDEN_SWORD,
        weaponMaterial = WeaponMaterial.GOLD,
        weaponType = WeaponType.RAPIER)

    val STONE_RAPIER = OdysseyItem(
        name = "stone_rapier",
        material = Material.STONE_SWORD,
        weaponMaterial = WeaponMaterial.STONE,
        weaponType = WeaponType.RAPIER)

    val IRON_RAPIER = OdysseyItem(
        name = "iron_rapier",
        material = Material.IRON_SWORD,
        weaponMaterial = WeaponMaterial.IRON,
        weaponType = WeaponType.RAPIER)

    val DIAMOND_RAPIER = OdysseyItem(
        name = "diamond_rapier",
        material = Material.DIAMOND_SWORD,
        weaponMaterial = WeaponMaterial.DIAMOND,
        weaponType = WeaponType.RAPIER)

    // Cutlass
    val WOODEN_CUTLASS = OdysseyItem(
        name = "wooden_cutlass",
        material = Material.WOODEN_SWORD,
        weaponMaterial = WeaponMaterial.WOOD,
        weaponType = WeaponType.CUTLASS)

    val GOLDEN_CUTLASS = OdysseyItem(
        name = "golden_cutlass",
        material = Material.GOLDEN_SWORD,
        weaponMaterial = WeaponMaterial.GOLD,
        weaponType = WeaponType.CUTLASS)

    val STONE_CUTLASS = OdysseyItem(
        name = "stone_cutlass",
        material = Material.STONE_SWORD,
        weaponMaterial = WeaponMaterial.STONE,
        weaponType = WeaponType.CUTLASS)

    val IRON_CUTLASS = OdysseyItem(
        name = "iron_cutlass",
        material = Material.IRON_SWORD,
        weaponMaterial = WeaponMaterial.IRON,
        weaponType = WeaponType.CUTLASS)

    val DIAMOND_CUTLASS = OdysseyItem(
        name = "diamond_cutlass",
        material = Material.DIAMOND_SWORD,
        weaponMaterial = WeaponMaterial.DIAMOND,
        weaponType = WeaponType.CUTLASS)

    // Sabers
    val WOODEN_SABER = OdysseyItem(
        name = "wooden_saber",
        material = Material.WOODEN_SWORD,
        weaponMaterial = WeaponMaterial.WOOD,
        weaponType = WeaponType.SABER)

    val GOLDEN_SABER = OdysseyItem(
        name = "golden_saber",
        material = Material.GOLDEN_SWORD,
        weaponMaterial = WeaponMaterial.GOLD,
        weaponType = WeaponType.SABER)

    val STONE_SABER = OdysseyItem(
        name = "stone_saber",
        material = Material.STONE_SWORD,
        weaponMaterial = WeaponMaterial.STONE,
        weaponType = WeaponType.SABER)

    val IRON_SABER = OdysseyItem(
        name = "iron_saber",
        material = Material.IRON_SWORD,
        weaponMaterial = WeaponMaterial.IRON,
        weaponType = WeaponType.SABER)

    val DIAMOND_SABER = OdysseyItem(
        name = "diamond_saber",
        material = Material.DIAMOND_SWORD,
        weaponMaterial = WeaponMaterial.DIAMOND,
        weaponType = WeaponType.SABER)

    // Zweihanders
    val NETHERITE_ZWEIHANDER = OdysseyItem(
        name = "netherite_zweihander",
        material = Material.NETHERITE_SWORD,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.ZWEIHANDER)


    // ---------------------------------------SPEARS--------------------------------------------

    // Spears
    val WOODEN_SPEAR = OdysseyItem(
        name = "wooden_spear",
        material = Material.WOODEN_SHOVEL,
        weaponMaterial = WeaponMaterial.WOOD,
        weaponType = WeaponType.SPEAR)

    val GOLDEN_SPEAR = OdysseyItem(
        name = "golden_spear",
        material = Material.GOLDEN_SHOVEL,
        weaponMaterial = WeaponMaterial.GOLD,
        weaponType = WeaponType.SPEAR)

    val STONE_SPEAR= OdysseyItem(
        name = "stone_spear",
        material = Material.STONE_SHOVEL,
        weaponMaterial = WeaponMaterial.STONE,
        weaponType = WeaponType.SPEAR)

    val IRON_SPEAR = OdysseyItem(
        name = "iron_spear",
        material = Material.IRON_SHOVEL,
        weaponMaterial = WeaponMaterial.IRON,
        weaponType = WeaponType.SPEAR)

    val DIAMOND_SPEAR = OdysseyItem(
        name = "diamond_spear",
        material = Material.DIAMOND_SHOVEL,
        weaponMaterial = WeaponMaterial.DIAMOND,
        weaponType = WeaponType.SPEAR)

    // Halberds
    val WOODEN_HALBERD = OdysseyItem(
        name = "wooden_halberd",
        material = Material.WOODEN_SHOVEL,
        weaponMaterial = WeaponMaterial.WOOD,
        weaponType = WeaponType.HALBERD)

    val GOLDEN_HALBERD = OdysseyItem(
        name = "golden_halberd",
        material = Material.GOLDEN_SHOVEL,
        weaponMaterial = WeaponMaterial.GOLD,
        weaponType = WeaponType.HALBERD)

    val STONE_HALBERD = OdysseyItem(
        name = "stone_halberd",
        material = Material.STONE_SHOVEL,
        weaponMaterial = WeaponMaterial.STONE,
        weaponType = WeaponType.HALBERD)

    val IRON_HALBERD = OdysseyItem(
        name = "iron_halberd",
        material = Material.IRON_SHOVEL,
        weaponMaterial = WeaponMaterial.IRON,
        weaponType = WeaponType.HALBERD)

    val DIAMOND_HALBERD = OdysseyItem(
        name = "diamond_halberd",
        material = Material.DIAMOND_SHOVEL,
        weaponMaterial = WeaponMaterial.DIAMOND,
        weaponType = WeaponType.HALBERD)

    // ---------------------------------------KNIFES--------------------------------------------

    // Daggers
    val WOODEN_DAGGER = OdysseyItem(
        name = "wooden_dagger",
        material = Material.WOODEN_SWORD,
        weaponMaterial = WeaponMaterial.WOOD,
        weaponType = WeaponType.DAGGER)

    val GOLDEN_DAGGER = OdysseyItem(
        name = "golden_dagger",
        material = Material.GOLDEN_SWORD,
        weaponMaterial = WeaponMaterial.GOLD,
        weaponType = WeaponType.DAGGER)

    val STONE_DAGGER = OdysseyItem(
        name = "stone_dagger",
        material = Material.STONE_SWORD,
        weaponMaterial = WeaponMaterial.STONE,
        weaponType = WeaponType.DAGGER)

    val IRON_DAGGER = OdysseyItem(
        name = "iron_dagger",
        material = Material.IRON_SWORD,
        weaponMaterial = WeaponMaterial.IRON,
        weaponType = WeaponType.DAGGER)

    val DIAMOND_DAGGER = OdysseyItem(
        name = "diamond_dagger",
        material = Material.DIAMOND_SWORD,
        weaponMaterial = WeaponMaterial.DIAMOND,
        weaponType = WeaponType.DAGGER)

    // -----------------------------------------AXES--------------------------------------------

    // Long Axe
    val WOODEN_LONG_AXE = OdysseyItem(
        name = "wooden_long_axe",
        material = Material.WOODEN_AXE,
        weaponMaterial = WeaponMaterial.WOOD,
        weaponType = WeaponType.LONG_AXE)

    val GOLDEN_LONG_AXE = OdysseyItem(
        name = "golden_long_axe",
        material = Material.GOLDEN_AXE,
        weaponMaterial = WeaponMaterial.GOLD,
        weaponType = WeaponType.LONG_AXE)

    val STONE_LONG_AXE = OdysseyItem(
        name = "stone_long_axe",
        material = Material.STONE_AXE,
        weaponMaterial = WeaponMaterial.STONE,
        weaponType = WeaponType.LONG_AXE)

    val IRON_LONG_AXE = OdysseyItem(
        name = "iron_long_axe",
        material = Material.IRON_AXE,
        weaponMaterial = WeaponMaterial.IRON,
        weaponType = WeaponType.LONG_AXE)

    val DIAMOND_LONG_AXE = OdysseyItem(
        name = "diamond_long_axe",
        material = Material.WOODEN_AXE,
        weaponMaterial = WeaponMaterial.DIAMOND,
        weaponType = WeaponType.LONG_AXE)

    // --------------------------------------POLE-ARMS------------------------------------------

    // War Hammer
    val WOODEN_WARHAMMER = OdysseyItem(
        name = "wooden_warhammer",
        material = Material.WOODEN_PICKAXE,
        weaponMaterial = WeaponMaterial.WOOD,
        weaponType = WeaponType.WARHAMMER)

    val GOLDEN_WARHAMMER = OdysseyItem(
        name = "golden_warhammer",
        material = Material.GOLDEN_PICKAXE,
        weaponMaterial = WeaponMaterial.GOLD,
        weaponType = WeaponType.WARHAMMER)

    val STONE_WARHAMMER = OdysseyItem(
        name = "stone_warhammer",
        material = Material.STONE_PICKAXE,
        weaponMaterial = WeaponMaterial.STONE,
        weaponType = WeaponType.WARHAMMER)

    val IRON_WARHAMMER = OdysseyItem(
        name = "iron_warhammer",
        material = Material.IRON_PICKAXE,
        weaponMaterial = WeaponMaterial.IRON,
        weaponType = WeaponType.WARHAMMER)

    val DIAMOND_WARHAMMER = OdysseyItem(
        name = "diamond_warhammer",
        material = Material.DIAMOND_PICKAXE,
        weaponMaterial = WeaponMaterial.DIAMOND,
        weaponType = WeaponType.WARHAMMER)

    // --------------------------------------STAFFS---------------------------------------------

    // Staff
    val BAMBOO_STAFF = OdysseyItem(
        name = "bamboo_staff",
        material = Material.STONE_SHOVEL,
        weaponMaterial = WeaponMaterial.BAMBOO,
        weaponType = WeaponType.STAFF)

    val BONE_STAFF = OdysseyItem(
        name = "bone_staff",
        material = Material.STONE_SHOVEL,
        weaponMaterial = WeaponMaterial.BONE,
        weaponType = WeaponType.STAFF)

    val WOODEN_STAFF = OdysseyItem(
        name = "wooden_staff",
        material = Material.STONE_SHOVEL,
        weaponMaterial = WeaponMaterial.WOOD,
        weaponType = WeaponType.STAFF)

    val BLAZE_ROD_STAFF = OdysseyItem(
        name = "blaze_rod_staff",
        material = Material.STONE_SHOVEL,
        weaponMaterial = WeaponMaterial.BLAZE_ROD,
        weaponType = WeaponType.STAFF)

    // --------------------------------------CLUBS----------------------------------------------

    // --------------------------------------OTHERS---------------------------------------------

    // Exotics
    val ABZU_BLADE = OdysseyItem(
        name = "TODO!!",
        material = Material.DIAMOND_SWORD,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.STAFF) // TODO: Change to Sealed Class

    // Bosses
    val KINETIC_BLASTER = OdysseyItem(
        name = "TODO!!",
        material = Material.DIAMOND_SWORD,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.STAFF) // TODO: Change to Sealed Class



}