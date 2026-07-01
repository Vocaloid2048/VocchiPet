# 💾 數據儲存系統 (Storage System)

VocchiPet 採用非同步架構處理數據持久化，目前主要使用 SQLite 本地資料庫。

## 🗄️ 資料庫架構 (Database Schema)

### 1. 寵物數據表 (`vocchipet_data`)
儲存寵物的核心屬性與狀態。

| 欄位名 | 類型 | 說明 |
| :--- | :--- | :--- |
| `pet_uuid` | TEXT (PK) | 寵物的唯一識別碼 (UUID) |
| `owner_uuid` | TEXT | 當前主人的唯一識別碼 (UUID) |
| `tamer_uuid` | TEXT | 最初馴養者（捕捉者）的唯一識別碼 (UUID) |
| `pet_type` | TEXT | 寵物種類 ID |
| `pet_name` | TEXT | 寵物的自定義名稱 |
| `level` | INTEGER | 寵物等級 |
| `exp` | INTEGER | 寵物經驗值 |
| `affection` | REAL | 寵物好感度 |
| `subspecies` | INTEGER | 是否為特殊亞種 (0=否, 1=是) |
| `stats_json` | TEXT | 序列化後的五維資質與技能數據 (JSON) |

### 2. 玩家數據表 (`vocchipet_players`)
儲存玩家相關的擴充數據。

| 欄位名 | 類型 | 說明 |
| :--- | :--- | :--- |
| `player_uuid` | TEXT (PK) | 玩家的 UUID |
| `max_bag_pages` | INTEGER | 背包解鎖的最大頁數 |

## 📊 五維資質數據結構 (Stats Structure)
寵物的先天加成 (Innate Bonus) 儲存在 `stats_json` 欄位中，包含：
- **HP / 攻擊 / 防禦 / 速度 / 專注**：每項均有 `base` (基礎值) 與 `potential` (先天階級)。
- **先天階級 (Tier)**：範圍為 `TIER_0` 到 `TIER_15` (對應遊戲內 1~16 級)。
- **技能槽位**：最多 4 個核心技能的 ID。

## 🛠️ 開發者對接 (API Access)

### 獲取儲存介面
透過主類別 `VocchiPet` 獲取儲存實例：

```kotlin
val plugin = JavaPlugin.getPlugin(VocchiPet::class.java)
val petStorage: PetStorage = plugin.getPetStorage()
```

### 非同步操作範例
所有資料庫操作皆返回 `CompletableFuture`，不會阻塞伺服器主執行緒。

```kotlin
petStorage.loadPet(uuid).thenAccept { pet ->
    if (pet != null) {
        println("找到寵物: ${pet.getName()} (種類: ${pet.getType()})")
    }
}
```

## 🏗️ 實作細節
- **底層技術**：使用 JDBC 連接 SQLite。
- **序列化**：使用 Gson 處理複雜屬性結構的 JSON 儲存。
- **非同步處理**：擁有獨立的 `VocchiPet-Database-Thread` 執行緒池，確保高效能。
