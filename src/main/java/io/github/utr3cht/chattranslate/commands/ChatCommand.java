package io.github.utr3cht.chattranslate.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import io.github.utr3cht.chattranslate.ChatTranslate;
import io.github.utr3cht.chattranslate.PlayerDataManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ChatCommand implements CommandExecutor, TabCompleter {

    private final ChatTranslate plugin;
    private final PlayerDataManager playerDataManager;

    public ChatCommand(ChatTranslate plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /chat <lang|on|off|reload> [args]");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        if (subCommand.equals("reload")) {
            if (!sender.hasPermission("chattranslate.reload") && !sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to do this.");
                return true;
            }
            plugin.reloadPlugin();
            sender.sendMessage(ChatColor.GREEN + "ChatTranslate configuration reloaded!");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        switch (subCommand) {
            case "lang":
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Usage: /chat lang <EN|JA>");
                    return true;
                }
                String langCode = args[1].toUpperCase();
                if (!langCode.equals("EN") && !langCode.equals("JA")) {
                    player.sendMessage(ChatColor.RED + "Only EN and JA are supported.");
                    return true;
                }
                playerDataManager.setPlayerLanguage(player, langCode);
                player.sendMessage(ChatColor.GREEN + "Target language set to: " + langCode);
                if (!playerDataManager.isTranslationEnabled(player)) {
                    player.sendMessage(ChatColor.YELLOW + "Tip: Run /chat on to enable translation!");
                }
                break;
            case "on":
                playerDataManager.setTranslationEnabled(player, true);
                player.sendMessage(ChatColor.GREEN + "Chat translation enabled.");
                break;
            case "off":
                playerDataManager.setTranslationEnabled(player, false);
                player.sendMessage(ChatColor.GREEN + "Chat translation disabled.");
                break;
            default:
                player.sendMessage(ChatColor.RED + "Unknown subcommand. Usage: /chat <lang|on|off|reload> [args]");
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            List<String> subcommands = Arrays.asList("lang", "on", "off", "reload");
            completions.addAll(subcommands.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList()));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("lang")) {
            List<String> langs = Arrays.asList("EN", "JA");
            completions.addAll(langs.stream()
                    .filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList()));
        }
        return completions;
    }
}
