# 🐾 VocchiPet

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.21%2B%20~%2026.x-emerald?style=for-the-badge&logo=minecraft)](https://papermc.io)
[![Language](https://img.shields.io/badge/Language-Kotlin-purple?style=for-the-badge&logo=kotlin)](https://kotlinlang.org)
[![Platform](https://img.shields.io/badge/Platform-PaperMC%20%2F%20Purpur-orange?style=for-the-badge)](https://papermc.io)
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)
[![Vibe Coded Repo](https://shields.io)](https://github.com/Vocaloid2048/VocchiPet)


一款基於 **Paper 1.21+** 核心開發的全新開源原創寵物/幻獸系統插件。結合了現代化的 3D 渲染技術、智慧型工作 AI 以及公平的結界對戰機制。

我們秉持類似 **Cobblemon** 的開源精神：核心機制與模型資產完全開放，歡迎伺服器主與開發者共享、擴充生態，但在使用或衍生時**必須標註原作者 (夜芷冰 - Vocaloid2048)**。

---

## 🎨 專案核心理念 (Our Philosophy)
* **100% 原創與法律安全：** 絕不包含、搬用任何具版權爭議（如寶可夢）之美術資產與專有名詞。
* **開箱即用 (Out-of-the-Box)：** 一個插件即搞定「模型渲染 + 智慧 AI + 戰鬥對戰」，無需同時堆疊複數高成本插件。
* **開發者友善：** 採用 **API 與實作分離** 架構，提供極高擴充性（內建對接 PlaceholderAPI, Vault）。

---

## 🚀 核心功能特色 (Key Features)

### 🎒 基礎原型 (Core Prototype) - [NEW]
* **指令系統：** 完善的 `/vp` 指令樹，包含管理員給予寵物、背包管理、重載等。
* **分頁背包：** 45 格 GUI 背包，支援寵物資質展示、翻頁切換。
* **預設模型：** 內建 5 隻測試寵物（火、水、草、光、暗），展示基礎五維資質（D~UR）。

### 💖 寵物互動與羈絆 (Affection System)
* 具備由 0 ~ 100 的好感度系統（疏離、普通、親密、羈絆）。
* 玩家可透過餵食、空手撫摸、共同戰鬥提升好感度，好感度將直接解鎖主動技能與工作效率。

### 🌾 智慧工具人 AI (Smart Work AI)
* **採收模式 (Harvester)：** 自動掃描半徑 8 格內成熟作物，觸發方塊破壞事件並**自動消耗種子補種**，剩餘產物存入寵物虛擬背包。
* **戰鬥模式 (Combat Helper)：** 與主人的仇恨同步，自動協助攻擊敵對目標，內建安全防護，絕不誤傷友方。

### ⚡ 屬性相剋與技能引擎 (Element & Skill Engine)
* 五大核心屬性：**火 (Fire)、水 (Water)、草 (Grass)、光 (Light)、暗 (Dark)**。
* 嚴格的強弱克制倍率矩陣（傷害倍率 x1.5 或 x0.75），施放主動/被動技能時自帶華麗粒子效果。

### ⚔️ 領域決鬥系統 (Duel Arena)
* 透過 `/pet duel <ID>` 發起 1v1 即時對戰。
* 在兩位玩家中點自動劃定 15 格半徑的虛擬「決鬥結界」，限制外在干擾。
* **零損失保護機制：** 戰敗寵物強制保留 1 HP 並安全收回，絕不真正死亡。

### 📦 模型渲染與自定義 (Model Engine)
* **Display Entity 技術：** 採用 `ItemDisplay` 渲染，比起傳統盔甲架 (ArmorStand) 更節省效能且支援更複雜的旋轉平滑度。
* **自定義模型映射：** 透過 `CustomModelData` 自由映射 Blockbench 匯出的 3D 模型。

---

## 📂 專案架構 (Project Structure)

本專案採用嚴格的模組化與多模組（Multi-module）設計：

* `vocchipet-api/` - 開放式 API 介面、事件觸發（Events Registry），供第三方插件對接。
* `vocchipet-core/` - 插件底層驅動、NMS、Pathfinder AI、資料庫實作（SQLite / MySQL）。
* `wiki/` - 完整的系統設定與開發者對接文件。
* `docs/agent/` - 用於約束與指導 AI Agent (如 Cursor / Claude) 進行協同開發的 SOP 規範。

---

## 🛠️ 開發與建置 (Development & Build)

專案採用 **Gradle (Kotlin DSL)** 進行管理，並搭配 **JUnit 5** 進行邏輯測試。

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

本專案之**核心程式碼、3D 寵物模型及美術資產**均以開放共享為前提開放使用，但必須遵守以下規範：

1. **必須標註來源：** 任何使用本插件、修改其原始碼、或在公開伺服器中使用本專案內建 50 隻寵物模型之個人或團隊，**必須在顯眼處（如 Github Repo 說明、伺服器選單或官方網站）明確標註原作者：`Vocaloid2048 (夜芷冰)**`。
2. **禁止惡意抄襲與未授權倒賣：** 禁止在未作本專案聲明的情況下，直接打包或將其重命名宣稱為自身原創作品。

---

## 🤝 關於作者 (About Us)

* **專案創始 / 核心開發：** Vocaloid2048 (夜芷冰)
* **AI 協作：** 本專案開發過程中使用 AI 工具輔助生成部分程式碼、資產與文件。
* **聯絡管道 / 社群：** [加入我們的 Discord 伺服器](https://discord.gg/uXatcbWKv2)
