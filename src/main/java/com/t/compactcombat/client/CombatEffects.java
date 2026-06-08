package com.t.compactcombat.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class CombatEffects {

    public static void slash(int comboStep) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || mc.level == null)
            return;

        Vec3 look = player.getLookAngle();
        Vec3 forward = new Vec3(look.x, 0.0, look.z).normalize();
        if (forward.lengthSqr() == 0.0) {
            forward = Vec3.directionFromRotation(0.0F, player.getYRot());
        }

        Vec3 side = new Vec3(-forward.z, 0.0, forward.x);
        Vec3 base = player.position()
                .add(0.0, 1.15, 0.0)
                .add(forward.scale(1.8));

        int count = switch (comboStep) {
            case 1 -> 14;
            case 2 -> 18;
            case 3 -> 24;
            default -> 14;
        };

        mc.level.addParticle(
                ParticleTypes.SWEEP_ATTACK,
                base.x,
                base.y,
                base.z,
                0.0,
                0.0,
                0.0);

        for (int i = 0; i < count; i++) {
            double progress = count == 1 ? 0.0 : i / (double) (count - 1);
            double arc = (progress - 0.5) * (comboStep == 3 ? 2.2 : 1.6);
            double height = Math.sin(progress * Math.PI) * 0.45;
            Vec3 pos = base
                    .add(side.scale(arc))
                    .add(forward.scale(Math.abs(arc) * -0.2))
                    .add(0.0, height, 0.0);

            mc.level.addParticle(
                    ParticleTypes.CRIT,
                    pos.x,
                    pos.y,
                    pos.z,
                    (Math.random() - 0.5) * 0.1,
                    0.05,
                    (Math.random() - 0.5) * 0.1);
        }
    }

    public static void dodgeBurst() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || mc.level == null)
            return;

        Vec3 pos = player.position();

        for (int i = 0; i < 8; i++) {
            mc.level.addParticle(
                    ParticleTypes.CLOUD,
                    pos.x,
                    pos.y + 0.1,
                    pos.z,
                    (Math.random() - 0.5) * 0.2,
                    0.02,
                    (Math.random() - 0.5) * 0.2);
        }
    }
}
