package com.redspeaks.stratosminion.listeners;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.redspeaks.stratosminion.lib.minion.Minion;
import com.redspeaks.stratosminion.lib.minion.MinionManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class SpawnListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(final PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(e.getClickedBlock() == null) return;
        if(!MinionManager.isValidItem(e.getItem())) return;
        final Location clickedLocation = e.getClickedBlock().getLocation().clone().add(0.5, 1, 0.5);
        Vector direction = clickedLocation.toVector().subtract(e.getPlayer().getEyeLocation().toVector()).normalize();
        double x = direction.getX();
        double y = direction.getY();
        double z = direction.getZ();

        // Now change the angle

        Optional<Minion> minionOpt = MinionManager.getMinionAt(clickedLocation);
        if(minionOpt.isPresent()) {
            e.getPlayer().sendMessage("There's already a minion at the location");
            return;
        }
        Minion suspectedMinion = createdMinion.getIfPresent(e.getPlayer());
        if(suspectedMinion == null) {
            Minion minion = MinionManager.spawnMinion(e.getItem(), clickedLocation, e.getPlayer());
            e.getPlayer().getInventory().removeItem(minion.convertToItem());
            e.getPlayer().updateInventory();
            minion.startMining();
            createdMinion.put(e.getPlayer(), minion);
        } else {
            suspectedMinion.startMining();
        }
    }

    private final Cache<Player, Minion> createdMinion = CacheBuilder.newBuilder()
            .expireAfterWrite(1L, TimeUnit.SECONDS)
            .build();

    private float toDegree(double angle) {
        return (float) Math.toDegrees(angle);
    }

    private String getCardinalDirection(Location location) {
        double rotation = (location.getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return "N";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "NE";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "E";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "SE";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "S";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "SW";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "W";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "NW";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "N";
        } else {
            return null;
        }
    }
}
