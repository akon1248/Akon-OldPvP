package com.akon.oldpvp.listener;

import com.akon.oldpvp.OldPvP;
import com.akon.oldpvp.utils.ConfigManager;
import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class InteractListener implements Listener {

    public static final HashMap<Player, BukkitRunnable> BLOCKING_TASK = Maps.newHashMap();

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (ConfigManager.getBoolean("Combat.sword-blocking") && BLOCKING_TASK.get(e.getPlayer()) == null && isSword(e.getItem())) {
                new BlockingTask(e.getPlayer()).runTaskTimer(OldPvP.getInstance(), 5, 1);
            } else if (ConfigManager.getBoolean("Projectile.disable-ender-pearl-cooldown") && e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL) {
                Bukkit.getScheduler().runTask(OldPvP.getInstance(), () -> e.getPlayer().setCooldown(Material.ENDER_PEARL, 0));
            }
        }
    }

    @EventHandler
    public void onClickEntity(PlayerInteractEntityEvent e) {
        if (isSword(e.getPlayer().getEquipment().getItemInMainHand())) {
            if (ConfigManager.getBoolean("Combat.sword-blocking") && BLOCKING_TASK.get(e.getPlayer()) == null && isSword(e.getPlayer().getEquipment().getItemInMainHand())) {
                new BlockingTask(e.getPlayer()).runTaskTimer(OldPvP.getInstance(), 5, 1);
            } else if (ConfigManager.getBoolean("Projectile.disable-ender-pearl-cooldown") && e.getPlayer().getEquipment().getItemInMainHand() != null && e.getPlayer().getEquipment().getItemInMainHand().getType() == Material.ENDER_PEARL) {
                Bukkit.getScheduler().runTask(OldPvP.getInstance(), () -> e.getPlayer().setCooldown(Material.ENDER_PEARL, 0));
            }
        }
    }

    private static boolean isSword(ItemStack item) {
        return item != null && (item.getType() == Material.WOOD_SWORD || item.getType() == Material.STONE_SWORD || item.getType() == Material.IRON_SWORD || item.getType() == Material.DIAMOND_SWORD || item.getType() == Material.GOLD_SWORD);
    }

    private static class BlockingTask extends BukkitRunnable  {

        private Player p;
        private ItemStack offhandItem;

        public BlockingTask(Player p) {
            this.p = p;
            this.offhandItem = p.getEquipment().getItemInOffHand();
            BLOCKING_TASK.put(p, this);
            ItemStack shield = new ItemStack(Material.SHIELD);
            ItemMeta meta = shield.getItemMeta();
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            shield.setItemMeta(meta);
            p.getEquipment().setItemInOffHand(shield);
        }

        @Override
        public void run() {
            if (!this.p.isHandRaised() || !isSword(this.p.getEquipment().getItemInMainHand()) || !ConfigManager.getBoolean("Combat.sword-blocking")) {
                this.cancel();
            }
        }

        @Override
        public void cancel() {
            super.cancel();
            this.p.getEquipment().setItemInOffHand(this.offhandItem);
            BLOCKING_TASK.remove(this.p);
        }

    }

}
