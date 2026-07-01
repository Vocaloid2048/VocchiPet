# 🎨 模型渲染引擎 (Model Engine)

本模組負責處理寵物在遊戲中的 3D 模型渲染，採用 Minecraft 1.19.4+ 引入的 `Display Entity` (具體為 `ItemDisplay`) 技術。

## 🚀 功能特點
- **高效能**：`Display Entity` 是客戶端側渲染，對伺服器 TPS 負擔極小。
- **自定義模型**：透過 `CustomModelData` 映射指定的 3D 模型（如紙張或骨頭）。
- **物理控制**：
  - **無視重力** (`HasGravity = false`)：模型不會受重力影響掉落。
  - **無視碰撞**：模型不會與玩家或方塊產生物理碰撞，無需隱身盔甲架作為基底。
  - **靈活旋轉**：支援動態更新 Yaw 與 Pitch，使其能實時面向玩家。

## 🛠️ API 使用說明

### 獲取渲染引擎實作
```kotlin
import com.voc2048.vocchiPet.render.PetSpawnManager
import com.voc2048.vocchipet.api.render.ModelEngine

// 透過介面 ModelEngine 進行操作
val modelEngine: ModelEngine = PetSpawnManager()
```

### 生成寵物模型
```kotlin
val location = player.location
val item = ItemStack(Material.PAPER)
val meta = item.itemMeta
meta?.setCustomModelData(1001) // 設置您的自定義模型 ID
item.itemMeta = meta

// 在指定座標生成模型
val display = modelEngine.spawnModel(location, item)
```

### 動態更新面向
```kotlin
// 更新模型旋轉角度，例如使其面向玩家
modelEngine.updateRotation(display, player.location.yaw, player.location.pitch)
```

## 🏗️ 實作細節
- **介面定義**：`com.voc2048.vocchipet.api.render.ModelEngine`
- **核心實作**：`com.voc2048.vocchiPet.render.PetSpawnManager`
- **技術選型**：使用 `org.bukkit.entity.ItemDisplay` 以獲得最佳的 3D 模型展示效果與性能表現。
