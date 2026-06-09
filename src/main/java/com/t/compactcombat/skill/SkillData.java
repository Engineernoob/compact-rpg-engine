package com.t.compactcombat.skill;

public class SkillData {

    public static final int MAX_LEVEL = 100;

    private int level;
    private int experience;

    public SkillData(int level, int experience) {
        this.level = level;
        this.experience = experience;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public boolean addExperience(int amount) {
        if (amount <= 0 || level >= MAX_LEVEL)
            return false;

        boolean leveledUp = false;
        experience += amount;

        while (level < MAX_LEVEL && experience >= getXpRequired()) {
            experience -= getXpRequired();
            level++;
            leveledUp = true;
        }

        if (level >= MAX_LEVEL) {
            experience = 0;
        }

        return leveledUp;
    }

    public int getXpRequired() {
        return level * 100;
    }
}
