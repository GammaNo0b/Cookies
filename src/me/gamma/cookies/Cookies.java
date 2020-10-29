
package me.gamma.cookies;


import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.gamma.cookies.command.CookieCommand;
import me.gamma.cookies.listeners.CustomBlockListener;
import me.gamma.cookies.listeners.CustomCraftingListener;
import me.gamma.cookies.listeners.CustomItemListener;
import me.gamma.cookies.listeners.MultiBlockListener;
import me.gamma.cookies.listeners.PlayerArmorEquipEventListener;
import me.gamma.cookies.managers.RecipeManager;
import me.gamma.cookies.objects.block.BlockRegister;
import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.item.PlayerRegister;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.setup.CustomRecipeSetup;
import me.gamma.cookies.util.ConfigValues;



public class Cookies extends JavaPlugin {

	public static Cookies INSTANCE;

	@Override
	public void onEnable() {
		INSTANCE = this;

		saveDefaultConfig();
		ConfigValues.init(this);
		RecipeManager.loadRecipes();
		CustomRecipeSetup.registerCustomRecipes();
		PlayerRegister.preRegisterPlayers();
		BlockRegister.loadConfigs();
		BlockTicker.startTicking();

		this.registerEvents();

		CustomItemSetup.getCustomTasks().forEach((task, delay) -> Bukkit.getScheduler().scheduleSyncRepeatingTask(this, task, delay, delay));

		getCommand("cookie").setExecutor(new CookieCommand());
	}


	@Override
	public void onDisable() {
		BlockRegister.saveConfigs();
		ConfigValues.save(this);
	}


	private void registerEvents() {
		PluginManager pmanager = Bukkit.getPluginManager();
		pmanager.registerEvents(new PlayerArmorEquipEventListener(), this);
		pmanager.registerEvents(new CustomCraftingListener(), this);
		pmanager.registerEvents(new CustomItemListener(), this);
		pmanager.registerEvents(new CustomBlockListener(), this);
		pmanager.registerEvents(new MultiBlockListener(), this);
		CustomItemSetup.getCustomListeners().forEach(listener -> pmanager.registerEvents(listener, this));
		CustomBlockSetup.getCustomListeners().forEach(listener -> pmanager.registerEvents(listener, this));
	}

}
