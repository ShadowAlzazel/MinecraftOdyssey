package me.shadowalzazel.mcodyssey.api

import me.shadowalzazel.mcodyssey.Odyssey
import me.shadowalzazel.mcodyssey.util.DataKeys
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player

object AdvancementManager {

    fun rewardAdvancement(viewers: List<HumanEntity>, advancement: String) {
        val key = DataKeys.newKey(advancement)
        val customAdvancement = Odyssey.instance.server.getAdvancement(key) ?: return
        viewers.forEach {
            if (it is Player) it.getAdvancementProgress(customAdvancement).awardCriteria("requirement")
        }
    }


}