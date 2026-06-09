package com.t.compactcombat.loot;

public enum LootRarity {
    COMMON("Common", 0xFFFFFFFF),
    RARE("Rare", 0xFF4FC3F7),
    EPIC("Epic", 0xFFB56DFF),
    LEGENDARY("Legendary", 0xFFFFA726),
    MYTHIC("Mythic", 0xFFFF4F8B);

    private final String displayName;
    private final int color;

    LootRarity(String displayName, int color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getColor() {
        return color;
    }
}
