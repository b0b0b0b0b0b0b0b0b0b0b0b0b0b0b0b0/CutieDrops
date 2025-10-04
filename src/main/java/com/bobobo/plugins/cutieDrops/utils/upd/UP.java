package com.bobobo.plugins.cutieDrops.utils.upd;
import com.bobobo.plugins.cutieDrops.CutieDrops;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class UP {
    private static final String VERSION_URL = "https://b0b0b0.dev/CutieDrops.txt";
    private static final String PREFIX = "\u001B[37m[\u001B[90mCutieDrops\u001B[37m]\u001B[0m ";
    private static final ConsoleCommandSender console = Bukkit.getConsoleSender();

    public static void checkVersion(String currentVersion) {
        Bukkit.getScheduler().runTaskAsynchronously(CutieDrops.getInstance(), () -> {
            try {
                String latestVersion = fetchLatestVersion();
                if (latestVersion == null) {
                    Bukkit.getScheduler().runTask(CutieDrops.getInstance(), () ->
                            logError("Failed to fetch the latest version. Skipping version check."));
                    return;
                }
                final String finalLatestVersion = latestVersion;
                Bukkit.getScheduler().runTask(CutieDrops.getInstance(), () -> {
                    if (!currentVersion.equalsIgnoreCase(finalLatestVersion)) {
                        console.sendMessage(PREFIX + " ");
                        logWarning("You are using an outdated version!");
                        logWarning("Current version: \u001B[90m" + currentVersion +
                                "\u001B[33m, latest version: \u001B[32m" + finalLatestVersion + "\u001B[0m.");
                        logWarning("Please update the plugin. Choose one of the following links to download:");

                        console.sendMessage(PREFIX + "\u001B[36m1. Black-Minecraft: \u001B[0mhttps://bm.wtf/resources/7808/");
                        console.sendMessage(PREFIX + "\u001B[36m2. Modrinth: \u001B[0mhttps://modrinth.com/project/cutiedrops");
                        console.sendMessage(PREFIX + " ");
                    } else {
                        logInfo("You are using the latest version of the plugin! Version: \u001B[32m" + currentVersion + "\u001B[0m.");
                    }
                });
            } catch (Exception e) {
                Bukkit.getScheduler().runTask(CutieDrops.getInstance(), () -> {
                    logError("Error during version check: " + e.getMessage());
                    e.printStackTrace();
                });
            }
        });
    }

    private static String fetchLatestVersion() {
        try {
            URL url = new URL(VERSION_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                return in.readLine().trim();
            }
        } catch (IOException e) {
            logError("Connection error to " + VERSION_URL + ": " + e.getMessage());
            return null;
        }
    }

    private static void logInfo(String message) {
        console.sendMessage(PREFIX + "\u001B[32m" + message + "\u001B[0m");
    }
    private static void logWarning(String message) {
        console.sendMessage(PREFIX + "\u001B[33m" + message + "\u001B[0m");
    }
    private static void logError(String message) {
        console.sendMessage(PREFIX + "\u001B[31m" + message + "\u001B[0m");
    }
}
