package com.voc2048.vocchipet.spawn

import com.voc2048.vocchipet.VocchiPet
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent

/**
 * 監聽麥塊原生生物生成事件，並依據機率替換為野生寵物。
 * Listens to Minecraft native creature spawn events and replaces them with wild pets based on probability.
 */
class SpawnListener(private val plugin: VocchiPet, private val engine: SpawnEngine) : Listener {

    /**
     * 當生物生成時觸發。
     */
    @EventHandler
    fun onCreatureSpawn(event: CreatureSpawnEvent) {
        if (event.spawnReason != CreatureSpawnEvent.SpawnReason.NATURAL) return
        
        val location = event.location
        val biome = location.block.biome
        
        // 計算是否應該生成寵物
        val petId = engine.calculateSpawn(biome) ?: return
        
        // 取消原生物生成
        event.isCancelled = true
        
        // TODO: 使用 NMS 實體接管邏輯生成原生生物寵物 (feat-pet-interaction)
        plugin.logger.info("檢測到野生寵物生成機緣：$petId at $location")
    }
}
