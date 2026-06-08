package com.t.compactcombat.combat;

import com.t.compactcombat.client.CombatEffects;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CombatManager {

    private static final double ATTACK_RANGE = 3.0;
    private static final double MIN_FRONT_DOT = 0.25;

    public static void performComboAttack(int comboStep) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || player.level() == null)
            return;

        LivingEntity target = findTarget(player);
        if (target != null) {
            target.hurt(player.damageSources().playerAttack(player), getComboDamage(comboStep));
            applyKnockback(player, target, comboStep);
        }

        CombatEffects.slash(comboStep);
    }

    private static LivingEntity findTarget(Player player) {
        Vec3 look = player.getLookAngle().normalize();
        Vec3 eyePosition = player.getEyePosition();
        AABB hitBox = player.getBoundingBox()
        .inflate(ATTACK_RANGE)
        .move(look.scale(ATTACK_RANGE * 0.5));

        LivingEntity bestTarget = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (LivingEntity candidate : player.level().getEntitiesOfClass(LivingEntity.class, hitBox)) {
            if (candidate == player || !candidate.isAlive())
                continue;

            Vec3 targetDirection = candidate.getEyePosition().subtract(eyePosition);
            double distance = targetDirection.length();
            if (distance > ATTACK_RANGE || distance == 0.0)
                continue;

            double dot = look.dot(targetDirection.normalize());
            if (dot < MIN_FRONT_DOT)
                continue;

            double score = dot - (distance * 0.1);
            if (score > bestScore) {
                bestScore = score;
                bestTarget = candidate;
            }
        }

        return bestTarget;
    }

    private static float getComboDamage(int comboStep) {
        return switch (comboStep) {
            case 1 -> 4.0F;
            case 2 -> 5.0F;
            case 3 -> 7.0F;
            default -> 4.0F;
        };
    }

    private static void applyKnockback(Player player, LivingEntity target, int comboStep) {
        Vec3 look = player.getLookAngle().normalize();
        double strength = comboStep == 3 ? 0.8 : 0.35;

        target.knockback(strength, -look.x, -look.z);
    }
}
