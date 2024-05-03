package me.shadowalzazel.mcodyssey.constants

object ItemModels {

    // Sniffer Seeds
    const val ASPEN_SEED: Int = 6905502
    const val MAPLE_SEED: Int = 6905503
    const val SAKURA_SEED: Int = 6905504
    const val REDWOOD_SEED: Int = 6905505

    // Potions
    const val CONICAL_BOTTLE: Int = 6905310 // For Odyssey Effect Potions With Time
    const val LARGE_BOTTLE: Int = 6905311 // Large Potions
    const val OIL_BOTTLE: Int = 6905312 // For Deprecated Weapon Oils
    const val VIAL: Int = 6905313 // For Small Potions
    const val JAR: Int = 6905314 //
    const val SQUARE_BOTTLE: Int = 6905315 // Higher Amplifier
    const val VOLUMETRIC_BOTTLE: Int = 6905316 // Higher Duration
    const val PEARSHAPE_BOTTLE: Int = 6905317 // For Enhanced potions (not obtainable through vanilla)
    const val DAIRYSHAPE_BOTTLE: Int = 6905318 // Super or For Fun
    const val ROUND_BOTTLE: Int = 6905319 // For Odyssey Effect Potions Without Time
    const val CONCOCTION_BOTTLE: Int = 6905320 // For Potion Effect Combinations
    // Vial
    const val VIAL_CHARGE_1: Int = 6905431
    const val VIAL_CHARGE_2: Int = 6905432
    const val VIAL_CHARGE_3: Int = 6905433
    const val VIAL_CHARGE_4: Int = 6905434
    const val VIAL_CHARGE_5: Int = 6905435

    // Totems
    const val TOTEM_OF_INSIGHT: Int = 6905400 // Totem for pottery shards?
    const val TOTEM_OF_VEXING: Int = 6905404

    // Books
    const val ARCANE_BOOK: Int = 6905501 // Different display for odyssey enchantment
    const val GILDED_BOOK: Int = 6905502 // Powerful permanent enchants
    const val PRISMATIC_BOOK: Int = 6905503 // Converts to Tomes

    // Volumes (books with multiple enchants)
    const val VOLUME_OF_BLUNTING: Int = 6905520
    const val VOLUME_OF_CLEAVING: Int = 6905521
    const val VOLUME_OF_POKING: Int = 6905522
    const val VOLUME_OF_SLASHING: Int = 6905523
    const val VOLUME_OF_TILLING: Int = 6905524
    const val VOLUME_OF_HELMETS: Int = 6905525

    // Tomes
    const val TOME_OF_DISCHARGE: Int = 6906067 // Removes 1 specific enchant
    const val TOME_OF_PROMOTION: Int = 6906068 // increases the level of 1 enchant
    const val TOME_OF_REPLICATION: Int = 6906069 // Copies a book
    const val TOME_OF_HARMONY: Int = 6906070 // Lowers combine/repair cost
    const val TOME_OF_BANISHMENT: Int = 6906071 // Destroys as slot (and the enchant if applicable, NOT WITH GILDED)
    const val TOME_OF_EMBRACE: Int = 6906072 // Adds a slot
    const val TOME_OF_EXPENDITURE: Int = 6906073 // Destroys item but extracts 1 enchantment
    const val TOME_OF_INFUSION: Int = 6906074
    const val TOME_OF_EUPHONY: Int = 6906075
    const val TOME_OF_AVARICE: Int = 6906076
    const val TOME_OF_POLYMERIZATION: Int = 6906077
    const val TOME_OF_EXTRACTION: Int = 6906077
    const val TOME_OF_IMITATION: Int = 6906077

    // Runesherds
    const val ASSAULT_RUNESHERD: Int = 6901453 // Attack Damage
    const val GUARD_RUNESHERD: Int = 6901454 // Armor
    const val FINESSE_RUNESHERD: Int = 6901455 // Attack Speed
    const val SWIFT_RUNESHERD: Int = 6901456 // Speed
    const val VITALITY_RUNESHERD: Int = 6901457 // Health
    const val STEADFAST_RUNESHERD: Int = 6901458 // Knockback Resistance
    const val FORCE_RUNESHERD: Int = 6901459 // Attack Knockback
    const val BREAK_RUNESHERD: Int = 6901460 // Break Speed
    const val BREACH_RUNESHERD: Int = 6901461 // Block Break Range
    const val JUMP_RUNESHERD: Int = 6901462 // Jump
    const val GRAVITY_RUNESHERD: Int = 6901463 // Gravity
    const val RANGE_RUNESHERD: Int = 6901464 // Attack Range
    const val SIZE_RUNESHERD: Int = 6901465 // Size

    // Runeware
    const val FRAGMENTED_ORB: Int = 6901544
    const val CLAY_TOTEM: Int = 6901545
    const val CLAY_SKULL: Int = 6901546
    const val CLAY_DOWEL: Int = 6901547
    const val FRAGMENTED_RODS: Int = 6901548
    const val CLAY_KEY: Int = 6901549

