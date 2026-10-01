package io.github.utr3cht.chattranslate.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import io.github.utr3cht.chattranslate.ChatTranslate;
import io.github.utr3cht.chattranslate.PlayerDataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChatListener implements Listener {

    private final ChatTranslate plugin;
    private final PlayerDataManager playerDataManager;

    public ChatListener(ChatTranslate plugin, PlayerDataManager playerDataManager) {
        this.plugin = plugin;
        this.playerDataManager = playerDataManager;
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        Player sender = event.getPlayer();
        String originalMessage = event.getMessage();
        String senderLang = playerDataManager.getPlayerLanguage(sender);
        Set<Player> recipients = event.getRecipients();
        String format = event.getFormat();

        plugin.getServer().getConsoleSender()
                .sendMessage(String.format(format, sender.getDisplayName(), originalMessage));

        Map<String, List<Player>> languageGroups = new HashMap<>();
        List<Player> noTranslationPlayers = new ArrayList<>();

        for (Player recipient : recipients) {
            String recipientLang = playerDataManager.getPlayerLanguage(recipient);
            boolean translationEnabled = playerDataManager.isTranslationEnabled(recipient);

            if (!translationEnabled || senderLang.equals(recipientLang)) {
                noTranslationPlayers.add(recipient);
            } else {
                languageGroups.computeIfAbsent(recipientLang, k -> new ArrayList<>()).add(recipient);
            }
        }

        String unformattedMessage = String.format(format, sender.getDisplayName(), originalMessage);
        for (Player p : noTranslationPlayers) {
            p.sendMessage(unformattedMessage);
        }

        for (Map.Entry<String, List<Player>> entry : languageGroups.entrySet()) {
            String targetLang = entry.getKey();
            List<Player> targetPlayers = entry.getValue();

            plugin.getTranslationService().translate(originalMessage, targetLang)
                    .thenAccept(translatedText -> {
                        String finalMessage = String.format(format, sender.getDisplayName(),
                                originalMessage + ChatColor.GOLD + " (" + translatedText + ")");
                        for (Player p : targetPlayers) {
                            p.sendMessage(finalMessage);
                        }
                    })
                    .exceptionally(ex -> {
                        plugin.getLogger().warning("Translation failed for language " + targetLang + ": " + ex.getMessage());
                        for (Player p : targetPlayers) {
                            p.sendMessage(unformattedMessage);
                        }
                        return null;
                    });
        }
    }
}
