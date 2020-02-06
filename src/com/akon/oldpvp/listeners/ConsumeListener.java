package com.akon.oldpvp.listeners;

import com.akon.oldpvp.utils.ConfigManager;
import com.akon.oldpvp.utils.ReflectionUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class ConsumeListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onConsume(PlayerItemConsumeEvent e) {
        if (ConfigManager.getBoolean("GoldenApple.old-god-apple-effects") && e.getItem().getType() == Material.GOLDEN_APPLE && e.getItem().getDurability() > 0 && !e.isCancelled()) {
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, new Random().nextFloat()*0.1F+0.9F);
            e.getPlayer().setFoodLevel(e.getPlayer().getFoodLevel()+4);
            e.getPlayer().setSaturation(e.getPlayer().getFoodLevel()-e.getPlayer().getSaturation() <= 9.6 ? e.getPlayer().getFoodLevel() : e.getPlayer().getSaturation()+9.6F);
            addPotionEffect(e.getPlayer(), new PotionEffect(PotionEffectType.REGENERATION, 600, 4, false));
            addPotionEffect(e.getPlayer(), new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6000, 0, false));
            addPotionEffect(e.getPlayer(), new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 0, false));
            addPotionEffect(e.getPlayer(), new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0, false));
            if (e.getItem().equals(e.getPlayer().getEquipment().getItemInMainHand())) {
                e.getPlayer().getEquipment().getItemInMainHand().setAmount(e.getItem().getAmount()-1);
            } else if (e.getItem().equals(e.getPlayer().getEquipment().getItemInOffHand())) {
                e.getPlayer().getEquipment().getItemInOffHand().setAmount(e.getItem().getAmount()-1);
            }
            e.setCancelled(true);
            try {
                ReflectionUtil.invokeMethod(ReflectionUtil.getHandle(e.getPlayer()), "cN");
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void addPotionEffect(LivingEntity entity, PotionEffect potionEffect) {
        if (!entity.hasPotionEffect(potionEffect.getType())) {
            entity.addPotionEffect(potionEffect, true);
        } else {
            for (PotionEffect effect: entity.getActivePotionEffects()) {
                if (effect.getType().equals(potionEffect.getType()) && ((effect.getAmplifier() < potionEffect.getAmplifier()) || (effect.getAmplifier() == potionEffect.getAmplifier() && effect.getDuration() <= potionEffect.getDuration()))) {
                    entity.addPotionEffect(potionEffect, true);
                    break;
                }
            }
        }
    }

}
