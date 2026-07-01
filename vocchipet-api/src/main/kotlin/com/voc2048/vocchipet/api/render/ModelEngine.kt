package com.voc2048.vocchipet.api.render

import org.bukkit.Location
import org.bukkit.entity.ItemDisplay

/**
 * 寵物模型渲染引擎介面，負責處理基於 Display Entity 的模型生成與外觀更新。
 * Interface for the pet model rendering engine, responsible for handling model spawning and appearance updates based on Display Entities.
 */
interface ModelEngine {

    /**
     * 在指定座標生成寵物模型實體。
     * Spawns a pet model entity at the specified location.
     *
     * @param location 生成座標 / The location to spawn the model.
     * @param modelKey 模型的識別鍵值（如 FIREBIRD） / The identification key of the model (e.g., FIREBIRD).
     * @return 生成的 ItemDisplay 實體，若找不到模型則可能拋出異常或返回 null / The spawned ItemDisplay entity, or null if the model is not found.
     */
    fun spawnModel(location: Location, modelKey: String): ItemDisplay?

    /**
     * 更新模型實體的旋轉角度。
     * Updates the rotation of the model entity.
     *
     * @param entity 要更新的實體 / The entity to update.
     * @param yaw 水平旋轉角度 / The horizontal rotation (yaw).
     * @param pitch 垂直旋轉角度 / The vertical rotation (pitch).
     */
    fun updateRotation(entity: ItemDisplay, yaw: Float, pitch: Float)
}
