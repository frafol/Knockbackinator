package it.frafol.knockbackinator.commands;

import it.frafol.knockbackinator.Knockbackinator;
import it.frafol.knockbackinator.enums.SpigotConfig;
import it.frafol.knockbackinator.enums.SpigotMessages;
import it.frafol.knockbackinator.objects.TextFile;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final Knockbackinator plugin = Knockbackinator.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            if (player.hasPermission(SpigotConfig.RELOAD_PERMISSION.get(String.class))) {
                player.sendMessage(SpigotMessages.USAGE.color().replace("%prefix%", SpigotMessages.PREFIX.color()));
                return true;
            }

            if (SpigotConfig.CREDIT_LESS.get(Boolean.class)) {
                player.sendMessage(SpigotMessages.NO_PERMISSION.color().replace("%prefix%", SpigotMessages.PREFIX.color()));
                return true;
            }

            player.sendMessage("§7This server is using §dKnockbackinator §7by §dfrafol§7.");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (player.hasPermission(SpigotConfig.RELOAD_PERMISSION.get(String.class))) {
                TextFile.reloadAll();
                player.sendMessage(SpigotMessages.RELOADED.color().replace("%prefix%", SpigotMessages.PREFIX.color()));
                return true;
            }

            if (SpigotConfig.CREDIT_LESS.get(Boolean.class)) {
                player.sendMessage(SpigotMessages.NO_PERMISSION.color().replace("%prefix%", SpigotMessages.PREFIX.color()));
                return true;
            }

            player.sendMessage("§7This server is using §dKnockbackinator §7by §dfrafol§7.");
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (player.hasPermission(SpigotConfig.GIVE_PERMISSION.get(String.class))) {
                if (args.length >= 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null || !target.isOnline()) {
                        player.sendMessage(SpigotMessages.TARGET_OFFLINE.color().replace("%prefix%", SpigotMessages.PREFIX.color()));
                        return true;
                    }
                    player.sendMessage(SpigotMessages.GIVEN.color().replace("%prefix%", SpigotMessages.PREFIX.color()));
                    plugin.startupPlayer(target);
                    return true;
                }

                player.sendMessage(SpigotMessages.GIVEN.color().replace("%prefix%", SpigotMessages.PREFIX.color()));
                plugin.startupPlayer(player);
                return true;
            }

            if (SpigotConfig.CREDIT_LESS.get(Boolean.class)) {
                player.sendMessage(SpigotMessages.NO_PERMISSION.color().replace("%prefix%", SpigotMessages.PREFIX.color()));
                return true;
            }

            player.sendMessage("§7This server is using §dKnockbackinator §7by §dfrafol§7.");
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> subCommands = new ArrayList<>();

            if (sender.hasPermission(SpigotConfig.RELOAD_PERMISSION.get(String.class))) {
                subCommands.add("reload");
            }
            if (sender.hasPermission(SpigotConfig.GIVE_PERMISSION.get(String.class))) {
                subCommands.add("give");
            }

            StringUtil.copyPartialMatches(args[0], subCommands, completions);
            Collections.sort(completions);
            return completions;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            if (sender.hasPermission(SpigotConfig.GIVE_PERMISSION.get(String.class))) {
                List<String> playerNames = new ArrayList<>();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    playerNames.add(p.getName());
                }

                StringUtil.copyPartialMatches(args[1], playerNames, completions);
                Collections.sort(completions);
                return completions;
            }
        }

        return Collections.emptyList();
    }
}