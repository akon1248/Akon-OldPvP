package com.akon.oldpvp.listener;

import com.akon.oldpvp.PotionDurationMapping;
import com.akon.oldpvp.utils.ConfigManager;
import org.bukkit.entity.SplashPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class SplashPotionListener implements Listener {

	@EventHandler
	public void onShoot(ProjectileLaunchEvent e) {
		if (e.getEntity() instanceof SplashPotion && ConfigManager.getBoolean("Potion.old-potion-duration")) {
			((SplashPotion)e.getEntity()).setItem(PotionDurationMapping.convertOldPotionDuration(((SplashPotion)e.getEntity()).getItem()));
		}
	}

}
