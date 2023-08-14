package me.shadowalzazel.mcodyssey.bosses.the_ambassador

enum class AmbassadorMessage(val text: String) {

    MAX_LIKENESS(", I am sorry. I can not receive any more pleasantries from you."),
    WAIT_FOR_GIFT_COOLDOWN(", please wait. Still... thinking..."),
    SKY_BOMBARD_ATTACK("I like fireworks, the heavens and gifts. But not you."),
    GRAVITY_LAUNCH_ATTACK("The universe is unfathomable. Your are just mundane..."),
    HIJACK_ATTACK("It appears your friends are actually your foes..."),
    FALLING_SINGULARITY_ATTACK("The point is that it's super massive..."),
    ELYTRA_PULL_BACK_ATTACK("You dare use such puny voidflight apparatus."),
    PATIENCE_MESSAGE_1(" threatens your safety..."),
    PATIENCE_MESSAGE_2(" is testing my patience!"),
    PATIENCE_MESSAGE_3("The lack of intelligence on this test world is apparent."),
    PATIENCE_MESSAGE_4("... I expected more from you..."),
    PATIENCE_MESSAGE_5("... Does your integrity not amount to anything?!"),
    PATIENCE_MESSAGE_6(", that is not an appropriate way to introduce yourself, though what is expected from such a lowlife..."),
    PATIENCE_MESSAGE_7("'s insolence has ended my graciousness. Fear me, if you dare."),
    PATIENCE_MESSAGE_8("... Time to teach the lowlifes basic manners..."),
    PATIENCE_MESSAGE_9("Who is doing that?!"),
    REJECT_ENCHANTS("The Arcane is not something to give..."),
    NICE_GIFTS("Your gifts are enjoyable."),
    SCULK_GIFT("The Sculk has corrupted this forsaken place... Its eradication is inevitable"),
    BAD_GIFTS("This is not a gift, but left over debris, suitable for fodder.")

