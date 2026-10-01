package io.github.utr3cht.chattranslate;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.utr3cht.chattranslate.commands.ChatCommand;
import io.github.utr3cht.chattranslate.listeners.ChatListener;
import io.github.utr3cht.chattranslate.translation.AzureTranslator;
import io.github.utr3cht.chattranslate.translation.DeepLTranslator;
import io.github.utr3cht.chattranslate.translation.DummyTranslator;
import io.github.utr3cht.chattranslate.translation.HybridTranslator;
import io.github.utr3cht.chattranslate.translation.TranslationService;

public class ChatTranslate extends JavaPlugin {

    private ConfigManager configManager;
    private PlayerDataManager playerDataManager;
    private TranslationService translationService;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);

        playerDataManager = new PlayerDataManager(this, configManager.getDefaultLanguage());

        setupTranslationService();

        ChatCommand chatCommand = new ChatCommand(this, playerDataManager);
        getCommand("chat").setExecutor(chatCommand);
        getCommand("chat").setTabCompleter(chatCommand);

        getServer().getPluginManager().registerEvents(new ChatListener(this, playerDataManager), this);

        getLogger().info("ChatTranslate has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ChatTranslate has been disabled!");
    }

    public void setupTranslationService() {
        String mode = configManager.getTranslatorMode().toUpperCase();
        String deeplKey = configManager.getDeepLApiKey();
        String azureKey = configManager.getAzureApiKey();

        boolean hasDeepl = deeplKey != null && !deeplKey.isEmpty();
        boolean hasAzure = azureKey != null && !azureKey.isEmpty();

        if (mode.equals("AUTO")) {
            if (hasDeepl && hasAzure) mode = "HYBRID";
            else if (hasDeepl) mode = "DEEPL";
            else if (hasAzure) mode = "AZURE";
            else mode = "NONE";
        }

        DeepLTranslator deepLTranslator = hasDeepl ? new DeepLTranslator(deeplKey) : null;
        AzureTranslator azureTranslator = hasAzure ? new AzureTranslator(azureKey, configManager.getAzureRegion()) : null;

        switch (mode) {
            case "DEEPL":
                if (!hasDeepl) {
                    getLogger().warning("DeepL mode selected but API key is missing! Translation disabled.");
                    translationService = new DummyTranslator();
                } else {
                    translationService = deepLTranslator;
                    getLogger().info("Using DeepL translator mode.");
                }
                break;
            case "AZURE":
                if (!hasAzure) {
                    getLogger().warning("Azure mode selected but API key is missing! Translation disabled.");
                    translationService = new DummyTranslator();
                } else {
                    translationService = azureTranslator;
                    getLogger().info("Using Azure translator mode.");
                }
                break;
            case "HYBRID":
                if (hasDeepl && hasAzure) {
                    translationService = new HybridTranslator(deepLTranslator, azureTranslator, getLogger());
                    getLogger().info("Using Hybrid translator mode (DeepL -> Azure fallback).");
                } else {
                    getLogger().warning("Hybrid mode requires BOTH API keys! Translation disabled.");
                    translationService = new DummyTranslator();
                }
                break;
            case "NONE":
            default:
                getLogger().warning("No valid API keys found in config.yml. Translation is currently disabled.");
                translationService = new DummyTranslator();
                break;
        }
    }

    public void reloadPlugin() {
        configManager.loadConfig();
        setupTranslationService();
        getLogger().info("Configuration and translation service reloaded.");
    }

    public TranslationService getTranslationService() {
        return translationService;
    }
}
