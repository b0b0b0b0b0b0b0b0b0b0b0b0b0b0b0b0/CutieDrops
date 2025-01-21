package com.bobobo.plugins.cutieDrops.utils;

import com.bobobo.plugins.cutieDrops.CutieDrops;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Reload implements CommandExecutor {

    private final CutieDrops plugin;

    public Reload(CutieDrops plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("cutiedrops.reload")) {
            sender.sendMessage(plugin.getMessage("no_permission"));
            return true;
        }

        // Перезагрузка конфига и сообщений
        plugin.reloadConfig();
        plugin.getConfigManager().reloadConfig();
        plugin.reloadMessages();

        sender.sendMessage(plugin.getMessage("reload_success"));
        return true;
    }
}
