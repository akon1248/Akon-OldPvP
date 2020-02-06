package com.akon.oldpvp;

import com.akon.oldpvp.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.akon.oldpvp.utils.ReflectionUtil.*;

public class OldPvPRunnable extends BukkitRunnable {

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            try {
                if (ConfigManager.getBoolean("Melee.disable-attack-speed") && (Integer)getField(getHandle(p), "aE") < 0) {
                    setField(getHandle(p), "aE", 1);
                }
                for (Object modifier : (HashSet<?>)((HashSet<?>)invokeMethod(getField(p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE), "handle"), "c")).clone()) {
                    if (ConfigManager.getBoolean("PotionEffects.old-strength") && ((String)invokeMethod(modifier, "b")).matches("effect\\.damageBoost -?\\d+") && invokeMethod(modifier, "a").equals(UUID.fromString("648d7064-6a60-4f59-8abe-c2c23a6dd7a9"))) {
                        int strengthTier = 0;
                        for (PotionEffect potionEffect: p.getActivePotionEffects()) {
                            if (potionEffect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                                strengthTier = potionEffect.getAmplifier()+1;
                            }
                        }
                        Map<Integer, Set<Object>> mapByOperation = (Map<Integer, Set<Object>>)getField(getField(p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE), "handle"), "c");
                        mapByOperation.get(0).remove(modifier);
                        mapByOperation.get(2).add(modifier);
                        setField(modifier, "a", 1.3*strengthTier);
                        setField(modifier, "b", 2);
                    }
                    if (ConfigManager.getBoolean("Melee.old-axe-damage") && invokeMethod(modifier, "b").equals("Tool modifier") && invokeMethod(modifier, "a").equals(UUID.fromString("cb3f55d3-645c-4f38-a497-9c13a33db5cf")) && p.getEquipment().getItemInMainHand() != null && ((p.getEquipment().getItemInMainHand().getType() == Material.WOOD_AXE && (Double)invokeMethod(modifier, "d") == 6) || (p.getEquipment().getItemInMainHand().getType() == Material.STONE_AXE && (Double)invokeMethod(modifier, "d") == 8) || (p.getEquipment().getItemInMainHand().getType() == Material.IRON_AXE && (Double)invokeMethod(modifier, "d") == 8) || (p.getEquipment().getItemInMainHand().getType() == Material.DIAMOND_AXE && (Double)invokeMethod(modifier, "d") == 8) || (p.getEquipment().getItemInMainHand().getType() == Material.GOLD_AXE && (Double)invokeMethod(modifier, "d") == 6))) {
                        p.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getModifiers().remove(modifier);
                        double amount = 0;
                        switch (p.getEquipment().getItemInMainHand().getType()) {
                            case WOOD_AXE:
                            case GOLD_AXE:
                                amount = 2;
                                break;
                            case STONE_AXE:
                                amount = 3;
                                break;
                            case IRON_AXE:
                                amount = 4;
                                break;
                            case DIAMOND_AXE:
                                amount = 5;
                                break;
                        }
                        setField(modifier, "a", amount);
                    }
                }
            } catch (ReflectiveOperationException ignored) {}
        }
    }

}