package com.t.compactcombat.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.t.compactcombat.CompactCombat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CompactCombat.MOD_ID, value = Dist.CLIENT)
public class DamageNumberRenderer {

    private static final int MAX_DAMAGE_NUMBERS = 32;
    private static final int DEFAULT_LIFETIME_TICKS = 24;
    private static final float TEXT_SCALE = 0.025F;
    private static final List<DamageNumber> DAMAGE_NUMBERS = new ArrayList<>();

    public static void spawn(double x, double y, double z, float damage) {
        addDamageNumber(new DamageNumber(formatDamage(damage), x, y, z, DEFAULT_LIFETIME_TICKS, 0x00FFDF6E));
    }

    public static void spawnText(double x, double y, double z, String text, int color) {
        addDamageNumber(new DamageNumber(text, x, y, z, DEFAULT_LIFETIME_TICKS, color & 0x00FFFFFF));
    }

    private static void addDamageNumber(DamageNumber damageNumber) {
        if (DAMAGE_NUMBERS.size() >= MAX_DAMAGE_NUMBERS) {
            DAMAGE_NUMBERS.remove(0);
        }

        DAMAGE_NUMBERS.add(damageNumber);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        Iterator<DamageNumber> iterator = DAMAGE_NUMBERS.iterator();
        while (iterator.hasNext()) {
            DamageNumber damageNumber = iterator.next();
            damageNumber.tick();
            if (damageNumber.isExpired()) {
                iterator.remove();
            }
        }
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES || DAMAGE_NUMBERS.isEmpty())
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null)
            return;

        Vec3 cameraPosition = event.getCamera().getPosition();
        PoseStack poseStack = event.getPoseStack();
        Font font = mc.font;
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();

        for (DamageNumber damageNumber : DAMAGE_NUMBERS) {
            renderDamageNumber(damageNumber, poseStack, bufferSource, font, cameraPosition, event.getPartialTick());
        }

        bufferSource.endBatch();
    }

    private static void renderDamageNumber(DamageNumber damageNumber, PoseStack poseStack,
            MultiBufferSource bufferSource, Font font, Vec3 cameraPosition, float partialTick) {
        float age = damageNumber.getAge(partialTick);
        float progress = Math.min(1.0F, age / damageNumber.maxLifetimeTicks);
        float alpha = 1.0F - progress;
        double floatingY = damageNumber.y + progress * 0.65;
        int color = ((int) (alpha * 255.0F) << 24) | damageNumber.color;

        poseStack.pushPose();
        poseStack.translate(damageNumber.x - cameraPosition.x, floatingY - cameraPosition.y,
                damageNumber.z - cameraPosition.z);
        poseStack.mulPose(Minecraft.getInstance().gameRenderer.getMainCamera().rotation());
        poseStack.scale(-TEXT_SCALE, -TEXT_SCALE, TEXT_SCALE);

        float textX = -font.width(damageNumber.text) / 2.0F;
        font.drawInBatch(damageNumber.text, textX, 0.0F, color, false, poseStack.last().pose(), bufferSource,
                Font.DisplayMode.NORMAL, 0, LightTexture.FULL_BRIGHT);
        poseStack.popPose();
    }

    private static class DamageNumber {
        private final double x;
        private final double y;
        private final double z;
        private final int maxLifetimeTicks;
        private final String text;
        private final int color;
        private int lifetimeTicks;

        private DamageNumber(String text, double x, double y, double z, int maxLifetimeTicks, int color) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.maxLifetimeTicks = maxLifetimeTicks;
            this.lifetimeTicks = maxLifetimeTicks;
            this.text = text;
            this.color = color;
        }

        private void tick() {
            lifetimeTicks--;
        }

        private boolean isExpired() {
            return lifetimeTicks <= 0;
        }

        private float getAge(float partialTick) {
            return maxLifetimeTicks - lifetimeTicks + partialTick;
        }

    }

    private static String formatDamage(float damage) {
        if (damage == Math.round(damage)) {
            return Integer.toString(Math.round(damage));
        }

        return String.format("%.1f", damage);
    }
}
