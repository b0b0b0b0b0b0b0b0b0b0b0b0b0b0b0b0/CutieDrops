package com.bobobo.plugins.cutieDrops.utils;
import com.bobobo.plugins.cutieDrops.CutieDrops;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Sound;
import org.bukkit.Particle;
public class ConfigManager {
    private final CutieDrops plugin;
    private final Map<EntityType, Material> dropMap = new HashMap<>();
    private boolean checkUpdates;
    private boolean enableAlreadyBabyMessage;
    private boolean enableTransformedMessage;
    private Sound alreadyBabySound;
    private Sound transformedSound;
    private Particle transformedParticle;
    public ConfigManager(CutieDrops plugin) {
        this.plugin = plugin;
        reloadConfig();
    }
    private void loadConfig() {
        for (String key : plugin.getConfig().getConfigurationSection("drops").getKeys(false)) {
            try {
                EntityType entityType = EntityType.valueOf(key.toUpperCase());
                Material dropMaterial = Material.valueOf(plugin.getConfig().getString("drops." + key).toUpperCase());
                dropMap.put(entityType, dropMaterial);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Ошибка в конфиге для " + key);
            }
        }
        try {
            alreadyBabySound = Sound.valueOf(plugin.getConfig().getString("sounds.already_baby").toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Неверный звук для 'already_baby' в конфиге, используется по умолчанию");
            alreadyBabySound = Sound.ENTITY_VILLAGER_NO;
        }
        try {
            transformedSound = Sound.valueOf(plugin.getConfig().getString("sounds.transformed").toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Неверный звук для 'transformed' в конфиге, используется по умолчанию");
            transformedSound = Sound.ENTITY_PLAYER_LEVELUP;
        }
        try {
            transformedParticle = Particle.valueOf(plugin.getConfig().getString("particles.transformed").toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Неверный партикл для 'transformed' в конфиге, используется по умолчанию");
            transformedParticle = Particle.CRIT;
        }
        enableAlreadyBabyMessage = plugin.getConfig().getBoolean("settings.enable_already_baby_message", true);
        enableTransformedMessage = plugin.getConfig().getBoolean("settings.enable_transformed_message", true);
        checkUpdates = plugin.getConfig().getBoolean("settings.check_updates", true);
    }
    public Material getDropMaterial(EntityType entityType) {
        return dropMap.get(entityType);
    }
    public boolean isEnableAlreadyBabyMessage() {
        return enableAlreadyBabyMessage;
    }

    public boolean isEnableTransformedMessage() {
        return enableTransformedMessage;
    }
    public Sound getAlreadyBabySound() {
        return alreadyBabySound;
    }

    public Sound getTransformedSound() {
        return transformedSound;
    }
    public boolean isCheckUpdates() {
        return checkUpdates;
    }
    public Particle getTransformedParticle() {
        return transformedParticle;
    }
    public void reloadConfig() {
        dropMap.clear();
        plugin.reloadConfig();
        loadConfig();
    }
}
