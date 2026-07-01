# 📜 指令與 GUI 操作說明 (Commands & GUI Instructions)

本文件詳細列出了 VocchiPet 插件的所有可用指令、權限節點以及 GUI 介面功能。

---

## 🛠️ 指令系統 (Command System)

主指令標籤為 `/vocchipet`，提供縮寫 `/vp`。

### 🛡️ 管理員指令 (Admin Commands)

| 指令 (Command) | 描述 (Description) | 權限 (Permission) |
| --- | --- | --- |
| `/vp admin give <玩家> <種類ID> [參數]` | 賦予指定玩家寵物。若無參數則開啟 GUI。 | `vocchipet.admin` |
| `/vp admin reload` | 重新讀取插件配置。 | `vocchipet.admin` |

#### 💡 高級給予語法 (Advanced Give Syntax)

管理員可以使用 `key=value` 格式指定寵物的詳細參數。

* **範例 (Example):** `/vp admin give Player1 dragon level=50 sub it_hp=16 name=小紅龍`
* **支援參數 (Supported Keys):**
  - `level`: 等級 (1~100)
  - `sub` / `subspecies`: 設為 true 開啟亞種狀態。
  - `name`: 寵物自定義名稱。
  - `it_hp`, `it_atk`, `it_def`, `it_spd`, `it_fcs`: 五維先天階級 (1~16)。

---

## 🖥️ GUI 介面說明 (GUI Descriptions)

### 🎒 寵物背包 (Pet Bag)
* **佈局：** 45 格寵物存放區 + 9 格導航控制欄。
* **寵物展示：**
  * **顯示名稱：** 若有改名則顯示自定義名稱，否則顯示種類名稱。
  * **亞種標誌：** 特殊亞種會顯示「✦ 特殊亞種 ✦」標籤。
  * **資質展示：** 顯示五維屬性的先天加成階級 (Tier 1 ~ 16)，並以不同顏色標註稀有度。

### 🛠️ 寵物構造大師 (Pet Construction Master)
* **開啟方式：** 管理員執行 `/vp admin give <玩家> <寵物ID>` 且不帶額外參數。
* **功能：**
  * **基礎設定：** 調整等級、亞種狀態。
  * **資質調整：** 點擊對應屬性旁的紅/綠玻璃按鈕，增減先天階級 (Tier 1~16)。
  * **確認生成：** 完成設定後點擊右上角按鈕將寵物發放至玩家背包。

---

## 🔑 權限節點總覽 (Permissions Overview)

* `vocchipet.use`: 允許玩家使用基礎指令 (預設：所有玩家)。
* `vocchipet.admin`: 允許玩家執行管理指令 (預設：僅限管理員)。
