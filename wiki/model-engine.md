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

### 模型鍵值標準化 (Key Normalization)
系統會自動將輸入的字串轉換為標準化格式，以確保一致性：
- **轉換邏輯**：轉為全大寫 (Uppercase) 並移除所有空白。
- **範例**：`"Fire Bird"` ⮕ `"FIREBIRD"`, `"water_slime"` ⮕ `"WATER_SLIME"` (若底線保留) ⮕ `"WATERSLIME"` (若移除空白)。
*註：目前實作為移除空白。*

### 模型註冊 (Model Registration)
在生成寵物前，需先將模型映射至註冊表。

```kotlin
import com.voc2048.vocchipet.render.DefaultModelRegistry
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

### 持久化儲存 (Persistent Storage)
模型映射資料會儲存在 SQLite 資料庫的 `vocchipet_models` 資料表中：

| 欄位名 | 類型 | 說明 |
| :--- | :--- | :--- |
| `model_key` | TEXT (PK) | 標準化後的模型鍵值 (如 FIREBIRD) |
| `material` | TEXT | 使用的物品材質 (如 PAPER) |
| `custom_model_data` | INTEGER | 映射的自定義模型 ID |

系統啟動時會自動載入所有已儲存的映射至 `ModelRegistry` 中。

### 獲取渲染引擎實作
```kotlin
import com.voc2048.vocchipet.render.PetSpawnManager
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
  - `com.voc2048.vocchipet.render.PetSpawnManager`
  - `com.voc2048.vocchipet.render.DefaultModelRegistry`
- **持久化儲存**：模型映射關係可透過 `ModelStorage` 介面儲存於 SQLite 資料庫中。
- **技術選型**：使用 `org.bukkit.entity.ItemDisplay` 以獲得最佳的 3D 模型展示效果與性能表現。
