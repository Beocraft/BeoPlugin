package net.beocraft.plugin;

import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;

import java.time.Instant;
import java.util.Objects;

public class DiscordLogs implements Listener {

    private final BeoPlugin plugin;

    public DiscordLogs(BeoPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldLoadEvent(WorldLoadEvent event) {
        if (plugin.getServer().getWorlds().get(0).equals(event.getWorld())) {
            plugin.getWebhook().send(new WebhookEmbedBuilder()
                    .setColor(Constants.SYSTEM_COLOR)
                    .setDescription("**Loading the world**")
                    .setTimestamp(Instant.now())
                    .build());
        }
    }

    @EventHandler
    public void onServerLoadEvent(ServerLoadEvent event) {
        if (event.getType() == ServerLoadEvent.LoadType.STARTUP) {
            plugin.getWebhook().send(new WebhookEmbedBuilder()
                    .setColor(Constants.SYSTEM_COLOR)
                    .setDescription("**Server loaded**")
                    .setTimestamp(Instant.now())
                    .build());
        }
        if (event.getType() == ServerLoadEvent.LoadType.RELOAD) {
            plugin.getWebhook().send(new WebhookEmbedBuilder()
                    .setColor(Constants.SYSTEM_COLOR)
                    .setDescription("**Server reloaded**")
                    .setTimestamp(Instant.now())
                    .build());
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getWebhook().send(new WebhookMessageBuilder()
                .setUsername(player.getName())
                .setAvatarUrl(generateUrl(player))
                .addEmbeds(new WebhookEmbedBuilder()
                        .setColor(Constants.JOIN_COLOR)
                        .setDescription("**Player joined the game**")
                        .addField(currentlyOnline(false))
                        .setTimestamp(Instant.now())
                        .build())
                .build());
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getWebhook().send(new WebhookMessageBuilder()
                .setUsername(player.getName())
                .setAvatarUrl(generateUrl(player))
                .addEmbeds(new WebhookEmbedBuilder()
                        .setColor(Constants.QUIT_COLOR)
                        .setDescription("**Player left the game**")
                        .addField(currentlyOnline(true))
                        .setTimestamp(Instant.now())
                        .build())
                .build());
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location location = player.getLocation();
        String world = StringUtils.capitalize(Objects.requireNonNull(player.getLocation().getWorld()).getName().replace("_", " "));
        String format = String.format("%s: %s %s %s", world, location.getBlockX(), location.getBlockY(), location.getBlockZ());
        plugin.getWebhook().send(new WebhookMessageBuilder()
                .setUsername(player.getName())
                .setAvatarUrl(generateUrl(player))
                .addEmbeds(new WebhookEmbedBuilder()
                        .setColor(Constants.DEATH_COLOR)
                        .setDescription("**" + ChatColor.stripColor(event.getDeathMessage()) + "**")
                        .addField(new WebhookEmbed.EmbedField(false, "Location:", format))
                        .setTimestamp(Instant.now())
                        .build())
                .build());
    }

    private WebhookEmbed.EmbedField currentlyOnline(boolean quit) {
        Server server = plugin.getServer();
        int i = (quit) ? 1 : 0;
        String online = String.format("%s/%s", server.getOnlinePlayers().size() - i, server.getMaxPlayers());
        return new WebhookEmbed.EmbedField(false, "Online:", online);
    }

    private String generateUrl(Player player) {
        String uuid = player.getUniqueId().toString().replace("-", "");
        return "https://crafatar.com/avatars/" + uuid;
    }
}
