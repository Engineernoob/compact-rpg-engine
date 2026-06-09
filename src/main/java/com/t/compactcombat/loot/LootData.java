package com.t.compactcombat.loot;

import java.util.List;

public class LootData {

    private final LootRarity rarity;
    private final List<AffixData> affixes;
    private final long seed;

    public LootData(LootRarity rarity, List<AffixData> affixes, long seed) {
        this.rarity = rarity;
        this.affixes = List.copyOf(affixes);
        this.seed = seed;
    }

    public LootRarity getRarity() {
        return rarity;
    }

    public List<AffixData> getAffixes() {
        return affixes;
    }

    public long getSeed() {
        return seed;
    }

    public String getDisplayName() {
        return rarity.getDisplayName() + " Knight's Greatsword";
    }
}
