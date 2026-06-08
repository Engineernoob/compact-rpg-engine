package com.t.compactcombat;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(CompactCombat.MOD_ID)
public class CompactCombat {

    public static final String MOD_ID = "compactcombat";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CompactCombat() {
        LOGGER.info("Compact Combat initialized!");
    }
}