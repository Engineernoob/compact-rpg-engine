package com.t.compactcombat.combat;

import com.t.compactcombat.client.CombatEffects;
import com.t.compactcombat.client.CombatFeedback;
import com.t.compactcombat.client.DamageNumberRenderer;
import com.t.compactcombat.combat.weapon.WeaponData;
import com.t.compactcombat.combat.weapon.WeaponManager;
import com.t.compactcombat.entity.elite.EliteData;
import com.t.compactcombat.entity.elite.EliteManager;
import com.t.compactcombat.skill.SkillBonuses;
import com.t.compactcombat.skill.SkillData;
import com.t.compactcombat.skill.SkillManager;
import com.t.compactcombat.skill.SkillType;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class CombatManager {

    private static final double MIN_FRONT_DOT = 0.25;

    public static void performComboAttack(int comboStep) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || player.level() == null)
            return;

        WeaponData weapon = WeaponManager.getCurrentWeapon(player);
        LivingEntity target = findTarget(player, weapon.getReach());
        if (target != null) {
            float damage = getComboDamage(comboStep, weapon);
            boolean damaged = target.hurt(player.damageSources().playerAttack(player), damage);
            if (damaged) {
                applyKnockback(player, target, comboStep, weapon);
                CombatFeedback.onHit();
                DamageNumberRenderer.spawn(target.getX(), target.getY() + target.getBbHeight() + 0.3, target.getZ(),
                        damage);
                showEliteHitText(target);
                awardCombatExperience(player);
            }
        }

        CombatEffects.slash(comboStep);
    }

    private static LivingEntity findTarget(Player player, float reach) {
        Vec3 look = player.getLookAngle().normalize();
        Vec3 eyePosition = player.getEyePosition();
        AABB hitBox = player.getBoundingBox()
                .inflate(reach)
                .move(look.scale(reach * 0.5));

        LivingEntity bestTarget = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (LivingEntity candidate : player.level().getEntitiesOfClass(LivingEntity.class, hitBox)) {
            if (candidate == player || !candidate.isAlive())
                continue;

            Vec3 targetDirection = candidate.getEyePosition().subtract(eyePosition);
            double distance = targetDirection.length();
            if (distance > reach || distance == 0.0)
                continue;

            double dot = look.dot(targetDirection.normalize());
            if (dot < MIN_FRONT_DOT)
                continue;

            double score = dot - (distance * 0.1);
            if (score > bestScore) {
                bestScore = score;
                bestTarget = candidate;
            }
        }

        return bestTarget;
    }

    private static float getComboDamage(int comboStep, WeaponData weapon) {
        float baseDamage = switch (comboStep) {
            case 1 -> 4.0F;
            case 2 -> 5.0F;
            case 3 -> 7.0F;
            default -> 4.0F;
        };

        float damage = baseDamage * weapon.getDamageMultiplier();
        if (comboStep == weapon.getMaxCombo()) {
            damage *= 1.2F;
        }

        return damage * (1.0F + SkillBonuses.getSwordDamageBonus());
    }

    private static void applyKnockback(Player player, LivingEntity target, int comboStep, WeaponData weapon) {
        Vec3 look = player.getLookAngle().normalize();
        double strength = comboStep == 3 ? 0.8 : 0.35;
        if (comboStep == weapon.getMaxCombo()) {
            strength *= 1.35;
        }

        target.knockback(strength, -look.x, -look.z);
    }

    private static void awardCombatExperience(Player player) {
        boolean leveledUp = SkillManager.addExperience(SkillType.SWORDSMANSHIP, 5);
        if (!leveledUp)
            return;

        SkillData swordsmanship = SkillManager.getSkill(SkillType.SWORDSMANSHIP);
        player.displayClientMessage(
                Component.literal("Swordsmanship Lv. " + swordsmanship.getLevel() + " ("
                        + swordsmanship.getExperience() + "/" + swordsmanship.getXpRequired() + ")"),
                true);
    }

    private static void showEliteHitText(LivingEntity target) {
        if (!EliteManager.isElite(target))
            return;

        EliteData eliteData = EliteManager.getEliteData(target);
        DamageNumberRenderer.spawnText(target.getX(), target.getY() + target.getBbHeight() + 0.65, target.getZ(),
                "ELITE: " + eliteData.getModifier(), eliteData.getColor());
    }
}
