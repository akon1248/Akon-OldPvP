package com.akon.oldpvp;

import com.akon.oldpvp.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static com.akon.oldpvp.utils.ReflectionUtil.*;

public class OldPvPRunnable extends BukkitRunnable {

    @Override
    public void run() {
        for (World world: Bukkit.getWorlds()) {
            for (Player player: world.getEntitiesByClass(Player.class)) {
                try {
                    if (ConfigManager.getBoolean("Melee.disable-attack-speed") && (Integer)getField(getHandle(player), "aE") < 0) {
                        setField(getHandle(player), "aE", 1);
                    }
                } catch (ReflectiveOperationException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}