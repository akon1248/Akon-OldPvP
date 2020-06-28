package com.akon.oldpvp.listener;

import com.akon.oldpvp.utils.ConfigManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Team;

public class JoinQuitListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent e) {
        if (ConfigManager.getBoolean("Combat.disable-attack-speed")) {
            e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(30);
        }
        if (ConfigManager.getBoolean("Utilities.disable-collision")) {
            if (e.getPlayer().getScoreboard().getTeam("collision") == null) {
                Team team = e.getPlayer().getScoreboard().registerNewTeam("collision");
                team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            }
            if (!e.getPlayer().getScoreboard().getTeam("collision").hasEntry(e.getPlayer().getName())) {
                e.getPlayer().getScoreboard().getTeam("collision").addEntry(e.getPlayer().getName());
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (ConfigManager.getBoolean("Combat.disable-attack-speed")) {
            e.getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
        }
        if (InteractListener.BLOCKING_TASK.get(e.getPlayer()) != null) {
            InteractListener.BLOCKING_TASK.get(e.getPlayer()).cancel();
        }
    }

}
