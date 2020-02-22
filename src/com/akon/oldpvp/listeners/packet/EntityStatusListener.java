package com.akon.oldpvp.listeners.packet;

import com.akon.oldpvp.utils.ConfigManager;
import com.akon.oldpvp.utils.ReflectionUtil;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
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
                PacketContainer updateHealth1 = new PacketContainer(PacketType.Play.Server.UPDATE_HEALTH);
                updateHealth1.getFloat().write(0, (float)e.getPlayer().getHealth()-0.000001F);
                updateHealth1.getIntegers().write(0, e.getPlayer().getFoodLevel());
                updateHealth1.getFloat().write(1, e.getPlayer().getSaturation());
                PacketContainer updateHealth2 = new PacketContainer(PacketType.Play.Server.UPDATE_HEALTH);
                updateHealth2.getFloat().write(0, (float)e.getPlayer().getHealth());
                updateHealth2.getIntegers().write(0, e.getPlayer().getFoodLevel());
                updateHealth2.getFloat().write(1, e.getPlayer().getSaturation());
                try {
                    ProtocolLibrary.getProtocolManager().sendServerPacket(e.getPlayer(), updateHealth1);
                    ProtocolLibrary.getProtocolManager().sendServerPacket(e.getPlayer(), updateHealth2);
                } catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                }
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
            if (flag && e.getPacket().getEntityModifier(e.getPlayer().getWorld()).read(0) == e.getPlayer() && ((ConfigManager.getBoolean("Melee.disable-shield-block-sound") && e.getPacket().getBytes().read(0) == 29) || (ConfigManager.getBoolean("Melee.disable-shield-break-sound") && e.getPacket().getBytes().read(0) == 30))) {
                e.setCancelled(true);
            }
        }
    }

}
