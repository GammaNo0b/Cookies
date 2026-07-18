
package me.gamma.cookies;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import me.gamma.cookies.command.Commands;
import me.gamma.cookies.command.GodCommand;
import me.gamma.cookies.feature.ChairFeature;
import me.gamma.cookies.feature.ColorEntityFeature;
import me.gamma.cookies.feature.CookieFeatures;
import me.gamma.cookies.feature.DispenserBlockPlacerFeature;
import me.gamma.cookies.feature.DispenserBucketCauldronFeature;
import me.gamma.cookies.feature.DispenserMilkFeature;
import me.gamma.cookies.feature.EggBounceFeature;
import me.gamma.cookies.feature.FlameArrowIngniteFeature;
import me.gamma.cookies.feature.FlowerSpreadFeature;
import me.gamma.cookies.feature.KnockDoorFeature;
import me.gamma.cookies.feature.LookInBookshelfFeature;
import me.gamma.cookies.feature.SpawnerFix;
import me.gamma.cookies.feature.SuspiciousHidingFeature;
import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.init.BookInit;
import me.gamma.cookies.init.Config;
import me.gamma.cookies.init.Inventories;
import me.gamma.cookies.init.Items;
import me.gamma.cookies.init.MultiBlockInit;
import me.gamma.cookies.init.RecipeInit;
import me.gamma.cookies.init.Registries;
import me.gamma.cookies.listener.CustomBlockListener;
import me.gamma.cookies.listener.CustomCraftingListener;
import me.gamma.cookies.listener.CustomInventoryListener;
import me.gamma.cookies.listener.CustomItemListener;
import me.gamma.cookies.listener.MachineRecipeListener;
import me.gamma.cookies.listener.MultiBlockListener;
import me.gamma.cookies.listener.PlayerArmorEquipEventListener;
import me.gamma.cookies.listener.TeamQueueListener;
import me.gamma.cookies.listener.TutorialListener;
import me.gamma.cookies.manager.HologramManager;
import me.gamma.cookies.manager.WireManager;
import me.gamma.cookies.object.ChunkPersistentDataStorage;
import me.gamma.cookies.object.Configurable;
import me.gamma.cookies.object.Ticker;
import me.gamma.cookies.object.WorldPersistentDataStorage;
import me.gamma.cookies.object.block.CustomBlockStorage;
import me.gamma.cookies.object.block.RandomTicker;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.PlayerRegister;
import me.gamma.cookies.object.network.NetworkManager;
import me.gamma.cookies.object.team.Team;
import me.gamma.cookies.object.tile.TileEntityStorage;
import me.gamma.cookies.util.Debug;



public class Cookies extends JavaPlugin {

	public static final Logger LOGGER = LogUtils.getLogger();

	private static final List<Initializer> initializers = new ArrayList<>();
	static {
		initializers.add(new Initializer("config", Config::init));
		initializers.add(new Initializer("blocks", Blocks::init));
		initializers.add(new Initializer("items", Items::init));
		initializers.add(new Initializer("recipes", RecipeInit::init));
		initializers.add(new Initializer("blockconfig", () -> Registries.BLOCKS.forEach(Configurable::configure)));
		initializers.add(new Initializer("multiblocks", MultiBlockInit::init));
		initializers.add(new Initializer("books", BookInit::init));
		initializers.add(new Initializer("inventories", Inventories::init));
		initializers.add(new Initializer("playerregisters", PlayerRegister::init));
	}

	public static Cookies INSTANCE;

	public final HologramManager holograms = new HologramManager();
	public final CookieFeatures features = new CookieFeatures();

	private PluginManager pmanager;

