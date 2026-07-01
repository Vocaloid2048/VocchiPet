# 指令與 GUI 操作說明

## 指令系統 (/vocchipet 或 /vp)

### 玩家指令
* `/vp`: 開啟主選單 GUI。
* `/vp bag`: 開啟寵物背包。
* `/vp summon <UUID>`: 召喚寵物。
* `/vp recall`: 收回寵物。

### 管理員指令 (權限節點: `vocchipet.admin`)
* `/vp admin give <玩家> <種類> <資質>`: 給予玩家指定寵物。
* `/vp admin addpage <玩家>`: 擴充玩家背包頁數。
* `/vp admin reload`: 重載插件配置。

## GUI 介面說明
* **主選單 GUI**: 顯示召喚寵物狀態，背包跳轉入口。
* **寵物背包 GUI**: 45格分頁介面，支援上下頁切換。
  * 懸停提示 (Lore): 展示寵物名字、屬性、五維資質與 TP 點數。
