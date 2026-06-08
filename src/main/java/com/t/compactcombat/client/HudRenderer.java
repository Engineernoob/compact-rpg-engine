package com.t.compactcombat.client;

import com.t.compactcombat.CompactCombat;
import com.t.compactcombat.combat.CombatState;
import com.t.compactcombat.combat.StaminaManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CompactCombat.MOD_ID, value = Dist.CLIENT)
public class HudRenderer {

    private static final int STAMINA_BAR_WIDTH = 100;
    private static final int STAMINA_BAR_HEIGHT = 8;
    private static final int MAX_STAMINA = 100;
    private static final int BACKGROUND_COLOR = 0xAA202020;
    private static final int STAMINA_COLOR = 0xFF2ECC71;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() != VanillaGuiOverlay.HOTBAR.type())
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || !CombatState.isCombatMode())
            return;

        renderStaminaBar(event.getGuiGraphics(), mc, event.getWindow().getGuiScaledWidth(),
                event.getWindow().getGuiScaledHeight());
    }

    private static void renderStaminaBar(GuiGraphics guiGraphics, Minecraft mc, int screenWidth, int screenHeight) {
        int x = screenWidth / 2 - STAMINA_BAR_WIDTH / 2;
        int y = screenHeight - 60;
        int stamina = Math.max(0, Math.min(MAX_STAMINA, StaminaManager.getStamina()));
        int filledWidth = Math.round((stamina / (float) MAX_STAMINA) * STAMINA_BAR_WIDTH);

        guiGraphics.fill(x, y, x + STAMINA_BAR_WIDTH, y + STAMINA_BAR_HEIGHT, BACKGROUND_COLOR);
        guiGraphics.fill(x, y, x + filledWidth, y + STAMINA_BAR_HEIGHT, STAMINA_COLOR);
        guiGraphics.drawString(mc.font, "STAMINA", x, y - 10, TEXT_COLOR);
    }
}
