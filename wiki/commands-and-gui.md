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

管理員可以使用 `key=value` 格式指定寵物的詳細參數。未指定的 IT 資質將由系統隨機生成，AT 訓練點數預設為 0。
Admins can specify pet parameters using `key=value` format. Unspecified IT will be randomized, and AT defaults to 0.

* **範例 (Example):** `/vp admin give Player1 dragon level=50 shinny it_hp=UR at_atk=25 element=WATER`

* **支援參數 (Supported Keys):**
  - `level`: 等級 (1~100)
  - `shinny` / `shiny`: 旗標參數，設為 true 開啟流光亞種。
  - `element`: 元素屬性 (FIRE, WATER, GRASS, LIGHT, DARK)
  - `IT_HP`, `IT_ATK`, `IT_DEF`, `IT_SPD`, `IT_FCS`: 五維先天資質 (D~UR)
  - `AT_HP`, `AT_ATK`, `AT_DEF`, `AT_SPD`, `AT_FCS`: 五維後天訓練點數

---

## 🖥️ GUI 介面說明 (GUI Descriptions)

### 🏠 主選單 (Main Menu)
... (略)

### 🎒 寵物背包 (Pet Bag)
* **佈局：** 45 格寵物存放區 + 9 格導航控制欄。
* **分頁邏輯：** 支援多頁顯示，透過底部「上一頁/下一頁」箭頭切換。
* **彩色 Lore 展示 (Hover Lore):**
  * **狀態：** 顯示等級、元素、以及是否為「流光亞種」。
  * **五維詳細數據：** 每一項屬性都會顯示其 **IT (資質階級)** 與 **AT (訓練投入點數)**。

### 🛠️ 寵物構造大師 (Pet Construction Master)
* **開啟方式：** 管理員執行 `/vp admin give <玩家> <寵物ID>` 且不帶參數。
* **功能：**
  * **基礎設定：** 調整等級、流光狀態、元素屬性。
  * **五維 IT 調整：** 點擊對應屬性的金錠圖示，循環切換 D ~ UR 階級。
  * **五維 AT 調整：** 點擊對應屬性旁的紅/綠玻璃，增減訓練點數 (-5, -1, +1, +5)。
  * **確認生成：** 完成設定後點擊右上角「確認生成並發放」。

---

## 🔑 權限節點總覽 (Permissions Overview)

* `vocchipet.use`: 允許玩家使用基礎指令與開啟 GUI (預設：所有玩家)。
* `vocchipet.admin`: 允許玩家執行管理指令 (預設：僅限管理員)。
