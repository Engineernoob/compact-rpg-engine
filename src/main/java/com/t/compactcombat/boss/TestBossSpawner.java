package com.t.compactcombat.boss;

import net.minecraft.world.entity.LivingEntity;

public class TestBossSpawner {

    private static final String TEST_BOSS_NAME = "Knight Captain";

    public static void makeBoss(LivingEntity entity) {
        BossManager.registerBoss(entity.getUUID(),
                new BossData(TEST_BOSS_NAME, entity.getMaxHealth(), entity.getHealth()));
    }
}
