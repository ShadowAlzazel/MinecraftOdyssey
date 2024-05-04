package me.shadowalzazel.mcodyssey.items

import me.shadowalzazel.mcodyssey.constants.ItemModels
import me.shadowalzazel.mcodyssey.items.base.OdysseyItem
import org.bukkit.Material

object Ingredients {

    // Gems
    val NEPTUNIAN_DIAMOND = OdysseyItem("neptunian_diamond", Material.DIAMOND, "Neptunian Diamond", ItemModels.NEPTUNIAN_DIAMOND) //TextColor.color(47, 122, 228)
    val JOVIAN_EMERALD = OdysseyItem("jovian_emerald", Material.EMERALD, "Jovian Emerald", ItemModels.NEPTUNIAN_DIAMOND)// TextColor.color(210, 234, 64)
    val RUBY = OdysseyItem("ruby", Material.EMERALD, "Ruby", ItemModels.RUBY) //  TextColor.color(210, 64, 64)
    val JADE = OdysseyItem("jade", Material.EMERALD, "Jade", ItemModels.JADE) // TextColor.color(0, 122, 85)
    val KUNZITE = OdysseyItem("kunzite", Material.EMERALD, "Kunzite", ItemModels.KUNZITE) // TextColor.color(255, 150, 210)
    val ALEXANDRITE = OdysseyItem("alexandrite", Material.EMERALD, "Alexandrite", ItemModels.ALEXANDRITE)

    // Drops
    val IRRADIATED_SHARD = OdysseyItem("irradiated_shard", Material.PRISMARINE_SHARD, "Irradiated Shard", ItemModels.IRRADIATED_SHARD)
    val IRRADIATED_ROD = OdysseyItem("irradiated_rod", Material.PRISMARINE_SHARD, "Irradiated Rod", ItemModels.IRRADIATED_SHARD)
    val ECTOPLASM = OdysseyItem("ectoplasm", Material.BONE, "Ectoplasm", ItemModels.ECTOPLASM)
    val COAGULATED_BLOOD = OdysseyItem("coagulated_blood", Material.SLIME_BALL, "Coagulated Blood", ItemModels.COAGULATED_BLOOD)
    val SOUL_QUARTZ = OdysseyItem("soul_quartz", Material.QUARTZ, "Soul Quartz", ItemModels.SOUL_CRYSTAL)
    val WARDEN_ENTRAILS = OdysseyItem("warden_entrails", Material.ROTTEN_FLESH, "Warden Entrails", ItemModels.WARDEN_ENTRAILS)

    // Materials
    val SOUL_STEEL_INGOT = OdysseyItem("soul_steel_ingot", Material.IRON_INGOT,"Soul Steel Ingot", ItemModels.SOUL_STEEL_INGOT)
    val SILVER_INGOT = OdysseyItem("silver_ingot", Material.IRON_INGOT, "Silver Ingot", ItemModels.SILVER_INGOT)
    val MITHRIL_INGOT = OdysseyItem("mithril_ingot", Material.IRON_INGOT,"Mithril Ingot", ItemModels.MITHRIL_INGOT)
    val IRIDIUM_INGOT = OdysseyItem("iridium_ingot", Material.IRON_INGOT,"Iridium Ingot", ItemModels.IRIDIUM_INGOT)
    val TITANIUM_INGOT = OdysseyItem("titanium_ingot", Material.IRON_INGOT,"Titanium Ingot", ItemModels.TITANIUM_INGOT)
    val HEATED_TITANIUM_INGOT = OdysseyItem("heated_titanium_ingot", Material.IRON_INGOT,"Heated Titanium Ingot", ItemModels.HEATED_TITANIUM_INGOT)
    val ANODIZED_TITANIUM_INGOT = OdysseyItem("anodized_titanium_ingot", Material.IRON_INGOT,"Anodized Titanium Ingot", ItemModels.ANODIZED_TITANIUM_INGOT)

    val SILVER_NUGGET = OdysseyItem("silver_nugget", Material.IRON_NUGGET, "Silver Nugget", ItemModels.SILVER_NUGGET)

}