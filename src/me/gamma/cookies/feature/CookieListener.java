
package me.gamma.cookies.feature;


import org.bukkit.event.Listener;

import me.gamma.cookies.Cookies;



public interface CookieListener extends CookieFeature, Listener {

	@Override
	default void register() {
		Cookies.INSTANCE.registerEvent(this);
	}

}
