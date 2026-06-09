package com.t.compactcombat.combat;

import com.t.compactcombat.client.CombatFeedback;
import com.t.compactcombat.combat.weapon.WeaponData;
import com.t.compactcombat.combat.weapon.WeaponManager;
import com.t.compactcombat.skill.SkillBonuses;

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

    public static int getComboStep() {
        return comboStep;
    }

    public static int getComboResetTicks() {
        return comboResetTicks;
    }

    public static void tryAttack() {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return;
        if (!CombatState.isCombatMode()) return;
        if (!CombatFeedback.canAttack()) return;
        if (attackCooldownTicks > 0) return;

        WeaponData weapon = WeaponManager.getCurrentWeapon(mc.player);
        int staminaCost = Math.round(15 * weapon.getStaminaCostMultiplier()
                * (1.0F - SkillBonuses.getAgilityStaminaDiscount()));
        if (StaminaManager.getStamina() < staminaCost) return;

        StaminaManager.useStamina(staminaCost);

        comboStep++;
        if (comboStep > weapon.getMaxCombo()) {
            comboStep = 1;
        }

        comboResetTicks = 20;
        attackCooldownTicks = weapon.getBaseCooldownTicks();
        CombatFeedback.onAttack(mc.player, comboStep);
        CombatManager.performComboAttack(comboStep);

        mc.player.displayClientMessage(
                Component.literal("Weapon: " + weapon.getType() + " | Combo " + comboStep + "/"
                        + weapon.getMaxCombo()),
                true
        );
    }
}
