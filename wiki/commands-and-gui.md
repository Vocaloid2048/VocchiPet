# 📜 指令與 GUI 操作說明 (Commands & GUI Instructions)

本文件詳細列出了 VocchiPet 插件的所有可用指令、權限節點以及 GUI 介面功能。
This document details all available commands, permission nodes, and GUI features of the VocchiPet plugin.

---

## 🛠️ 指令系統 (Command System)

主指令標籤為 `/vocchipet`，並提供縮寫 `/vp`。
The main command is `/vocchipet`, with the alias `/vp`.

### 👤 玩家基礎指令 (Player Commands)

| 指令 (Command) | 描述 (Description) | 權限 (Permission) |
| --- | --- | --- |
| `/vp` / `/vp home` | 開啟插件主選單 GUI / Opens the main menu GUI. | `vocchipet.use` |
| `/vp bag` | 直接開啟寵物背包 GUI / Opens the pet bag GUI. | `vocchipet.use` |
| `/vp summon` | 召喚當前選定的寵物 / Summons the currently selected pet. | `vocchipet.use` |
| `/vp recall` | 收回當前召喚的寵物 / Recalls the currently summoned pet. | `vocchipet.use` |

### 🛡️ 管理員指令 (Admin Commands)

| 指令 (Command) | 描述 (Description) | 權限 (Permission) |
| --- | --- | --- |
| `/vp admin give <玩家> <種類ID> [參數]` | 賦予指定玩家寵物。若無參數則開啟 GUI / Gives a pet. Opens GUI if no params. | `vocchipet.admin` |
| `/vp admin addpage <玩家>` | 提升指定玩家的寵物背包上限頁數 / Increases pet bag page limit. | `vocchipet.admin` |
| `/vp admin reload` | 重新讀取插件配置與數據映射 / Reloads plugin configuration. | `vocchipet.admin` |

#### 💡 高級給予語法 (Advanced Give Syntax)

管理員可以在指令後方帶入 JSON 格式或鍵值對格式的參數：
Admins can use JSON or Key-Value pairs for parameters:

* **JSON 格式:** `/vp admin give Player1 dragon {"level":50,"shinny":true,"tier":"UR","element":"FIRE"}`
* **鍵值對格式:** `/vp admin give Player1 dragon level=50 shinny=true tier=UR element=FIRE`

* **參數支援:** `level`, `tier` (D~UR), `element` (FIRE, WATER, GRASS, LIGHT, DARK), `shinny` (true/false)

---

## 🖥️ GUI 介面說明 (GUI Descriptions)

### 🏠 主選單 (Main Menu)
... (略)

### 🎒 寵物背包 (Pet Bag)
... (略)

### 🛠️ 寵物構造大師 (Pet Construction Master)
* **開啟方式：** 管理員執行 `/vp admin give <玩家> <寵物ID>` 且不帶參數。
* **功能：**
  * **等級調整：** 透過按鈕快速增減等級 (+1/-1/+10/-10)。
  * **流光切換：** 一鍵切換寵物是否為「流光 (Streaming/Shiny)」亞種。
  * **元素切換：** 循環切換寵物的五大元素屬性。
  * **資質切換：** 循環切換資質階級 (D ~ UR)。
  * **確認生成：** 完成設定後點擊，寵物將直接發放至目標玩家的 `/petbag` 中。
* **佈局：** 45 格寵物存放區 + 9 格導航控制欄。
* **分頁邏輯：**
  * 支援多頁顯示，透過底部「上一頁/下一頁」箭頭切換。
* **彩色 Lore 展示 (Hover Lore):**
  * **名字：** 包含等級資訊。
  * **屬性：** 顯示元素圖示與顏色（火、水、草、光、暗）。
  * **五維資質：** 以彩色標籤顯示 D 到 UR 的階級。
  * **數值：** 顯示剩餘 TP 點數與目前好感度。

---

## 🔑 權限節點總覽 (Permissions Overview)

* `vocchipet.use`: 允許玩家使用基礎指令與開啟 GUI (預設：所有玩家)。
* `vocchipet.admin`: 允許玩家執行管理指令 (預設：僅限管理員)。
