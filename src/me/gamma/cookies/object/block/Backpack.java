
package me.gamma.cookies.object.block;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.object.WorldPersistentDataStorage;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.ItemStackProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.object.property.StringProperty;
import me.gamma.cookies.object.property.UUIDProperty;
import me.gamma.cookies.util.collection.Pair;
import me.gamma.cookies.util.collection.Triple;
import me.gamma.cookies.util.core.MinecraftPersistentDataHelper;
import me.gamma.cookies.util.math.IDGen;



public class Backpack extends AbstractCustomBlock implements Ownable {

	public static final Backpacks BACKPACKS = new Backpacks();
	public static final UUIDProperty uuid = Properties.UUID;

	private final String identifier;
	private final String title;
	private final String texture;
	private final int size;

	public Backpack(String identifier, String title, String texture, int size) {
		this.identifier = identifier;
		this.title = title;
		this.texture = texture;
		this.size = size;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	public String getTitle() {
		return this.title;
	}


	@Override
	public String getBlockTexture() {
		return this.texture;
	}


	public int getSize() {
		return this.size;
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(uuid);
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(player.isSneaking())
			return false;

		if(!this.canAccess(block, player))
			return true;

		player.playSound(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1.0F, 1.0F);
		player.openInventory(BACKPACKS.getInventory(uuid.fetch(block)));
		return true;
	}

	public static class Backpacks implements WorldPersistentDataStorage {

		private static final String KEY_BACKPACKS = "backpacks";

		private static final IntegerProperty SIZE = Properties.SIZE;
		private static final StringProperty TITLE = Properties.TITLE;

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


		public void upgradeInventory(UUID olduid, UUID newuid) {
			Inventory oldinv = this.getInventory(olduid);
			Inventory newinv = this.getInventory(newuid);
			for(int i = 0; i < oldinv.getSize(); i++)
				newinv.setItem(i, oldinv.getItem(i));
		}


		@Override
		public void load(World world, PersistentDataContainer container) {
			this.uuids.reset();
			List<PersistentDataContainer> containers = container.getOrDefault(new NamespacedKey(Cookies.INSTANCE, KEY_BACKPACKS), PersistentDataType.LIST.dataContainers(), new ArrayList<>());
			for(PersistentDataContainer c : containers) {
				Triple<UUID, Inventory, String> triple = this.loadInventory(c);
				this.backpacks.put(triple.left, new Pair<>(triple.middle, triple.right));
			}
		}


		@Override
		public void save(World world, PersistentDataContainer container) {
			List<PersistentDataContainer> containers = new ArrayList<>();
			for(Map.Entry<UUID, Pair<Inventory, String>> entry : this.backpacks.entrySet()) {
				PersistentDataContainer c = MinecraftPersistentDataHelper.createNewPersistentDataContainer(container);
				this.saveInventory(entry.getKey(), entry.getValue().left, entry.getValue().right, c);
				containers.add(c);
			}
			container.set(new NamespacedKey(Cookies.INSTANCE, KEY_BACKPACKS), PersistentDataType.LIST.dataContainers(), containers);
		}


		private ItemStackProperty[] createItemProperties(int size) {
			ItemStackProperty[] properties = new ItemStackProperty[size];
			for(int i = 0; i < size; i++)
				properties[i] = new ItemStackProperty(String.valueOf(i));
			return properties;
		}


		private Triple<UUID, Inventory, String> loadInventory(PersistentDataContainer container) {
			final UUID uid = uuid.fetch(container);
			final int size = SIZE.fetch(container);
			final String title = TITLE.fetch(container);
			Inventory inventory = Bukkit.createInventory(null, size, title);
			ItemStackProperty[] properties = this.createItemProperties(size);
			for(int i = 0; i < size; i++)
				inventory.setItem(i, properties[i].fetch(container));
			return new Triple<>(uid, inventory, title);
		}


		private void saveInventory(UUID uid, Inventory inventory, String title, PersistentDataContainer container) {
			uuid.store(container, uid);
			int size = inventory.getSize();
			SIZE.store(container, size);
			TITLE.store(container, title);
			ItemStackProperty[] properties = this.createItemProperties(size);
			for(int i = 0; i < size; i++)
				properties[i].store(container, inventory.getItem(i));
		}

	}

}
