package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.attributes.AttributeManager
import me.shadowalzazel.mcodyssey.constants.AttributeTags
import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import me.shadowalzazel.mcodyssey.items.utility.WeaponMaterial
import me.shadowalzazel.mcodyssey.items.utility.WeaponType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Weapons: AttributeManager {

    // DeprecatedWeapon (Odyssey Item) is a holder for enums
    // Add Recipe to show and hide attributes

    // Used to create multiple weapons of different materials from the same type
    fun OdysseyItem.createWeapon(damageOverride: Double = 0.0): ItemStack {
        if (weaponMaterial == null) { return ItemStack(Material.AIR) }
        if (weaponType == null) { return ItemStack(Material.AIR) }

        val newWeapon = this.createItemStack(1).apply {
            // Attributes
            val damage = weaponType.baseDamage + weaponMaterial.damage + damageOverride
            addAttackDamageAttribute(damage, AttributeTags.ITEM_BASE_ATTACK_DAMAGE)
            val speed = weaponType.baseAttackSpeed
            setNewAttackSpeedAttribute(speed, AttributeTags.ITEM_BASE_ATTACK_SPEED)
        }
        newWeapon.itemMeta = newWeapon.itemMeta.also {
            // Set Name
            val content = weaponMaterial.namePrefix + " " + weaponType.baseName
            it.displayName(Component.text(content).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
            // Set Appearances
            val model = customModel ?: weaponType.model
            it.setCustomModelData(model)
            //it.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        }
        return newWeapon
    }

    /* ---------------------------------------------------------------------------------------------- */
    /* --------------------------------------------SWORDS-------------------------------------------- */
    /* ---------------------------------------------------------------------------------------------- */

    /*-----------------------------------------------------------------------------------------------*/

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

    val NETHERITE_KATANA = OdysseyItem(
        name = "diamond_katana",
        material = Material.NETHERITE_SWORD,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.KATANA)

    val SOUL_STEEL_KATANA = OdysseyItem(
        name = "soul_steel_katana",
        material = Material.IRON_SWORD,
        customModel = ItemModels.SOUL_STEEL_KATANA,
        weaponMaterial = WeaponMaterial.SOUL_STEEL,
        weaponType = WeaponType.KATANA)

    /*-----------------------------------------------------------------------------------------------*/

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

    val NETHERITE_CLAYMORE = OdysseyItem(
        name = "netherite_claymore",
        material = Material.NETHERITE_SWORD,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.CLAYMORE)

    /*-----------------------------------------------------------------------------------------------*/

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

    val NETHERITE_RAPIER = OdysseyItem(
        name = "netherite_rapier",
        material = Material.NETHERITE_SWORD,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.RAPIER)

    /*-----------------------------------------------------------------------------------------------*/

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

    val NETHERITE_CUTLASS = OdysseyItem(
        name = "netherite_cutlass",
        material = Material.NETHERITE_SWORD,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.CUTLASS)

    /*-----------------------------------------------------------------------------------------------*/

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

    val NETHERITE_SABER = OdysseyItem(
        name = "netherite_saber",
        material = Material.NETHERITE_SWORD,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.SABER)

    /* ---------------------------------------------------------------------------------------------- */
    /* --------------------------------------------SPEARS-------------------------------------------- */
    /* ---------------------------------------------------------------------------------------------- */

    /*-----------------------------------------------------------------------------------------------*/
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

    val NETHERITE_SPEAR = OdysseyItem(
        name = "netherite_spear",
        material = Material.NETHERITE_SHOVEL,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.SPEAR)

    /*-----------------------------------------------------------------------------------------------*/
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

    val NETHERITE_HALBERD = OdysseyItem(
        name = "netherite_halberd",
        material = Material.NETHERITE_SHOVEL,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.HALBERD)

    /*-----------------------------------------------------------------------------------------------*/
    val WOODEN_LANCE = OdysseyItem(
        name = "wooden_lance",
        material = Material.WOODEN_SHOVEL,
        weaponMaterial = WeaponMaterial.WOOD,
        weaponType = WeaponType.LANCE)

    val GOLDEN_LANCE = OdysseyItem(
        name = "golden_lance",
        material = Material.GOLDEN_SHOVEL,
        weaponMaterial = WeaponMaterial.GOLD,
        weaponType = WeaponType.LANCE)

    val STONE_LANCE = OdysseyItem(
        name = "stone_lance",
        material = Material.STONE_SHOVEL,
        weaponMaterial = WeaponMaterial.STONE,
        weaponType = WeaponType.LANCE)

    val IRON_LANCE = OdysseyItem(
        name = "iron_lance",
        material = Material.IRON_SHOVEL,
        weaponMaterial = WeaponMaterial.IRON,
        weaponType = WeaponType.LANCE)

    val DIAMOND_LANCE = OdysseyItem(
        name = "diamond_lance",
        material = Material.DIAMOND_SHOVEL,
        weaponMaterial = WeaponMaterial.DIAMOND,
        weaponType = WeaponType.LANCE)

    val NETHERITE_LANCE = OdysseyItem(
        name = "netherite_lance",
        material = Material.NETHERITE_SHOVEL,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.LANCE)

    /* ---------------------------------------------------------------------------------------------- */
    /* --------------------------------------------KNIFES-------------------------------------------- */
    /* ---------------------------------------------------------------------------------------------- */

    /*-----------------------------------------------------------------------------------------------*/
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

    val NETHERITE_DAGGER = OdysseyItem(
        name = "netherite_dagger",
        material = Material.NETHERITE_SWORD,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.DAGGER)

    /*-----------------------------------------------------------------------------------------------*/
    val WOODEN_SICKLE = OdysseyItem(
        name = "wooden_sickle",
        material = Material.WOODEN_SWORD,
        weaponMaterial = WeaponMaterial.WOOD,
        weaponType = WeaponType.SICKLE)

    val GOLDEN_SICKLE = OdysseyItem(
        name = "golden_sickle",
        material = Material.GOLDEN_SWORD,
        weaponMaterial = WeaponMaterial.GOLD,
        weaponType = WeaponType.SICKLE)

    val STONE_SICKLE = OdysseyItem(
        name = "stone_sickle",
        material = Material.STONE_SWORD,
        weaponMaterial = WeaponMaterial.STONE,
        weaponType = WeaponType.SICKLE)

    val IRON_SICKLE = OdysseyItem(
        name = "iron_sickle",
        material = Material.IRON_SWORD,
        weaponMaterial = WeaponMaterial.IRON,
        weaponType = WeaponType.SICKLE)

    val DIAMOND_SICKLE = OdysseyItem(
        name = "diamond_sickle",
        material = Material.DIAMOND_SWORD,
        weaponMaterial = WeaponMaterial.DIAMOND,
        weaponType = WeaponType.SICKLE)

    val NETHERITE_SICKLE = OdysseyItem(
        name = "netherite_sickle",
        material = Material.NETHERITE_SWORD,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.SICKLE)

    /*-----------------------------------------------------------------------------------------------*/
    val WOODEN_CHAKRAM = OdysseyItem(
        name = "wooden_chakram",
        material = Material.WOODEN_SWORD,
        weaponMaterial = WeaponMaterial.WOOD,
        weaponType = WeaponType.CHAKRAM)

    val GOLDEN_CHAKRAM = OdysseyItem(
        name = "golden_chakram",
        material = Material.GOLDEN_SWORD,
        weaponMaterial = WeaponMaterial.GOLD,
        weaponType = WeaponType.CHAKRAM)

    val STONE_CHAKRAM = OdysseyItem(
        name = "stone_chakram",
        material = Material.STONE_SWORD,
        weaponMaterial = WeaponMaterial.STONE,
        weaponType = WeaponType.CHAKRAM)

    val IRON_CHAKRAM = OdysseyItem(
        name = "iron_chakram",
        material = Material.IRON_SWORD,
        weaponMaterial = WeaponMaterial.IRON,
        weaponType = WeaponType.CHAKRAM)

    val DIAMOND_CHAKRAM = OdysseyItem(
        name = "diamond_chakram",
        material = Material.DIAMOND_SWORD,
        weaponMaterial = WeaponMaterial.DIAMOND,
        weaponType = WeaponType.CHAKRAM)

    val NETHERITE_CHAKRAM = OdysseyItem(
        name = "netherite_chakram",
        material = Material.NETHERITE_SWORD,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.CHAKRAM)

    /* ---------------------------------------------------------------------------------------------- */
    /* ---------------------------------------------AXES--------------------------------------------- */
    /* ---------------------------------------------------------------------------------------------- */

    /*-----------------------------------------------------------------------------------------------*/
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
        material = Material.DIAMOND_AXE,
        weaponMaterial = WeaponMaterial.DIAMOND,
        weaponType = WeaponType.LONG_AXE)

    val NETHERITE_LONG_AXE = OdysseyItem(
        name = "netherite_long_axe",
        material = Material.NETHERITE_AXE,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.LONG_AXE)

    /* ---------------------------------------------------------------------------------------------- */
    /* -----------------------------------------POLE-ARMS-------------------------------------------- */
    /* ---------------------------------------------------------------------------------------------- */

    /*-----------------------------------------------------------------------------------------------*/
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

    val NETHERITE_WARHAMMER = OdysseyItem(
        name = "netherite_warhammer",
        material = Material.NETHERITE_PICKAXE,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.WARHAMMER)

    /*-----------------------------------------------------------------------------------------------*/
    val WOODEN_SCYTHE = OdysseyItem(
        name = "wooden_scythe",
        material = Material.WOODEN_HOE,
        weaponMaterial = WeaponMaterial.WOOD,
        weaponType = WeaponType.SCYTHE)

    val GOLDEN_SCYTHE = OdysseyItem(
        name = "golden_scythe",
        material = Material.GOLDEN_HOE,
        weaponMaterial = WeaponMaterial.GOLD,
        weaponType = WeaponType.SCYTHE)

    val STONE_SCYTHE = OdysseyItem(
        name = "stone_scythe",
        material = Material.STONE_HOE,
        weaponMaterial = WeaponMaterial.STONE,
        weaponType = WeaponType.SCYTHE)

    val IRON_SCYTHE = OdysseyItem(
        name = "iron_scythe",
        material = Material.IRON_HOE,
        weaponMaterial = WeaponMaterial.IRON,
        weaponType = WeaponType.SCYTHE)

    val DIAMOND_SCYTHE = OdysseyItem(
        name = "diamond_scythe",
        material = Material.DIAMOND_HOE,
        weaponMaterial = WeaponMaterial.DIAMOND,
        weaponType = WeaponType.SCYTHE)


    /* ---------------------------------------------------------------------------------------------- */
    /* --------------------------------------------STAFFS-------------------------------------------- */
    /* ---------------------------------------------------------------------------------------------- */

    /*-----------------------------------------------------------------------------------------------*/
    /*-----------------------------------------------------------------------------------------------*/
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

    /* ---------------------------------------------------------------------------------------------- */
    /* ---------------------------------------------CLUBS-------------------------------------------- */
    /* ---------------------------------------------------------------------------------------------- */

    /* ---------------------------------------------------------------------------------------------- */
    /* --------------------------------------------OTHERS-------------------------------------------- */
    /* ---------------------------------------------------------------------------------------------- */
    // Change to Sealed Class

    // Exotics
    val ABZU_BLADE = OdysseyItem(
        name = "TODO!!",
        material = Material.DIAMOND_SWORD,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.STAFF)

    // Zweihanders
    val NETHERITE_ZWEIHANDER = OdysseyItem(
        name = "netherite_zweihander",
        material = Material.NETHERITE_SWORD,
        weaponMaterial = WeaponMaterial.NETHERITE,
        weaponType = WeaponType.ZWEIHANDER)
}
