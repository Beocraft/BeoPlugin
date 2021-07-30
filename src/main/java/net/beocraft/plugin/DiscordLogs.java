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
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Objects;

public class DiscordLogs implements Listener {

    private final BeoPlugin plugin;

    public DiscordLogs(BeoPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldLoadEvent(WorldLoadEvent event) {
        new Thread(() -> {
            if (plugin.getServer().getWorlds().get(0).equals(event.getWorld())) {
                plugin.getWebhook().send(new WebhookEmbedBuilder()
                        .setColor(Constants.SYSTEM_COLOR)
                        .setDescription("**Loading the world**")
                        .setTimestamp(Instant.now())
                        .build());
            }
        }).start();
    }

    @EventHandler
    public void onServerLoadEvent(ServerLoadEvent event) {
        new Thread(() -> {
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
        }).start();
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        new Thread(() -> {
            Player player = event.getPlayer();
            plugin.getWebhook().send(new WebhookMessageBuilder()
                    .setUsername(player.getName())
                    .setAvatarUrl(generateAvatarUrl(player))
                    .addEmbeds(new WebhookEmbedBuilder()
                            .setColor(Constants.JOIN_COLOR)
                            .setDescription("**" + player.getName() + " joined the game**")
                            .addField(currentlyOnline(false))
                            .setTimestamp(Instant.now())
                            .build())
                    .build());
        }).start();
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        new Thread(() -> {
            Player player = event.getPlayer();
            plugin.getWebhook().send(new WebhookMessageBuilder()
                    .setUsername(player.getName())
                    .setAvatarUrl(generateAvatarUrl(player))
                    .addEmbeds(new WebhookEmbedBuilder()
                            .setColor(Constants.QUIT_COLOR)
                            .setDescription("**" + player.getName() + " left the game**")
                            .addField(currentlyOnline(true))
                            .setTimestamp(Instant.now())
                            .build())
                    .build());
        }).start();
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        new Thread(() -> {
            Player player = event.getEntity();
            Location location = player.getLocation();
            String world = StringUtils.capitalize(Objects.requireNonNull(player.getLocation().getWorld()).getName().replace("_", " "));
            String format = String.format("%s: x:`%s` y:`%s` z:`%s`", world, location.getBlockX(), location.getBlockY(), location.getBlockZ());
            plugin.getWebhook().send(new WebhookMessageBuilder()
                    .setUsername(player.getName())
                    .setAvatarUrl(generateAvatarUrl(player))
                    .addEmbeds(new WebhookEmbedBuilder()
                            .setColor(Constants.DEATH_COLOR)
                            .setDescription("**" + ChatColor.stripColor(event.getDeathMessage()) + "**")
                            .addField(new WebhookEmbed.EmbedField(false, "Location:", format))
                            .setTimestamp(Instant.now())
                            .build())
                    .build());
        }).start();
    }

    @EventHandler
    public void onPlayerAdvancementDoneEvent(PlayerAdvancementDoneEvent event) {
        new Thread(() -> {
            String[] advancement = event.getAdvancement().getKey().getKey().split("/");
            // Recipes should not be displayed
            if (advancement[0].equalsIgnoreCase("recipes")) {
                return;
            }
            String category = StringUtils.capitalize(advancement[0]);
            String name = StringUtils.capitalize(advancement[1].replace("_", " "));
            String format = String.format("%s: %s", category, name);

            Player player = event.getPlayer();
            plugin.getWebhook().send(new WebhookMessageBuilder()
                    .setUsername(player.getName())
                    .setAvatarUrl(generateAvatarUrl(player))
                    .addEmbeds(new WebhookEmbedBuilder()
                            .setColor(Constants.SYSTEM_COLOR)
                            .setDescription("**" + player.getName() + " made an advancement**")
                            .addField(new WebhookEmbed.EmbedField(false, "Advancement:", format))
                            .setTimestamp(Instant.now())
                            .build())
                    .build());
        }).start();
    }

    private synchronized WebhookEmbed.@NotNull EmbedField currentlyOnline(boolean quit) {
        Server server = plugin.getServer();
        int i = (quit) ? 1 : 0;
        String online = String.format("%s/%s", server.getOnlinePlayers().size() - i, server.getMaxPlayers());
        return new WebhookEmbed.EmbedField(false, "Online:", online);
    }

    private @NotNull String generateAvatarUrl(@NotNull Player player) {
        String uuid = player.getUniqueId().toString().replace("-", "");
        return "https://crafatar.com/avatars/" + uuid;
    }
}
