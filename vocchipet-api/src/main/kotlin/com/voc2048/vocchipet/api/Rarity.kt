package com.voc2048.vocchipet.api

/**
 * 寵物的稀有度類別。
 * Rarity categories for pets.
 */
enum class Rarity {
    /**
     * 普通：最常見的寵物。
     * COMMON: The most common pets.
     */
    COMMON,

    /**
     * 罕見：稍具價值的寵物。
     * UNCOMMON: Slightly more valuable pets.
     */
    UNCOMMON,

    /**
     * 稀有：具有一定挑戰性或價值的寵物。
     * RARE: Pets with certain challenge or value.
     */
    RARE,

    /**
     * 史詩：非常強大且難以遇見的寵物。
     * EPIC: Very powerful and hard-to-encounter pets.
     */
    EPIC,

    /**
     * 傳奇：極其罕見，擁有強大能力的寵物。
     * LEGENDARY: Extremely rare pets with powerful abilities.
     */
    LEGENDARY,

    /**
     * 神秘：僅在特殊條件或傳說中出現的寵物。
     * MYTHICAL: Pets that only appear in special conditions or legends.
     */
    MYTHICAL
}
