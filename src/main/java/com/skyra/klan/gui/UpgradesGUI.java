package com.skyra.klan.gui;

import com.skyra.klan.SkyraKlanPlugin;
import com.skyra.klan.data.Clan;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class UpgradesGUI implements Listener {
    private final SkyraKlanPlugin plugin;
    private final Player player;
    private final Clan clan;
    private final Economy economy;
    private final String guiName = "§6Ulepszenia Klanu";

    public UpgradesGUI(SkyraKlanPlugin plugin, Player player, Clan clan) {
        this.plugin = plugin;
        this.player = player;
        this.clan = clan;
        this.economy = plugin.getEconomy();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void open() {
        Inventory gui = Bukkit.createInventory(null, 27, guiName);

        // Upgrade 1: 5000$ -> 5 do 10 miejsc
        ItemStack upgrade1 = createUpgradeItem(Material.EMERALD, "§aUlepszenie Miejsc I", 
                new String[]{"§7Koszt: §a$5000", "§75 → 10 miejsc", ""}, 
                clan.getMaxMembers() >= 10);
        gui.setItem(10, upgrade1);

        // Upgrade 2: 10000$ -> 10 do 15 miejsc
        ItemStack upgrade2 = createUpgradeItem(Material.EMERALD_BLOCK, "§aUlepszenie Miejsc II", 
                new String[]{"§7Koszt: §a$10000", "§710 → 15 miejsc", ""}, 
                clan.getMaxMembers() >= 15);
        gui.setItem(12, upgrade2);

        // Upgrade 3: 20000$ -> 15 do 20 miejsc
        ItemStack upgrade3 = createUpgradeItem(Material.DIAMOND, "§aUlepszenie Miejsc III", 
                new String[]{"§7Koszt: §a$20000", "§715 → 20 miejsc", ""}, 
                clan.getMaxMembers() >= 20);
        gui.setItem(14, upgrade3);

        // Info
        ItemStack info = createInfoItem();
        gui.setItem(4, info);

        // Zamknięcie
        ItemStack close = createCloseItem();
        gui.setItem(26, close);

        player.openInventory(gui);
    }

    private ItemStack createUpgradeItem(Material material, String name, String[] lore, boolean purchased) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(purchased ? "§8" + name : name);
        meta.setLore(Arrays.asList(purchased ? (String[]) Arrays.stream(lore)
                .map(s -> "§8" + s).toArray(String[]::new) : lore));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createInfoItem() {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§eInformacje");
        meta.setLore(Arrays.asList(
                "§7Klan: §e" + clan.getName(),
                "§7Lider: §e" + clan.getLeader(),
                "§7Aktualne miejsc: §e" + clan.getMaxMembers()
        ));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createCloseItem() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cZamknij");
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(guiName)) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player clicker = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();

        switch (slot) {
            case 10:
                handleUpgrade(clicker, 1, 5000, 10);
                break;
            case 12:
                handleUpgrade(clicker, 2, 10000, 15);
                break;
            case 14:
                handleUpgrade(clicker, 3, 20000, 20);
                break;
            case 26:
                clicker.closeInventory();
                break;
        }
    }

    private void handleUpgrade(Player player, int level, double cost, int newMax) {
        if (clan.getMaxMembers() >= newMax) {
            player.sendMessage("§cJuż posiadasz to ulepszenie!");
            return;
        }

        if (economy.getBalance(player) < cost) {
            player.sendMessage("§cNie masz wystarczającej ilości pieniędzy!");
            player.sendMessage("§7Potrzebujesz: §a$" + cost + " §7Posiadasz: §a$" + economy.getBalance(player));
            return;
        }

        economy.withdrawPlayer(player, cost);
        plugin.getClanManager().upgradeClanSlots(clan.getName(), newMax);
        
        player.sendMessage("§a✓ Pomyślnie ulepszyłeś klan!");
        player.sendMessage("§7Nowa liczba miejsc: §e" + newMax);
        player.sendMessage("§7Zapłaciłeś: §a$" + cost);
        
        player.closeInventory();
    }
}