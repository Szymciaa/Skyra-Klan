package com.skyra.klan.manager;

import com.skyra.klan.SkyraKlanPlugin;
import com.skyra.klan.data.Clan;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class ClanManager {
    private final SkyraKlanPlugin plugin;
    private final Map<String, Clan> clans;
    private final File clansFile;
    private final FileConfiguration clansConfig;

    public ClanManager(SkyraKlanPlugin plugin) {
        this.plugin = plugin;
        this.clans = new HashMap<>();
        this.clansFile = new File(plugin.getDataFolder(), "clans.yml");
        this.clansConfig = YamlConfiguration.loadConfiguration(clansFile);
        loadClans();
    }

    public boolean createClan(String name, String tag, String leader) {
        if (clans.values().stream().anyMatch(c -> c.getName().equalsIgnoreCase(name))) {
            return false;
        }
        if (clans.values().stream().anyMatch(c -> c.getTag().equalsIgnoreCase(tag))) {
            return false;
        }
        if (getPlayerClan(leader) != null) {
            return false;
        }

        Clan clan = new Clan(name, tag, leader);
        clans.put(name.toLowerCase(), clan);
        saveClan(clan);
        return true;
    }

    public boolean deleteClan(String name) {
        if (clans.remove(name.toLowerCase()) != null) {
            clansConfig.set("clans." + name.toLowerCase(), null);
            saveClansFile();
            return true;
        }
        return false;
    }

    public Clan getClan(String name) {
        return clans.get(name.toLowerCase());
    }

    public Clan getPlayerClan(String playerName) {
        return clans.values().stream()
                .filter(c -> c.isMember(playerName))
                .findFirst()
                .orElse(null);
    }

    public boolean addMemberToClan(String clanName, String playerName) {
        Clan clan = getClan(clanName);
        if (clan != null && clan.canAddMore()) {
            clan.addMember(playerName);
            saveClan(clan);
            return true;
        }
        return false;
    }

    public boolean removeMemberFromClan(String playerName) {
        Clan clan = getPlayerClan(playerName);
        if (clan != null) {
            clan.removeMember(playerName);
            if (clan.getMemberCount() == 0) {
                deleteClan(clan.getName());
            } else {
                saveClan(clan);
            }
            return true;
        }
        return false;
    }

    public void upgradeClanSlots(String clanName, int newMax) {
        Clan clan = getClan(clanName);
        if (clan != null) {
            clan.setMaxMembers(newMax);
            saveClan(clan);
        }
    }

    public List<Clan> getAllClans() {
        return new ArrayList<>(clans.values());
    }

    private void saveClan(Clan clan) {
        String path = "clans." + clan.getName().toLowerCase();
        clansConfig.set(path + ".name", clan.getName());
        clansConfig.set(path + ".tag", clan.getTag());
        clansConfig.set(path + ".leader", clan.getLeader());
        clansConfig.set(path + ".members", clan.getMembers());
        clansConfig.set(path + ".maxMembers", clan.getMaxMembers());
        saveClansFile();
    }

    private void saveClansFile() {
        try {
            clansConfig.save(clansFile);
        } catch (Exception e) {
            plugin.getLogger().severe("Błąd przy zapisywaniu klanów: " + e.getMessage());
        }
    }

    private void loadClans() {
        if (!clansFile.exists()) {
            return;
        }

        if (clansConfig.contains("clans")) {
            for (String key : clansConfig.getConfigurationSection("clans").getKeys(false)) {
                String name = clansConfig.getString("clans." + key + ".name");
                String tag = clansConfig.getString("clans." + key + ".tag");
                String leader = clansConfig.getString("clans." + key + ".leader");
                List<String> members = clansConfig.getStringList("clans." + key + ".members");
                int maxMembers = clansConfig.getInt("clans." + key + ".maxMembers", 5);

                Clan clan = new Clan(name, tag, leader);
                clan.setMaxMembers(maxMembers);
                for (String member : members) {
                    if (!member.equals(leader)) {
                        clan.addMember(member);
                    }
                }
                clans.put(name.toLowerCase(), clan);
            }
        }
    }

    public void saveAllClans() {
        for (Clan clan : clans.values()) {
            saveClan(clan);
        }
    }
}