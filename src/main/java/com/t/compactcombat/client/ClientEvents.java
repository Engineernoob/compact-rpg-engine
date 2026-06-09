package com.t.compactcombat.client;

import com.t.compactcombat.CompactCombat;
import com.t.compactcombat.combat.CombatState;
import com.t.compactcombat.combat.StaminaManager;
import com.t.compactcombat.combat.DodgeManager;
import com.t.compactcombat.combat.ComboManager;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
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
        }
    }
}
