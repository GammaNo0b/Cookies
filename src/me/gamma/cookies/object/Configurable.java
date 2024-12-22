
package me.gamma.cookies.object;


import org.bukkit.configuration.ConfigurationSection;

import me.gamma.cookies.init.Config;



public interface Configurable {

	default ConfigurationSection getConfig() {
		return Config.CONFIG.getConfig();
	}


	default void configure() {
		this.configure(this.getConfig());
	}


	default void configure(ConfigurationSection config) {}

}
