
package me.gamma.cookies.feature;


import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;



public class DropsKeepMomentumFeature implements CookieListener {

	private boolean enabled = false;

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	@Override
	public boolean isEnabled() {
		return this.enabled;
	}


	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {

	}

}
