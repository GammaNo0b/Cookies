package me.gamma.cookies.util;

import java.lang.reflect.Field;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigValues {
	
	public static int BLOCK_BREAKER_DELAY;
	public static int COBBLESTONE_GENERATOR_DELAY;
	public static int EXPERIENCE_ABSORBER_DELAY;
	public static int EXPERIENCE_ABSORBER_RANGE;
	public static int FARMER_DELAY;
	public static int FARMER_RANGE;
	public static int FARMER_HEIGHT;
	public static int ITEM_ABSORBER_DELAY;
	public static int ITEM_ABSORBER_RANGE;
	public static int QUARRY_DELAY;
	public static int QUARRY_RANGE;
	public static int MAX_STORAGE_CONNECTORS;
	public static String MINECRAFTY_TEXTURE_FILE_PATH;
	
	public static void init(JavaPlugin plugin) {
		final FileConfiguration config = plugin.getConfig();
		for(Field field : ConfigValues.class.getFields()) {
			try {
				field.set(null, config.get(field.getName().toLowerCase().replace('_', '-')));
			} catch(IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void save(JavaPlugin plugin) {
		plugin.saveConfig();
	}

}
