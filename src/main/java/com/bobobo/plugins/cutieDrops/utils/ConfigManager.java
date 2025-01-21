package com.bobobo.plugins.cutieDrops.utils;
import com.bobobo.plugins.cutieDrops.CutieDrops;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import java.util.HashMap;
import java.util.Map;
public class ConfigManager {
    private final CutieDrops plugin;
    private final Map<EntityType, Material> dropMap = new HashMap<>();
    private boolean enableMessages;
    private boolean checkUpdates;
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
        enableMessages = plugin.getConfig().getBoolean("settings.enable_messages", true);
        checkUpdates = plugin.getConfig().getBoolean("settings.check_updates", true);
    }
    public Material getDropMaterial(EntityType entityType) {
        return dropMap.get(entityType);
    }
    public boolean isEnableMessages() {
        return enableMessages;
    }
    public boolean isCheckUpdates() {
        return checkUpdates;
    }
    public void reloadConfig() {
        dropMap.clear();
        plugin.reloadConfig();
        loadConfig();
    }
}
