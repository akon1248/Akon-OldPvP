package com.akon.oldpvp.listener;

import com.akon.oldpvp.OldPvP;
import com.akon.oldpvp.utils.ConfigManager;
import com.akon.oldpvp.utils.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

public class EnchantListener implements Listener {

    @EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
    	boolean flag1 = e.getClickedInventory() == e.getView().getTopInventory() && e.getCurrentItem().getType() == Material.AIR && (e.getClick() == ClickType.RIGHT || e.getClick() == ClickType.LEFT);
    	boolean flag2 = e.getClickedInventory() == e.getView().getTopInventory() && e.getClick() == ClickType.NUMBER_KEY && e.getView().getPlayer().getInventory().getItem(e.getHotbarButton()) != null;
    	boolean flag3 = e.getClickedInventory() == e.getView().getPlayer().getInventory() && e.getCurrentItem() != null && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY;
		if (ConfigManager.getBoolean("Enchantment.old-enchanting-table") && e.getView().getTopInventory() instanceof EnchantingInventory && (flag1 || flag2 || flag3)) {
			try {
				ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("EntityHuman"), ReflectionUtil.getHandle(e.getView().getPlayer()), "enchantDone", new Class[]{ReflectionUtil.getNMSClass("ItemStack"), int.class}, new Object[]{null, 0});
				ReflectionUtil.setField(ReflectionUtil.getField(ReflectionUtil.getHandle(e.getView().getPlayer()), "activeContainer"), "f", ReflectionUtil.invokeMethod(ReflectionUtil.getHandle(e.getView().getPlayer()), "dg"));
			} catch (ReflectiveOperationException ex) {
				ex.printStackTrace();
			}
		}
	}

    @EventHandler
    public void onEnchant(EnchantItemEvent e) {
        if (ConfigManager.getBoolean("Enchantment.no-lapis-enchantments")) {
            Bukkit.getScheduler().runTask(OldPvP.getInstance(), () -> ((EnchantingInventory)e.getInventory()).setSecondary(new ItemStack(Material.INK_SACK, 64, (short)4)));
        }
    }
}
