package com.redspeaks.stratosminion.listeners;

import com.redspeaks.stratosminion.lib.chat.ChatUtil;
import com.redspeaks.stratosminion.lib.storage.Storage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerPlaceListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlace(BlockPlaceEvent e) {
        if(Storage.prohibitedBlocks.contains(ChatUtil.serializeLocation(e.getBlock().getLocation()))) {
            e.setCancelled(true);
        }
    }
}
