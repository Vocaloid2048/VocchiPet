package com.voc2048.vocchipet.api.storage

import org.bukkit.inventory.ItemStack
import java.util.concurrent.CompletableFuture

/**
 * 負責持久化儲存寵物模型映射資料的介面。
 * Interface responsible for persisting pet model mapping data.
 */
interface ModelStorage {

    /**
     * 儲存模型映射。
     * Saves a model mapping.
     *
     * @param key 模型的鍵值 / The key of the model.
     * @param itemStack 映射的物品堆疊 / The mapped item stack.
     * @return 非同步執行的 CompletableFuture / A CompletableFuture for asynchronous execution.
     */
    fun saveModelMapping(key: String, itemStack: ItemStack): CompletableFuture<Void>

    /**
     * 讀取所有已儲存的模型映射。
     * Loads all saved model mappings.
     *
     * @return 包含所有模型映射的 Map 的 CompletableFuture / A CompletableFuture containing a map of all model mappings.
     */
    fun loadAllModels(): CompletableFuture<Map<String, ItemStack>>
}
