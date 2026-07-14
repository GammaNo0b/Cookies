
package me.gamma.cookies.object.tile;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.WorldPersistentDataStorage;
import me.gamma.cookies.object.block.BackpackBlock;
import me.gamma.cookies.object.block.Ownable;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.Pair;
import me.gamma.cookies.util.collection.PersistentDataObject;
import me.gamma.cookies.util.math.IDGen;



public class Backpack extends AbstractCustomTileEntity<Backpack, BackpackBlock> implements Ownable, BlockInventoryHolder {

	public static final String KEY_UUID = "uuid";

	public static final Backpacks BACKPACKS = new Backpacks();

	private UUID uuid = null;

	public Backpack(BackpackBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.uuid = PersistentDataUtils.getUUID(data, KEY_UUID);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		PersistentDataUtils.setUUID(data, KEY_UUID, this.uuid);

		return true;
	}


	@Override
	public UUID getOwner() {
		return this.uuid;
	}


	@Override
	public void setOwner(UUID uuid) {
		this.uuid = uuid;
	}


	@Override
	public Inventory getInventory() {
		return BACKPACKS.getInventory(this.uuid);
	}


	@Override
	public Backpack castTileEntity() {
		return this;
	}

	public static class Backpacks implements WorldPersistentDataStorage {

		private static final String KEY_BACKPACKS = "backpacks";
		private static final String KEY_UUID = "uuid";
		private static final String KEY_INVENTORY = "inventory";

		private final IDGen<UUID> uuids = IDGen.newIDGen(UUID::randomUUID);

		private final HashMap<UUID, Pair<Inventory, String>> backpacks = new HashMap<>();

		public Backpacks() {
			this.register();
		}


		@Override
		public String getIdentifier() {
			return "backpacks";
		}


		public Inventory getInventory(UUID backpack) {
			return this.backpacks.get(backpack).left;
		}


		public UUID createNewInventory(String title, int size) {
			UUID uuid = this.uuids.generate();
			Inventory inventory = Bukkit.createInventory(null, size, title);
			this.backpacks.put(uuid, new Pair<>(inventory, title));
			return uuid;
		}


		public void updateInventory(UUID olduid, UUID newuid) {
			Inventory oldinv = this.getInventory(olduid);
			Inventory newinv = this.getInventory(newuid);
			for(int i = 0; i < Math.min(oldinv.getSize(), newinv.getSize()); i++)
				newinv.setItem(i, oldinv.getItem(i));
		}


		@Override
		public void load(World world, PersistentDataObject object) {
			this.uuids.reset();

			List<PersistentDataObject> backpacks = object.getObjectList(KEY_BACKPACKS);
			if(backpacks == null)
				return;

			for(PersistentDataObject backpack : backpacks) {
				UUID uuid = PersistentDataUtils.getUUID(backpack, KEY_UUID);
				if(uuid == null)
					continue;

				Pair<Inventory, String> inventory = PersistentDataUtils.getInventory(object, KEY_INVENTORY);
				if(inventory == null)
					continue;

				this.uuids.register(uuid);
				this.backpacks.put(uuid, inventory);
			}
		}


		@Override
		public void save(World world, PersistentDataObject object) {
			List<PersistentDataObject> backpacks = new ArrayList<>();
			for(Map.Entry<UUID, Pair<Inventory, String>> entry : this.backpacks.entrySet()) {
				PersistentDataObject backpack = new PersistentDataObject(object.getAdapterContext());
				PersistentDataUtils.setUUID(backpack, KEY_UUID, entry.getKey());
				Pair<Inventory, String> inventory = entry.getValue();
				PersistentDataUtils.setInventory(backpack, KEY_INVENTORY, inventory.left, inventory.right);
			}
			object.setObjectList(KEY_BACKPACKS, backpacks);
		}

	}

}
