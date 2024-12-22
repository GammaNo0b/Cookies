
package me.gamma.cookies.feature;


import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;



public class OpenShulkerInInventoryFeature implements CookieListener {

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
	public void onShulkerClick(InventoryClickEvent event) {

	}

}
