package com.t.compactcombat.client;

import com.t.compactcombat.CompactCombat;
import com.t.compactcombat.combat.CombatState;
import com.t.compactcombat.combat.ComboManager;
import com.t.compactcombat.combat.StaminaManager;
import com.t.compactcombat.combat.weapon.WeaponData;
import com.t.compactcombat.combat.weapon.WeaponManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
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
    private static final int LOW_STAMINA_COLOR = 0xFFFFD166;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int MUTED_TEXT_COLOR = 0xFFDDDDDD;

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() != VanillaGuiOverlay.HOTBAR.type())
            return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || !CombatState.isCombatMode())
            return;

        drawCombatStatus(event.getGuiGraphics(), mc, player, event.getWindow().getGuiScaledWidth(),
                event.getWindow().getGuiScaledHeight());
    }

    private static void drawCombatStatus(GuiGraphics guiGraphics, Minecraft mc, Player player, int screenWidth,
            int screenHeight) {
        int x = screenWidth / 2 - STAMINA_BAR_WIDTH / 2;
        int y = screenHeight - 60;
        int stamina = Math.max(0, Math.min(MAX_STAMINA, StaminaManager.getStamina()));
        WeaponData weapon = WeaponManager.getCurrentWeapon(player);

        drawCenteredText(guiGraphics, mc, "Weapon: " + weapon.getType(), screenWidth / 2, y - 31, MUTED_TEXT_COLOR);
        drawCenteredText(guiGraphics, mc, "Combo: " + ComboManager.getComboStep() + " / " + weapon.getMaxCombo(),
                screenWidth / 2, y - 21, TEXT_COLOR);
        drawCenteredText(guiGraphics, mc, "STAMINA", screenWidth / 2, y - 10, TEXT_COLOR);
        drawBar(guiGraphics, x, y, STAMINA_BAR_WIDTH, STAMINA_BAR_HEIGHT, stamina / (float) MAX_STAMINA,
                BACKGROUND_COLOR, STAMINA_COLOR);

        if (stamina < 25) {
            drawCenteredText(guiGraphics, mc, "LOW STAMINA", screenWidth / 2, y + STAMINA_BAR_HEIGHT + 4,
                    LOW_STAMINA_COLOR);
        }
    }

    private static void drawBar(GuiGraphics guiGraphics, int x, int y, int width, int height, float fillPercent,
            int backgroundColor, int fillColor) {
        int filledWidth = Math.round(Math.max(0.0F, Math.min(1.0F, fillPercent)) * width);

        guiGraphics.fill(x, y, x + width, y + height, backgroundColor);
        guiGraphics.fill(x, y, x + filledWidth, y + height, fillColor);
    }

    private static void drawCenteredText(GuiGraphics guiGraphics, Minecraft mc, String text, int centerX, int y,
            int color) {
        int x = centerX - mc.font.width(text) / 2;
        guiGraphics.drawString(mc.font, text, x, y, color);
    }
}
