# ⚔️ VocchiPet - 技能清單與配置 (skillList.md)

> **文件目的：** 本文件定義了寵物可裝備的通用技能池。技能與寵物本體解耦，屬性由技能決定。每個寵物最多裝備 4 個技能。

---

## 🔥 火屬性技能 (Fire Skills)

| 技能名稱 | Focus 消耗 | 傷害/效果 | 粒子特效 (Particle) | 音效 (Sound) | 描述 |
| :--- | :---: | :--- | :--- | :--- | :--- |
| **火花 (Ember)** | 10 | 15 傷害 | `FLAME` | `ENTITY_GENERIC_BURN` | 射出細小的火星攻擊敵人。 |
| **火焰旋渦 (Fire Spin)** | 25 | 10 傷害 + 燒傷 3s | `LAVA` | `ITEM_FIRECHARGE_USE` | 將目標困在火焰旋渦中持續造成傷害。 |
| **烈焰衝鋒 (Flame Burst)** | 40 | 35 傷害 | `EXPLOSION` | `ENTITY_GENERIC_EXPLODE` | 全身覆蓋烈焰撞擊目標，威力強大。 |
| **煉獄 (Inferno)** | 60 | 50 傷害 | `FLAME` (大量) | `ENTITY_DRAGON_FIREBALL_EXPLODE` | 從地底召喚毀滅性的煉獄之火。 |

---

## 💧 水屬性技能 (Water Skills)

| 技能名稱 | Focus 消耗 | 傷害/效果 | 粒子特效 (Particle) | 音效 (Sound) | 描述 |
| :--- | :---: | :--- | :--- | :--- | :--- |
| **水槍 (Water Gun)** | 10 | 15 傷害 | `WATER_SPLASH` | `ENTITY_GENERIC_SWIM` | 向目標噴射強力的水流。 |
| **水環 (Aqua Ring)** | 20 | 每秒恢復 5 HP (5s) | `WATER_WAKE` | `ITEM_BOTTLE_FILL` | 在周身形成水環，緩慢恢復生命值。 |
| **噴射水柱 (Aqua Jet)** | 35 | 30 傷害 | `BUBBLE` | `ENTITY_PLAYER_SPLASH_HIGH_SPEED` | 以極快速度包裹水流衝向目標。 |
| **水炮 (Hydro Pump)** | 65 | 55 傷害 | `WATER_DROP` (噴射) | `ENTITY_GENERIC_EXTINGUISH_FIRE` | 釋放巨大水壓的衝擊波。 |

---

## 🍃 草屬性技能 (Grass Skills)

| 技能名稱 | Focus 消耗 | 傷害/效果 | 粒子特效 (Particle) | 音效 (Sound) | 描述 |
| :--- | :---: | :--- | :--- | :--- | :--- |
| **藤鞭 (Vine Whip)** | 12 | 18 傷害 | `HAPPY_VILLAGER` | `BLOCK_GRASS_BREAK` | 使用長長的藤蔓抽打對手。 |
| **葉刃 (Leaf Blade)** | 30 | 32 傷害 | `SNEEZE` (綠色) | `ITEM_TRIDENT_THROW` | 用如利刃般的葉片切裂目標。 |
| **寄生種子 (Leech Seed)** | 25 | 吸取 5 HP/s (4s) | `HEART` (綠色) | `BLOCK_CHERRY_SAPLING_PLACE` | 種下種子，持續吸取目標生命值回饋自身。 |
| **陽光烈焰 (Solar Beam)** | 70 | 60 傷害 | `END_ROD` (亮線) | `ENTITY_ILLUSIONER_PREPARE_BLINDNESS` | 聚集陽光能量，發射毀滅性的光束。 |

---

## ☀️ 光屬性技能 (Light Skills)

| 技能名稱 | Focus 消耗 | 傷害/效果 | 粒子特效 (Particle) | 音效 (Sound) | 描述 |
| :--- | :---: | :--- | :--- | :--- | :--- |
| **聖光 (Holy Light)** | 15 | 12 傷害 + 擊退 | `INSTANT_EFFECT` | `BLOCK_BEACON_ACTIVATE` | 釋放淨化之光，擊退周圍的黑暗。 |
| **閃耀 (Gleam)** | 30 | 25 傷害 + 盲目 2s | `FLASH` | `BLOCK_AMETHYST_BLOCK_CHIME` | 發出強光使目標暫時失去視覺。 |
| **光輝護盾 (Radiance)** | 40 | 減免 40% 傷害 (5s) | `GLOW` | `BLOCK_CONDUIT_ACTIVATE` | 以神聖光輝包裹全身，提升防禦力。 |
| **制裁 (Judgment)** | 80 | 75 傷害 | `TOTEM_OF_UNDYING` | `ENTITY_LIGHTNING_BOLT_THUNDER` | 從天而降的光之審判，對暗屬性有極大威效。 |

---

## 🌑 暗屬性技能 (Dark Skills)

| 技能名稱 | Focus 消耗 | 傷害/效果 | 粒子特效 (Particle) | 音效 (Sound) | 描述 |
| :--- | :---: | :--- | :--- | :--- | :--- |
| **暗影球 (Shadow Ball)** | 18 | 20 傷害 | `WITCH` | `ENTITY_WITHER_SHOOT` | 投擲凝聚黑暗能量的影子球。 |
| **惡之波動 (Dark Pulse)** | 35 | 28 傷害 + 恐懼 1s | `SQUID_INK` | `ENTITY_ENDER_DRAGON_GROWL` | 釋放恐怖的黑暗波動震懾對手。 |
| **夜襲 (Night Slash)** | 45 | 40 傷害 | `SMOKE` | `ENTITY_ENDERMAN_TELEPORT` | 瞬間沒入陰影並在目標身後發動斬擊。 |
| **深淵 (Abyss)** | 75 | 65 傷害 | `LARGE_SMOKE` | `BLOCK_SCULK_SHRIEKER_SHRIEK` | 開啟通往深淵的裂縫，吞噬一切光芒。 |

---

## 📝 技能系統開發規範

1. **Focus 系統：** 技能使用需消耗 Focus 值，Focus 隨時間緩慢恢復。
2. **冷卻時間 (Cooldown)：** 每項技能應有獨立的冷卻時間設定（yml 設定）。
3. **特效觸發：** 技能實作類別必須 override `onCast()` 方法，並在其中調用 `spawnParticle` 與 `playSound`。
