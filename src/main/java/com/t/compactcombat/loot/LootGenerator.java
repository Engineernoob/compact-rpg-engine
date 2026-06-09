package com.t.compactcombat.loot;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.RandomSource;

public class LootGenerator {

    public static LootData generate(RandomSource random) {
        long seed = random.nextLong();
        LootRarity rarity = rollRarity(random);
        int affixCount = getAffixCount(rarity);
        List<AffixData> affixes = new ArrayList<>(affixCount);

        for (int i = 0; i < affixCount; i++) {
            AffixType type = AffixType.values()[random.nextInt(AffixType.values().length)];
            affixes.add(new AffixData(type, rollAffixValue(random, type, rarity)));
        }

        return new LootData(rarity, affixes, seed);
    }

    private static LootRarity rollRarity(RandomSource random) {
        int roll = random.nextInt(100);

        if (roll < 60) {
            return LootRarity.COMMON;
        }
        if (roll < 85) {
            return LootRarity.RARE;
        }
        if (roll < 95) {
            return LootRarity.EPIC;
        }
        if (roll < 99) {
            return LootRarity.LEGENDARY;
        }

        return LootRarity.MYTHIC;
    }

    private static int getAffixCount(LootRarity rarity) {
        return switch (rarity) {
            case COMMON -> 0;
            case RARE -> 1;
            case EPIC -> 2;
            case LEGENDARY -> 3;
            case MYTHIC -> 4;
        };
    }

    private static float rollAffixValue(RandomSource random, AffixType type, LootRarity rarity) {
        float rarityScale = switch (rarity) {
            case COMMON -> 1.0F;
            case RARE -> 1.15F;
            case EPIC -> 1.35F;
            case LEGENDARY -> 1.6F;
            case MYTHIC -> 2.0F;
        };

        return switch (type) {
            case ATTACK_DAMAGE -> rollPercent(random, 4.0F, 10.0F, rarityScale);
            case CRIT_CHANCE -> rollPercent(random, 2.0F, 6.0F, rarityScale);
            case ATTACK_SPEED -> rollPercent(random, 3.0F, 8.0F, rarityScale);
            case REACH -> rollFlat(random, 0.15F, 0.45F, rarityScale);
            case STAMINA -> rollPercent(random, 4.0F, 12.0F, rarityScale);
            case DEFENSE -> rollPercent(random, 3.0F, 9.0F, rarityScale);
            case AGILITY -> rollPercent(random, 3.0F, 9.0F, rarityScale);
        };
    }

    private static float rollPercent(RandomSource random, float min, float max, float scale) {
        return roundOneDecimal((min + random.nextFloat() * (max - min)) * scale);
    }

    private static float rollFlat(RandomSource random, float min, float max, float scale) {
        return roundTwoDecimals((min + random.nextFloat() * (max - min)) * scale);
    }

    private static float roundOneDecimal(float value) {
        return Math.round(value * 10.0F) / 10.0F;
    }

    private static float roundTwoDecimals(float value) {
        return Math.round(value * 100.0F) / 100.0F;
    }
}
