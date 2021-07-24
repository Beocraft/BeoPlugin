package net.beocraft.plugin;

import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class DiscordLogs implements Listener {

    private final BeoPlugin plugin;

    public DiscordLogs(BeoPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldLoadEvent(WorldLoadEvent event) {
        if (plugin.getServer().getWorlds().get(0).equals(event.getWorld())) {
            plugin.getWebhook().send("**Loading the world**");
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        String player = event.getPlayer().getName();
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(player);
        builder.setContent("**Player " + player + " joined the game**");
        plugin.getWebhook().send(builder.build());
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        String player = event.getPlayer().getName();
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(player);
        builder.setContent("**Player " + player + " left the game**");
        plugin.getWebhook().send(builder.build());
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        String player = event.getEntity().getName();
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(player);
        builder.setContent("**" + event.getDeathMessage() + "**");
        plugin.getWebhook().send(builder.build());
    }
}