	@Override
	public void onEnable() {
		INSTANCE = this;

		this.pmanager = Bukkit.getPluginManager();

		initializers.forEach(Initializer::init);

		ChunkPersistentDataStorage.STORAGES.register(CustomBlockStorage.BLOCK_STORAGE);
		ChunkPersistentDataStorage.STORAGES.register(TileEntityStorage.TILE_ENTITY_STORAGE);
		ChunkPersistentDataStorage.STORAGES.register(WireManager.WIRE_MANAGER);
		ChunkPersistentDataStorage.STORAGES.register(NetworkManager.NETWORK_MANAGER);

		Commands.registerCommands();
		this.registerEvents();
		this.registerFeatures();

		Team.TEAM_REGISTRY.register();

		RandomTicker.RANDOM_TICKER.registerTicker();
		WireManager.WIRE_MANAGER.registerTicker();
		NetworkManager.NETWORK_MANAGER.registerTicker();

		ChunkPersistentDataStorage.loadAllChunks();
		WorldPersistentDataStorage.loadAllWorlds();
		Ticker.startTicking();

		BiConsumer<AttributeInstance, Double> attrsizer = (attrinst, factor) -> { attrinst.setBaseValue(attrinst.getBaseValue() * factor); };

		BiConsumer<Player, Double> sizer = (player, factor) -> {
			attrsizer.accept(player.getAttribute(Attribute.ATTACK_SPEED), factor);
			attrsizer.accept(player.getAttribute(Attribute.MOVEMENT_SPEED), factor);
			attrsizer.accept(player.getAttribute(Attribute.SCALE), factor);
			attrsizer.accept(player.getAttribute(Attribute.STEP_HEIGHT), factor);
			attrsizer.accept(player.getAttribute(Attribute.JUMP_STRENGTH), factor);
			attrsizer.accept(player.getAttribute(Attribute.BLOCK_INTERACTION_RANGE), factor);
			attrsizer.accept(player.getAttribute(Attribute.ENTITY_INTERACTION_RANGE), factor);
			attrsizer.accept(player.getAttribute(Attribute.GRAVITY), factor);
			attrsizer.accept(player.getAttribute(Attribute.SAFE_FALL_DISTANCE), factor);
			attrsizer.accept(player.getAttribute(Attribute.FALL_DAMAGE_MULTIPLIER), 1.0D / factor);
		};

		Debug.addScript("grow", () -> {
			for(Player p : Bukkit.getOnlinePlayers())
				sizer.accept(p, 2.0D);
		});
		Debug.addScript("shrink", () -> {
			for(Player p : Bukkit.getOnlinePlayers())
				sizer.accept(p, 0.5D);
		});
	}


	@Override
	public void onDisable() {
		Ticker.stopTicking();

		ChunkPersistentDataStorage.saveAllChunks();
		WorldPersistentDataStorage.saveAllWorlds();
	}


	/**
	 * Registers the given listener for this plugin.
	 * 
	 * @param listener the listener
	 */
	public void registerEvent(Listener listener) {
		if(listener != null)
			this.pmanager.registerEvents(listener, this);
	}


	private void registerEvents() {
		this.registerEvent(new CustomBlockListener());
		this.registerEvent(new CustomCraftingListener());
		this.registerEvent(new CustomInventoryListener());
		this.registerEvent(new CustomItemListener());
		this.registerEvent(new GodCommand());
		this.registerEvent(new MachineRecipeListener());
		this.registerEvent(new MultiBlockListener());
		this.registerEvent(new PlayerArmorEquipEventListener());
		this.registerEvent(new TeamQueueListener());
		this.registerEvent(new TutorialListener());
		this.registerEvent(ChunkPersistentDataStorage.getChunkListener());
		this.registerEvent(WorldPersistentDataStorage.getWorldListener());

		Blocks.getCustomListeners().forEach(this::registerEvent);
		Registries.ITEMS.stream().filter(AbstractCustomItem::hasListener).forEach(item -> this.registerEvent(item.getListener()));
	}


	private void registerFeatures() {
		this.features.register(new ChairFeature());
		this.features.register(new ColorEntityFeature());
		this.features.register(new DispenserBlockPlacerFeature());
		this.features.register(new DispenserBucketCauldronFeature());
		this.features.register(new DispenserMilkFeature());
		this.features.register(new EggBounceFeature());
		this.features.register(new FlameArrowIngniteFeature());
		this.features.register(new FlowerSpreadFeature());
		this.features.register(new KnockDoorFeature());
		this.features.register(new LookInBookshelfFeature());
		this.features.register(new SpawnerFix());
		this.features.register(new SuspiciousHidingFeature());
	}


	/**
	 * Checks if a plugin with the given name is installed.
	 * 
	 * @param name the plugin name
	 * @return if a plugin with that name exists
	 */
	public static boolean isInstalled(String name) {
		return Bukkit.getPluginManager().getPlugin(name) != null;
	}


	/**
	 * Checks for the file under the given path in the data folder. If no file is found, the file is searched in the resources of the plugin jar and saved
	 * to the data folder.
	 * 
	 * @param path the path
	 * @return the (possible newly copied) file
	 */
	public File getResourceFile(String path) {
		File file = new File(this.getDataFolder(), path);
		if(!file.exists())
			this.saveResource(path, false);

		return file.exists() ? file : null;
	}

}
