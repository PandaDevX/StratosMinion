package com.redspeaks.stratosminion.lib.gui;

import org.bukkit.inventory.Inventory;

public interface GUI {

    void init();
    void addEvent(ClickEvent e);
    Inventory getInventory();
    void setItem(int slot, ItemBuilder builder);
}
