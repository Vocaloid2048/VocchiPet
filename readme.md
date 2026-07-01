# 🐾 VocchiPet

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21%2B%20~%2026.x-emerald?style=for-the-badge&logo=minecraft)](https://papermc.io)
[![Language](https://img.shields.io/badge/Language-Kotlin-purple?style=for-the-badge&logo=kotlin)](https://kotlinlang.org)
[![Platform](https://img.shields.io/badge/Platform-PaperMC%20%2F%20Purpur-orange?style=for-the-badge)](https://papermc.io)
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)
[![Vibe Coded Repo](https://shields.io)](https://github.com/Vocaloid2048/VocchiPet)

一款基於 **Paper 1.21+** 核心開發的全新開源原創寵物系統插件。採用原生生物魔改、智慧型工作 AI 與獨特的育種遺傳機制。

我們秉持開源精神：核心機制完全開放，歡迎伺服器主與開發者共享、擴充生態，但在使用或衍生時**必須標註原作者 (夜芷冰 - Vocaloid2048)**。

---

## 🎨 專案核心理念 (Our Philosophy)
* **原生體驗：** 全面改用 Minecraft 原生實體 (Wolf, Cat, Fox 等)，確保伺服器效能與兼容性。
* **高自定義性：** 玩家可自由改名，並透過育種系統打造最強資質的寵物。
* **開發者友善：** 採用 **API 與實作分離** 架構，提供極高擴充性。

---

## 🚀 核心功能特色 (Key Features)

### 🧬 先天加成項與育種系統 (Innate Bonus & Breeding) - [NEW]
* **16 階級資質：** 寵物具備 HP、攻擊、防禦、速度、專注五維屬性，每項皆有 **1~16 級 (Tier 1-16)** 的先天加成。
* **遺傳算法：** 支援育種遺傳，玩家可透過特定攜帶道具保底遺傳父母的先天數值。

### 💖 寵物互動與改名 (Interaction & Customization)
* **自由改名：** 支援透過 GUI 或命名牌隨意修改寵物名稱。
* **羈絆系統：** 具備好感度系統，影響工作效率與技能解鎖。

### 🌾 智慧工具人 AI (Smart Work AI)
* **自動作業：** 採收、守衛、隨從模式，協助玩家進行日常作業。
* **原生繼承：** 繼承原生生物的 Pathfinder 並進行高度自定義。

### 🔮 技能引擎 (Skill Engine)
* **去屬性化本體：** 寵物本體無屬性，屬性（火、水、草、光、暗）完全由**技能**決定。
* **屬性相剋：** 基於技能屬性的強弱克制倍率（x1.5 或 x0.75）。
* **技能槽位：** 每個寵物擁有最多 4 個核心技能槽。

### 🟢 捕捉機制 (Capture System)
* **膠囊捕捉：** 使用「寵物膠囊」物品進行捕捉，捕捉率隨目標血量降低而提高。

---

## 📂 專案架構 (Project Structure)

* `vocchipet-api/` - 開放式 API 介面、數據模型定義。
* `vocchipet-core/` - 插件底層驅動、NMS 接管、AI 邏輯、資料庫實作 (SQLite)。
* `wiki/` - 完整的系統設定與開發者對接文件。
* `docs/agent/` - 用於約束與指導 AI Agent 的 SOP 規範與遊戲邏輯白皮書。

---

## 🛠️ 開發與建置 (Development & Build)

### ⚙️ 環境需求
* Java 21+
* Gradle 8.x+
* Paper 1.21+ Core 依賴

### 🔨 編譯指令
```bash
./gradlew clean build
```

---

## 📜 版權與開源聲明 (License & Credits)

1. **必須標註來源：** 使用本專案之個人或團隊，**必須在顯眼處明確標註原作者：`Vocaloid2048 (夜芷冰)**`。
2. **禁止惡意抄襲：** 禁止在未作本專案聲明的情況下，直接打包或將其重命名宣稱為自身原創作品。

---

## 🤝 關於作者 (About Us)
* **核心開發：** Vocaloid2048 (夜芷冰)
* **聯絡管道：** [加入 Discord 伺服器](https://discord.gg/uXatcbWKv2)
