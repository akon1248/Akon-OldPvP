package com.akon.oldpvp.listeners;

import com.akon.oldpvp.OldPvP;
import com.akon.oldpvp.utils.ConfigManager;
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

    public static final HashMap<Player, BlockingRunnable> BLOCKING_RUNNABLE = new HashMap<>();

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if (ConfigManager.getBoolean("Melee.sword-blocking") && BLOCKING_RUNNABLE.get(e.getPlayer()) == null && isSword(e.getItem())) {
                new BlockingRunnable(e.getPlayer()).runTaskTimer(OldPvP.getInstance(), 5, 1);
            } else if (ConfigManager.getBoolean("Projectiles.disable-ender-pearl-cooldown") && e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL) {
                Bukkit.getScheduler().runTask(OldPvP.getInstance(), () -> e.getPlayer().setCooldown(Material.ENDER_PEARL, 0));
            }
        }
    }

    @EventHandler
    public void onClickEntity(PlayerInteractEntityEvent e) {
        if (isSword(e.getPlayer().getEquipment().getItemInMainHand())) {
            if (ConfigManager.getBoolean("Melee.sword-blocking") && BLOCKING_RUNNABLE.get(e.getPlayer()) == null && isSword(e.getPlayer().getEquipment().getItemInMainHand())) {
                new BlockingRunnable(e.getPlayer()).runTaskTimer(OldPvP.getInstance(), 5, 1);
            } else if (ConfigManager.getBoolean("Projectiles.disable-ender-pearl-cooldown") && e.getPlayer().getEquipment().getItemInMainHand() != null && e.getPlayer().getEquipment().getItemInMainHand().getType() == Material.ENDER_PEARL) {
                Bukkit.getScheduler().runTask(OldPvP.getInstance(), () -> e.getPlayer().setCooldown(Material.ENDER_PEARL, 0));
            }
        }
    }

    private static boolean isSword(ItemStack item) {
        return item != null && (item.getType() == Material.WOOD_SWORD || item.getType() == Material.STONE_SWORD || item.getType() == Material.IRON_SWORD || item.getType() == Material.DIAMOND_SWORD || item.getType() == Material.GOLD_SWORD);
    }

    public static class BlockingRunnable extends BukkitRunnable  {

        private Player p;
        private ItemStack offhandItem;

        public BlockingRunnable(Player p) {
            this.p = p;
            this.offhandItem = p.getEquipment().getItemInOffHand();
            BLOCKING_RUNNABLE.put(p, this);
            ItemStack shield = new ItemStack(Material.SHIELD);
            ItemMeta meta = shield.getItemMeta();
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            shield.setItemMeta(meta);
            p.getEquipment().setItemInOffHand(shield);
        }

        @Override
        public void run() {
            if (!p.isHandRaised() || !isSword(this.p.getEquipment().getItemInMainHand()) || !ConfigManager.getBoolean("Melee.sword-blocking")) {
                this.cancel();
            }
        }

        @Override
        public void cancel() {
            super.cancel();
            p.getEquipment().setItemInOffHand(offhandItem);
            BLOCKING_RUNNABLE.remove(this.p);
        }

    }

}
