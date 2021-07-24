package net.beocraft.plugin;

import org.bukkit.Material;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

public class CustomMobs implements Listener {

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        EntityType type = entity.getType();
        if (type == EntityType.SHULKER) {
            event.getDrops().clear();
            ItemStack stack = new ItemStack(Material.SHULKER_SHELL, 2);
            entity.getWorld().dropItemNaturally(entity.getLocation(), stack);
            return;
        }
        if (type == EntityType.ENDER_DRAGON) {
            event.getDrops().clear();
            ItemStack stack = new ItemStack(Material.ELYTRA, 1);
            entity.getWorld().dropItemNaturally(entity.getLocation(), stack);
        }
    }

    @EventHandler
    public void onEntitySpawnEvent(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Bat) {
            event.setCancelled(true);
        }
    }
}