
package me.gamma.cookies;


import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.gamma.cookies.command.CookieCommand;
import me.gamma.cookies.feature.CookieFeature;
import me.gamma.cookies.feature.DispenserBlockPlacerFeature;
import me.gamma.cookies.feature.EditSignFeature;
import me.gamma.cookies.listeners.CustomBlockListener;
import me.gamma.cookies.listeners.CustomCraftingListener;
import me.gamma.cookies.listeners.CustomItemListener;
import me.gamma.cookies.listeners.MultiBlockListener;
import me.gamma.cookies.listeners.PlayerArmorEquipEventListener;
import me.gamma.cookies.listeners.TeamQueueListener;
import me.gamma.cookies.managers.InventoryManager;
import me.gamma.cookies.managers.RecipeManager;
import me.gamma.cookies.objects.block.BlockTicker;
import me.gamma.cookies.objects.item.PlayerRegister;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.setup.CustomRecipeSetup;
import me.gamma.cookies.team.Team;
import me.gamma.cookies.util.ConfigValues;
import me.gamma.cookies.util.INBTRegistry;



public class Cookies extends JavaPlugin {

	public static Cookies INSTANCE;

	private PluginManager pluginManager;

	@Override
	public void onEnable() {
		INSTANCE = this;

		saveDefaultConfig();
		ConfigValues.init(this);
		CustomRecipeSetup.registerCustomRecipes();
		CustomBlockSetup.initializeMachineRecipes();
		RecipeManager.loadRecipes();
		PlayerRegister.preRegisterPlayers();
		Team.TEAM_REGISTRY.register();
		INBTRegistry.loadRegistries();
		BlockTicker.startTicking();

		this.registerEvents();
		this.registerFeatures();

		CustomItemSetup.getCustomTasks().forEach((task, delay) -> Bukkit.getScheduler().scheduleSyncRepeatingTask(this, task, delay, delay));

		getCommand("cookie").setExecutor(new CookieCommand());
	}


	@Override
	public void onDisable() {
		INBTRegistry.saveRegistries();
		ConfigValues.save(this);
	}


	public void registerEvent(Listener listener) {
		this.pluginManager.registerEvents(listener, this);
	}


	private void registerEvents() {
		this.pluginManager = Bukkit.getPluginManager();

		this.registerEvent(new PlayerArmorEquipEventListener());
		this.registerEvent(new CustomCraftingListener());
		this.registerEvent(new CustomItemListener());
		this.registerEvent(new CustomBlockListener());
		this.registerEvent(new MultiBlockListener());
		this.registerEvent(new TeamQueueListener());

		this.registerEvent(InventoryManager.getInventoryListener());

		CustomItemSetup.getCustomListeners().forEach(this::registerEvent);
		CustomBlockSetup.getCustomListeners().forEach(this::registerEvent);
	}


	public void registerFeature(CookieFeature feature) {
		feature.register(this);
	}


	private void registerFeatures() {
		this.registerFeature(new DispenserBlockPlacerFeature());
		this.registerFeature(new EditSignFeature());
	}


	public static boolean isInstalled(String name) {
		return Bukkit.getPluginManager().getPlugin(name) != null;
	}

}
