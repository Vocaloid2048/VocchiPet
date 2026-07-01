package com.voc2048.vocchipet.api

import org.bukkit.entity.EntityType

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
     * 對應的 Minecraft 原生實體類型。
     * The corresponding Minecraft vanilla entity type.
     */
    val entityType: EntityType,

    /**
     * 基礎屬性。
     * Base stats.
     */
    val baseStats: PetStats
)
