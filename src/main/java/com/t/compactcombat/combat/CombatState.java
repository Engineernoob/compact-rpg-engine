package com.t.compactcombat.combat;

public class CombatState {

    private static boolean combatMode = false;

    public static boolean isCombatMode() {
        return combatMode;
    }

    public static void toggleCombatMode() {
        combatMode = !combatMode;
    }
}