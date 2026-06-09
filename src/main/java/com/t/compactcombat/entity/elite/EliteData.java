package com.t.compactcombat.entity.elite;

public class EliteData {

    private final EliteModifier modifier;
    private final float healthMultiplier;
    private final float damageMultiplier;
    private final int color;

    public EliteData(EliteModifier modifier, float healthMultiplier, float damageMultiplier, int color) {
        this.modifier = modifier;
        this.healthMultiplier = healthMultiplier;
        this.damageMultiplier = damageMultiplier;
        this.color = color;
    }

    public EliteModifier getModifier() {
        return modifier;
    }

    public float getHealthMultiplier() {
        return healthMultiplier;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public int getColor() {
        return color;
    }
}
