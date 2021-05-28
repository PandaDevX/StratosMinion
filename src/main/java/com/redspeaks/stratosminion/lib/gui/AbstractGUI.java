package com.redspeaks.stratosminion.lib.gui;

import com.redspeaks.stratosminion.StratosMinion;
import com.redspeaks.stratosminion.lib.chat.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public abstract class AbstractGUI implements GUI, Listener {
    private Inventory inventory = null;
    public ClickEvent clickEvent;

    public AbstractGUI(String title, int rows) {
        this.inventory = Bukkit.createInventory(null, (9*rows), ChatUtil.colorize(title));
        init();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void addEvent(ClickEvent e) {
        this.clickEvent = e;
        StratosMinion.getInstance().getServer().getPluginManager().registerEvents(this, StratosMinion.getInstance());
    }

    @Override
    public void setItem(int slot, ItemBuilder builder) {
        inventory.setItem(slot-1, builder.build());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null) return;
        if(e.getClickedInventory().equals(e.getWhoClicked().getInventory())) return;
        if(!e.getInventory().equals(inventory)) return;
        if(e.getCurrentItem() == null) return;
        if(!e.getCurrentItem().hasItemMeta()) return;
        if(!e.getCurrentItem().getItemMeta().hasDisplayName()) return;
        if(clickEvent == null) return;
        clickEvent.caterEvent(e);
    }
}
