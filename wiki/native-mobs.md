# 🐾 原生生物系統 (Native Mobs System)

VocchiPet 捨棄了複雜的 3D 模型渲染，全面改用 Minecraft 原生實體 (Vanilla Mobs) 作為寵物載體。這確保了伺服器的極致效能以及對客戶端 0 依賴的純淨體驗。

## 🚀 技術核心：NMS 接管
我們透過 **NMS (Native Minecraft Server)** 技術對原生生物進行深度魔改：
- **實體繼承**：每種寵物種類 (`PetSpecies`) 對應一種原生實體類型（如 `Wolf`, `Cat`, `Fox`, `Blaze`）。
- **AI 路徑接管**：移除原生生物的目標選擇器 (GoalSelector)，替換為自定義的跟隨 (Follow)、保護 (Protect) 與工作 (Work) AI。
- **改名機制**：透過 `CustomName` 屬性實現寵物名稱在頭頂的即時顯示。

## 🎨 寵物亞種 (Subspecies)
為了增加視覺多樣性，我們利用原生生物的變體屬性來實作「亞種」：
- **變體與顏色**：利用羊的顏色、熱帶魚的圖樣或狐狸的種類（赤狐/雪狐）來定義稀有亞種。
- **粒子效果**：亞種寵物身邊會環繞特定的粒子特效，以彰顯其特殊身份。

## 🛠️ 開發者定義種類 (Defining Species)
在 `PetSpecies` 資料結構中，現在直接指定 `EntityType`：

```kotlin
val fireDragon = PetSpecies(
    id = "fire_dragon",
    displayName = "§c炙焰紅龍",
    entityType = EntityType.BLAZE, // 使用烈焰神作為外觀
    baseStats = stats // 基礎屬性定義
)
```

## 🏗️ 渲染優點
- **0 額外負擔**：不再需要同步 `ItemDisplay` 的位移與旋轉，完全交由 Minecraft 內建實體引擎處理。
- **物理特性**：繼承原生生物的碰撞箱、重力與水中浮力，表現更自然。
- **動畫支援**：直接使用原生生物的攻擊、受傷與閒置動畫。
