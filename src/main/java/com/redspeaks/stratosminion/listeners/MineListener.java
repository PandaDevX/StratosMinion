package com.redspeaks.stratosminion.listeners;

import com.redspeaks.stratosminion.lib.gui.ItemBuilder;
import me.revils.enchants.CustomEvents.RevMineEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class MineListener implements Listener {

    @EventHandler
    public void onMine(RevMineEvent e) {
        if(e.getConfigOption("Enabled", 24).equalsIgnoreCase("true")) {
            if(e.getEnchantLevel(24) > 0) {
                if(e.IfChance(24)) {
                    ItemStack star_dust = new ItemBuilder("&e&lStar Dust", Collections.emptyList(), Material.YELLOW_DYE).build();
                    e.getPlayer().getInventory().addItem(star_dust);
                    e.getPlayer().updateInventory();
                }
            }
        }
    }
}
