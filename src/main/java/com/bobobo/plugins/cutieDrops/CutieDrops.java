package com.bobobo.plugins.cutieDrops;
import com.bobobo.plugins.cutieDrops.utils.Reload;
import com.bobobo.plugins.cutieDrops.utils.TC;
import com.bobobo.plugins.cutieDrops.utils.UP;
import com.bobobo.plugins.cutieDrops.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import com.bobobo.plugins.cutieDrops.events.InteractListener;
import java.io.File;
import java.util.Objects;
public final class CutieDrops extends JavaPlugin {
    private static CutieDrops instance;
    private FileConfiguration messagesConfig;
    private File messagesFile;
    String version = getDescription().getVersion();
    public static final String PREFIX = "\u001B[37m[\u001B[90mCutieDrops\u001B[37m]\u001B[0m ";
    @Override
    public void onEnable() {
        instance = this;
        final ConsoleCommandSender console = getServer().getConsoleSender();
        saveDefaultConfig();
        createMessagesFile();
        ConfigManager.load(getConfig());
        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
        Objects.requireNonNull(getCommand("cutiedrops")).setExecutor(new Reload(this));
        Objects.requireNonNull(getCommand("cutiedrops")).setTabCompleter(new TC());
        console.sendMessage(" ");
        console.sendMessage(PREFIX + "==============================");
        console.sendMessage(PREFIX + "The CutieDrops plugin is embarking on its journey on your server!");
        console.sendMessage(PREFIX + "Version:\u001B[90m " + version + " \u001B[0m| Author: \u001B[90mb0b0b0\u001B[0m");
        console.sendMessage(PREFIX + " ");
        console.sendMessage(PREFIX + "Available commands:");
        console.sendMessage(PREFIX + "  /cutiedrops reload - Reloads the plugin configuration and messages.");
        console.sendMessage(PREFIX + "==============================");
        console.sendMessage(" ");
        if (ConfigManager.isCheckUpdates()) {
            Bukkit.getScheduler().runTaskLater(this, () -> UP.checkVersion(version), 60L);
        }
    }
    public static CutieDrops getInstance() {
        return instance;
    }
    public String getMessage(String key) {
        String message = messagesConfig.getString(key, "Сообщение не найдено: " + key);
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    private void createMessagesFile() {
        messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }
    public void reloadMessages() {
        if (messagesFile == null) {
            createMessagesFile();
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }
}
