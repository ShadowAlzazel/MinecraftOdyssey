package me.shadowalzazel.mcodyssey.common.mobs.hostile

import me.shadowalzazel.mcodyssey.common.items.ToolMaterial
import me.shadowalzazel.mcodyssey.common.items.ToolType
import me.shadowalzazel.mcodyssey.common.mobs.base.OdysseyMob
import me.shadowalzazel.mcodyssey.util.EquipmentRandomizer
import me.shadowalzazel.mcodyssey.util.constants.AttributeTags
import net.kyori.adventure.text.Component
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Skeleton
import org.bukkit.entity.SkeletonHorse
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import java.util.*

object Vanguard : OdysseyMob("Vanguard", "vanguard", EntityType.SKELETON, 25.0) {

    fun createKnight(world: World, location: Location): Pair<Skeleton, SkeletonHorse> {
        // Modify Vanguard to a Knight
        val knight = this.createMob(world, location).apply {
            customName(Component.text("Vanguard Knight"))
        }
        // Knight Steed
        val mount = (world.spawnEntity(location, EntityType.SKELETON_HORSE) as SkeletonHorse).apply {
            isTamed = false
            addPassenger(knight)
            setHealthAttribute(100.0, AttributeTags.MOB_HEALTH)
            addSpeedAttribute(0.13, AttributeTags.MOB_MOVEMENT_SPEED)
            addStepAttribute(2.5)
            heal(100.0)
        }
        return Pair(knight, mount)
    }


    override fun createMob(world: World, location: Location): Skeleton {
        val mob = super.createMob(world, location) as Skeleton
        // Weapon
        val mainHand = createToolStack(ToolMaterial.IRON, ToolType.POLEAXE)
        val weapon = mainHand.clone()
        // Add Enchantments
        val enchantItem = ItemStack(Material.NETHERITE_SWORD).enchantWithLevels(30, false, Random())
        val swordEnchants = enchantItem.enchantments
        weapon.apply {
            addUnsafeEnchantment(Enchantment.BREACH, 4)
            addUnsafeEnchantment(Enchantment.KNOCKBACK, 3)
            for (enchant in swordEnchants) {
                if (enchant.key.canEnchantItem(weapon)) {
                    addEnchantment(enchant.key, enchant.value)
                }
            }
            updateEnchantPoints()
        }
        val offHand = ItemStack(Material.SHIELD)

        // Get Equipment Randomizer
        val equipmentRandomizer = EquipmentRandomizer(
            listOf(ToolMaterial.SILVER),
            listOf(ToolType.HALBERD),
            listOf("imperial"),
            null,
            listOf(TrimMaterial.DIAMOND),
            listOf(TrimPattern.WILD))

        // Do Custom mob
        createRandomizedMob(mob, equipmentRandomizer, enchanted = true, newWeapon = false)

        // Apply Stats and weapon
        mob.apply {
            setHealthAttribute(25.0, AttributeTags.MOB_HEALTH)
            heal(25.0)
            addAttackAttribute(10.0, AttributeTags.MOB_ATTACK_DAMAGE)
            addScaleAttribute(0.1, AttributeTags.MOB_SCALE)
            equipment.also {
                it.setItemInOffHand(offHand)
                it.setItemInMainHand(weapon)
                it.itemInMainHandDropChance = 0.05F // Change to difficulty
            }
        }
        return mob
    }


}