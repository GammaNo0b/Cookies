
package me.gamma.cookies.feature;


import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import me.gamma.cookies.Cookies;



public interface CookieListener extends CookieFeature, Listener {

	@Override
	default void register(Cookies cookies) {
		Bukkit.getPluginManager().registerEvents(this, cookies);
	}

}
