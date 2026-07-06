package com.skyra.klan.listener;

import com.skyra.klan.SkyraKlanPlugin;
import com.skyra.klan.manager.PointsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerKillListener implements Listener {
    private final SkyraKlanPlugin plugin;
    private final PointsManager pointsManager;

    public PlayerKillListener(SkyraKlanPlugin plugin) {
        this.plugin = plugin;
        this.pointsManager = plugin.getPointsManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        pointsManager.initializePlayer(event.getPlayer());
    }
}