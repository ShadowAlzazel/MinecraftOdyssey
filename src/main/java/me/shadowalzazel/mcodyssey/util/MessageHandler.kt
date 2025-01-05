package me.shadowalzazel.mcodyssey.util

import me.shadowalzazel.mcodyssey.util.constants.CustomColors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.LivingEntity

object MessageHandler {

    fun LivingEntity.sendBarMessage(reason: String, color: TextColor = CustomColors.ENCHANT.color) {
        this.sendActionBar(
            Component.text(
                reason,
                color
            )
        )
    }

}