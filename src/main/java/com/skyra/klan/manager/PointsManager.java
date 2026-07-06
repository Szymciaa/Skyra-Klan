package com.skyra.klan.manager;

import com.skyra.klan.SkyraKlanPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class PointsManager {
    private final SkyraKlanPlugin plugin;
    private final Map<String, Integer> points;
    private final File pointsFile;
    private final FileConfiguration pointsConfig;
    private final Random random;

    public PointsManager(SkyraKlanPlugin plugin) {
        this.plugin = plugin;
        this.points = new HashMap<>();
        this.pointsFile = new File(plugin.getDataFolder(), "points.yml");
        this.pointsConfig = YamlConfiguration.loadConfiguration(pointsFile);
        this.random = new Random();
        loadPoints();
    }

    public void initializePlayer(Player player) {
        String playerName = player.getName();
        if (!points.containsKey(playerName)) {
            points.put(playerName, 1000);
            savePoints();
        }
    }

    public int getPoints(String playerName) {
        return points.getOrDefault(playerName, 1000);
    }

    public void addPoints(String playerName, int amount) {
        int current = getPoints(playerName);
        points.put(playerName, current + amount);
        savePoints();
    }

    public void removePoints(String playerName, int amount) {
        int current = getPoints(playerName);
        int newAmount = Math.max(0, current - amount);
        points.put(playerName, newAmount);
        savePoints();
    }

    public int getRandomKillPoints() {
        // Losowy zakres punktów za zabójstwo: 50-150
        return 50 + random.nextInt(101);
    }

    public int getRandomDeathPoints() {
        // Losowy zakres punktów straconych za śmierć: 30-80
        return 30 + random.nextInt(51);
    }

    private void loadPoints() {
        if (!pointsFile.exists()) {
            return;
        }

        if (pointsConfig.contains("points")) {
            for (String playerName : pointsConfig.getConfigurationSection("points").getKeys(false)) {
                int playerPoints = pointsConfig.getInt("points." + playerName, 1000);
                points.put(playerName, playerPoints);
            }
        }
    }

    private void savePoints() {
        for (Map.Entry<String, Integer> entry : points.entrySet()) {
            pointsConfig.set("points." + entry.getKey(), entry.getValue());
        }

        try {
            pointsConfig.save(pointsFile);
        } catch (Exception e) {
            plugin.getLogger().severe("Błąd przy zapisywaniu punktów: " + e.getMessage());
        }
    }

    public void saveAllPoints() {
        savePoints();
    }
}