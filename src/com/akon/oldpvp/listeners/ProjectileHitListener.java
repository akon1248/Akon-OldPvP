package com.akon.oldpvp.listeners;

import com.akon.oldpvp.utils.ConfigManager;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.lang.reflect.InvocationTargetException;

import static com.akon.oldpvp.utils.ReflectionUtil.*;

public class ProjectileHitListener implements Listener {

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (ConfigManager.getBoolean("Projectiles.fishing-rod-knockback") && e.getEntity() instanceof FishHook && e.getHitEntity() != null && e.getHitEntity() instanceof LivingEntity) {
            try {
                Object damageSource = invokeStaticMethod(getNMSClass("DamageSource"), "projectile", new Class[] {getNMSClass("Entity"), getNMSClass("Entity")}, new Object[] {getHandle(e.getEntity()), e.getEntity().getShooter() == null ? null : getHandle(e.getEntity().getShooter())});
                invokeMethod(getHandle(e.getHitEntity()), "damageEntity", new Class[] {getNMSClass("DamageSource"), float.class}, new Object[] {damageSource, 0.0000001F});
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }

        }
    }

}
