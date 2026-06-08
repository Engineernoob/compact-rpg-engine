package com.t.compactcombat.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ComboManager {

    private static int comboStep = 0;
    private static int comboResetTicks = 0;
    private static int attackCooldownTicks = 0;

    public static void tick() {
        if (comboResetTicks > 0) {
            comboResetTicks--;
        } else {
            comboStep = 0;
        }

        if (attackCooldownTicks > 0) {
            attackCooldownTicks--;
        }
    }

    public static void tryAttack() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return;
        if (!CombatState.isCombatMode()) return;
        if (attackCooldownTicks > 0) return;
        if (StaminaManager.getStamina() < 15) return;

        StaminaManager.useStamina(15);

        comboStep++;
        if (comboStep > 3) {
            comboStep = 1;
        }

        comboResetTicks = 20;
        attackCooldownTicks = 8;
        CombatManager.performComboAttack(comboStep);

        mc.player.displayClientMessage(
                Component.literal("Combo Attack " + comboStep + " | Stamina: " + StaminaManager.getStamina()),
                true
        );
    }
}
