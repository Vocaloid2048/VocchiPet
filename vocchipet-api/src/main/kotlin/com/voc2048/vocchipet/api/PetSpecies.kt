package com.voc2048.vocchipet.api

import org.bukkit.inventory.ItemStack

/**
 * 代表寵物的種類定義。
 * Represents the definition of a pet species.
 */
data class PetSpecies(
    /**
     * 種類的唯一識別碼。
     * Unique identifier for the species.
     */
    val id: String,

    /**
     * 顯示名稱。
     * Display name.
     */
    val displayName: String,

    /**
     * 元素屬性。
     * Elemental attribute.
     */
    val element: Element,

    /**
     * 基礎屬性。
     * Base stats.
     */
    val baseStats: PetStats,

    /**
     * 渲染模型所需的物品。
     * ItemStack required for model rendering.
     */
    val modelItem: ItemStack
)
