package com.voc2048.vocchiPet.spawn

import com.voc2048.vocchiPet.VocchiPet
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent

/**
 * 監聽麥塊原生生物生成事件，並依據機率替換為野生寵物。
 * Listens to Minecraft native creature spawn events and replaces them with wild pets based on probability.
 *
 * @property plugin 插件主實例 / The main plugin instance.
 * @property engine 生成引擎 / The spawn engine.
 */
class SpawnListener(private val plugin: VocchiPet, private val engine: SpawnEngine) : Listener {

    /**
     * 當生物生成時觸發。
     * Triggered when a creature spawns.
     *
     * @param event 生物生成事件 / The creature spawn event.
     */
    @EventHandler
    fun onCreatureSpawn(event: CreatureSpawnEvent) {
        // 僅處理自然生成
        // Only handle natural spawns
        if (event.spawnReason != CreatureSpawnEvent.SpawnReason.NATURAL) return
        
        val location = event.location
        val biome = location.block.biome
        
        // 計算是否應該生成寵物
        // Calculate if a pet should be spawned
        val petId = engine.calculateSpawn(biome) ?: return
        
        // 取消原生物生成
        // Cancel native creature spawn
        event.isCancelled = true
        
        // 呼叫模型引擎生成寵物模型
        // Call model engine to spawn pet model
        plugin.getModelEngine().spawnModel(location, petId)
        
        // 注意：目前僅實作模型生成，後續需結合實體屬性與 AI 邏輯。
        // Note: Currently only model spawning is implemented, entity attributes and AI logic will be added later.
    }
}
