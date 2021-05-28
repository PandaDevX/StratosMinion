package com.redspeaks.stratosminion.listeners;

import com.redspeaks.stratosminion.lib.minion.Minion;
import com.redspeaks.stratosminion.lib.minion.MinionManager;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import java.util.Optional;

public class DeathListener implements Listener {

    @EventHandler
    public void entityDeath(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Player)) return;
        if(!(e.getEntity() instanceof ArmorStand)) return;
        ArmorStand as = (ArmorStand) e.getEntity();
        if(!as.isSmall()) return;
        if(!as.hasArms()) return;
        Optional<Minion> minion = MinionManager.getMinion(as);
        if(minion.isPresent()) {
            Player player = (Player)e.getDamager();
            e.setDamage(0);
            e.setCancelled(true);
            if(!player.hasPermission("minion.admin") && !minion.get().getOwner().getUniqueId().equals(player.getUniqueId())) {
                e.setCancelled(true);
                return;
            }
            Minion m = minion.get();
            ItemStack itemStack = m.convertToItem();
            Player owner = minion.get().getOwner().getPlayer();
            if(owner != null) {
                owner.getInventory().addItem(itemStack);
                owner.updateInventory();
            }
            MinionManager.getMinions().remove(m);
            m.kill();
            m.deleteData();
        }
    }
}
