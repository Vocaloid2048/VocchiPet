# 💾 數據儲存系統 (Storage System)

VocchiPet 採用非同步架構處理數據持久化，預設支援 SQLite 本地資料庫。

## 🗄️ 資料庫架構 (Database Schema)

### 1. 寵物數據表 (`vocchipet_data`)
儲存寵物的核心屬性與狀態。

| 欄位名 | 類型 | 說明 |
| :--- | :--- | :--- |
| `pet_uuid` | TEXT (PK) | 寵物的唯一識別碼 (UUID) |
| `owner_uuid` | TEXT | 當前主人的唯一識別碼 (UUID) |
| `tamer_uuid` | TEXT | 最初馴養者（捕捉者）的唯一識別碼 (UUID) |
| `pet_type` | TEXT | 寵物種類 (如 FIREBIRD) |
| `level` | INTEGER | 寵物等級 |
| `exp` | INTEGER | 寵物經驗值 |
| `affection` | REAL | 寵物好感度 |
| `element` | TEXT | 寵物元素屬性 (FIRE, WATER, etc.) |

### 2. 模型映射表 (`vocchipet_models`)
儲存模型鍵值與遊戲內物品的映射關係。

| 欄位名 | 類型 | 說明 |
| :--- | :--- | :--- |
| `model_key` | TEXT (PK) | 標準化後的模型識別鍵 |
| `material` | TEXT | 物品材質名稱 (Enum Name) |
| `custom_model_data` | INTEGER | `CustomModelData` 數值 |

## 🛠️ 開發者對接 (API Access)

### 獲取儲存介面
透過主類別 `VocchiPet` 獲取儲存實例：

```kotlin
val plugin = JavaPlugin.getPlugin(VocchiPet::class.java)
val petStorage: PetStorage = plugin.getPetStorage()
val modelStorage: ModelStorage = plugin.getPetStorage() as ModelStorage
```

### 非同步操作範例
所有資料庫操作皆返回 `CompletableFuture`，不會阻塞伺服器主執行緒。

```kotlin
petStorage.loadPet(uuid).thenAccept { pet ->
    if (pet != null) {
        println("找到寵物: ${pet.getType()} (等級: ${pet.getLevel()})")
    }
}
```

## 🏗️ 實作細節
- **底層技術**：使用 JDBC 連接 SQLite。
- **非同步處理**：擁有獨立的 `VocchiPet-Database-Thread` 執行緒池，確保高效能。
- **資料完整性**：使用 `INSERT OR REPLACE` 處理數據更新。
