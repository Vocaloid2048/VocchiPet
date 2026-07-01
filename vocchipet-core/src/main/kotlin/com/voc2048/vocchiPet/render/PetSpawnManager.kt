package com.voc2048.vocchiPet.render

import com.voc2048.vocchipet.api.render.ModelEngine
import org.bukkit.Location
import org.bukkit.entity.ItemDisplay
import org.bukkit.inventory.ItemStack

/**
 * 寵物生成管理器，負責處理寵物模型的生成與物理屬性設定。
 * Pet spawn manager, responsible for handling pet model spawning and physical attribute settings.
 */
class PetSpawnManager : ModelEngine {

    /**
     * 在指定座標生成一個 ItemDisplay 實體作為寵物模型。
     * Spawns an ItemDisplay entity at the specified location as a pet model.
     *
     * @param location 生成的座標 / The location to spawn.
     * @param itemStack 模型使用的物品堆疊 / The item stack used for the model.
     * @return 生成的 ItemDisplay 實體 / The spawned ItemDisplay entity.
     */
    override fun spawnModel(location: Location, itemStack: ItemStack): ItemDisplay {
        return location.world.spawn(location, ItemDisplay::class.java) { display ->
            display.setItemStack(itemStack)
            display.setGravity(false)
            display.isPersistent = true
            display.isInvulnerable = true
            // Display Entity 本身即無碰撞與實體基底，符合隱形基底需求。
            // Display Entities naturally have no collision or physical base, fulfilling the invisible base requirement.
        }
    }

    /**
     * 更新模型的旋轉角度，使其能面向玩家。
     * Updates the rotation of the model so it can face the player.
     *
     * @param entity 要旋轉的模型實體 / The model entity to rotate.
     * @param yaw 水平角度 / The yaw angle.
     * @param pitch 垂直角度 / The pitch angle.
     */
    override fun updateRotation(entity: ItemDisplay, yaw: Float, pitch: Float) {
        entity.setRotation(yaw, pitch)
    }
}