    /*
        when (giftedMaterial) {
            Material.NETHER_STAR -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                giftLikeness += 45
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}An unborn Unit-${ChatColor.MAGIC}092412X.${ChatColor.RESET}. I shall start its sentience activation cycle for you...")
                givingPlayer.inventory.addItem(Miscellaneous.DORMANT_SENTIENT_STAR.createItemStack(1))
                val gravityBook = Arcane.GILDED_BOOK.createGildedBook(OdysseyEnchantments.GRAVITY_WELL, (1..3).random())
                givingPlayer.inventory.addItem(gravityBook)
            }
            Material.NETHERITE_INGOT -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                giftLikeness += 6
                if (appeasement > 50) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Hmm casted Neutronium Bark... Here take this special Unit-${ChatColor.MAGIC}092412X.")
                    givingPlayer.inventory.addItem(Miscellaneous.DORMANT_SENTIENT_STAR.createItemStack(1))
                }
                else {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Hmm casted Neutronium Bark... Here are some quantum-entangled vacuums repurposed as storage " +
                            "that might help you as well as some gifts")
                    givingPlayer.inventory.addItem(Miscellaneous.HAWKING_ENTANGLED_UNIT.createItemStack(likenessReward + 2))
                    givingPlayer.inventory.addItem(Miscellaneous.REFINED_NEPTUNIAN_DIAMONDS.createItemStack(likenessReward * 4))
                    givingPlayer.inventory.addItem(Miscellaneous.REFINED_IOJOVIAN_EMERALDS.createItemStack(likenessReward * 4))
                }
            }
            Material.DIAMOND, Material.EMERALD, Material.AMETHYST_SHARD -> {
                giftLikeness -= 1
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}These gems are not that refined according to standards. But... here is something for such work.")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward + 1))
            }
            Material.COPPER_INGOT, Material.IRON_INGOT, Material.GOLD_INGOT -> {
                giftLikeness += 2
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}The tribute of raw materials express the loyalty and growth of this world...")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward))
            }
            Material.IRON_BLOCK, Material.GOLD_BLOCK, Material.COPPER_BLOCK -> {
                bossEntity?.world!!.spawnParticle(Particle.VILLAGER_HAPPY, givingPlayer.location, 15, 1.0, 1.0, 1.0)
                giftLikeness += 4
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}This much raw materials should garner good attention...")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                if (appeasement > 50) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}This will help you towards mechanization...")
                    givingPlayer.inventory.addItem(Miscellaneous.KUGELBLITZ_CONTAINMENT_UNIT.createItemStack(1))
                }
            }
            Material.ACACIA_LOG, Material.OAK_LOG, Material.SPRUCE_LOG, Material.DARK_OAK_LOG, Material.BIRCH_LOG, Material.JUNGLE_LOG, Material.MANGROVE_LOG,
            Material.ACACIA_WOOD, Material.OAK_WOOD, Material.SPRUCE_WOOD, Material.DARK_OAK_WOOD, Material.BIRCH_WOOD, Material.JUNGLE_WOOD, Material.MANGROVE_WOOD -> {
                giftLikeness += 2
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Wood is always a commodity that should be accepted!")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward))
                if (appeasement > 50) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}This will help you towards industrialization...")
                    givingPlayer.inventory.addItem(Miscellaneous.POLYMORPHIC_GLUE.createItemStack(6))
                }
            }
            Material.ROTTEN_FLESH -> {
                giftLikeness -= 2
                patience -= 2
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}You think of me so low and stupid. I believe this world does not deserve anything close to respect...")
            }
            Material.DIRT, Material.COARSE_DIRT, Material.ROOTED_DIRT -> {
                giftLikeness -= 2
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}I can not accept something that crude...")
            }
            Material.SCULK, Material.SCULK_CATALYST, Material.SCULK_SENSOR, Material.SCULK_SHRIEKER, Material.SCULK_VEIN -> {
                giftLikeness -= 45
                patience -= 45
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}The Sculk has corrupted this forsaken place... Its eradication is inevitable")
            }
            Material.ENCHANTED_BOOK ->  {
                giftLikeness += 4
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Enchanted Literature! Something quite interesting this test-site was made for...and a Good Read")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward + 1))
                if (appeasement < 35) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}I am pleased so far.. here have some extra gifts...")
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward + 1))
                }
                else if (appeasement >= 35) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Tokens for your prosperity and of the upcoming ${ChatColor.MAGIC}World Integration Procedure")
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                }
            }
            Material.BOOK, Material.PAINTING -> {
                giftLikeness += 4
                if (appeasement < 55) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Hopefully this culture is not so dull as other test-s... Never mind...")
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward))
                }
                else {
                    val randomEnchantments = listOf(OdysseyEnchantments.SPOREFUL, OdysseyEnchantments.SQUIDIFY, OdysseyEnchantments.BACKSTABBER, OdysseyEnchantments.BUZZY_BEES, OdysseyEnchantments.FRUITFUL_FARE, OdysseyEnchantments.POTION_BARRIER)
                    val randomBook = Arcane.GILDED_BOOK.createGildedBook(randomEnchantments.random(), 1)
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}So far I can say I am enjoying this...")
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward + 1))
                    givingPlayer.inventory.addItem(randomBook)
                }
            }
            Material.ELYTRA -> {
                giftLikeness += 3
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Sub-Dimensional Flying. A step towards industrialization I see...And excellent presents!")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward + 1))
            }
            Material.ENCHANTED_GOLDEN_APPLE -> {
                giftLikeness += 50
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}How did you get this?... I did not hear that Vail planted Aether roots from Lupercal...")
                givingPlayer.sendMessage("${ChatColor.DARK_GRAY}${ChatColor.ITALIC}Do not alert the others... Here take this. Keep it safe, it will help you soon to come...")
                givingPlayer.inventory.addItem(Miscellaneous.BABEL_ANNULUS_SCHEMATICS.createItemStack(1))
            }
            Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM, Material.AZURE_BLUET, Material.ORANGE_TULIP,
            Material.RED_TULIP, Material.WHITE_TULIP, Material.PINK_TULIP, Material.CORNFLOWER, Material.LILY_OF_THE_VALLEY, Material.SUNFLOWER,
            Material.LILAC, Material.ROSE_BUSH, Material.PEONY -> {
                bossEntity?.world!!.spawnParticle(Particle.VILLAGER_HAPPY, givingPlayer.location, 15, 1.0, 1.0, 1.0)
                giftLikeness += 2
                if (appeasement < 45) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}For an uncivilized world, flowers still bloom... Hopefully you do as well...")
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward))
                }
                else if (appeasement >= 45) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}This should grow your knowledge and strength in time...")
                    givingPlayer.inventory.addItem(Miscellaneous.IDESCINE_SAPLING.createItemStack(1))
                }
            }
            Material.GOAT_HORN -> {
                giftLikeness += 3
                bossEntity?.world!!.spawnParticle(Particle.VILLAGER_HAPPY, givingPlayer.location, 15, 1.0, 1.0, 1.0)
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Musical anomalies... Interesting...")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                //Give sound based enchant if likeness high
            }
            Material.MUSIC_DISC_WARD, Material.MUSIC_DISC_11, Material.MUSIC_DISC_5, Material.MUSIC_DISC_13, Material.MUSIC_DISC_CHIRP, Material.MUSIC_DISC_FAR, Material.MUSIC_DISC_MALL, Material.MUSIC_DISC_MELLOHI, Material.MUSIC_DISC_BLOCKS,
            Material.MUSIC_DISC_PIGSTEP, Material.MUSIC_DISC_STAL, Material.MUSIC_DISC_OTHERSIDE, Material.MUSIC_DISC_STRAD, Material.MUSIC_DISC_WAIT, Material.MUSIC_DISC_CAT -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                giftLikeness += 45
                bossEntity?.world!!.spawnParticle(Particle.VILLAGER_HAPPY, givingPlayer.location, 15, 1.0, 1.0, 1.0)
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Primitive music... Something I find amusing...")
                val randomEnchantments = listOf(OdysseyEnchantments.BANE_OF_THE_ILLAGER, OdysseyEnchantments.BANE_OF_THE_SWINE, OdysseyEnchantments.BACKSTABBER, OdysseyEnchantments.BUZZY_BEES,
                    OdysseyEnchantments.GUARDING_STRIKE, OdysseyEnchantments.FRUITFUL_FARE, OdysseyEnchantments.DOUSE, OdysseyEnchantments.POTION_BARRIER)
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 5))
                if (appeasement >= 35) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}These are some relics I have collected from previous visits...")
                    val randomBook = Arcane.GILDED_BOOK.createGildedBook(randomEnchantments.random(), 1)
                    givingPlayer.inventory.addItem(randomBook)
                }
            }
            // Later add books
            Material.CONDUIT -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}The ocean is essential to this world's stability! Though, power does not give you an excuse to butcher.")
                giftLikeness += 45
                val seaBook = Arcane.GILDED_BOOK.createGildedBook(OdysseyEnchantments.BANE_OF_THE_SEA, 4)
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                if (appeasement >= 45) {
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                }
                givingPlayer.inventory.addItem(seaBook)
            }
            Material.TRIDENT -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Do no be afraid of the sea or its monsters...")
                giftLikeness += 30
                val seaBook = Arcane.GILDED_BOOK.createGildedBook(OdysseyEnchantments.BANE_OF_THE_SEA, (1..3).random())
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                givingPlayer.inventory.addItem(seaBook)

            }
            Material.BEACON -> {
                bossEntity?.world!!.spawnParticle(Particle.SPELL_WITCH, givingPlayer.location, 35, 1.0, 1.0, 1.0)
                giftLikeness += 45
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Follow the path of doubt...and not blinding light")
                val randomEnchantments = listOf(OdysseyEnchantments.VOID_JUMP, OdysseyEnchantments.ECHO, OdysseyEnchantments.VOID_STRIKE, OdysseyEnchantments.SOUL_REND, OdysseyEnchantments.BACKSTABBER)
                val randomBook = Arcane.GILDED_BOOK.createGildedBook(randomEnchantments.random(), 1)
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                if (appeasement >= 45) {
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                    givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward * 2))
                }
                givingPlayer.inventory.addItem(randomBook)
            }
            Material.CHEST, Material.BARREL -> {
                giftLikeness += 1
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Nice and quaint boxes...")
                givingPlayer.inventory.addItem(someGift.createItemStack(likenessReward + 1))
                if (appeasement >= 65) {
                    givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}This is what a present look like however...")
                    givingPlayer.inventory.addItem(Miscellaneous.KUGELBLITZ_CONTAINMENT_UNIT.createItemStack(1))
                }
            }
            else -> {
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Well...Let me appraise this for you...")
                giftAccepted = false

            }
        }

        if (giftAccepted) {
            giftedItem.remove()
            // Calculation for gift likeness
            appeasement += giftLikeness
            val newLikeness = playerLikeness[givingPlayer.uniqueId]!! + giftLikeness
            playerLikeness[givingPlayer.uniqueId] = newLikeness
            if (playerLikeness[givingPlayer.uniqueId]!! > 50.0) {
                givingPlayer.sendMessage("${ChatColor.LIGHT_PURPLE}[The Ambassador] ${ChatColor.RESET}Your gifts are enjoyable")
            }
        }

         */

}