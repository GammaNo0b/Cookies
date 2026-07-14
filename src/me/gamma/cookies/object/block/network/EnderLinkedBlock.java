
package me.gamma.cookies.object.block.network;


import static me.gamma.cookies.object.tile.network.EnderLinkedTileEntity.KEY_COLOR;
import static me.gamma.cookies.object.tile.network.EnderLinkedTileEntity.KEY_OWNER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.WorldPersistentDataStorage;
import me.gamma.cookies.object.block.AbstractCustomTileBlock;
import me.gamma.cookies.object.block.BlockInventoryProvider;
import me.gamma.cookies.object.tile.ContainerTile;
import me.gamma.cookies.object.tile.network.EnderLinkedTileEntity;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.MapUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class EnderLinkedBlock<T> extends AbstractCustomTileBlock<EnderLinkedBlock<T>, EnderLinkedTileEntity<T>> implements WorldPersistentDataStorage, BlockInventoryProvider {

	private static final String KEY_RESOURCES = "resources";

	private final HashMap<UUID, HashMap<Integer, T>> linkedResources = new HashMap<>();

	/**
	 * Creates a new empty resource.
	 * 
	 * @return The new resource.
	 */
	protected abstract T newResource();


	public T getResource(UUID owner, int color) {
		return MapUtils.getOrStoreDefault(MapUtils.getOrStoreDefault(this.linkedResources, owner, new HashMap<>()), color, this::newResource);
	}


	/**
	 * Loads the resource from the given data.
	 * 
	 * @param resource the resource
	 * @param data     the data
	 * @return if the resource get loaded successfully
	 */
	protected abstract boolean loadResource(T resource, PersistentDataObject data);

	/**
	 * Saves the resource to the given data.
	 * 
	 * @param resource the resource
	 * @param data     the data
	 * @return if the resource got saved successfully
	 */
	protected abstract boolean saveResource(T resource, PersistentDataObject data);

	/**
	 * Displays the resources stored under the current owner with the given color.
	 * 
	 * @param player   the player interacting with the ender linked block
	 * @param resource the resource
	 */
	protected abstract void displayResources(Player player, T resource);


	@Override
	protected void transferCustomData(PersistentDataObject tileData, PersistentDataObject itemData) {
		super.transferCustomData(tileData, itemData);

		itemData.setInteger(KEY_COLOR, tileData.getInteger(KEY_COLOR, 0));
	}


	@Override
	public void load(World world, PersistentDataObject object) {
		List<PersistentDataObject> resources = object.getObjectList(KEY_RESOURCES);
		for(PersistentDataObject o : resources) {
			UUID owner = PersistentDataUtils.getUUID(o, KEY_OWNER);
			if(owner == null)
				continue;

			Integer color = o.getInteger(KEY_COLOR);
			if(color == null)
				continue;

			T resource = this.getResource(owner, color);
			this.loadResource(resource, o);
		}
	}


	@Override
	public void save(World world, PersistentDataObject object) {
		List<PersistentDataObject> resources = new ArrayList<>();
		for(UUID uuid : this.linkedResources.keySet()) {
			HashMap<Integer, T> m = this.linkedResources.get(uuid);
			for(Entry<Integer, T> entry : m.entrySet()) {
				T resource = entry.getValue();
				if(resource == null)
					continue;

				PersistentDataObject o = new PersistentDataObject(object.getAdapterContext());
				if(!this.saveResource(resource, o))
					continue;

				PersistentDataUtils.setUUID(o, KEY_OWNER, uuid);
				o.setInteger(KEY_COLOR, entry.getKey());
				resources.add(o);
			}
		}
		object.setObjectList(KEY_RESOURCES, resources);
	}


	@Override
	public boolean canPlace(Player player, Block block) {
		return player != null && super.canPlace(player, block);
	}


	@Override
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, block, stack, event))
			return false;

		if(ItemUtils.isType(stack, Material.ENDER_EYE)) {
			this.openGui(player, block);
		} else {
			EnderLinkedTileEntity<T> tileEntity = this.getTileEntity(block);
			if(tileEntity == null)
				return true;

			this.displayResources(player, tileEntity.getResource());
		}
		return true;
	}


	@Override
	public String getTitle(Block data) {
		return "§6Color Selector";
	}


	@Override
	public int rows() {
		return 0;
	}


	@Override
	public String getIdentifier() {
		return this.getIdentifier() + "_color_selector";
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public Sound getSound() {
		return Sound.BLOCK_ENDER_CHEST_OPEN;
	}


	@Override
	public Inventory createGui(Block data) {
		Inventory gui = Bukkit.createInventory(null, InventoryType.DROPPER, this.getTitle(data));
		InventoryUtils.fillInventory(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));

		EnderLinkedTileEntity<T> tileEntity = this.getTileEntity(data);
		if(tileEntity != null)
			tileEntity.updateColor(gui);

		return gui;
	}


	@Override
	public ContainerTile getContainer(Block block) {
		return this.getTileEntity(block);
	}

}
