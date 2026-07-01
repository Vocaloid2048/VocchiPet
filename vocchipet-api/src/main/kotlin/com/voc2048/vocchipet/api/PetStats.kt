package com.voc2048.vocchipet.api

/**
 * 代表寵物的單項屬性組成。
 * Represents the composition of a single pet stat.
 */
data class StatComponent(
    val base: Int,
    val potential: Tier
)

/**
 * 寵物屬性結構。
 * Pet stats structure.
 */
data class PetStats(
    val hp: StatComponent,
    val attack: StatComponent,
    val defense: StatComponent,
    val speed: StatComponent,
    val focus: StatComponent,
    val skills: Array<String?>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PetStats) return false
        if (hp != other.hp) return false
        if (attack != other.attack) return false
        if (defense != other.defense) return false
        if (speed != other.speed) return false
        if (focus != other.focus) return false
        if (!skills.contentEquals(other.skills)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = hp.hashCode()
        result = 31 * result + attack.hashCode()
        result = 31 * result + defense.hashCode()
        result = 31 * result + speed.hashCode()
        result = 31 * result + focus.hashCode()
        result = 31 * result + skills.contentHashCode()
        return result
    }
}
