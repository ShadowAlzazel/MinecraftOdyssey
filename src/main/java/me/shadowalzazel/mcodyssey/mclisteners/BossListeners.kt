@file:Suppress("DEPRECATION")

package me.shadowalzazel.mcodyssey.mclisteners

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent
import com.destroystokyo.paper.event.player.PlayerSetSpawnEvent
import me.shadowalzazel.mcodyssey.MinecraftOdyssey
import me.shadowalzazel.mcodyssey.bosses.AmbassadorBoss
import org.bukkit.Sound
import org.bukkit.*
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.event.Listener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
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
        Material.RED_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.SUNFLOWER, Material.BOOK, Material.ROTTEN_FLESH,
        Material.LILAC, Material.ROSE_BUSH, Material.PEONY, Material.ACACIA_LOG, Material.OAK_LOG, Material.SPRUCE_LOG, Material.DARK_OAK_LOG, Material.BIRCH_LOG, Material.JUNGLE_LOG, Material.MANGROVE_LOG,
        Material.ACACIA_WOOD, Material.OAK_WOOD, Material.SPRUCE_WOOD, Material.DARK_OAK_WOOD, Material.BIRCH_WOOD, Material.JUNGLE_WOOD, Material.MANGROVE_WOOD, Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.COPPER_BLOCK)


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

    // Move to main listener class
    @EventHandler
    fun onAmbassadorDeath(event: EntityDeathEvent) {
        if (ambassadorBoss.ambassadorActive) {
            if (event.entity == ambassadorBoss.ambassadorBossEntity) {
                if (event.entity.killer is Player) {
                    for (aPlayer in event.entity.world.players) {
                        aPlayer.sendMessage("${ChatColor.YELLOW}${ChatColor.ITALIC}The Ambassador has departed ungracefully!")
                        aPlayer.sendMessage("${ChatColor.YELLOW}${ChatColor.ITALIC}With ${ChatColor.GOLD}${event.entity.killer!!.name} ${ChatColor.RESET}${ChatColor.YELLOW}${ChatColor.ITALIC}taking the final blow!")
                        aPlayer.playSound(aPlayer, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F)
                        aPlayer.giveExpLevels(70)
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
                event.player.world.spawnParticle(Particle.DAMAGE_INDICATOR, event.player.location, 55, 1.5, 0.5, 1.5)
                event.player.world.spawnParticle(Particle.CRIT, event.player.location, 15, 2.5, 0.5, 2.5)
                event.player.world.spawnParticle(Particle.END_ROD, event.player.location, 15, 2.0, 1.0, 2.0)
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
            val galvanizedSteel = ItemStack(Material.IRON_BLOCK, 4)
            val galvanizedSteelMeta: ItemMeta = galvanizedSteel.itemMeta
            galvanizedSteelMeta.setDisplayName("${ChatColor.DARK_GRAY}${ChatColor.ITALIC}Galvanized Steel")
            galvanizedSteel.itemMeta = galvanizedSteelMeta

            // PureAlloyCopper
            val pureAlloyCopper = ItemStack(Material.COPPER_BLOCK, 6)
            val pureAlloyCopperMeta: ItemMeta = pureAlloyCopper.itemMeta
            pureAlloyCopperMeta.setDisplayName("${ChatColor.DARK_GRAY}${ChatColor.ITALIC}Pure-Alloy Copper")
            pureAlloyCopper.itemMeta = pureAlloyCopperMeta

            // Neutronium Scraps
            val neutroniumBarkScraps = ItemStack(Material.NETHERITE_SCRAP, 1)
            val neutroniumBarkScrapsMeta: ItemMeta = neutroniumBarkScraps.itemMeta
            neutroniumBarkScrapsMeta.setDisplayName("${ChatColor.DARK_GRAY}${ChatColor.ITALIC}Neutronium-Bark Scraps")
            neutroniumBarkScraps.itemMeta = neutroniumBarkScrapsMeta

            // Hawking Containment Unit
            val hawkingEntangledUnit = ItemStack(Material.ENDER_CHEST, 5)
            val hawkingEntangledUnitMeta: ItemMeta = hawkingEntangledUnit.itemMeta
            hawkingEntangledUnitMeta.setDisplayName("${ChatColor.BLUE}${ChatColor.ITALIC}Hawking Containment Unit-${ChatColor.MAGIC}0x0000008")
            hawkingEntangledUnitMeta.lore = listOf("A linked vacuum of matter and energy...")
            hawkingEntangledUnit.itemMeta = hawkingEntangledUnitMeta

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

            // rhoSchematics
            val rhoAnnulusSchematics = ItemStack(Material.PAPER, 1)
            val rhoAnnulusSchematicsMeta: ItemMeta = rhoAnnulusSchematics.itemMeta
            rhoAnnulusSchematicsMeta.setDisplayName("${ChatColor.LIGHT_PURPLE}${ChatColor.ITALIC}rhoAnnulusSchematics-${ChatColor.MAGIC}Kepler-186f")
            rhoAnnulusSchematicsMeta.lore = listOf("${ChatColor.MAGIC}Keple${ChatColor.RESET}r-186f", "Rho Sys${ChatColor.MAGIC}tem. Vail's Test Site... Section A2${ChatColor.RESET}002")
            rhoAnnulusSchematics.itemMeta = rhoAnnulusSchematicsMeta

            // Neptunian Diamonds
            val refinedNeptunianDiamonds = ItemStack(Material.DIAMOND, 5)
            val refinedNeptunianDiamondsMeta: ItemMeta = refinedNeptunianDiamonds.itemMeta
            refinedNeptunianDiamondsMeta.addEnchant(Enchantment.LUCK, 1, true)
            refinedNeptunianDiamondsMeta.setDisplayName("${ChatColor.AQUA}${ChatColor.ITALIC}Refined Neptunian-Diamond")
            refinedNeptunianDiamondsMeta.lore = listOf("A diamond forged inside a colossal planet refined to an impressive caliber")
            refinedNeptunianDiamonds.itemMeta = refinedNeptunianDiamondsMeta

            // Iojovian Emerald
            val refinedIojovianEmeralds = ItemStack(Material.EMERALD, 5)
            val refinedIojovianEmeraldsMeta: ItemMeta = refinedIojovianEmeralds.itemMeta
            refinedIojovianEmeraldsMeta.addEnchant(Enchantment.LUCK, 1, true)
            refinedIojovianEmeraldsMeta.setDisplayName("${ChatColor.GREEN}${ChatColor.ITALIC}Refined Iojovian-Emerald")
            refinedIojovianEmeraldsMeta.lore = listOf("An emerald grown near a Jovian super-planet to unmatched pristine")
            refinedIojovianEmeralds.itemMeta = refinedIojovianEmeraldsMeta

            // Polymorphic Glue
            val polymorphicGlue = ItemStack(Material.SLIME_BLOCK, 16)
            val polymorphicGlueMeta: ItemMeta = polymorphicGlue.itemMeta
            polymorphicGlueMeta.setDisplayName("${ChatColor.GRAY}${ChatColor.ITALIC}Polymorphic Glue")
            polymorphicGlueMeta.lore = listOf("Industrial Glue...")
            polymorphicGlue.itemMeta = polymorphicGlueMeta

            // Artificial Unit
            val artificialUnitStar = ItemStack(Material.NETHER_STAR, 1)
            val artificialUnitStarMeta: ItemMeta = artificialUnitStar.itemMeta
            artificialUnitStarMeta.setDisplayName("${ChatColor.DARK_PURPLE}${ChatColor.ITALIC}Artificial Star Unit-${ChatColor.MAGIC}092412X")
            artificialUnitStarMeta.lore = listOf("${ChatColor.GOLD}${ChatColor.ITALIC}Something is speaking to you...")
            artificialUnitStar.itemMeta = artificialUnitStarMeta

            // LOOT TABLE GIVE
            val giftLootTable = listOf(refinedIojovianEmeralds, refinedNeptunianDiamonds, kugelblitzContainmentSilo, galvanizedSteel, pureAlloyCopper, polymorphicGlue, hawkingEntangledUnit, neutroniumBarkScraps)

            // MAKE DEATH MESSAGE!!!!!!

            when (droppedMaterial) {
                Material.NETHERITE_INGOT -> {
                    ambassadorBoss.ambassadorBossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 25, 1.0, 1.0, 1.0)
                    ambassadorBoss.appeasementMeter += 5
                    if (ambassadorBoss.appeasementMeter > 50) {
                        givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Hmm casted Neutronium Bark... Here take this special Unit-${ChatColor.MAGIC}092412X.")
                        val randomGift2 = giftLootTable.random()
                        givingPlayer.inventory.addItem(randomGift2)
                        val randomGift3 = giftLootTable.random()
                        givingPlayer.inventory.addItem(randomGift3)
                        givingPlayer.inventory.addItem(artificialUnitStar)
                    }
                    else {
                        givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Hmm casted Neutronium Bark... Here are some quantum-entangled vacuums repurposed as storage " +
                                "that might help you as well as some gifts")
                        givingPlayer.inventory.addItem(hawkingEntangledUnit)
                        givingPlayer.inventory.addItem(hawkingEntangledUnit)
                        givingPlayer.inventory.addItem(polymorphicGlue)
                        givingPlayer.inventory.addItem(polymorphicGlue)
                        givingPlayer.inventory.addItem(kugelblitzContainmentSilo)
                        givingPlayer.inventory.addItem(kugelblitzContainmentSilo)
                        givingPlayer.inventory.addItem(kugelblitzContainmentSilo)
                        val randomGift2 = giftLootTable.random()
                        givingPlayer.inventory.addItem(randomGift2)
                        val randomGift3 = giftLootTable.random()
                        givingPlayer.inventory.addItem(randomGift3)
                    }
                }
                Material.DIAMOND, Material.EMERALD, Material.AMETHYST_SHARD -> {
                    ambassadorBoss.appeasementMeter -= 1
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}These gems are not that refined according to standards. But... here is something for such work.")
                    // Random Loot
                    val randomGift = giftLootTable.random()
                    givingPlayer.inventory.addItem(randomGift)
                }
                Material.COPPER_INGOT, Material.IRON_INGOT, Material.GOLD_INGOT -> {
                    ambassadorBoss.appeasementMeter += 1
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}The tribute of raw materials express the loyalty and growth of this world...")
                    // Random Loot
                    val randomGift = giftLootTable.random()
                    givingPlayer.inventory.addItem(randomGift)
                }
                Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.COPPER_BLOCK -> {
                    ambassadorBoss.appeasementMeter += 4
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}This much raw materials should garner good attention...")
                    // Random Loot
                    val randomGift = giftLootTable.random()
                    givingPlayer.inventory.addItem(randomGift)
                    val randomGift2 = giftLootTable.random()
                    givingPlayer.inventory.addItem(randomGift2)
                    if (ambassadorBoss.appeasementMeter > 50) {
                        givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}This will help you towards industrialization...")
                        givingPlayer.inventory.addItem(polymorphicGlue)
                    }
                }
                Material.ACACIA_LOG, Material.OAK_LOG, Material.SPRUCE_LOG, Material.DARK_OAK_LOG, Material.BIRCH_LOG, Material.JUNGLE_LOG, Material.MANGROVE_LOG,
                Material.ACACIA_WOOD, Material.OAK_WOOD, Material.SPRUCE_WOOD, Material.DARK_OAK_WOOD, Material.BIRCH_WOOD, Material.JUNGLE_WOOD, Material.MANGROVE_WOOD -> {
                    ambassadorBoss.appeasementMeter += 2
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Wood is always a commodity that should be accepted!")
                    val randomGift = giftLootTable.random()
                    givingPlayer.inventory.addItem(randomGift)
                    if (ambassadorBoss.appeasementMeter > 50) {
                        givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}This will help you towards industrialization...")
                        givingPlayer.inventory.addItem(polymorphicGlue)
                    }
                }
                Material.ROTTEN_FLESH -> {
                    ambassadorBoss.appeasementMeter -= 2
                    ambassadorBoss.patienceMeter -= 2
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}YOu think of me so low and stupid. I believe this world does not deserve anything close to respect...")
                }
                Material.DIRT, Material.COARSE_DIRT, Material.ROOTED_DIRT -> {
                    ambassadorBoss.appeasementMeter -= 2
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}I can not accept something that crude...")
                }
                Material.ENCHANTED_BOOK ->  {
                    ambassadorBoss.appeasementMeter += 4
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Enchanted Literature! Something quite interesting this test-site was made for...and a Good Read")
                    val randomGift = giftLootTable.random()
                    givingPlayer.inventory.addItem(randomGift)
                    if (ambassadorBoss.appeasementMeter < 35) {
                        givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}I am pleased so far.. here have some extra gifts...")
                        val randomGift2 = giftLootTable.random()
                        givingPlayer.inventory.addItem(randomGift2)
                    }
                    else if (ambassadorBoss.appeasementMeter >= 35) {
                        givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Tokens for your prosperity and of the upcoming ${ChatColor.MAGIC}World Integration Procedure")
                        val randomGift2 = giftLootTable.random()
                        givingPlayer.inventory.addItem(randomGift2)
                        val randomGift3 = giftLootTable.random()
                        givingPlayer.inventory.addItem(randomGift3)
                    }
                }
                Material.BOOK -> {
                    ambassadorBoss.appeasementMeter += 1
                    if (ambassadorBoss.appeasementMeter < 45) {
                        givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Hopefully this culture is not so dull as other test-s... Nevermind...")
                    }
                    else {
                        givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}So far I can say I am enjoying this...")
                        val randomGift = giftLootTable.random()
                        givingPlayer.inventory.addItem(randomGift)
                    }

                }
                Material.ENDER_CHEST, Material.SHULKER_BOX -> {
                    ambassadorBoss.appeasementMeter += 3
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Sub-Dimensional Storage. A step towards industrialization I see...And excellent presents!")
                    givingPlayer.inventory.addItem(polymorphicGlue)
                    givingPlayer.inventory.addItem(polymorphicGlue)
                    givingPlayer.inventory.addItem(polymorphicGlue)
                    val randomGift = giftLootTable.random()
                    givingPlayer.inventory.addItem(randomGift)
                }
                Material.ENCHANTED_GOLDEN_APPLE -> {
                    ambassadorBoss.appeasementMeter += 10
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}How did you get this?... I did not hear that Vail planted Aether roots from Lupercal...")
                    givingPlayer.sendMessage("${ChatColor.DARK_GRAY}${ChatColor.ITALIC}Do not alert the others... Here take this. Keep it safe, it will help you soon to come...")
                    givingPlayer.inventory.addItem(rhoAnnulusSchematics)

                }
                Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, Material.ORANGE_TULIP,
                Material.RED_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.SUNFLOWER,
                Material.LILAC, Material.ROSE_BUSH, Material.PEONY -> {
                    ambassadorBoss.appeasementMeter += 1
                    if (ambassadorBoss.appeasementMeter < 45) {
                        givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}For an uncivilized world, flowers still bloom... Hopefully you do as well...")
                        val randomGift = giftLootTable.random()
                        givingPlayer.inventory.addItem(randomGift)
                    }
                    else if (ambassadorBoss.appeasementMeter >= 45) {
                        givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}This should grow your knowledge and strength in time...")
                        givingPlayer.inventory.addItem(idescineSaplings)
                        val randomGift = giftLootTable.random()
                        givingPlayer.inventory.addItem(randomGift)
                    }
                }
                else -> {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Well...No.")
                }
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
                            ambassadorBoss.appeasementMeter -= (event.damage + 3)
                            for (aPlayer in dPlayer.world.players) {
                                if (ambassadorBoss.patienceMeter <= 15) {
                                    aPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}${dPlayer.name} wants to endanger you all!")
                                }
                                else {
                                    aPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}${dPlayer.name} is testing my patience!")
                                }

                                if (ambassadorBoss.appeasementMeter <= -10) {
                                    aPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}${dPlayer.name} has shown me extreme disrespect!")
                                }
                            }
                        }
                        if (ambassadorBoss.patienceMeter <= 0) {
                            ambassadorBoss.ambassadorBossEntity!!.isAware = true
                            for (aPlayer in dPlayer.world.players) {
                                aPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Due to ${dPlayer.name}'s insolence, You all shall be taught some respect")
                                aPlayer.playSound(aPlayer, Sound.ENTITY_WITHER_SPAWN, 1.0F, 0.8F)
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
                    if (timeElapsed >= 8000) {
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
        val nearbyPlayers = ambassadorEntity.world.getNearbyPlayers(ambassadorLocation, 8.0, 8.0, 8.0)
        val randomMessageList = listOf("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Such Folly, Such Weakness!", "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}I can not tolerate such savagery",
            "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}The damage you think your inflicting is minimal!", "${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}I am just disappointed...")
        val randomMessage = randomMessageList.random()

        for (aPlayers in nearbyPlayers) {
            aPlayers.sendMessage(randomMessage)
        }

        when((0..4).random()) {
            // Gravity Attack
            1, 3 -> {
                // Clean Up Later!!!!
                // Assert Boss Active!
                ambassadorBoss.spawnDummy()
                ambassadorBoss.spawnDummy()
                for (aPlayer in nearbyPlayers) {
                    aPlayer.addPotionEffects(voidRisingEffects)
                    aPlayer.damage(10.0)
                    aPlayer.playSound(aPlayer, Sound.ENTITY_EVOKER_PREPARE_WOLOLO, 1.3F, 1.1F)
                    aPlayer.playSound(aPlayer, Sound.ITEM_TRIDENT_THUNDER, 2.5F, 0.8F)
                    aPlayer.playSound(aPlayer, Sound.BLOCK_ANVIL_LAND, 1.0F, 0.5F)
                    aPlayer.playSound(aPlayer, Sound.BLOCK_ANVIL_BREAK, 1.0F, 0.8F)
                    aPlayer.playSound(aPlayer, Sound.ITEM_TRIDENT_RIPTIDE_3, 1.0F, 1.0F)
                    aPlayer.playSound(aPlayer, Sound.ENTITY_EVOKER_PREPARE_ATTACK, 1.5F, 1.0F)
                    aPlayer.playSound(aPlayer, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1.5F, 1.0F)
                    ambassadorEntity.world.spawnParticle(Particle.DAMAGE_INDICATOR, aPlayer.location, 55, 1.5, 0.5, 1.5)
                    ambassadorEntity.world.spawnParticle(Particle.CRIT, aPlayer.location, 15, 2.5, 0.5, 2.5)
                    ambassadorEntity.world.spawnParticle(Particle.END_ROD, aPlayer.location, 15, 2.0, 1.0, 2.0)
                    ambassadorEntity.world.spawnParticle(Particle.FLASH, aPlayer.location, 5, 1.0, 1.0, 1.0)
                    ambassadorEntity.world.spawnParticle(Particle.EXPLOSION_NORMAL, aPlayer.location, 10, 2.0, 1.0, 2.0)
                    val newTP = aPlayer.location
                    newTP.y += 2.5
                    aPlayer.teleport(newTP)
                }
                ambassadorEntity.world.spawnParticle(Particle.EXPLOSION_HUGE, ambassadorEntity.location, 20, 2.0, 2.0, 2.0)
                val randomPlayer = nearbyPlayers.random()
                val randomPlayerLocation = randomPlayer.location
                randomPlayerLocation.y += 17.5
                ambassadorEntity.teleport(randomPlayerLocation)
                ambassadorEntity.addPotionEffect(voidFall)
            }


            // Rocket Attack
            0, 2, 4 -> {
                // Launch at target
                if (ambassadorEntity.target is Player) {
                    val playerTarget: Player = ambassadorEntity.target as Player
                    val playerLocation = playerTarget.location
                    val rocketNearbyPlayers = playerTarget.world.getNearbyPlayers(playerLocation, 2.5, 2.5, 2.5)

                    // Make SEPARATE LATER!!!

                    // Spawn Firework
                    val superFirework: Firework = ambassadorEntity.world.spawnEntity(playerLocation, EntityType.FIREWORK) as Firework
                    playerLocation.z += 1.5
                    val superFirework1: Firework = ambassadorEntity.world.spawnEntity(playerLocation, EntityType.FIREWORK) as Firework
                    playerLocation.z += 1.5
                    val superFirework2: Firework = ambassadorEntity.world.spawnEntity(playerLocation, EntityType.FIREWORK) as Firework
                    // Create Firework Effects
                    val superFireworkMeta = superFirework.fireworkMeta
                    val superFireworkMeta1 = superFirework1.fireworkMeta
                    val superFireworkMeta2 = superFirework2.fireworkMeta
                    superFireworkMeta.power = 120
                    superFireworkMeta1.power = 120
                    superFireworkMeta2.power = 120
                    // Add Effects, Sound, and Power
                    superFireworkMeta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.BLUE).withFade(Color.PURPLE).trail(true).flicker(true).build())
                    superFireworkMeta1.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.PURPLE).withFade(Color.YELLOW).trail(true).flicker(true).build())
                    superFireworkMeta2.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(Color.YELLOW).withFade(Color.BLUE).trail(true).flicker(true).build())
                    superFirework.fireworkMeta = superFireworkMeta
                    superFirework1.fireworkMeta = superFireworkMeta1
                    superFirework2.fireworkMeta = superFireworkMeta2
                    superFirework.detonate()
                    superFirework2.ticksToDetonate = 1
                    superFirework1.ticksToDetonate = 2
                    for (bPlayer in rocketNearbyPlayers) {
                        bPlayer.damage(13.0)
                        bPlayer.playSound(playerTarget.location, Sound.AMBIENT_BASALT_DELTAS_MOOD, 2.5F, 0.8F)
                        bPlayer.playSound(playerTarget.location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 2.5F, 0.8F)
                        bPlayer.playSound(playerTarget.location, Sound.ENTITY_IRON_GOLEM_DEATH, 2.0F, 0.8F)
                        bPlayer.world.spawnParticle(Particle.FLASH, bPlayer.location, 5, 1.0, 1.0, 1.0)
                        bPlayer.world.spawnParticle(Particle.LAVA, bPlayer.location, 35, 1.5, 1.0, 1.5)
                    }
                    playerTarget.damage(13.0)

                }
            }
        }
    }



}