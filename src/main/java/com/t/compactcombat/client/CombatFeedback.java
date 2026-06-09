package com.t.compactcombat.client;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class CombatFeedback {

    private static final int HIT_STOP_DURATION_TICKS = 2;

    private static int hitStopTicks;

    public static void tick() {
        if (hitStopTicks > 0) {
            hitStopTicks--;
        }
    }

    public static boolean canAttack() {
        return hitStopTicks <= 0;
    }

    public static void onAttack(Player player, int comboStep) {
        applyAttackLunge(player, comboStep);
        applyAttackCameraImpulse(player, comboStep);
    }

    public static void onHit() {
        hitStopTicks = HIT_STOP_DURATION_TICKS;
    }

    public static void onDodge(Player player) {
        player.setXRot(clampPitch(player.getXRot() - 1.0F));
    }

    private static void applyAttackLunge(Player player, int comboStep) {
        Vec3 look = player.getLookAngle();
        Vec3 forward = new Vec3(look.x, 0.0, look.z);
        if (forward.lengthSqr() == 0.0)
            return;

        double strength = switch (comboStep) {
            case 1 -> 0.12;
            case 2 -> 0.16;
            case 3 -> 0.22;
            default -> 0.28;
        };

        player.setDeltaMovement(player.getDeltaMovement().add(forward.normalize().scale(strength)));
        player.hurtMarked = true;
    }

    private static void applyAttackCameraImpulse(Player player, int comboStep) {
        float pitchImpulse = switch (comboStep) {
            case 1 -> 0.6F;
            case 2 -> 0.9F;
            default -> 1.2F;
        };

        player.setXRot(clampPitch(player.getXRot() + pitchImpulse));
    }

    private static float clampPitch(float pitch) {
        return Math.max(-90.0F, Math.min(90.0F, pitch));
    }
}
