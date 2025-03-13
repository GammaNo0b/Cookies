
package me.gamma.cookies.object.block.network;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.object.WorldPersistentDataStorage;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.BlockInventoryProvider;
import me.gamma.cookies.object.block.Ownable;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.MapUtils;
import me.gamma.cookies.util.core.MinecraftPersistentDataHelper;



public abstract class EnderLinkedBlock<T> extends AbstractCustomBlock implements Ownable, BlockInventoryProvider, WorldPersistentDataStorage {

	private static final String TAG_ENDER_RESOURCES = "enderresources";

	private static final Material[] colors = { Material.WHITE_STAINED_GLASS_PANE, Material.LIGHT_GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE, Material.BLACK_STAINED_GLASS_PANE, Material.BROWN_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS_PANE, Material.CYAN_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS_PANE };
	public static final char[] colorcodes = "f7806c6ea23b95dd".toCharArray();

	public static final IntegerProperty COLOR = new IntegerProperty("color");

	private final HashMap<UUID, HashMap<Integer, T>> linkedResources = new HashMap<>();

	public EnderLinkedBlock() {
		this.register();
	}


	/**
	 * Creates a new empty resource.
	 * 
	 * @return The new resource.
	 */
	protected abstract T newResource();


	protected T getResource(UUID owner, int color) {
		return MapUtils.getOrStoreDefault(MapUtils.getOrStoreDefault(this.linkedResources, owner, new HashMap<>()), color, this::newResource);
	}


	protected T getResource(PersistentDataHolder holder) {
		return this.getResource(OWNER.fetch(holder), COLOR.fetch(holder));
	}


	/**
	 * Loads the resource from the container.
	 * 
	 * @param resource  the resource
	 * @param container the container
	 */
	protected abstract void loadResource(T resource, PersistentDataContainer container);

	/**
	 * Saves the resource in the container.
	 * 
	 * @param resource  the resource
	 * @param container the container
	 * @return If the resource got saved successfully.
	 */
	protected abstract boolean saveResource(T resource, PersistentDataContainer container);


	@Override
	public void load(World world, PersistentDataContainer container) {
		List<PersistentDataContainer> containers = container.getOrDefault(new NamespacedKey(Cookies.INSTANCE, TAG_ENDER_RESOURCES), PersistentDataType.LIST.dataContainers(), new ArrayList<>());
		for(PersistentDataContainer c : containers) {
			UUID owner = OWNER.fetch(c);
			int color = COLOR.fetch(c);
			T resource = this.getResource(owner, color);
			this.loadResource(resource, container);
		}
	}


	@Override
	public void save(World world, PersistentDataContainer container) {
		List<PersistentDataContainer> containers = new ArrayList<>();
		for(UUID uuid : this.linkedResources.keySet()) {
			HashMap<Integer, T> m = this.linkedResources.get(uuid);
			for(Entry<Integer, T> entry : m.entrySet()) {
				T resource = entry.getValue();
				if(resource == null)
					continue;

				PersistentDataContainer c = MinecraftPersistentDataHelper.createNewPersistentDataContainer(container);
				if(!this.saveResource(resource, c))
					continue;

				OWNER.store(c, uuid);
				COLOR.store(c, entry.getKey());
				containers.add(c);
			}
		}
		container.set(new NamespacedKey(Cookies.INSTANCE, TAG_ENDER_RESOURCES), PersistentDataType.LIST.dataContainers(), containers);
	}


	@Override
	public boolean canPlace(Player player, Block block) {
		return player != null && super.canPlace(player, block);
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(COLOR);
	}


	/**
	 * Displays the resources stored under the current owner with the given color.
	 * 
	 * @param player   the player interacting with the ender linked block
	 * @param block    the interacted ender linked block
	 * @param resource the resource
	 */
	protected abstract void displayResources(Player player, TileState block, T resource);


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(ItemUtils.isType(stack, Material.ENDER_EYE)) {
			this.openGui(player, block);
		} else {
			this.displayResources(player, block, this.getResource(block));
		}
		return true;
	}


	@Override
	public String getTitle(TileState block) {
		return "§6Color Selector";
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public int rows() {
		return 0;
	}


	@Override
	public Sound getSound() {
		return Sound.BLOCK_ENDER_CHEST_OPEN;
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = Bukkit.createInventory(null, InventoryType.DROPPER, this.getTitle(block));
		InventoryUtils.fillInventory(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		this.updateColor(gui, COLOR.fetch(block));
		return gui;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState block, Inventory gui, InventoryClickEvent event) {
		int i = event.getSlot() - 3;
		if(0 <= i && i < 3) {
			int color = COLOR.fetch(block);
			int shift = i << 2;
			int mask = 0xF << shift;
			int c = (color & mask) >> shift;
			int d = 1;
			if(event.getClick().isShiftClick())
				d = 4;
			if(event.getClick().isRightClick())
				d = -d;
			c += d;
			color &= ~mask;
			color |= (c & 0xF) << shift;
			this.updateColor(gui, color);
			COLOR.store(block, color);
			block.update();
		}

		return true;
	}


	private void updateColor(Inventory inventory, int color) {
		for(int i = 0; i < 3; i++) {
			int c = (color >> (i << 2)) & 0xF;
			inventory.setItem(i + 3, new ItemBuilder(colors[c]).setName(String.format("§%c%d", colorcodes[c], c)).build());
		}
	}

}
