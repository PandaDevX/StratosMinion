package com.redspeaks.stratosminion.listeners;

import com.redspeaks.stratosminion.lib.chat.ChatUtil;
import com.redspeaks.stratosminion.lib.storage.Storage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerBreakListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent e) {
         if(Storage.prohibitedBlocks.contains(ChatUtil.serializeLocation(e.getBlock().getLocation()))) {
             e.setCancelled(true);
         }
    }
}
