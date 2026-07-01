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

### 模型註冊 (Model Registration)
在生成寵物前，需先將模型映射至註冊表。系統會自動將鍵值轉為標準化格式（大寫且移除空格）。

```kotlin
import com.voc2048.vocchiPet.render.DefaultModelRegistry
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

val registry = DefaultModelRegistry()
val item = ItemStack(Material.PAPER)
val meta = item.itemMeta
meta?.setCustomModelData(1001)
item.itemMeta = meta

// 註冊為 FIREBIRD (輸入 "Fire Bird" 也會自動轉為 "FIREBIRD")
registry.registerModel("Fire Bird", item)
```

### 獲取渲染引擎實作
```kotlin
import com.voc2048.vocchiPet.render.PetSpawnManager
import com.voc2048.vocchipet.api.render.ModelEngine

// 傳入註冊表以初始化引擎
val modelEngine: ModelEngine = PetSpawnManager(registry)
```

### 生成寵物模型
```kotlin
val location = player.location

// 直接使用字串鍵值生成，引擎會自動從註冊表查找對應物品
val display = modelEngine.spawnModel(location, "FIREBIRD")
```

### 動態更新面向
```kotlin
// 更新模型旋轉角度，例如使其面向玩家
modelEngine.updateRotation(display, player.location.yaw, player.location.pitch)
```

## 🏗️ 實作細節
- **介面定義**：
  - `com.voc2048.vocchipet.api.render.ModelEngine`
  - `com.voc2048.vocchipet.api.render.ModelRegistry`
- **核心實作**：
  - `com.voc2048.vocchiPet.render.PetSpawnManager`
  - `com.voc2048.vocchiPet.render.DefaultModelRegistry`
- **持久化儲存**：模型映射關係可透過 `ModelStorage` 介面儲存於 SQLite 資料庫中。
- **技術選型**：使用 `org.bukkit.entity.ItemDisplay` 以獲得最佳的 3D 模型展示效果與性能表現。
