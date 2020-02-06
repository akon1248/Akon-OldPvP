package com.akon.oldpvp.listeners;

import com.akon.oldpvp.utils.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingListener implements Listener {

    @EventHandler
    public void onFishing(PlayerFishEvent e) {
        if (ConfigManager.getBoolean("Projectiles.fishing-rod-knockback") && e.getState() == PlayerFishEvent.State.CAUGHT_ENTITY && e.getCaught() instanceof Player) {
            e.getHook().remove();
            e.setCancelled(true);
        }
    }

}