    const val GLAZED_RUNE_ORB: Int = 6901544
    const val GLAZED_RUNE_TOTEM: Int = 6901545
    const val GLAZED_RUNE_SKULL: Int = 6901546
    const val GLAZED_RUNE_DOWEL: Int = 6901547
    const val GLAZED_RUNE_RODS: Int = 6901548
    const val GLAZED_RUNE_KEY: Int = 6901549

    // Unknown Runesherd
    const val UNKNOWN_RUNESHERD: Int = 6905632
    //
    const val SPACERUNE_TABLET: Int = 6905635

    // Food
    const val GREEN_APPLE: Int = 6904511
    const val CANDY_APPLE: Int = 6904512
    const val SUGARY_BREAD: Int = 6904710
    const val FRENCH_TOAST: Int = 6904711
    const val APPLE_COOKIE: Int = 6905040
    const val BEETROOT_COOKIE: Int = 6905041
    const val PUMPKIN_COOKIE: Int = 6905042
    const val HONEY_COOKIE: Int = 6905043
    const val BERRY_COOKIE: Int = 6905044
    const val GLOW_BERRY_COOKIE: Int = 6905045
    const val MELON_COOKIE: Int = 6905046
    const val SUGAR_COOKIE: Int = 6905047
    const val GOLDEN_COOKIE: Int = 6905048
    const val CHORUS_FRUIT_COOKIE: Int = 6905049
    const val CRYSTAL_CANDY: Int = 6905868
    const val BACON: Int = 6908047
    const val SALMON_ROLL: Int = 6908048
    const val FISH_N_CHIPS: Int = 6908049
    const val COFFEE: Int = 6908050
    const val FRUIT_BOWL: Int = 6908051
    const val SPIDER_EYE_BOBA: Int = 6908052
    const val STRAWBERRY_TART: Int = 6908053 // 14/24
    const val CHOCOLATE_MOCHI: Int = 6908054 // 14/24
    const val SHOYU_RAMEN: Int = 6908055
    const val RAINBOW_TROUT: Int = 6908121
    const val BLUEGILL: Int = 6908122

    // Dog
    const val DOG_SPINACH: Int = 6908123
    const val DOG_SIZZLE_CRISP: Int = 6908124
    const val DOG_MILK_BONE: Int = 6908125

    // Misc
    const val ELENCUILE_ESSENCE: Int = 6905465
    const val IRRADIATED_SHARD: Int = 6905466
    const val IRRADIATED_ROD: Int = 6905467
    const val SOUL_CRYSTAL: Int = 6906613
    const val WARDEN_ENTRAILS: Int = 6903620
    const val ECTOPLASM: Int = 6906614
    const val COAGULATED_BLOOD: Int = 6906616
    const val NEPTUNIAN_DIAMOND: Int = 6905006
    const val JOVIAN_EMERALD: Int = 6905105

    // Gems
    const val RUBY: Int = 6905106
    const val JADE: Int = 6905107
    const val KUNZITE: Int = 6905108
    const val ALEXANDRITE: Int = 6905109

    // Soul Steel
    const val SOUL_CATALYST: Int = 6906617
    const val SOUL_STEEL_HELMET: Int = 6906645
    const val SOUL_STEEL_UPGRADE_TEMPLATE: Int = 6906646
    const val ENIGMATIC_OMAMORI: Int = 6906649
    const val SOUL_STEEL_INGOT: Int = 6906618

    // Ingots
    const val SILVER_INGOT: Int = 6906619
    const val MITHRIL_INGOT: Int = 6906777
    const val IRIDIUM_INGOT: Int = 6906878
    const val TITANIUM_INGOT: Int = 6906879
    const val HEATED_TITANIUM_INGOT: Int = 6906880
    const val ANDONIZED_TITANIUM_INGOT: Int = 6906881

    const val SILVER_NUGGET: Int = 6906619

    const val NECRONOMICON_BOOK: Int = 6905066

    // Unused/Misc
    const val ELENCUILE_SAPLING: Int = 6905464
    const val PRIMOGEM: Int = 6900000
    const val BREEZE_IN_A_BOTTLE: Int = 6903617
    const val HOURGLASS_FROM_BABEL: Int = 6903618
    const val ENIGMATIC_ANCHOR: Int = 6903619
    const val SCULK_HEART: Int = 6903621
    const val SUSPICIOUS_TOTEM: Int = 6903622
    const val EVOLUTION_TOTEM: Int = 6903623
    const val CRYING_GOLD: Int = 6903888
    const val BLAZING_ROCKET: Int = 6903889
    const val BONE_OF_FROST: Int = 6905304
    const val IRRADIATED_FRUIT: Int = 6905468
    const val SOUL_SPICE: Int = 6906649
    const val SCULK_POINTER: Int = 6906677

