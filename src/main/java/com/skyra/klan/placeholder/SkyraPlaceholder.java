package com.skyra.klan.placeholder;

import com.skyra.klan.SkyraKlanPlugin;
import com.skyra.klan.data.Clan;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SkyraPlaceholder extends PlaceholderExpansion {
    private final SkyraKlanPlugin plugin;

    public SkyraPlaceholder(SkyraKlanPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "skyra";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Szymciaa";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equals("klan")) {
            Clan clan = plugin.getClanManager().getPlayerClan(player.getName());
            if (clan != null) {
                return "[" + clan.getTag() + "] " + clan.getName();
            }
            return "§7Brak klanu";
        }

        if (params.equals("punkty")) {
            int points = plugin.getPointsManager().getPoints(player.getName());
            return String.valueOf(points);
        }

        return null;
    }

    @Override
    public boolean persist() {
        return true;
    }
}