package net.beocraft.plugin;

import org.bukkit.Material;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CustomMobs implements Listener {

    @EventHandler
    public void onEntityDeathEvent(@NotNull EntityDeathEvent event) {
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
    public void onEntitySpawnEvent(@NotNull EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Bat) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplodeEvent(EntityExplodeEvent event) {
        EntityType type = event.getEntityType();
        if (type == EntityType.ENDER_DRAGON || type == EntityType.FIREBALL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
        if (event.getEntity().getType() == EntityType.ENDERMAN) {
            event.setCancelled(true);
        }
    }
}
