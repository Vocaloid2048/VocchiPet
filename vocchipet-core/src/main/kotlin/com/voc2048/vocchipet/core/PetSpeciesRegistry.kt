package com.voc2048.vocchipet.core

import com.voc2048.vocchipet.api.PetSpecies

/**
 * 寵物種類註冊表。
 * Registry for pet species.
 */
class PetSpeciesRegistry {
    private val speciesMap = mutableMapOf<String, PetSpecies>()

    /**
     * 註冊一個寵物種類。
     * Registers a pet species.
     *
     * @param species 寵物種類 / Pet species.
     */
    fun register(species: PetSpecies) {
        speciesMap[species.id] = species
    }

    /**
     * 獲取指定的寵物種類。
     * Gets the specified pet species.
     *
     * @param id 種類 ID / Species ID.
     * @return 寵物種類，若不存在則返回 null / Pet species, or null if not found.
     */
    fun getSpecies(id: String): PetSpecies? = speciesMap[id]

    /**
     * 獲取所有已註冊的寵物種類。
     * Gets all registered pet species.
     *
     * @return 種類列表 / A list of species.
     */
    fun getAllSpecies(): List<PetSpecies> = speciesMap.values.toList()
}
