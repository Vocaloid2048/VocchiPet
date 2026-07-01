package com.voc2048.vocchipet.core.capsule

import com.voc2048.vocchipet.PetImpl
import com.voc2048.vocchipet.VocchiPet
import com.voc2048.vocchipet.api.Tier
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

/**
 * 寵物捕捉監聽器，負責處理膠囊命中後的捕捉邏輯。
 * Pet capture listener, responsible for handling capture logic after capsule hits.
 *
 * @property plugin 插件實例 / Plugin instance.
 */
class CaptureListener(private val plugin: VocchiPet) : Listener {

    /**
     * 處理投擲物命中事件。
     * Handles projectile hit events.
     *
     * @param event 命中事件 / Hit event.
     */
    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent) {
        val projectile = event.entity
        if (projectile !is Snowball) return

        val shooter = projectile.shooter as? Player ?: return
        val capsuleItem = projectile.item
        val tier = plugin.getCapsuleManager().getTier(capsuleItem) ?: return

        val target = event.hitEntity as? LivingEntity ?: return
        
        // 檢查是否為可捕捉的野生生物
        // Check if it is a catchable wild creature
        if (!isCatchable(target)) return

        // 阻止原生物理效果並移除膠囊
        // Cancel vanilla physics and remove capsule
        event.isCancelled = true
        projectile.remove()

        val maxHp = target.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0
        val successRate = CapsuleManager.calculateCaptureRate(target.health, maxHp, tier)
        
        if (Random.nextDouble() <= successRate) {
            handleCaptureSuccess(shooter, target)
        } else {
            handleCaptureFailure(shooter, target)
        }
    }

    private fun isCatchable(entity: Entity): Boolean {
        // 判斷是否為野生動物 (未馴服的)
        // Determine if it is a wild animal (untamed)
        if (entity is Tameable && entity.isTamed) return false
        return entity is Wolf || entity is Cat || entity is Fox || entity is Parrot || entity is Rabbit
    }

    private fun handleCaptureSuccess(player: Player, target: LivingEntity) {
        // 煙火與聲音表現
        // Firework and sound effects
        target.world.spawnParticle(Particle.FIREWORK, target.location.add(0.0, 1.0, 0.0), 20)
        target.world.playSound(target.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)

        // 隨機資質 (TIER_0 到 TIER_15)
        // Random tier (TIER_0 to TIER_15)
        val quality = Tier.entries.random()
        
        // 獲取寵物種類，若找不到則使用第一個註冊的
        // Get pet species, use the first registered if not found
        val species = plugin.getSpeciesRegistry().getAllSpecies().find { 
            it.entityType == target.type 
        } ?: plugin.getSpeciesRegistry().getAllSpecies().firstOrNull()

        if (species == null) {
            player.sendMessage("§c發生錯誤：找不到該生物對應的寵物數據！")
            return
        }

        val pet = PetImpl.create(species, player.uniqueId, quality)

        // 非同步寫入玩家的寵物背包資料庫
        // Asynchronously write to player's pet bag database
        plugin.getPetStorage().savePet(pet).thenAccept {
            player.sendMessage("§a成功捕捉了 ${pet.getName()}！資質：${quality.name}")
        }.exceptionally { ex ->
            plugin.logger.severe("捕捉儲存失敗: ${ex.message}")
            null
        }

        // 移除野生實體
        // Remove wild entity
        target.remove()
    }

    private fun handleCaptureFailure(player: Player, target: LivingEntity) {
        // 失敗特效 (膠囊破碎)
        // Failure effects (capsule break)
        target.world.spawnParticle(Particle.ITEM_CRACK, target.location.add(0.0, 1.0, 0.0), 10, ItemStack(Material.GLASS))
        target.world.playSound(target.location, Sound.BLOCK_GLASS_BREAK, 1.0f, 1.0f)

        // 野生動物產生仇恨
        // Wild animal generates aggression
        if (target is Mob) {
            target.target = player
        }
        
        player.sendMessage("§c捕捉失敗！${target.name} 被你激怒了！")
    }
}
