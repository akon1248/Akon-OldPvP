package com.akon.oldpvp.listeners;

import com.akon.oldpvp.utils.ConfigManager;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.inventory.ItemStack;

public class BlockListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (ConfigManager.getBoolean("Utilities.old-brewing-stand") && e.getBlock().getState() instanceof BrewingStand) {
            ((BrewingStand)e.getBlock().getState()).getInventory().setFuel(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onBrewingFuel(BrewingStandFuelEvent e) {
        if (ConfigManager.getBoolean("Utilities.old-brewing-stand")) {
            e.setConsuming(false);
        }
    }

}
