package com.akon.oldpvp.listeners;

import com.akon.oldpvp.utils.ConfigManager;
import com.akon.oldpvp.utils.ReflectionUtil;
import org.bukkit.EntityEffect;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) < 0) {
            double damageReducation = e.getDamage()*ConfigManager.getNumber("Melee.shield-damage-reducation").doubleValue();
            if (damageReducation <= 0) {
                damageReducation = 0;
            }
            double finalDamage = e.getFinalDamage()-e.getDamage(EntityDamageEvent.DamageModifier.BLOCKING);
            e.setDamage(EntityDamageEvent.DamageModifier.BLOCKING, finalDamage >= damageReducation ? -damageReducation : -finalDamage);
            e.getEntity().playEffect(EntityEffect.HURT);
        }
        if (e.getEntity() instanceof Player && ((e.getDamager() instanceof Egg && ConfigManager.getBoolean("Projectiles.egg-knockback")) || (e.getDamager() instanceof EnderPearl && ConfigManager.getBoolean("Projectiles.ender-pearl-knockback")) || (e.getDamager() instanceof Snowball && ConfigManager.getBoolean("Projectiles.snowball-knockback")))) {
            e.setDamage(0.0000001);
        } else if (ConfigManager.getBoolean("Melee.disable-sweep-attack") && e.getDamager() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            e.setCancelled(true);
        } else if (ConfigManager.getBoolean("Enchantments.old-sharpness") && e.getDamager() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            boolean flag = false;
            for (StackTraceElement ste: Thread.currentThread().getStackTrace()) {
                if (ste.getClassName().equals("net.minecraft.server." + ReflectionUtil.getVersion() + ".EntityHuman") && ste.getMethodName().equals("attack")) {
                    flag = true;
                    break;
                }
            }
            if (flag && ((Player)e.getDamager()).getEquipment().getItemInMainHand().getItemMeta() != null && ((Player)e.getDamager()).getEquipment().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.DAMAGE_ALL)) {
                int sharpnessLevel = ((Player)e.getDamager()).getEquipment().getItemInMainHand().getItemMeta().getEnchantLevel(Enchantment.DAMAGE_ALL);
                double sharpnessDamage = sharpnessLevel <= 1 ? 1 : 1+(sharpnessLevel-1)*0.5;
                double rawDamage = e.getDamage()-sharpnessDamage;
                double newDamage = rawDamage+sharpnessLevel*1.25;
                e.setDamage(newDamage <= 0 ? 0 : newDamage);
            }
        }
    }

}
