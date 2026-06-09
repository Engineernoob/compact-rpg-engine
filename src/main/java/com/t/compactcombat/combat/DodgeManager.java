package com.t.compactcombat.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import com.t.compactcombat.client.CombatEffects;
import com.t.compactcombat.client.CombatFeedback;
import com.t.compactcombat.skill.SkillBonuses;

public class DodgeManager {

    private static int cooldownTicks = 0;

    public static void tick() {
        if (cooldownTicks > 0) {
            cooldownTicks--;
        }
    }

    public static void tryDodge() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null)
            return;
        if (!CombatState.isCombatMode())
            return;
        if (cooldownTicks > 0)
            return;

        int staminaCost = Math.round(25 * (1.0F - SkillBonuses.getAgilityStaminaDiscount()));
        if (StaminaManager.getStamina() < staminaCost)
            return;

        StaminaManager.useStamina(staminaCost);
        cooldownTicks = 14;

        Vec3 look = player.getLookAngle();

        player.setDeltaMovement(
                -look.x * 0.9,
                0.15,
                -look.z * 0.9);

        player.hurtMarked = true;
        CombatFeedback.onDodge(player);
        CombatEffects.dodgeBurst();
    }
}
