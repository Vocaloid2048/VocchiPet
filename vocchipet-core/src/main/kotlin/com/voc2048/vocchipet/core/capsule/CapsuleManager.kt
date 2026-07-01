package com.voc2048.vocchipet.core.capsule

import com.voc2048.vocchipet.VocchiPet
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.persistence.PersistentDataType

/**
 * 寵物膠囊階級枚舉。
 * Enum for pet capsule tiers.
 *
 * @property id 唯一識別碼 / Unique identifier.
 * @property displayName 顯示名稱 / Display name.
 * @property coefficient 捕捉修正係數 / Capture modification coefficient.
 */
enum class CapsuleTier(val id: String, val displayName: String, val coefficient: Double) {
    REGULAR("regular", "§f初級寵物膠囊 (Regular Capsule)", 1.0),
    GREAT("great", "§e高級寵物膠囊 (Great Capsule)", 1.5),
    ULTRA("ultra", "§b特級寵物膠囊 (Ultra Capsule)", 2.0)
}

/**
 * 寵物膠囊管理器，負責定義與生成膠囊以及註冊合成表。
 * Pet capsule manager, responsible for defining and generating capsules and registering recipes.
 *
 * @property plugin 插件實例 / Plugin instance.
 */
class CapsuleManager(private val plugin: VocchiPet) {

    private val tierKey = NamespacedKey(plugin, "capsule_tier")
    private val customItemKey = NamespacedKey(plugin, "custom_item")

    /**
     * 獲取撞針 (Firing Pin) 物品。
     * Gets the Firing Pin item.
     *
     * @return 撞針 ItemStack / The Firing Pin ItemStack.
     */
    fun getFiringPin(): ItemStack {
        val item = ItemStack(Material.IRON_NUGGET)
        val meta = item.itemMeta ?: return item
        meta.setDisplayName("§7撞針 (Firing Pin)")
        meta.persistentDataContainer.set(customItemKey, PersistentDataType.STRING, "firing_pin")
        item.itemMeta = meta
        return item
    }

    /**
     * 獲取能量核心 (Energy Core) 物品。
     * Gets the Energy Core item.
     *
     * @return 能量核心 ItemStack / The Energy Core ItemStack.
     */
    fun getEnergyCore(): ItemStack {
        val item = ItemStack(Material.HEART_OF_THE_SEA)
        val meta = item.itemMeta ?: return item
        meta.setDisplayName("§b能量核心 (Energy Core)")
        meta.persistentDataContainer.set(customItemKey, PersistentDataType.STRING, "energy_core")
        item.itemMeta = meta
        return item
    }

    /**
     * 根據階級獲取寵物膠囊。
     * Gets a pet capsule based on the tier.
     *
     * @param tier 膠囊階級 / The capsule tier.
     * @return 寵物膠囊 ItemStack / The pet capsule ItemStack.
     */
    fun getCapsule(tier: CapsuleTier): ItemStack {
        val item = ItemStack(Material.SNOWBALL)
        val meta = item.itemMeta ?: return item
        meta.setDisplayName(tier.displayName)
        meta.persistentDataContainer.set(tierKey, PersistentDataType.STRING, tier.id)
        item.itemMeta = meta
        return item
    }

    /**
     * 從 ItemStack 中獲取膠囊階級。
     * Gets the capsule tier from an ItemStack.
     *
     * @param item 待檢查的物品 / The item to check.
     * @return 膠囊階級，若非膠囊則返回 null / The capsule tier, or null if not a capsule.
     */
    fun getTier(item: ItemStack?): CapsuleTier? {
        val meta = item?.itemMeta ?: return null
        val id = meta.persistentDataContainer.get(tierKey, PersistentDataType.STRING) ?: return null
        return CapsuleTier.entries.find { it.id == id }
    }

    /**
     * 計算捕捉成功率。
     * Calculates the capture success rate.
     *
     * @param currentHp 當前血量 / Current HP.
     * @param maxHp 最大血量 / Max HP.
     * @param tier 膠囊階級 / Capsule tier.
     * @return 捕捉成功率 (0.0 - 1.0+) / Capture success rate.
     */
    fun calculateCaptureRate(currentHp: Double, maxHp: Double, tier: CapsuleTier): Double {
        return Companion.calculateCaptureRate(currentHp, maxHp, tier)
    }

    companion object {
        /**
         * 計算捕捉成功率。
         * Calculates the capture success rate.
         *
         * @param currentHp 當前血量 / Current HP.
         * @param maxHp 最大血量 / Max HP.
         * @param tier 膠囊階級 / Capsule tier.
         * @return 捕捉成功率 (0.0 - 1.0+) / Capture success rate.
         */
        fun calculateCaptureRate(currentHp: Double, maxHp: Double, tier: CapsuleTier): Double {
            if (maxHp <= 0.0) return 0.0
            val rate = (1.0 - (currentHp / maxHp)) * tier.coefficient
            return rate.coerceIn(0.0, 1.0)
        }
    }

    /**
     * 註冊膠囊與材料的合成表。
     * Registers recipes for capsules and materials.
     */
    fun registerRecipes() {
        // 註冊撞針合成表
        // Shaped Recipe for Firing Pin
        val firingPinRecipe = ShapedRecipe(NamespacedKey(plugin, "firing_pin"), getFiringPin().asQuantity(4))
        firingPinRecipe.shape(" I ", " I ", "   ")
        firingPinRecipe.setIngredient('I', Material.IRON_INGOT)
        Bukkit.addRecipe(firingPinRecipe)

        // 註冊初級膠囊: 鐵錠 + 撞針 + 紅石
        // Shaped Recipe for Regular Capsule: Iron Ingot + Firing Pin + Redstone
        val regularRecipe = ShapedRecipe(NamespacedKey(plugin, "capsule_regular"), getCapsule(CapsuleTier.REGULAR))
        regularRecipe.shape(" I ", " F ", " R ")
        regularRecipe.setIngredient('I', Material.IRON_INGOT)
        regularRecipe.setIngredient('F', RecipeChoice.ExactChoice(getFiringPin()))
        regularRecipe.setIngredient('R', Material.REDSTONE)
        Bukkit.addRecipe(regularRecipe)

        // 註冊高級膠囊: 金錠 + 撞針 + 螢石粉
        // Shaped Recipe for Great Capsule: Gold Ingot + Firing Pin + Glowstone Dust
        val greatRecipe = ShapedRecipe(NamespacedKey(plugin, "capsule_great"), getCapsule(CapsuleTier.GREAT))
        greatRecipe.shape(" G ", " F ", " L ")
        greatRecipe.setIngredient('G', Material.GOLD_INGOT)
        greatRecipe.setIngredient('F', RecipeChoice.ExactChoice(getFiringPin()))
        greatRecipe.setIngredient('L', Material.GLOWSTONE_DUST)
        Bukkit.addRecipe(greatRecipe)

        // 註冊特級膠囊: 鑽石 + 撞針 + 能量核心
        // Shaped Recipe for Ultra Capsule: Diamond + Firing Pin + Energy Core
        val ultraRecipe = ShapedRecipe(NamespacedKey(plugin, "capsule_ultra"), getCapsule(CapsuleTier.ULTRA))
        ultraRecipe.shape(" D ", " F ", " E ")
        ultraRecipe.setIngredient('D', Material.DIAMOND)
        ultraRecipe.setIngredient('F', RecipeChoice.ExactChoice(getFiringPin()))
        ultraRecipe.setIngredient('E', RecipeChoice.ExactChoice(getEnergyCore()))
        Bukkit.addRecipe(ultraRecipe)
    }
}
