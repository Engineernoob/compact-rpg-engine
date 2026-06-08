package com.t.compactcombat.combat.weapon;

public class WeaponData {

    private final WeaponType type;
    private final float damageMultiplier;
    private final float staminaCostMultiplier;
    private final float attackSpeedMultiplier;
    private final float reach;
    private final int maxCombo;
    private final int baseCooldownTicks;

    public WeaponData(WeaponType type, float damageMultiplier, float staminaCostMultiplier,
            float attackSpeedMultiplier, float reach, int maxCombo, int baseCooldownTicks) {
        this.type = type;
        this.damageMultiplier = damageMultiplier;
        this.staminaCostMultiplier = staminaCostMultiplier;
        this.attackSpeedMultiplier = attackSpeedMultiplier;
        this.reach = reach;
        this.maxCombo = maxCombo;
        this.baseCooldownTicks = baseCooldownTicks;
    }

    public WeaponType getType() {
        return type;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public float getStaminaCostMultiplier() {
        return staminaCostMultiplier;
    }

    public float getAttackSpeedMultiplier() {
        return attackSpeedMultiplier;
    }

    public float getReach() {
        return reach;
    }

    public int getMaxCombo() {
        return maxCombo;
    }

    public int getBaseCooldownTicks() {
        return baseCooldownTicks;
    }
}
