package com.t.compactcombat.skill;

public class SkillBonuses {

    public static float getSwordDamageBonus() {
        int level = SkillManager.getLevel(SkillType.SWORDSMANSHIP);
        return (level / 5) * 0.02F;
    }

    public static float getAgilityStaminaDiscount() {
        int level = SkillManager.getLevel(SkillType.AGILITY);
        return Math.min(0.20F, (level / 5) * 0.01F);
    }

    public static float getDefenseKnockbackResistance() {
        int level = SkillManager.getLevel(SkillType.DEFENSE);
        return Math.min(0.30F, (level / 10) * 0.02F);
    }
}
