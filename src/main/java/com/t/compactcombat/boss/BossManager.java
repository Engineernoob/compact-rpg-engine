package com.t.compactcombat.boss;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossManager {

    private static final Map<UUID, BossData> BOSSES = new HashMap<>();

    public static void registerBoss(UUID entityId, BossData boss) {
        BOSSES.put(entityId, boss);
    }

    public static boolean isBoss(UUID entityId) {
        return BOSSES.containsKey(entityId);
    }

    public static BossData getBoss(UUID entityId) {
        return BOSSES.get(entityId);
    }

    public static void removeBoss(UUID entityId) {
        BOSSES.remove(entityId);
    }

    public static BossHudData getActiveBossHudData() {
        if (BOSSES.isEmpty()) {
            return null;
        }

        BossData boss = BOSSES.values().iterator().next();
        return new BossHudData(boss.getBossName(), boss.getCurrentHealth() / boss.getMaxHealth(),
                boss.getCurrentPhase());
    }
}
