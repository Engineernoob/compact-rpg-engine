package com.t.compactcombat.skill;

import java.util.EnumMap;
import java.util.Map;

public class SkillManager {

    private static final Map<SkillType, SkillData> SKILLS = new EnumMap<>(SkillType.class);

    static {
        for (SkillType type : SkillType.values()) {
            SKILLS.put(type, new SkillData(1, 0));
        }
    }

    public static SkillData getSkill(SkillType type) {
        return SKILLS.get(type);
    }

    public static boolean addExperience(SkillType type, int amount) {
        return getSkill(type).addExperience(amount);
    }

    public static int getLevel(SkillType type) {
        return getSkill(type).getLevel();
    }
}
