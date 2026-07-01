package com.voc2048.vocchipet.api.render

import org.bukkit.inventory.ItemStack

/**
 * 寵物模型註冊表，負責管理模型 ID 與物品堆疊（包含 CustomModelData）之間的映射。
 * Pet model registry, responsible for managing mappings between model keys and item stacks (including CustomModelData).
 */
interface ModelRegistry {

    /**
     * 註冊一個模型。
     * Registers a model.
     *
     * @param key 模型的唯一鍵值（通常為大寫，如 FIREBIRD） / The unique key for the model (usually uppercase, e.g., FIREBIRD).
     * @param itemStack 映射的物品堆疊 / The mapped item stack.
     */
    fun registerModel(key: String, itemStack: ItemStack)

    /**
     * 獲取指定鍵值的模型物品堆疊。
     * Gets the model item stack for the specified key.
     *
     * @param key 模型的鍵值 / The key of the model.
     * @return 物品堆疊，若未註冊則返回 null / The item stack, or null if not registered.
     */
    fun getModel(key: String): ItemStack?

    /**
     * 將輸入字串標準化為模型鍵值。
     * Normalizes the input string into a model key.
     *
     * @param input 輸入字串（如 "Fire Bird"） / The input string (e.g., "Fire Bird").
     * @return 標準化後的鍵值（如 "FIREBIRD"） / The normalized key (e.g., "FIREBIRD").
     */
    fun normalizeKey(input: String): String {
        return input.uppercase().replace(" ", "")
    }
}
