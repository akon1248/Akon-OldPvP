package com.akon.oldpvp.listeners.packet;

import com.akon.oldpvp.utils.ConfigManager;
import com.akon.oldpvp.utils.ReflectionUtil;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Sound;
import org.bukkit.plugin.Plugin;

public class SoundListener extends PacketAdapter {

    public SoundListener(Plugin plugin, ListenerPriority listenerPriority) {
        super(plugin, listenerPriority, PacketType.Play.Server.NAMED_SOUND_EFFECT);
    }

    @Override
    public void onPacketSending(PacketEvent e) {
        if (e.getPacketType() == PacketType.Play.Server.NAMED_SOUND_EFFECT) {
            boolean flag1 = false;
            boolean flag2 = false;
            for (StackTraceElement ste: Thread.currentThread().getStackTrace()) {
                if (ste.getClassName().equals("net.minecraft.server." + ReflectionUtil.getVersion() + ".EntityHuman") && ste.getMethodName().equals("damageEntity")) {
                    flag1 = true;
                    break;
                } else if (ste.getClassName().equals("net.minecraft.server." + ReflectionUtil.getVersion() + ".EntityHuman") && ste.getMethodName().equals("attack")) {
                    flag2 = true;
                    break;
                }
            }
            if (flag1 && ConfigManager.getBoolean("Utilities.custom-player-damage-sound.enabled") && (e.getPacket().getSoundEffects().read(0) == Sound.ENTITY_PLAYER_HURT || e.getPacket().getSoundEffects().read(0) == Sound.ENTITY_PLAYER_HURT_DROWN || e.getPacket().getSoundEffects().read(0) == Sound.ENTITY_PLAYER_HURT_ON_FIRE)) {
                Sound sound = null;
                try {
                    sound = Sound.valueOf(ConfigManager.getString("Utilities.custom-player-damage-sound.sound"));
                } catch (IllegalArgumentException | NullPointerException ignored) {}
                if (sound != null) {
                    e.getPacket().getSoundEffects().write(0, sound);
                }
            } else if (flag2 && ConfigManager.getBoolean("Melee.disable-sweep-attack") && e.getPacket().getSoundEffects().read(0) == Sound.ENTITY_PLAYER_ATTACK_SWEEP) {
                e.setCancelled(true);
            } else if (flag2 && !ConfigManager.getBoolean("Melee.AttackSounds.crit") && e.getPacket().getSoundEffects().read(0) == Sound.ENTITY_PLAYER_ATTACK_CRIT) {
                e.setCancelled(true);
            } else if (flag2 && !ConfigManager.getBoolean("Melee.AttackSounds.knockback") && e.getPacket().getSoundEffects().read(0) == Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK) {
                e.setCancelled(true);
            } else if (flag2 && !ConfigManager.getBoolean("Melee.AttackSounds.nodamage") && e.getPacket().getSoundEffects().read(0) == Sound.ENTITY_PLAYER_ATTACK_NODAMAGE) {
                e.setCancelled(true);
            } else if (flag2 && !ConfigManager.getBoolean("Melee.AttackSounds.strong") && e.getPacket().getSoundEffects().read(0) == Sound.ENTITY_PLAYER_ATTACK_STRONG) {
                e.setCancelled(true);
            } else if (flag2 && !ConfigManager.getBoolean("Melee.AttackSounds.weak") && e.getPacket().getSoundEffects().read(0) == Sound.ENTITY_PLAYER_ATTACK_WEAK) {
                e.setCancelled(true);
            }
        }
    }

}
