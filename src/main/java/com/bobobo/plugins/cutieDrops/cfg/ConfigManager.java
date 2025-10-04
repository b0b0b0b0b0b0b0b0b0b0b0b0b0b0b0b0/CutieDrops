package com.bobobo.plugins.cutieDrops.cfg;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
public class ConfigManager {
    private static final Map<EntityType, Material> dropMap = new HashMap<>();
    private static final Map<EntityType, Material> fireDropMap = new HashMap<>();
    private static boolean checkUpdates;
    private static boolean enableAlreadyBabyMessage;
    private static boolean enableTransformedMessage;
    private static Sound alreadyBabySound;
    private static Sound transformedSound;
    private static Particle transformedParticle;
    private static int fireDropChance;
    private ConfigManager() {}
    public static void load(FileConfiguration config) {
        dropMap.clear();
        fireDropMap.clear();
        for (String key : Objects.requireNonNull(config.getConfigurationSection("drops")).getKeys(false)) {
            try {
                EntityType entityType = EntityType.valueOf(key.toUpperCase());
                Material dropMaterial = Material.valueOf(Objects.requireNonNull(config.getString("drops." + key)).toUpperCase());
                dropMap.put(entityType, dropMaterial);
            } catch (IllegalArgumentException e) {
                System.out.println("Config error for " + key);
            }
        }
        for (String key : Objects.requireNonNull(config.getConfigurationSection("fire_drops")).getKeys(false)) {
            try {
                EntityType entityType = EntityType.valueOf(key.toUpperCase());
                Material dropMaterial = Material.valueOf(Objects.requireNonNull(config.getString("fire_drops." + key)).toUpperCase());
                fireDropMap.put(entityType, dropMaterial);
            } catch (IllegalArgumentException e) {
                System.out.println("Error in fire drops for " + key);
            }
        }
        try {
            alreadyBabySound = Sound.valueOf(Objects.requireNonNull(config.getString("sounds.already_baby")).toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid sound for 'already_baby', using default");
            alreadyBabySound = Sound.ENTITY_VILLAGER_NO;
        }
        try {
            transformedSound = Sound.valueOf(Objects.requireNonNull(config.getString("sounds.transformed")).toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid sound for 'transformed', using default");
            transformedSound = Sound.ENTITY_PLAYER_LEVELUP;
        }
        try {
            transformedParticle = Particle.valueOf(Objects.requireNonNull(config.getString("particles.transformed")).toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid particle for 'transformed', using default");
            transformedParticle = Particle.CRIT;
        }
        fireDropChance = config.getInt("drop_chances.fire_drops", 20);
        enableAlreadyBabyMessage = config.getBoolean("settings.enable_already_baby_message", true);
        enableTransformedMessage = config.getBoolean("settings.enable_transformed_message", true);
        checkUpdates = config.getBoolean("settings.check_updates", true);
    }
    public static Material getDropMaterial(EntityType entityType) {
        return dropMap.get(entityType);
    }
    public static Material getFireDropMaterial(EntityType entityType) {
        return fireDropMap.get(entityType);
    }
    public static int getFireDropChance() {
        return fireDropChance;
    }
    public static boolean isEnableAlreadyBabyMessage() {
        return enableAlreadyBabyMessage;
    }
    public static boolean isEnableTransformedMessage() {
        return enableTransformedMessage;
    }
    public static boolean isCheckUpdates() {
        return checkUpdates;
    }
    public static Sound getAlreadyBabySound() {
        return alreadyBabySound;
    }
    public static Sound getTransformedSound() {
        return transformedSound;
    }
    public static Particle getTransformedParticle() {
        return transformedParticle;
    }
}