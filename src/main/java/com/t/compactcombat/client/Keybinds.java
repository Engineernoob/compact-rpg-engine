package com.t.compactcombat.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.t.compactcombat.CompactCombat;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static final String CATEGORY = "key.categories." + CompactCombat.MOD_ID;

    public static final KeyMapping TOGGLE_COMBAT = new KeyMapping(
            "key." + CompactCombat.MOD_ID + ".toggle_combat",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            CATEGORY);

    public static final KeyMapping DODGE = new KeyMapping(
            "key." + CompactCombat.MOD_ID + ".dodge",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            CATEGORY);

    public static final KeyMapping MAKE_TARGET_BOSS = new KeyMapping(
            "key." + CompactCombat.MOD_ID + ".make_target_boss",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            CATEGORY);
}
