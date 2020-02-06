package com.akon.oldpvp.listeners;

import com.akon.oldpvp.utils.ConfigManager;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if ((ConfigManager.getBoolean("Utilities.old-brewing-stand") && e.getClickedInventory() instanceof BrewerInventory && e.getSlot() == 4) || (ConfigManager.getBoolean("Utilities.no-lapis-enchantments") && e.getClickedInventory() instanceof EnchantingInventory && e.getSlot() == 1) || (e.getClickedInventory() == e.getWhoClicked().getInventory() && (((ConfigManager.getBoolean("Melee.disable-offhand") || e.getWhoClicked().isBlocking()) && e.getSlot() == 40) || (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.SHIELD && e.getClickedInventory().getItem(40) == null && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (ConfigManager.getBoolean("Utilities.old-brewing-stand") && e.getInventory() instanceof BrewerInventory) {
            ((BrewerInventory)e.getInventory()).setFuel(new ItemStack(Material.BLAZE_POWDER, 64));
        } else if (ConfigManager.getBoolean("Utilities.no-lapis-enchantments") && e.getInventory() instanceof EnchantingInventory) {
            ((EnchantingInventory)e.getInventory()).setSecondary(new ItemStack(Material.INK_SACK, 64, (short)4));
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (ConfigManager.getBoolean("Utilities.no-lapis-enchantments") && e.getInventory() instanceof EnchantingInventory) {
            ((EnchantingInventory)e.getInventory()).setSecondary(new ItemStack(Material.AIR));
        }
    }

}
