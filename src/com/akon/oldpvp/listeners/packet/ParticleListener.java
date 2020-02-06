package com.akon.oldpvp.listeners.packet;

import com.akon.oldpvp.utils.ConfigManager;
import com.akon.oldpvp.utils.ReflectionUtil;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.plugin.Plugin;

public class ParticleListener extends PacketAdapter {

    public ParticleListener(Plugin plugin, ListenerPriority listenerPriority) {
        super(plugin, listenerPriority, PacketType.Play.Server.WORLD_PARTICLES);
    }

    @Override
    public void onPacketSending(PacketEvent e) {
        if (e.getPacketType() == PacketType.Play.Server.WORLD_PARTICLES) {
            boolean flag = false;
            for (StackTraceElement ste: Thread.currentThread().getStackTrace()) {
                if (ste.getClassName().equals("net.minecraft.server." + ReflectionUtil.getVersion() + ".EntityHuman") && ste.getMethodName().equals("attack")) {
                    flag = true;
                    break;
                }
            }
            if (flag && ConfigManager.getBoolean("Melee.disable-sweep-attack") && e.getPacket().getParticles().read(0) == EnumWrappers.Particle.SWEEP_ATTACK) {
                e.setCancelled(true);
            } else if (flag && ConfigManager.getBoolean("Melee.disable-damage-indicator-particle") && e.getPacket().getParticles().read(0) == EnumWrappers.Particle.DAMAGE_INDICATOR) {
                e.setCancelled(true);
            }
        }
    }

}