    // Equipment
    const val SAYA: Int = 6903819
    const val EXPLOSIVE_ARROW: Int = 6903820

    // Wands
    const val ARCANE_WAND: Int = 6903910
    const val WARPING_WAND: Int = 6903911

    // Crossbows
    const val GRAPPLING_HOOK: Int = 6905530

    const val COMPACT_CROSSBOW: Int = 6905535
    const val AUTO_CROSSBOW: Int = 6905536
    const val ALCHEMICAL_BOLTER: Int = 6905537 // Alchemy Artillery
    const val CROSSPIKE: Int = 6905538 // Has +6 ATK cuz twin bayonet

    // Bows
    const val WARPED_BOW: Int = 6905580 // +Dmg tho  less accurate

    // Helmets
    const val HORNED_HELMET: Int = 6900773

    // Templates (NOT USED)
    const val KATANA_TEMPLATE: Int = 6905819
    const val KATANA_MOLD: Int = 6905819

    // Swords [69057XX]
    const val KATANA: Int = 6905744
    const val CLAYMORE: Int = 6905745
    const val DAGGER: Int = 6905746
    const val RAPIER: Int = 6905747
    const val CUTLASS: Int = 6905748
    const val SABER: Int = 6905749
    const val SICKLE: Int = 6905750
    const val CHAKRAM: Int = 6905751
    const val KUNAI: Int = 6905752
    const val LONGSWORD: Int = 6905753

    // Shovels
    const val SPEAR: Int = 6905774
    const val HALBERD: Int = 6905775
    const val LANCE: Int = 6905776
    const val NAGINATA: Int = 6905777 // CQ
    const val PIKE: Int = 6905778 // Stationary

    // Pickaxe
    const val WARHAMMER: Int = 6905802
    const val MACE: Int = 6905803  // One-handed
    const val MAUL: Int = 6905804  // Two-Handed
    const val GUANDAO: Int = 6905805 // Lacerate, AVg speed
    const val SCYTHE: Int = 6905806 // AOE

    // Axe
    const val BATTLE_AXE: Int = 6905784
    const val LONG_AXE: Int = 6905785
    const val POLEAXE: Int = 6905786
    const val LABRYS: Int = 6905787

    // Misc
    const val BAMBOO_STAFF: Int = 6905793
    const val BONE_STAFF: Int = 6905794
    const val WOODEN_STAFF: Int = 6905795
    const val BLAZE_ROD_STAFF: Int = 6905796

    // Copper [69055XX]

    // Soul Steel [69063XX]

    // Soul Steel [69066XX] TODO !!! Conversion map for smithing
    const val SOUL_STEEL_KATANA: Int = 6906644
    const val SOUL_STEEL_CLAYMORE: Int = 6906645
    const val SOUL_STEEL_DAGGER: Int = 6906646
    const val SOUL_STEEL_RAPIER: Int = 6906647
    const val SOUL_STEEL_CUTLASS: Int = 6906648
    const val SOUL_STEEL_SABER: Int = 6906649
    const val SOUL_STEEL_SICKLE: Int = 6906650
    const val SOUL_STEEL_CHAKRAM: Int = 6906651

    const val SOUL_STEEL_SPEAR: Int = 6906674
    const val SOUL_STEEL_HALBERD: Int = 6906675
    const val SOUL_STEEL_LANCE: Int = 6906676
    const val SOUL_STEEL_NAGINATA: Int = 6906677
    const val SOUL_STEEL_PIKE: Int = 6906678

    const val SOUL_STEEL_WARHAMMER: Int = 6906702
    const val SOUL_STEEL_MACE: Int = 6906703
    const val SOUL_STEEL_MAUL: Int = 6906704
    const val SOUL_STEEL_GUANDAO: Int = 6906705
    const val SOUL_STEEL_SCYTHE: Int = 6906706

    const val SOUL_STEEL_BATTLE_AXE: Int = 6906684
    const val SOUL_STEEL_LONG_AXE: Int = 6906685
    const val SOUL_STEEL_POLEAXE: Int = 6906686
    const val SOUL_STEEL_LABRYS: Int = 6906687

    // Special Crafted Weapons
    const val ZWEIHANDER: Int = 6905777
    const val VOID_LINKED_KUNAI: Int = 6905778

    // EXOTICS
    const val KINETIC_BLASTER: Int = 6908600
    // MELEE EXOTICS
    const val KNIGHT_BREAKER: Int = 6909600
    const val SHOGUN_LIGHTNING: Int = 6909601
    const val ABZU_BLADE: Int = 6909602
    const val EXCALIBUR: Int = 6909603
    const val FROST_FANG: Int = 6909604
    const val ELUCIDATOR: Int = 6909605
    const val SERENITY_IRIS: Int = 6909606

}