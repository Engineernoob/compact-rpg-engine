package com.t.compactcombat.client;

import com.t.compactcombat.CompactCombat;
import com.t.compactcombat.boss.TestBossSpawner;
import com.t.compactcombat.combat.CombatState;
import com.t.compactcombat.combat.StaminaManager;
import com.t.compactcombat.combat.DodgeManager;
import com.t.compactcombat.combat.ComboManager;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CompactCombat.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public class ClientEvents {

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(Keybinds.TOGGLE_COMBAT);
        event.register(Keybinds.DODGE);
        event.register(Keybinds.MAKE_TARGET_BOSS);
    }

    @Mod.EventBusSubscriber(modid = CompactCombat.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END)
                return;

            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null)
                return;
            StaminaManager.regenerate();
            DodgeManager.tick();
            ComboManager.tick();
            CombatFeedback.tick();

            while (Keybinds.TOGGLE_COMBAT.consumeClick()) {
                CombatState.toggleCombatMode();

                mc.player.displayClientMessage(
                        Component.literal(
                                "Combat Mode: "
                                        + (CombatState.isCombatMode() ? "ON" : "OFF")
                                        + " | Stamina: "
                                        + StaminaManager.getStamina()),
                        true);
            }

            while (Keybinds.DODGE.consumeClick()) {
                DodgeManager.tryDodge();
            }
            while (mc.options.keyAttack.consumeClick()) {
                ComboManager.tryAttack();
            }
            while (Keybinds.MAKE_TARGET_BOSS.consumeClick()) {
                makeTargetBoss(mc);
            }
        }

        private static void makeTargetBoss(Minecraft mc) {
            LivingEntity target = findTargetInFront(mc.player, 6.0F);
            if (target == null)
                return;

            TestBossSpawner.makeBoss(target);
            mc.player.displayClientMessage(Component.literal("Knight Captain awakened"), true);
        }

        private static LivingEntity findTargetInFront(Player player, float reach) {
            Vec3 look = player.getLookAngle().normalize();
            Vec3 eyePosition = player.getEyePosition();
            AABB hitBox = player.getBoundingBox()
                    .inflate(reach)
                    .move(look.scale(reach * 0.5));

            LivingEntity bestTarget = null;
            double bestScore = Double.NEGATIVE_INFINITY;

            for (LivingEntity candidate : player.level().getEntitiesOfClass(LivingEntity.class, hitBox)) {
                if (candidate == player || !candidate.isAlive())
                    continue;

                Vec3 targetDirection = candidate.getEyePosition().subtract(eyePosition);
                double distance = targetDirection.length();
                if (distance > reach || distance == 0.0)
                    continue;

                double dot = look.dot(targetDirection.normalize());
                if (dot < 0.35)
                    continue;

                double score = dot - distance * 0.05;
                if (score > bestScore) {
                    bestScore = score;
                    bestTarget = candidate;
                }
            }

            return bestTarget;
        }
    }
}
