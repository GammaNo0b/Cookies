
package me.gamma.cookies.init;


import static me.gamma.cookies.init.Registries.CONFIGS;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.gamma.cookies.Cookies;



public class Config {

	public static final Config CONFIG = CONFIGS.register(new Config("config.yml"));
	public static final Config BLOCKS = CONFIGS.register(new Config("blocks.yml"));
	public static final Config GENERATORS = CONFIGS.register(new Config("generators.yml"));
	public static final Config MACHINES = CONFIGS.register(new Config("machines.yml"));
	public static final Config ITEMS = CONFIGS.register(new Config("items.yml"));

	public static void init() {
		for(Config config : CONFIGS) {
			config.init(Cookies.INSTANCE);
			config.saveDefaultConfig();
			config.reloadConfig();
		}
	}

	private final String name;

	private JavaPlugin plugin;
	private File configFile;
	private FileConfiguration config;

	public Config(String name) {
		this.name = name;
	}


	public void init(JavaPlugin plugin) {
		this.plugin = plugin;

		this.configFile = new File(plugin.getDataFolder(), this.name);
	}


	public FileConfiguration getConfig() {
		if(this.config == null) {
			this.reloadConfig();
		}

		return this.config;
	}


	public void reloadConfig() {
		this.config = YamlConfiguration.loadConfiguration(this.configFile);
	}


	public void saveConfig() {
		try {
			this.getConfig().save(this.configFile);
		} catch(IOException e) {
			this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
		}
	}


	public void saveDefaultConfig() {
		if(!this.configFile.exists()) {
			this.plugin.saveResource(this.name, false);
		}
	}

}
