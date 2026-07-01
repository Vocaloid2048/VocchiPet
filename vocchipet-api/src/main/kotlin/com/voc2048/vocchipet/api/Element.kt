package com.voc2048.vocchipet.api

/**
 * 寵物的元素屬性，影響戰鬥中的傷害克制。
 * Elemental attributes of pets, affecting damage multipliers in combat.
 */
enum class Element {
    /**
     * 火屬性。克制草屬性，被水屬性克制。
     * Fire attribute. Strong against Grass, weak against Water.
     */
    FIRE,

    /**
     * 水屬性。克制火屬性，被草屬性克制。
     * Water attribute. Strong against Fire, weak against Grass.
     */
    WATER,

    /**
     * 草屬性。克制水屬性，被火屬性克制。
     * Grass attribute. Strong against Water, weak against Fire.
     */
    GRASS,

    /**
     * 光屬性。與暗屬性互相克制。
     * Light attribute. Mutually strong against Dark.
     */
    LIGHT,

    /**
     * 暗屬性。與光屬性互相克制。
     * Dark attribute. Mutually strong against Light.
     */
    DARK
}
