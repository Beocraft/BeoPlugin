package net.beocraft.plugin;

import club.minnced.discord.webhook.WebhookClient;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class BeoPlugin extends JavaPlugin {

    private final WebhookClient webhook;

    public BeoPlugin() throws IOException {
        String url = Files.readString(Paths.get("webhook.txt"));
        this.webhook = WebhookClient.withUrl(url);
    }

    @Override
    public void onEnable() {
        webhook.send("**Server starting**");
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new DiscordLogs(this), this);
        manager.registerEvents(new CustomMobs(), this);
    }

    @Override
    public void onDisable() {
        webhook.send("**Server stopped**");
        webhook.close();
    }

    public WebhookClient getWebhook() {
        return webhook;
    }
}
