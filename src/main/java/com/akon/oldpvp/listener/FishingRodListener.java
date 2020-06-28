package com.akon.oldpvp.listener;

import com.akon.oldpvp.utils.ConfigManager;
import com.akon.oldpvp.utils.ReflectionUtil;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingRodListener implements Listener {

    @EventHandler
    public void onFishing(PlayerFishEvent e) {
        if (ConfigManager.getBoolean("Projectile.fishing-rod-knockback") && e.getState() == PlayerFishEvent.State.CAUGHT_ENTITY && e.getCaught() instanceof Player) {
            e.getHook().remove();
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (ConfigManager.getBoolean("Projectile.fishing-rod-knockback") && e.getEntity() instanceof FishHook && e.getHitEntity() != null && e.getHitEntity() instanceof LivingEntity) {
            try {
                Object damageSource = ReflectionUtil.invokeStaticMethod(ReflectionUtil.getNMSClass("DamageSource"), "projectile", new Class[]{ReflectionUtil.getNMSClass("Entity"), ReflectionUtil.getNMSClass("Entity")}, new Object[]{ReflectionUtil.getHandle(e.getEntity()), e.getEntity().getShooter() == null ? null : ReflectionUtil.getHandle(e.getEntity().getShooter())});
                ReflectionUtil.invokeMethod(ReflectionUtil.getNMSClass("Entity"), ReflectionUtil.getHandle(e.getHitEntity()), "damageEntity", new Class[]{ReflectionUtil.getNMSClass("DamageSource"), float.class}, new Object[]{damageSource, 0.0000001F});
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
            }

        }
    }

}
