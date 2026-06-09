package com.t.compactcombat.boss;

public class BossData {

    private final String bossName;
    private BossPhase currentPhase;
    private final float maxHealth;
    private float currentHealth;

    public BossData(String bossName, float maxHealth, float currentHealth) {
        this.bossName = bossName;
        this.maxHealth = Math.max(1.0F, maxHealth);
        this.currentHealth = Math.max(0.0F, Math.min(currentHealth, this.maxHealth));
        updatePhase();
    }

    public String getBossName() {
        return bossName;
    }

    public BossPhase getCurrentPhase() {
        return currentPhase;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(float currentHealth) {
        this.currentHealth = Math.max(0.0F, Math.min(currentHealth, maxHealth));
        updatePhase();
    }

    public void updatePhase() {
        float healthPercentage = currentHealth / maxHealth;

        if (healthPercentage >= 0.66F) {
            currentPhase = BossPhase.PHASE_ONE;
        } else if (healthPercentage >= 0.33F) {
            currentPhase = BossPhase.PHASE_TWO;
        } else {
            currentPhase = BossPhase.PHASE_THREE;
        }
    }
}
