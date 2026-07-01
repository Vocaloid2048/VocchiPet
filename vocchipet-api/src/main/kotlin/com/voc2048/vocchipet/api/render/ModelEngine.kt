package com.voc2048.vocchipet.api.render

import org.bukkit.Location
import org.bukkit.entity.ItemDisplay
import org.bukkit.inventory.ItemStack

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
     * @param itemStack 模型所使用的物品（包含 CustomModelData） / The item stack used for the model (includes CustomModelData).
     * @return 生成的 ItemDisplay 實體 / The spawned ItemDisplay entity.
     */
    fun spawnModel(location: Location, itemStack: ItemStack): ItemDisplay

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
