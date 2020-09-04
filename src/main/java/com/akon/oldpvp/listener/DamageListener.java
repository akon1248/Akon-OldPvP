package com.akon.oldpvp.listener;

import com.akon.oldpvp.OldPvP;
import com.akon.oldpvp.utils.ConfigManager;
import com.akon.oldpvp.utils.ReflectionUtil;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) < 0) {
            double damageReduction = e.getDamage()*ConfigManager.getNumber("Combat.shield-damage-reducation").doubleValue();
            if (damageReduction <= 0) {
                damageReduction = 0;
            }
            double finalDamage = e.getFinalDamage()-e.getDamage(EntityDamageEvent.DamageModifier.BLOCKING);
            e.setDamage(EntityDamageEvent.DamageModifier.BLOCKING, finalDamage >= damageReduction ? -damageReduction : -finalDamage);
            e.getEntity().playEffect(EntityEffect.HURT);
            if (ConfigManager.getBoolean("Combat.disable-shield-cooldown")) {
                Bukkit.getScheduler().runTask(OldPvP.getInstance(), () -> ((Player)e.getEntity()).setCooldown(Material.SHIELD, 0));
            }
        }
        if (e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && ((e.getDamager() instanceof Egg && ConfigManager.getBoolean("Projectile.egg-knockback")) || (e.getDamager() instanceof EnderPearl && ConfigManager.getBoolean("Projectile.ender-pearl-knockback")) || (e.getDamager() instanceof Snowball && ConfigManager.getBoolean("Projectile.snowball-knockback")))) {
            e.setDamage(0.0000001);
        } else if (ConfigManager.getBoolean("Combat.disable-sweep-attack") && e.getDamager() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            e.setCancelled(true);
        } else if (ConfigManager.getBoolean("Enchantment.old-sharpness") && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            boolean flag = false;
            for (StackTraceElement ste: Thread.currentThread().getStackTrace()) {
                if ((ste.getClassName().equals("net.minecraft.server." + ReflectionUtil.getVersion() + ".EntityHuman") && ste.getMethodName().equals("attack")) || (ste.getClassName().equals("net.minecraft.server." + ReflectionUtil.getVersion() + ".EntityMonster") && ste.getMethodName().equals("B"))) {
                    flag = true;
                    break;
                }
            }
            if (flag && ((LivingEntity)e.getDamager()).getEquipment().getItemInMainHand().getItemMeta() != null && ((LivingEntity)e.getDamager()).getEquipment().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.DAMAGE_ALL)) {
                int sharpnessLevel = ((LivingEntity)e.getDamager()).getEquipment().getItemInMainHand().getItemMeta().getEnchantLevel(Enchantment.DAMAGE_ALL);
                double sharpnessDamage = sharpnessLevel <= 1 ? 1 : 1+(sharpnessLevel-1)*0.5;
                double rawDamage = e.getDamage()-sharpnessDamage;
                double newDamage = rawDamage+sharpnessLevel*1.25;
                e.setDamage(newDamage <= 0 ? 0 : newDamage);
            }
        }
    }

}
