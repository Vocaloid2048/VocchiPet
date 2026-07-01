# 野生寵物生成設定與權重機制 (Spawner Configuration)

本文件說明 `spawner.yml` 的配置方式以及內部使用的權重計算邏輯。

## 1. 權重算法 (Weight Algorithm)

系統採用「隨機加權分佈」演算法：
1. 計算所有項目權重之總和 ($S$)。
2. 生成一個 $0$ 至 $S-1$ 的隨機整數 ($R$)。
3. 遍歷項目列表，逐項減去權重，當 $R < 0$ 時，該項目即為目標。

此方法確保機率嚴格等於：
$$\text{機率} = \frac{\text{項目權重}}{\text{總權重}}$$

## 2. spawner.yml 配置範例

```yaml
rarity-rates:
  COMMON: 50
  UNCOMMON: 25
  RARE: 15
  EPIC: 7
  LEGENDARY: 2
  MYTHICAL: 1

biomes:
  PLAINS:
    COMMON:
      - id: "pet_small_bird"
        weight: 70
      - id: "pet_bunny"
        weight: 30
    UNCOMMON:
      - id: "pet_sheep_spirit"
        weight: 100
  FOREST:
    COMMON:
      - id: "pet_small_bird"
        weight: 40
      - id: "pet_fox_spirit"
        weight: 60
    RARE:
      - id: "pet_dryad"
        weight: 100
```
