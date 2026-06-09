package com.t.compactcombat.entity.elite;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.t.compactcombat.CompactCombat;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;

@Mod.EventBusSubscriber(modid = CompactCombat.MOD_ID, value = Dist.CLIENT)
public class EliteManager {

    private static final float ELITE_CHANCE = 0.05F;
    private static final int MAX_PARTICLES_PER_TICK = 4;
    private static final Map<UUID, EliteData> ELITES = new HashMap<>();

    public static boolean isElite(LivingEntity entity) {
        return ELITES.containsKey(entity.getUUID());
    }

    public static EliteData getEliteData(LivingEntity entity) {
        return ELITES.get(entity.getUUID());
    }

    public static String getDisplayName(LivingEntity entity) {
        return "Elite " + entity.getType().getDescription().getString();
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Monster monster) || ELITES.containsKey(monster.getUUID()))
            return;

        if (monster.getRandom().nextFloat() >= ELITE_CHANCE)
            return;

        EliteData data = createEliteData(monster.getRandom().nextInt(EliteModifier.values().length));
        ELITES.put(monster.getUUID(), data);
        applyEliteAttributes(monster, data);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof LivingEntity livingAttacker) || !isElite(livingAttacker))
            return;

        EliteData data = getEliteData(livingAttacker);
        if (data.getModifier() == EliteModifier.BRUTAL) {
            event.setAmount(event.getAmount() * data.getDamageMultiplier());
        }

        if (data.getModifier() == EliteModifier.VAMPIRIC) {
            livingAttacker.heal(event.getAmount() * 0.2F);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null)
            return;

        int particles = 0;
        Iterator<Map.Entry<UUID, EliteData>> iterator = ELITES.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, EliteData> entry = iterator.next();
            Entity entity = findRenderedEntity(mc, entry.getKey());
            if (!(entity instanceof LivingEntity livingEntity) || !livingEntity.isAlive()) {
                iterator.remove();
                continue;
            }

            if (particles >= MAX_PARTICLES_PER_TICK)
                continue;

            spawnEliteParticle(mc, livingEntity, entry.getValue());
            particles++;
        }
    }

    private static EliteData createEliteData(int index) {
        EliteModifier modifier = EliteModifier.values()[index];
        return switch (modifier) {
            case BRUTAL -> new EliteData(modifier, 1.0F, 1.5F, 0xFFE74C3C);
            case QUICK -> new EliteData(modifier, 1.0F, 1.0F, 0xFF4FC3F7);
            case VAMPIRIC -> new EliteData(modifier, 1.0F, 1.0F, 0xFFB0005A);
            case JUGGERNAUT -> new EliteData(modifier, 2.0F, 1.0F, 0xFFB0BEC5);
        };
    }

    private static void applyEliteAttributes(LivingEntity entity, EliteData data) {
        if (data.getHealthMultiplier() > 1.0F) {
            AttributeInstance maxHealth = entity.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealth != null) {
                maxHealth.setBaseValue(maxHealth.getBaseValue() * data.getHealthMultiplier());
                entity.setHealth(entity.getMaxHealth());
            }
        }

        if (data.getModifier() == EliteModifier.QUICK) {
            AttributeInstance speed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speed != null) {
                speed.setBaseValue(speed.getBaseValue() * 1.2F);
            }
        }

        if (data.getModifier() == EliteModifier.JUGGERNAUT) {
            AttributeInstance knockbackResistance = entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
            if (knockbackResistance != null) {
                knockbackResistance.setBaseValue(Math.min(1.0D, knockbackResistance.getBaseValue() + 0.2D));
            }
        }
    }

    private static void spawnEliteParticle(Minecraft mc, LivingEntity entity, EliteData data) {
        float red = ((data.getColor() >> 16) & 0xFF) / 255.0F;
        float green = ((data.getColor() >> 8) & 0xFF) / 255.0F;
        float blue = (data.getColor() & 0xFF) / 255.0F;
        double angle = entity.tickCount * 0.25D;
        double radius = entity.getBbWidth() * 0.7D;

        mc.level.addParticle(
                new DustParticleOptions(new Vector3f(red, green, blue), 0.8F),
                entity.getX() + Math.cos(angle) * radius,
                entity.getY() + entity.getBbHeight() * 0.65D,
                entity.getZ() + Math.sin(angle) * radius,
                0.0D,
                0.02D,
                0.0D);
    }

    private static Entity findRenderedEntity(Minecraft mc, UUID uuid) {
        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity.getUUID().equals(uuid)) {
                return entity;
            }
        }

        return null;
    }
}
