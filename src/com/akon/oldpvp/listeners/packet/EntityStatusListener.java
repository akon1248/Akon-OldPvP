package com.akon.oldpvp.listeners.packet;

import com.akon.oldpvp.utils.ConfigManager;
import com.akon.oldpvp.utils.ReflectionUtil;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.plugin.Plugin;

import java.util.Random;

public class EntityStatusListener extends PacketAdapter {

    public EntityStatusListener(Plugin plugin, ListenerPriority listenerPriority) {
        super(plugin, listenerPriority, PacketType.Play.Server.ENTITY_STATUS);
    }

    @Override
    public void onPacketSending(PacketEvent e) {
        if (e.getPacketType() == PacketType.Play.Server.ENTITY_STATUS) {
            boolean flag = false;
            for (StackTraceElement ste: Thread.currentThread().getStackTrace()) {
                if (ste.getClassName().equals("net.minecraft.server." + ReflectionUtil.getVersion() + ".EntityHuman") && ste.getMethodName().equals("damageEntity")) {
                    flag = true;
                    break;
                }
            }
            if (flag && ConfigManager.getBoolean("Utilities.custom-player-damage-sound.enabled") && e.getPacket().getEntityModifier(e.getPlayer().getWorld()).read(0) == e.getPlayer() && (e.getPacket().getBytes().read(0) == 2 || e.getPacket().getBytes().read(0) == 36 || e.getPacket().getBytes().read(0) == 37)) {
                Sound sound = null;
                try {
                    sound = Sound.valueOf(ConfigManager.getString("Utilities.custom-player-damage-sound.sound"));
                } catch (IllegalArgumentException | NullPointerException ignored) {}
                if (sound != null) {
                    e.setCancelled(true);
                    Random random = new Random();
                    e.getPlayer().playSound(e.getPlayer().getLocation(), sound, SoundCategory.PLAYERS, 1, (random.nextFloat()-random.nextFloat())*0.2F+1);
                }
            }
        }
    }

}
