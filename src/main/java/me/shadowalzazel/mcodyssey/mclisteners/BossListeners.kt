package me.shadowalzazel.mcodyssey.mclisteners

import me.shadowalzazel.mcodyssey.bosses.AmbassadorBoss
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.UUID

object AmbassadorBossListener : Listener {
    private val itemLootTable = listOf<Material>(Material.NETHERITE_INGOT, Material.DIAMOND)
    private val ambassadorBoss = AmbassadorBoss()
    private var counter = 0
    private var playersGiftCooldown = mutableMapOf<UUID, Long>()

    //Temp make specific command/event later
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (counter >=   0) {
            counter += 1
            ambassadorBoss.createBoss(event.player.world)
            ambassadorBoss.ambassadorActive = true
        }
    }


    @EventHandler
    fun appeasementItemDrop(event: PlayerDropItemEvent){
        if (ambassadorBoss.ambassadorActive) {
            val droppedItem = event.itemDrop

            // Needs patience
            if (ambassadorBoss.patienceMeter > 0) {
                for (anEntity in ambassadorBoss.ambassadorBossEntity?.getNearbyEntities(5.0, 5.0, 3.0)!!) {
                    //Entity is Player
                    if ((anEntity is Player) && (anEntity.uniqueId == event.player.uniqueId)) {

                        if (!playersGiftCooldown.containsKey(anEntity.uniqueId)) {
                            playersGiftCooldown[anEntity.uniqueId] = System.currentTimeMillis()
                            calculateLootTable(droppedItem, anEntity)
                        }
                        else {
                            // difference in ms
                            val timeElapsed: Long = System.currentTimeMillis() - playersGiftCooldown[anEntity.uniqueId]!!
                            // 5 secs cooldown
                            if (timeElapsed >= 5000) {
                                playersGiftCooldown[anEntity.uniqueId] = System.currentTimeMillis()
                                calculateLootTable(droppedItem, anEntity)
                            }
                            // nothing
                            else {
                            }
                        }
                    }
                }
            }
        }
    }

    private fun calculateLootTable(droppedItem: Item, givingPlayer: Player){
        val droppedMaterial: Material = droppedItem.itemStack.type
        if (droppedMaterial in itemLootTable) {
            when (droppedMaterial) {
                Material.NETHERITE_INGOT -> {
                    ambassadorBoss.appeasementMeter += 2
                    givingPlayer.sendMessage("Such fine Goods!")
                    givingPlayer.inventory.addItem(ItemStack(Material.ACACIA_DOOR, 2))
                }
                Material.DIAMOND, Material.EMERALD, Material.AMETHYST_SHARD -> {
                    ambassadorBoss.appeasementMeter += 2
                    givingPlayer.sendMessage("Such fine Gems!")
                }
                Material.DRIED_KELP_BLOCK -> {
                    ambassadorBoss.appeasementMeter -= 4
                    givingPlayer.sendMessage("You think me a fool! Such garbage shall not be accepted!")
                }
                else -> {}
            }
            droppedItem.remove()
        }
    }

    @EventHandler
    fun onPlayerEnderPearlAway(event: PlayerTeleportEvent) {

    }


    // Check if Patience is low
    @EventHandler
    fun onAmbassadorTakeDamage(event: EntityDamageByEntityEvent) {
        if (ambassadorBoss.ambassadorActive) {
            // Check if Ambassador Has patience
            if (ambassadorBoss.patienceMeter > 0) {
                if (event.damager is Player) {
                    val dPlayer = event.damager
                    if (ambassadorBoss.patienceMeter >= 0) {
                        ambassadorBoss.patienceMeter -= event.damage
                        for (aPlayer in dPlayer.world.players) {
                            aPlayer.sendMessage("${dPlayer.name} is testing my patience!")
                        }
                    }
                    if (ambassadorBoss.patienceMeter <= 0) {
                        ambassadorBoss.ambassadorBossEntity!!.isAware = true
                        for (aPlayer in dPlayer.world.players) {
                            aPlayer.sendMessage("Due to ${dPlayer.name}'s insolence, you will be judged!")
                        }

                    }
                }
                // For projectiles
                else {
                    ambassadorBoss.patienceMeter -= event.damage
                }
            }
        }
    }
}