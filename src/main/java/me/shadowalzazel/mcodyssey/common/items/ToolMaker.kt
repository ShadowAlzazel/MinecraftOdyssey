package me.shadowalzazel.mcodyssey.common.items

import io.papermc.paper.datacomponent.DataComponentBuilder
import io.papermc.paper.datacomponent.DataComponentTypes
import io.papermc.paper.datacomponent.item.AttackRange
import io.papermc.paper.datacomponent.item.BlocksAttacks
import io.papermc.paper.datacomponent.item.Consumable
import io.papermc.paper.datacomponent.item.KineticWeapon
import io.papermc.paper.datacomponent.item.PiercingWeapon
import io.papermc.paper.datacomponent.item.SwingAnimation
import io.papermc.paper.datacomponent.item.UseEffects
import io.papermc.paper.datacomponent.item.Weapon
import io.papermc.paper.datacomponent.item.blocksattacks.DamageReduction
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.TypedKey
import io.papermc.paper.registry.keys.DamageTypeKeys
import io.papermc.paper.registry.set.RegistryKeySet
import io.papermc.paper.registry.set.RegistrySet
import me.shadowalzazel.mcodyssey.util.AttributeManager
import me.shadowalzazel.mcodyssey.util.DataTagManager
import me.shadowalzazel.mcodyssey.util.ToolComponentHelper
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import me.shadowalzazel.mcodyssey.util.constants.ItemDataTags
import me.shadowalzazel.mcodyssey.util.constants.WeaponMaps
import net.kyori.adventure.key.Namespaced
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.minecraft.sounds.SoundEvent
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.damage.DamageType
import org.bukkit.inventory.ItemStack

@Suppress("UnstableApiUsage")
interface ToolMaker : AttributeManager, DataTagManager, ToolComponentHelper {

