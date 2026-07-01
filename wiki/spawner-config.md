# VocchiPet 野生寵物刷新配置說明
# VocchiPet Wild Pet Spawner Configuration Guide

本文件說明 `spawner.yml` 的結構、權重計算邏輯與配置規範。
This document describes the structure, weight calculation logic, and configuration rules of `spawner.yml`.

---

## 📋 配置檔案結構 / Configuration File Structure

```yaml
biomes:
  <生態域名稱>:
    rarity-chances:
      <稀有度>: <機率值>
    spawn-groups:
      <稀有度>:
        <寵物 ID>: <權重>
```

### 層級說明 / Layer Description

| 層級 | 說明 | 範例 |
|------|------|------|
| `biomes` | 根節點，包含所有生態域配置 | - |
| `PLAINS` | 生態域名稱（必須為有效 Bukkit Biome 名稱） | `PLAINS`, `DESERT`, `OCEAN` |
| `rarity-chances` | 各稀有度在該生態域的生成機率 | `COMMON: 70.0` |
| `spawn-groups` | 各稀有度下的寵物及其權重 | `slime_green: 100` |

---

## 🎲 六大稀有度類別 / Six Rarity Categories

| 稀有度 | 英文名稱 | 代表意義 | 建議機率範圍 |
|--------|----------|----------|--------------|
| 普通 | `COMMON` | 最常見的寵物 | 50% ~ 70% |
| 罕見 | `UNCOMMON` | 稍具價值的寵物 | 15% ~ 30% |
| 稀有 | `RARE` | 具有一定挑戰性或價值的寵物 | 5% ~ 15% |
| 史詩 | `EPIC` | 非常強大且難以遇見的寵物 | 1% ~ 5% |
| 傳奇 | `LEGENDARY` | 極其罕見，擁有強大能力的寵物 | 0.1% ~ 1% |
| 神秘 | `MYTHICAL` | 僅在特殊條件或傳說中出現的寵物 | 0.01% ~ 0.1% |

> **注意：** `rarity-chances` 所有稀有度的機率總和建議為 `100.0`，以確保機率分佈正確。
> **Note:** The sum of all rarity chances in `rarity-chances` should ideally be `100.0` to ensure correct probability distribution.

---

## ⚖️ 權重計算邏輯 / Weight Calculation Logic

系統採用**兩階段權重選擇法**來決定生成的寵物：
The system uses a **two-stage weighted selection** to determine the spawned pet:

### 第一階段：選擇稀有度 / Stage 1: Select Rarity

根據 `rarity-chances` 中設定的機率，隨機選擇一個稀有度類別。
Randomly select a rarity category based on the probabilities set in `rarity-chances`.

```
假設平原生態域：
COMMON: 70.0
UNCOMMON: 20.0
RARE: 10.0

總權重 = 70.0 + 20.0 + 10.0 = 100.0

隨機值 0.0 ~ 69.9  → COMMON
隨機值 70.0 ~ 89.9  → UNCOMMON
隨機值 90.0 ~ 100.0 → RARE
```

### 第二階段：選擇寵物 ID / Stage 2: Select Pet ID

在選定的稀有度類別中，根據各寵物的**權重**進行隨機選擇。
Within the selected rarity category, randomly select a pet based on its **weight**.

```
假設 COMMON 稀有度：
slime_green: 100
rabbit_brown: 50

總權重 = 100 + 50 = 150

slime_green 的實際生成機率 = 70% × (100 / 150) = 46.67%
rabbit_brown 的實際生成機率 = 70% × (50 / 150) = 23.33%
```

### 最終機率公式 / Final Probability Formula

$$
\text{寵物實際生成機率} = \text{稀有度機率} \times \frac{\text{該寵物權重}}{\text{該稀有度所有寵物權重總和}}
$$

---

## 📊 配置範例 / Configuration Example

```yaml
biomes:
  PLAINS:
    rarity-chances:
      COMMON: 70.0
      UNCOMMON: 20.0
      RARE: 7.0
      EPIC: 2.5
      LEGENDARY: 0.4
      MYTHICAL: 0.1
    spawn-groups:
      COMMON:
        slime_green: 100
        rabbit_brown: 50
      UNCOMMON:
        fox_red: 10
      RARE:
        wolf_grey: 5
      EPIC:
        dragon_baby: 1
      LEGENDARY:
        phoenix_young: 1
      MYTHICAL:
        void_spirit: 1

  DESERT:
    rarity-chances:
      COMMON: 60.0
      UNCOMMON: 25.0
      RARE: 10.0
      EPIC: 4.0
      LEGENDARY: 0.9
      MYTHICAL: 0.1
    spawn-groups:
      COMMON:
        scorpio_sand: 100
      UNCOMMON:
        snake_desert: 50
      RARE:
        mummy_cat: 10
      EPIC:
        sand_storm_elemental: 5
      LEGENDARY:
        anubis_shadow: 1
      MYTHICAL:
        sphinx_riddle: 1
```

---

## 🧪 測試驗證 / Test Validation

系統包含完整的 JUnit 測試，確保：
The system includes comprehensive JUnit tests to ensure:

1. **機率分佈正確性**：多次隨機抽樣驗證機率分佈符合預期（允許 ±6% 統計誤差）。
   **Probability Distribution Correctness**: Multiple random sampling tests verify the probability distribution matches expectations (allowing ±6% statistical deviation).

2. **權重加總正確性**：驗證 `rarity-chances` 總和是否為 100%，以及各稀有度內權重分配是否正確。
   **Weight Sum Correctness**: Verifies that `rarity-chances` sums to 100% and weight distribution within each rarity is correct.

3. **配置文件解析**：驗證 YAML 加載、無效生態域名稱與無效稀有度名稱的過濾邏輯。
   **Configuration File Parsing**: Verifies YAML loading, filtering of invalid biome names, and invalid rarity names.

---

## 📝 開發者備註 / Developer Notes

- 所有方法與類別皆使用**繁體中文 + 英文**的 JavaDoc 雙語註釋。
  All methods and classes use bilingual JavaDoc comments in **Traditional Chinese + English**.
- 無效的生態域名稱將在加載時被自動忽略，並且不影響其他有效配置。
  Invalid biome names are automatically ignored during loading and do not affect other valid configurations.
- 無效的稀有度名稱同樣會被忽略，確保配置的健壯性。
  Invalid rarity names are also ignored to ensure configuration robustness.
