@file:Suppress("DEPRECATION")

package me.shadowalzazel.mcodyssey.mclisteners

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import com.destroystokyo.paper.event.player.PlayerSetSpawnEvent
import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.bosses.AmbassadorBoss
import org.bukkit.Sound
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerCommandSendEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.UUID

object AmbassadorBossListener : Listener {
    private val itemLootTable = listOf<Material>(Material.NETHERITE_INGOT, Material.DIAMOND, Material.EMERALD, Material.AMETHYST_SHARD, Material.ENDER_CHEST,
        Material.COPPER_INGOT, Material.IRON_INGOT, Material.GOLD_INGOT, Material.DIRT, Material.COARSE_DIRT, Material.ROOTED_DIRT,
        Material.ENCHANTED_BOOK, Material.SHULKER_BOX, Material.ENCHANTED_GOLDEN_APPLE, Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, Material.ORANGE_TULIP,
        Material.RED_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.SUNFLOWER,
        Material.LILAC, Material.ROSE_BUSH, Material.PEONY)


    // Move this to MAIN LATER
    private val ambassadorBoss = AmbassadorBoss()
    private var counter = 0
    private var playersGiftCooldown = mutableMapOf<UUID, Long>()
    private var takeDamageCooldown: Long = 0

    //Temp make specific command/event later
    @EventHandler
    fun onPlayerSummonAmbassador(event: PlayerSetSpawnEvent) {
        if (event.player.isOp) {
            if (MinecraftOdyssey.instance.activeBoss) {
                if (counter == 0) {
                    counter += 1
                    println("${event.player} called the Ambassador")
                    ambassadorBoss.createBoss(event.player.world)
                    ambassadorBoss.ambassadorActive = true
                }
            }
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

    @EventHandler
    fun onAmbassadorDeath(event: EntityDeathEvent) {
        if (ambassadorBoss.ambassadorActive) {
            if (event.entity == ambassadorBoss.ambassadorBossEntity) {
                if (event.entity.killer is Player) {
                    for (aPlayer in event.entity.world.players) {
                        aPlayer.sendMessage("${ChatColor.YELLOW}${ChatColor.ITALIC}The Ambassador has departed ungracefully!")
                        aPlayer.sendMessage("${ChatColor.YELLOW}${ChatColor.ITALIC}With ${event.entity.killer!!.name} taking the final blow!")
                    }
                }
                MinecraftOdyssey.instance.ambassadorDeafeated = true
            }
        }
    }

    @EventHandler
    fun onPlayerEnderFlyAway(event: PlayerElytraBoostEvent) {
        // MAKER THIS MORE READABLE
        if (ambassadorBoss.ambassadorActive) {
            val ambassadorLocation = ambassadorBoss.ambassadorBossEntity!!.location
            val nearbyPlayers = event.player.world.getNearbyPlayers(ambassadorLocation, 25.0, 25.0, 25.0)
            ambassadorLocation.y += 1
            if (event.player in nearbyPlayers) {
                event.isCancelled = true
                event.player.teleport(ambassadorLocation)
                val voidForcePull = PotionEffect(PotionEffectType.CONFUSION, 200, 0)
                val voidRise = PotionEffect(PotionEffectType.LEVITATION, 200, 0)
                val voidPullEffects = listOf<PotionEffect>(voidRise, voidForcePull)
                event.player.addPotionEffects(voidPullEffects)
                event.player.damage(4.5)
                event.player.playSound(event.player, Sound.ENTITY_ENDERMAN_TELEPORT, 0.8F, 1.0F)
                event.player.playSound(event.player, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0F, 1.1F)
                event.player.playSound(event.player, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 0.8F, 0.9F)
            }
        }
    }

    private fun calculateLootTable(droppedItem: Item, givingPlayer: Player){
        val droppedMaterial: Material = droppedItem.itemStack.type
        // Check if item in table
        if (droppedMaterial in itemLootTable) {

            // MAKE THIS OWN UTILITY CLASS LATER
            // ACTUALLY !!!!!!!!!!!!!!!!!!

            // Galvanized Steel
            val galvanizedSteel = ItemStack(Material.IRON_BLOCK, 8)
            val galvanizedSteelMeta: ItemMeta = galvanizedSteel.itemMeta
            galvanizedSteelMeta.setDisplayName("${ChatColor.DARK_GRAY}${ChatColor.ITALIC}Galvanized Steel")
            galvanizedSteel.itemMeta = galvanizedSteelMeta

            // Hawking Containment Unit
            val miniaturizedBlackHole = ItemStack(Material.ENDER_CHEST, 8)
            val miniaturizedBlackHoleMeta: ItemMeta = miniaturizedBlackHole.itemMeta
            miniaturizedBlackHoleMeta.setDisplayName("${ChatColor.BLUE}${ChatColor.ITALIC}Hawking Containment Unit-${ChatColor.MAGIC}0x0000008")
            miniaturizedBlackHole.itemMeta = miniaturizedBlackHoleMeta

            // Idescine Saplings
            val idescineSaplings = ItemStack(Material.OAK_SAPLING, 2)
            val idescineSaplingsMeta: ItemMeta = idescineSaplings.itemMeta
            idescineSaplingsMeta.setDisplayName("${ChatColor.GREEN}${ChatColor.ITALIC}Idescine Sapling")
            idescineSaplingsMeta.lore = listOf("A seed not ready to fully mature", "due to the conditions of the test-world...")
            idescineSaplings.itemMeta = idescineSaplingsMeta

            // Kugelblitz Containment Silo
            val kugelblitzContainmentSilo = ItemStack(Material.YELLOW_SHULKER_BOX, 1)
            val kugelblitzContainmentSiloMeta: ItemMeta = kugelblitzContainmentSilo.itemMeta
            kugelblitzContainmentSiloMeta.setDisplayName("${ChatColor.YELLOW}${ChatColor.ITALIC}Kugelblitz Containment Silo-${ChatColor.MAGIC}0x0000008")
            kugelblitzContainmentSiloMeta.lore = listOf("A portable source of energy and matter")
            kugelblitzContainmentSilo.itemMeta = kugelblitzContainmentSiloMeta

            //
            val rhoAnnulusSchematics = ItemStack(Material.PAPER, 1)
            val rhoAnnulusSchematicsMeta: ItemMeta = rhoAnnulusSchematics.itemMeta
            rhoAnnulusSchematicsMeta.setDisplayName("${ChatColor.LIGHT_PURPLE}${ChatColor.ITALIC}rhoAnnulusSchematics-${ChatColor.MAGIC}Kepler-186f")
            rhoAnnulusSchematicsMeta.lore = listOf("${ChatColor.MAGIC}Keple${ChatColor.RESET}r-186f", "Rho Sys${ChatColor.MAGIC}tem. Vail's Test Site... Section A2${ChatColor.RESET}002")
            rhoAnnulusSchematics.itemMeta = rhoAnnulusSchematicsMeta

            // MAKE DEATH MESSAGE!!!!!!

            when (droppedMaterial) {
                Material.NETHERITE_INGOT -> {
                    ambassadorBoss.appeasementMeter += 1
                    if (ambassadorBoss.appeasementMeter > 50) {
                        givingPlayer.sendMessage("Hmm casted Neutronium Bark... Here are some miniaturized black holes repurposed as storage that might help you.")
                        givingPlayer.inventory.addItem(kugelblitzContainmentSilo)
                        givingPlayer.inventory.addItem(kugelblitzContainmentSilo)
                        givingPlayer.inventory.addItem(kugelblitzContainmentSilo)
                    }
                    else {
                        givingPlayer.sendMessage("Hmm casted Neutronium Bark... Here are some quantum-entangled vacuums repurposed as storage that might help you.")
                        givingPlayer.inventory.addItem(miniaturizedBlackHole)
                    }
                }
                Material.DIAMOND, Material.EMERALD, Material.AMETHYST_SHARD -> {
                    givingPlayer.sendMessage("These gems are not that refined according to standards. But... here is something for such work.")
                    // Random Loot
                }
                Material.COPPER_INGOT, Material.IRON_INGOT, Material.GOLD_INGOT -> {
                    ambassadorBoss.appeasementMeter += 1
                    // Random Loot
                    givingPlayer.sendMessage("Such fine Ingots!")
                }
                Material.DIRT, Material.COARSE_DIRT, Material.ROOTED_DIRT -> {
                    ambassadorBoss.appeasementMeter -= 2
                    givingPlayer.sendMessage("I can not accept something that crude...")
                }
                Material.ENCHANTED_BOOK ->  {
                    ambassadorBoss.appeasementMeter += 4
                    givingPlayer.sendMessage("Enchanted Literature! Something quite interesting this test-site was made for...")
                }
                Material.ENDER_CHEST, Material.SHULKER_BOX -> {
                    ambassadorBoss.appeasementMeter += 4
                    givingPlayer.sendMessage("Sub-Dimensional Storage. A step towards industrialization I see...")
                    givingPlayer.inventory.addItem(galvanizedSteel)
                }
                Material.ENCHANTED_GOLDEN_APPLE -> {
                    ambassadorBoss.appeasementMeter += 10
                    givingPlayer.sendMessage("How did you get this?... I did not hear that Vail planted Aether roots from Lupercal...")
                    givingPlayer.sendMessage("${ChatColor.ITALIC}Do not alert the others... Here take this. Keep it safe, it will help you soon to come...")
                    givingPlayer.inventory.addItem(rhoAnnulusSchematics)

                }
                Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, Material.ORANGE_TULIP,
                Material.RED_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.SUNFLOWER,
                Material.LILAC, Material.ROSE_BUSH, Material.PEONY -> {
                    ambassadorBoss.appeasementMeter += 1
                    givingPlayer.sendMessage("For an uncivilized world, flowers still bloom... Hopefully you do as well...")
                    givingPlayer.inventory.addItem(idescineSaplings)

                }

                else -> {}
            }
            droppedItem.remove()
        }
    }


    // Check if Patience is low
    @EventHandler
    fun onAmbassadorTakeDamage(event: EntityDamageByEntityEvent) {
        // Check if Ambassador Active
        if (ambassadorBoss.ambassadorActive) {
            // Confirm ID
            if (event.entity.uniqueId == ambassadorBoss.ambassadorBossEntity!!.uniqueId) {
                // Check if Ambassador Has patience
                if (ambassadorBoss.patienceMeter >= 0) {
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
                                aPlayer.sendMessage("Due to ${dPlayer.name}'s insolence, you shall be judged!")
                            }
                        }
                    }
                    // For projectiles
                    else {
                        ambassadorBoss.patienceMeter -= event.damage
                    }
                }
                // Combat Mode
                else {
                    val timeElapsed: Long = System.currentTimeMillis() - takeDamageCooldown
                    if (timeElapsed >= 12000) {
                        takeDamageCooldown = System.currentTimeMillis()
                        ambassadorAttacks(ambassadorBoss.ambassadorBossEntity!!)
                    }
                }
            }
        }
    }

    private fun ambassadorAttacks(ambassadorEntity: Illusioner) {
        val ambassadorLocation = ambassadorEntity.location
        val voidRise = PotionEffect(PotionEffectType.LEVITATION, 120, 0)
        val voidFall = PotionEffect(PotionEffectType.SLOW_FALLING, 120, 0)
        val voidShatter = PotionEffect(PotionEffectType.WEAKNESS, 160, 0)
        val voidRisingEffects = listOf<PotionEffect>(voidShatter, voidRise)
        val nearbyPlayers = ambassadorEntity.world.getNearbyPlayers(ambassadorLocation, 15.0, 15.0, 15.0)
        for (aPlayers in nearbyPlayers) {
            aPlayers.sendMessage("Such Folly!")
        }

        when((0..1).random()) {
            // Gravity Attack
            0 -> {
                // Clean Up Later!!!!
                for (aPlayer in nearbyPlayers) {
                    aPlayer.addPotionEffects(voidRisingEffects)
                    aPlayer.playEffect(EntityEffect.ARROW_PARTICLES)
                    aPlayer.damage(10.0)
                    aPlayer.playSound(aPlayer, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0F, 1.1F)
                    aPlayer.playSound(aPlayer, Sound.AMBIENT_BASALT_DELTAS_MOOD, 2.5F, 0.8F)
                    aPlayer.playSound(aPlayer, Sound.BLOCK_ANVIL_LAND, 1.0F, 0.5F)
                    aPlayer.playSound(aPlayer, Sound.BLOCK_ANVIL_BREAK, 1.0F, 0.8F)
                    aPlayer.playSound(aPlayer, Sound.ITEM_TRIDENT_RIPTIDE_3, 1.0F, 1.0F)
                    aPlayer.playSound(aPlayer, Sound.ENTITY_EVOKER_CAST_SPELL, 1.5F, 1.0F)
                }
                val randomPlayer = nearbyPlayers.random()
                val randomPlayerLocation = randomPlayer.location
                randomPlayerLocation.y += 10
                ambassadorEntity.teleport(randomPlayerLocation)
                ambassadorEntity.addPotionEffect(voidFall)
            }


            // Rocket Attack
            1 -> {
                // Launch at target
                if (ambassadorEntity.target is Player) {
                    val playerTarget: Player = ambassadorEntity.target as Player
                    val playerLocation = playerTarget.location
                    // Spawn Firework
                    val superFirework: Firework = ambassadorEntity.world.spawnEntity(playerLocation, EntityType.FIREWORK) as Firework
                    playerLocation.x += 1
                    val superFirework1: Firework = ambassadorEntity.world.spawnEntity(playerLocation, EntityType.FIREWORK) as Firework
                    playerLocation.z += 1
                    val superFirework2: Firework = ambassadorEntity.world.spawnEntity(playerLocation, EntityType.FIREWORK) as Firework
                    // Create Firework Effects
                    val superFireworkMeta = superFirework.fireworkMeta
                    superFireworkMeta.power = 70
                    // Add Effects, Sound, and Power
                    superFireworkMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.BLUE).withFade(Color.PURPLE).trail(true).flicker(true).build())
                    superFirework.fireworkMeta = superFireworkMeta
                    superFirework1.fireworkMeta = superFireworkMeta
                    superFirework2.fireworkMeta = superFireworkMeta
                    superFirework.detonate()
                    superFirework2.detonate()
                    superFirework1.detonate()

                    playerTarget.playSound(playerTarget.location, Sound.AMBIENT_BASALT_DELTAS_MOOD, 2.5F, 0.8F)
                    playerTarget.playSound(playerTarget.location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 2.5F, 0.8F)
                    playerTarget.playSound(playerTarget.location, Sound.ENTITY_IRON_GOLEM_DEATH, 2.0F, 0.8F)


                }
            }
        }
    }



}