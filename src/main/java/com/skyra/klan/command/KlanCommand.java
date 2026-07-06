package com.skyra.klan.command;

import com.skyra.klan.SkyraKlanPlugin;
import com.skyra.klan.data.Clan;
import com.skyra.klan.gui.UpgradesGUI;
import com.skyra.klan.manager.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KlanCommand implements CommandExecutor {
    private final SkyraKlanPlugin plugin;
    private final ClanManager clanManager;

    public KlanCommand(SkyraKlanPlugin plugin) {
        this.plugin = plugin;
        this.clanManager = plugin.getClanManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cTa komenda jest dostępna tylko dla graczy!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "zaloz":
                handleZaloz(player, args);
                break;
            case "zapos":
                handleZapos(player, args);
                break;
            case "opusc":
                handleOpusc(player);
                break;
            case "usun":
                handleUsun(player);
                break;
            case "ulepszenia":
                handleUlepszenia(player);
                break;
            case "info":
                handleInfo(player);
                break;
            default:
                sendHelp(player);
        }

        return true;
    }

    private void handleZaloz(Player player, String[] args) {
        if (args.length < 3) {
            player.sendMessage("§c/klan zaloz <nazwa> <tag>");
            player.sendMessage("§7Nazwa max 5 znaków, tag max 3 znaki");
            return;
        }

        String name = args[1];
        String tag = args[2];

        if (name.length() > 5) {
            player.sendMessage("§cNazwa klanu może mieć max 5 znaków!");
            return;
        }

        if (tag.length() > 3) {
            player.sendMessage("§cTag klanu może mieć max 3 znaki!");
            return;
        }

        if (clanManager.getPlayerClan(player.getName()) != null) {
            player.sendMessage("§cJuż jesteś członkiem klanu!");
            return;
        }

        if (clanManager.createClan(name, tag, player.getName())) {
            player.sendMessage("§a✓ Klan §e" + name + "§a został utworzony!");
            player.sendMessage("§7Tag: §e[" + tag + "]");
        } else {
            player.sendMessage("§cNie można utworzyć klanu (nazwa/tag zajęty lub już jesteś w klanie)");
        }
    }

    private void handleZapos(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§c/klan zapos <nick gracza>");
            return;
        }

        Clan clan = clanManager.getPlayerClan(player.getName());
        if (clan == null) {
            player.sendMessage("§cNie jesteś członkiem żadnego klanu!");
            return;
        }

        if (!clan.isLeader(player.getName())) {
            player.sendMessage("§cTylko lider klanu może zapraszać graczy!");
            return;
        }

        String targetName = args[1];
        Player target = Bukkit.getPlayer(targetName);

        if (target == null) {
            player.sendMessage("§cGracz nie jest online!");
            return;
        }

        if (clanManager.getPlayerClan(target.getName()) != null) {
            player.sendMessage("§cGracz już jest członkiem klanu!");
            return;
        }

        if (!clan.canAddMore()) {
            player.sendMessage("§cKlan nie ma wystarczającej liczby miejsc!");
            return;
        }

        clanManager.addMemberToClan(clan.getName(), target.getName());
        player.sendMessage("§a✓ §e" + target.getName() + "§a dołączył do klanu!");
        target.sendMessage("§a✓ Zostałeś zaproszony do klanu §e" + clan.getName() + "§a!");
    }

    private void handleOpusc(Player player) {
        Clan clan = clanManager.getPlayerClan(player.getName());
        if (clan == null) {
            player.sendMessage("§cNie jesteś członkiem żadnego klanu!");
            return;
        }

        if (clan.isLeader(player.getName())) {
            player.sendMessage("§cLider klanu nie może go opuścić!");
            return;
        }

        clanManager.removeMemberFromClan(player.getName());
        player.sendMessage("§a✓ Opuściłeś klan!");
    }

    private void handleUsun(Player player) {
        Clan clan = clanManager.getPlayerClan(player.getName());
        if (clan == null) {
            player.sendMessage("§cNie jesteś członkiem żadnego klanu!");
            return;
        }

        if (!clan.isLeader(player.getName())) {
            player.sendMessage("§cTylko lider klanu może go usunąć!");
            return;
        }

        String clanName = clan.getName();
        clanManager.deleteClan(clanName);
        player.sendMessage("§a✓ Klan §e" + clanName + "§a został usunięty!");
    }

    private void handleUlepszenia(Player player) {
        Clan clan = clanManager.getPlayerClan(player.getName());
        if (clan == null) {
            player.sendMessage("§cNie jesteś członkiem żadnego klanu!");
            return;
        }

        if (!clan.isLeader(player.getName())) {
            player.sendMessage("§cTylko lider klanu może ulepszać!");
            return;
        }

        new UpgradesGUI(plugin, player, clan).open();
    }

    private void handleInfo(Player player) {
        Clan clan = clanManager.getPlayerClan(player.getName());
        if (clan == null) {
            player.sendMessage("§cNie jesteś członkiem żadnego klanu!");
            return;
        }

        player.sendMessage("§6════════════════════════════════");
        player.sendMessage("§e" + clan.getName() + " §7[§e" + clan.getTag() + "§7]");
        player.sendMessage("§7Lider: §e" + clan.getLeader());
        player.sendMessage("§7Członkowie: §e" + clan.getMemberCount() + "§7/§e" + clan.getMaxMembers());
        player.sendMessage("§7Członkowie: §e" + String.join(", ", clan.getMembers()));
        player.sendMessage("§6════════════════════════════════");
    }

    private void sendHelp(Player player) {
        player.sendMessage("§6════════════════════════════════");
        player.sendMessage("§e>>> SKYRA-KLAN <<<");
        player.sendMessage("§6════════════════════════════════");
        player.sendMessage("§7/klan zaloz <nazwa> <tag> - Załóż klan");
        player.sendMessage("§7/klan zapos <nick> - Zaproś gracza");
        player.sendMessage("§7/klan opusc - Opuść klan");
        player.sendMessage("§7/klan usun - Usuń klan (tylko lider)");
        player.sendMessage("§7/klan ulepszenia - Otwórz GUI ulepszań");
        player.sendMessage("§7/klan info - Informacje o klanie");
        player.sendMessage("§6════════════════════════════════");
    }
}