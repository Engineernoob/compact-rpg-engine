package com.t.compactcombat.loot;

public class AffixData {

    private final AffixType type;
    private final float value;

    public AffixData(AffixType type, float value) {
        this.type = type;
        this.value = value;
    }

    public AffixType getType() {
        return type;
    }

    public float getValue() {
        return value;
    }
}
