package com.akon.oldpvp.listener.packet;

import com.akon.oldpvp.OldPvP;
import com.akon.oldpvp.utils.ConfigManager;
import com.akon.oldpvp.utils.ReflectionUtil;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class WindowItemsListener extends PacketAdapter {

    public WindowItemsListener() {
        super(OldPvP.getInstance(), PacketType.Play.Server.WINDOW_ITEMS);
    }

    @Override
    public void onPacketSending(PacketEvent e) {
        if (e.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS && ConfigManager.getBoolean("Combat.old-hit-animation")) {
            for (StackTraceElement ste: Thread.currentThread().getStackTrace()) {
                if (ste.getClassName().equals("net.minecraft.server." + ReflectionUtil.getVersion() + ".EntityHuman") && ste.getMethodName().equals("attack")) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

}
