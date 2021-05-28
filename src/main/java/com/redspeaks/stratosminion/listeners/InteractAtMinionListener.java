package com.redspeaks.stratosminion.listeners;

import com.redspeaks.stratosminion.lib.minion.Minion;
import com.redspeaks.stratosminion.lib.minion.MinionManager;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.Optional;

public class InteractAtMinionListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if(!(e.getRightClicked() instanceof ArmorStand)) return;
        ArmorStand as = (ArmorStand) e.getRightClicked();
        if(!as.isSmall()) return;
        if(!as.hasArms()) return;
        if(as.hasBasePlate()) return;
        Optional<Minion> optionalMinion = MinionManager.getMinion(as);
        if(optionalMinion.isPresent()) {
            e.setCancelled(true);
            if(optionalMinion.get().getOwner().getUniqueId().equals(e.getPlayer().getUniqueId()) && e.getPlayer().hasPermission("minion.admin")) {
                e.getPlayer().openInventory(optionalMinion.get().getInventory());
            }
        }
    }
}
