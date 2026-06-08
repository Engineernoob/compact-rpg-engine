package com.t.compactcombat.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import com.t.compactcombat.client.CombatEffects;

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
        if (StaminaManager.getStamina() < 25)
            return;

        StaminaManager.useStamina(25);
        cooldownTicks = 14;

        Vec3 look = player.getLookAngle();

        player.setDeltaMovement(
                -look.x * 0.9,
                0.15,
                -look.z * 0.9);

        player.hurtMarked = true;
        CombatEffects.dodgeBurst();
    }
}