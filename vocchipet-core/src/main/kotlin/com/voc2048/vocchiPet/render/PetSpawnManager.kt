package com.voc2048.vocchiPet.render

import com.voc2048.vocchipet.api.render.ModelEngine
import com.voc2048.vocchipet.api.render.ModelRegistry
import org.bukkit.Location
import org.bukkit.entity.ItemDisplay

/**
 * 寵物生成管理器，負責處理寵物模型的生成與物理屬性設定。
 * Pet spawn manager, responsible for handling pet model spawning and physical attribute settings.
 *
 * @property registry 模型註冊表 / The model registry.
 */
class PetSpawnManager(private val registry: ModelRegistry) : ModelEngine {

    /**
     * 在指定座標生成一個 ItemDisplay 實體作為寵物模型。
     * Spawns an ItemDisplay entity at the specified location as a pet model.
     *
     * @param location 生成的座標 / The location to spawn.
     * @param modelKey 模型的識別鍵值 / The identification key of the model.
     * @return 生成的 ItemDisplay 實體，若找不到模型則返回 null / The spawned ItemDisplay entity, or null if the model is not found.
     */
    override fun spawnModel(location: Location, modelKey: String): ItemDisplay? {
        val itemStack = registry.getModel(modelKey) ?: return null
        
        return location.world.spawn(location, ItemDisplay::class.java) { display ->
            display.setItemStack(itemStack)
            display.setGravity(false)
            display.isPersistent = true
            display.isInvulnerable = true
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
