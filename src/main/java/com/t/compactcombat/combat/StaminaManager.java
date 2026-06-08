package com.t.compactcombat.combat;

public class StaminaManager {

    private static int stamina = 100;
    private static final int MAX_STAMINA = 100;

    public static int getStamina() {
        return stamina;
    }

    public static void useStamina(int amount) {
        stamina = Math.max(0, stamina - amount);
    }

    public static void regenerate() {
        if (stamina < MAX_STAMINA) {
            stamina++;
        }
    }
}