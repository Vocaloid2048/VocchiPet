package com.voc2048.vocchiPet.ai

import org.bukkit.Location
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import com.destroystokyo.paper.entity.Pathfinder

/**
 * 寵物跟隨主人的 AI 目標。
 * Pet follow owner AI goal.
 *
 * @param mob 寵物實體 / The mob entity.
 * @param owner 寵物主人 / The pet owner.
 */
class FollowOwnerGoal(private val mob: Mob, private val owner: Player) {

    /**
     * 檢查並更新跟隨邏輯。
     * Checks and updates the follow logic.
     */
    fun tick() {
        val distance = mob.location.distanceSquared(owner.location)

        // 距離大於 20 格時傳送 / Teleport if distance > 20
        if (distance > 20 * 20) {
            mob.teleport(owner.location.add(1.0, 0.0, 0.0))
            return
        }

        // 距離大於 16 格時開始移動 / Move if distance > 16
        if (distance > 16 * 16) {
            mob.pathfinder.moveTo(owner.location)
        }
        // 距離小於 6 格時停止 / Stop if distance < 6
        else if (distance < 6 * 6) {
            mob.pathfinder.stopPathfinding()
        }
    }
}
