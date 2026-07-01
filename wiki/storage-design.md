# 📦 VocchiPet 數據庫設計 (Storage Design)

## 📊 五維屬性模型 (Five-Dimensional Stats)

寵物階級採用：`D`, `C`, `B`, `A`, `S`, `SS`, `SS_PLUS`, `UR`。

### 數據結構
- `base`: 品種基礎值
- `potential`: 先天資質階級 (Tier)
- `trained`: 後天訓練投入點數
- `available_tp`: 剩餘可分配特訓點數
- `skills`: 6 個技能槽 (Array)

## 💾 資料庫架構 (Database Schema)

### 1. `vocchipet_data` (寵物數據)
- `pet_uuid` (TEXT, PK)
- `owner_uuid` (TEXT)
- `tamer_uuid` (TEXT)
- `pet_type` (TEXT)
- `level` (INTEGER)
- `exp` (INTEGER)
- `affection` (REAL)
- `element` (TEXT)
- `stats_json` (TEXT) - 序列化五維屬性與技能

### 2. `vocchipet_players` (玩家數據)
- `player_uuid` (TEXT, PK)
- `max_bag_pages` (INTEGER, Default 1, Max 50)

### 3. `vocchipet_bag` (玩家背包)
- `player_uuid` (TEXT, PK)
- `page` (INTEGER, PK)
- `slot` (INTEGER, PK)
- `pet_uuid` (TEXT)
