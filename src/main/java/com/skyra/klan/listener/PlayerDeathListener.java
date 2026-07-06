package com.skyra.klan.listener;

import com.skyra.klan.SkyraKlanPlugin;
import com.skyra.klan.manager.PointsManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    private final SkyraKlanPlugin plugin;
    private final PointsManager pointsManager;

    public PlayerDeathListener(SkyraKlanPlugin plugin) {
        this.plugin = plugin;
        this.pointsManager = plugin.getPointsManager();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player deadPlayer = event.getEntity();
        Entity killer = deadPlayer.getKiller();

        // Jeśli gracz nie został zabity przez innego gracza
        if (!(killer instanceof Player)) {
            return;
        }

        Player killerPlayer = (Player) killer;

        // Zabójca zyskuje punkty
        int killPoints = pointsManager.getRandomKillPoints();
        pointsManager.addPoints(killerPlayer.getName(), killPoints);
        killerPlayer.sendMessage("§a+§e" + killPoints + " §aSkyraPunktów!");

        // Zabity traci punkty
        int deathPoints = pointsManager.getRandomDeathPoints();
        pointsManager.removePoints(deadPlayer.getName(), deathPoints);
        deadPlayer.sendMessage("§c-§e" + deathPoints + " §cSkyraPunktów!");
    }
}