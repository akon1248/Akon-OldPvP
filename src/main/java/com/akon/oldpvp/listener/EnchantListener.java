package com.akon.oldpvp.listener;

import com.akon.oldpvp.OldPvP;
import com.akon.oldpvp.utils.ConfigManager;
import com.akon.oldpvp.utils.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

public class EnchantListener implements Listener {

    @EventHandler
    public void onPrepareEnchant(PrepareItemEnchantEvent e) {
        if (ConfigManager.getBoolean("Enchantment.old-enchanting-table")) {
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
