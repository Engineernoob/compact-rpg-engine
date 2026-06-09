package com.t.compactcombat.boss;

public class BossHudData {

    private final String bossName;
    private final float healthPercentage;
    private final BossPhase currentPhase;

    public BossHudData(String bossName, float healthPercentage, BossPhase currentPhase) {
        this.bossName = bossName;
        this.healthPercentage = Math.max(0.0F, Math.min(1.0F, healthPercentage));
        this.currentPhase = currentPhase;
    }

    public String getBossName() {
        return bossName;
    }

    public float getHealthPercentage() {
        return healthPercentage;
    }

    public BossPhase getCurrentPhase() {
        return currentPhase;
    }
}
