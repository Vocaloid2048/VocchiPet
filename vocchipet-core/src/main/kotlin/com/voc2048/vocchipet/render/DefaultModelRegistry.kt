package com.voc2048.vocchipet.render

import com.voc2048.vocchipet.api.render.ModelRegistry
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ConcurrentHashMap

/**
 * 預設的模型註冊表實作，使用 ConcurrentHashMap 儲存映射。
 * Default model registry implementation, using ConcurrentHashMap for storage.
 */
class DefaultModelRegistry : ModelRegistry {

    private val modelMap = ConcurrentHashMap<String, ItemStack>()

    /**
     * 註冊模型並自動將鍵值轉為標準化格式。
     * Registers a model and automatically normalizes the key.
     */
    override fun registerModel(key: String, itemStack: ItemStack) {
        modelMap[normalizeKey(key)] = itemStack.clone()
    }

    /**
     * 獲取模型並自動將鍵值轉為標準化格式。
     * Gets a model and automatically normalizes the key.
     */
    override fun getModel(key: String): ItemStack? {
        return modelMap[normalizeKey(key)]?.clone()
    }
}