    fun createToolStack(material: ToolMaterial, type: ToolType, amount: Int = 1): ItemStack {
        val otherTools = listOf(ToolType.SHURIKEN)
        val minecraftItemKey = if (type in otherTools) {
            type.vanillaBase // Get item key from tool
        } else {
            "${material.vanillaBase}_${type.vanillaBase}"
        }
        val minecraftMaterial = Material.matchMaterial(minecraftItemKey) ?: return ItemStack(Material.AIR)
        val itemStack = ItemStack(minecraftMaterial, amount).apply {
            // Create Variables
            val itemName = "${material.nameId}_${type.toolName}"
            val upperName = "${material.customName} ${type.fullName}"
            val customName = Component.text(upperName).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            val damage = material.attackDamage + type.baseDamage
            val maxDurability = material.maxDurability
            var speed = type.baseSpeed
            val bonusRange = type.bonusRange
            val itemModel = createOdysseyKey(itemName)
            // Iridium and titanium have different speeds
            if (material == ToolMaterial.IRIDIUM) {
                speed *= 0.9
            }
            else if (material == ToolMaterial.TITANIUM || material == ToolMaterial.ANODIZED_TITANIUM) {
                speed *= 1.1
            }
            // ------------------------ADD COMPONENTS---------------------------------

            // Set item names and models
            this.setData(DataComponentTypes.ITEM_MODEL, itemModel)
            this.setData(DataComponentTypes.CUSTOM_NAME, customName)
            this.setData(DataComponentTypes.ITEM_NAME, Component.text(itemName))
            // Set type and material identifiers
            this.setStringTag("item", itemName) // ItemKey
            this.setStringTag(ItemDataTags.TOOL_TYPE, type.toolName)
            this.setStringTag(ItemDataTags.MATERIAL_TYPE, material.nameId)

            // Tool and Mining
            if (maxDurability != null) this.setData(DataComponentTypes.MAX_DAMAGE, maxDurability)
            // Tools with mining ToolComponent
            val mineableTags = getMiningTags(type.toolName)
            if (mineableTags != null) {
                val newToolComponent = newToolComponent(material.nameId, type.toolName)
                if (newToolComponent != null) {
                    this.resetData(DataComponentTypes.TOOL)
                    this.setData(DataComponentTypes.TOOL, newToolComponent)
                }
            }

            // Assign Base Combat Attributes
            this.addAttackDamageAttribute(damage, AttributeTags.ITEM_BASE_ATTACK_DAMAGE)
            this.setNewAttackSpeedAttribute(speed)

            // Pure weapons cant mine any blocks
            if (mineableTags == null) {
                if (!this.hasData(DataComponentTypes.TOOL)) {
                    val weaponData = Weapon.weapon()
                        .itemDamagePerAttack(1)

                    // Check can disable blocking
                    val disableBlockingTime = WeaponMaps.DISABLE_SHIELDS[type.toolName]  // Is In Ticks
                    if (disableBlockingTime != null) {
                        weaponData.disableBlockingForSeconds(disableBlockingTime / 20F)
                    }
                    // Build Weapon Data
                    this.setData(DataComponentTypes.WEAPON, weaponData.build())
                }
            }

            // ---------------------------------------------------------
            // Assign Special Combat Components

            // Damage Types one of (Slash, Pierce, Blunt)
            val defaultDamageType = DamageType.PLAYER_ATTACK
            if (type.toolName in WeaponMaps.PIERCING_WEAPONS) {
                // Set Damage type
                val piercingDamageType = DamageType.SPEAR
                this.setData(DataComponentTypes.DAMAGE_TYPE, piercingDamageType)
                // Set piercing weapon
                val piercingData = PiercingWeapon.piercingWeapon()
                    .dealsKnockback(true)
                    .build()
                this.setData(DataComponentTypes.PIERCING_WEAPON, piercingData)
            }
            else if (type.toolName in WeaponMaps.SLASHING_WEAPONS) {
                val slashingDamageType = DamageType.PLAYER_ATTACK
                this.setData(DataComponentTypes.DAMAGE_TYPE, slashingDamageType)
            }
            else if (type.toolName in WeaponMaps.BLUNT_WEAPONS) {
                val bluntDamageType = DamageType.MACE_SMASH
                this.setData(DataComponentTypes.DAMAGE_TYPE, bluntDamageType)
            } else {
                this.setData(DataComponentTypes.DAMAGE_TYPE, defaultDamageType)
            }

            // Change swing animation
            if (type.hasSwingAnimation()) {
                var animationDuration = 12 // In Ticks
                var animationType = SwingAnimation.Animation.WHACK
                // Check if other animation types
                if (WeaponMaps.STAB_ANIMATION.contains(type.toolName)) {
                    animationType = SwingAnimation.Animation.STAB
                    animationDuration = WeaponMaps.STAB_ANIMATION[type.toolName]!!
                } else if (WeaponMaps.WHACK_ANIMATION.contains(type.toolName)) {
                    animationDuration = WeaponMaps.WHACK_ANIMATION[type.toolName]!!
                }
                // Build
                val swingAnimation = SwingAnimation.swingAnimation()
                    .type(animationType)
                    .duration(animationDuration) // In Ticks
                    .build()
                this.setData(DataComponentTypes.SWING_ANIMATION, swingAnimation)
            }

            // Interaction and Attack Ranges
            if (bonusRange != null) {
                this.addEntityRangeAttribute(bonusRange, AttributeTags.ITEM_BASE_ENTITY_RANGE)
                val trueRange = (3.0 + bonusRange).toFloat()
                val hitboxAssist = WeaponMaps.HITBOX_ASSIST[type.toolName] ?: 0.3F
                // Create Range Component
                val attackRange = AttackRange.attackRange()
                    .hitboxMargin(hitboxAssist)
                    .minReach(0.0F)
                    .maxReach(trueRange)
                    .mobFactor(0.85F)
                    .build()
                this.setData(DataComponentTypes.ATTACK_RANGE, attackRange)
            }

            // Change Spear mechanics
            if (type.vanillaBase == "spear" && type != ToolType.SPEAR) {
                // Remove old spear data first
                this.unsetData(DataComponentTypes.KINETIC_WEAPON)
            }

            // If kinetic charge
            if (type.canKineticCharge()) {
                this.unsetData(DataComponentTypes.KINETIC_WEAPON)
                val kineticWeapon = KineticWeapon.kineticWeapon()
                    .delayTicks(10)
                    .damageConditions(
                        KineticWeapon.condition(
                            100,
                            0.0F,
                            0.0F
                        )
                    )
                    .knockbackConditions(
                        KineticWeapon.condition(
                            60,
                            0.0F,
                            0.0F
                        )
                    )
                // Check if Pike
                if (type == ToolType.PIKE) {
                    kineticWeapon.damageMultiplier(1.5F) // 50% Damage * Speed
                }

                this.setData(DataComponentTypes.KINETIC_WEAPON, kineticWeapon.build())
            }

            // Assign parry/blocking
            if (type.canParry()) {
                val damageReductions = DamageReduction.damageReduction()
                    .base(0.0F)
                    .factor(0.5F) // Negates 50% damage on parry/block
                    .type(
                        RegistrySet.keySet(RegistryKey.DAMAGE_TYPE,
                            listOf(TypedKey.create(RegistryKey.DAMAGE_TYPE, "mob_attack"),
                            TypedKey.create(RegistryKey.DAMAGE_TYPE, "mob_attack_no_aggro"),
                            TypedKey.create(RegistryKey.DAMAGE_TYPE, "player_attack"))
                        )
                    )
                    .build()
                val blockingAttacks = BlocksAttacks.blocksAttacks()
                    .disableCooldownScale(1F)
                    .damageReductions(listOf(damageReductions))
                    .blockSound(NamespacedKey.minecraft("block.anvil.place"))
                    //.bypassedBy(DamageType.MAGIC)
                    .build()
                this.setData(DataComponentTypes.BLOCKS_ATTACKS, blockingAttacks)
            }

            // Minimum Charge (noob protection)
            if (WeaponMaps.MINIMUM_CHARGE.containsKey(type.toolName)) {
                val minCharge = WeaponMaps.MINIMUM_CHARGE[type.toolName]!!
                this.setData(DataComponentTypes.MINIMUM_ATTACK_CHARGE, minCharge.toFloat())
            }

            // Unique
            if (type.toolName == "battlesaw") { //BATTLESAW
                // Special Hitbox Mechanic
                this.unsetData(DataComponentTypes.KINETIC_WEAPON)
                val kineticWeapon = KineticWeapon.kineticWeapon()
                    .delayTicks(16)
                    .damageConditions(
                        KineticWeapon.condition(
                            200,
                            0.0F,
                            0.0F
                        )
                    )
                    .knockbackConditions(
                        KineticWeapon.condition(
                            60,
                            0.0F,
                            0.0F
                        )
                    )
                    .build()
                this.setData(DataComponentTypes.KINETIC_WEAPON, kineticWeapon)
                // Speed
                val useEffects = UseEffects.useEffects()
                    .canSprint(true)
                    .speedMultiplier(0.6F)
                    .build()
                this.setData(DataComponentTypes.USE_EFFECTS, useEffects)
            }

            // Finish
        }
        return itemStack
    }

}