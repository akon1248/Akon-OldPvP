package com.akon.oldpvp.listeners;

import com.akon.oldpvp.utils.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class SwapHandItemsListener implements Listener {

    @EventHandler
    public void onSwapHandItems(PlayerSwapHandItemsEvent e) {
        if (ConfigManager.getBoolean("Melee.disable-offhand")) {
            e.setCancelled(true);
        }
    }

}
