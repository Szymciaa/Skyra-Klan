package com.skyra.klan;

import com.skyra.klan.command.KlanCommand;
import com.skyra.klan.listener.PlayerDeathListener;
import com.skyra.klan.listener.PlayerKillListener;
import com.skyra.klan.manager.ClanManager;
import com.skyra.klan.manager.PointsManager;
import com.skyra.klan.placeholder.SkyraPlaceholder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyraKlanPlugin extends JavaPlugin {

    private static SkyraKlanPlugin instance;
    private ClanManager clanManager;
    private PointsManager pointsManager;
    private Economy economy;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("=====================================");
        getLogger().info("Włączanie Skyra-Klan v1.0.0");
        getLogger().info("=====================================");

        // Tworzenie folderu pluginu
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Załadowanie konfiguracji
        saveDefaultConfig();

        // Inicjalizacja Vault
        if (!setupEconomy()) {
            getLogger().severe("Vault nie znaleziony! Plugin wyłączony.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Inicjalizacja PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new SkyraPlaceholder(this).register();
            getLogger().info("PlaceholderAPI zarejestrowana!");
        } else {
            getLogger().warning("PlaceholderAPI nie znaleziona!");
        }

        // Inicjalizacja managerów
        this.clanManager = new ClanManager(this);
        this.pointsManager = new PointsManager(this);

        // Rejestracja komend
        getCommand("klan").setExecutor(new KlanCommand(this));

        // Rejestracja listenerów
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerKillListener(this), this);

        getLogger().info("=====================================");
        getLogger().info("Skyra-Klan pomyślnie włączony!");
        getLogger().info("=====================================");
    }

    @Override
    public void onDisable() {
        getLogger().info("Wyłączanie Skyra-Klan...");
        if (clanManager != null) {
            clanManager.saveAllClans();
        }
        if (pointsManager != null) {
            pointsManager.saveAllPoints();
        }
        getLogger().info("Skyra-Klan wyłączony!");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public static SkyraKlanPlugin getInstance() {
        return instance;
    }

    public ClanManager getClanManager() {
        return clanManager;
    }

    public PointsManager getPointsManager() {
        return pointsManager;
    }

    public Economy getEconomy() {
        return economy;
    }
}