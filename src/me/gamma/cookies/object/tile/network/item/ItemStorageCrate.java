
package me.gamma.cookies.object.tile.network.item;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.BlockInventoryProvider;
import me.gamma.cookies.object.block.Ownable;
import me.gamma.cookies.object.block.network.item.ItemStorageCrateBlock;
import me.gamma.cookies.object.item.BigItemStack;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemStorage;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.object.tile.ContainerTile;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.Utils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class ItemStorageCrate extends AbstractCustomTileEntity<ItemStorageCrate, ItemStorageCrateBlock> implements ItemStorage, Ownable, ContainerTile {

	public static final String KEY_OWNER = "owner";
	public static final String KEY_PAGE = "page";
	public static final String KEY_CONTENTS = "contents";

	public static final int PAGE_SIZE = 4 * 7;
	public static final ItemStack EMPTY_SLOT = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);

	private UUID owner = null;
	private int page = 0;
	private final BigItemStack[] contents;

	public ItemStorageCrate(ItemStorageCrateBlock customBlock, Block block) {
		super(customBlock, block);

		int capacity = customBlock.getCapacity();

		this.contents = new BigItemStack[capacity];
		for(int i = 0; i < this.contents.length; i++)
			this.contents[i] = new BigItemStack(Material.AIR, 0, customBlock.getMaxStackSize());
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.owner = PersistentDataUtils.getUUID(data, KEY_OWNER);
		this.page = data.getInteger(KEY_PAGE, 0);

		List<PersistentDataObject> array = data.getObjectList(KEY_CONTENTS);
		if(array != null)
			for(int i = 0; i < Math.min(this.contents.length, array.size()); ++i)
				PersistentDataUtils.loadBigItemStack(array.get(i), this.contents[i]);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		PersistentDataUtils.setUUID(data, KEY_OWNER, this.owner);
		data.setInteger(KEY_PAGE, this.page);

		List<PersistentDataObject> array = new ArrayList<>(this.contents.length);
		for(int i = 0; i < this.contents.length; i++) {
			PersistentDataObject o = new PersistentDataObject(data.getAdapterContext());
			PersistentDataUtils.saveBigItemStack(o, this.contents[i]);
			array.add(o);
		}
		data.setObjectList(KEY_CONTENTS, array);

		return true;
	}


	@Override
	public void setupInventory(Inventory inventory) {
		this.updateInventory(inventory, null);
	}


	@Override
	public boolean canAccessItemInputs(BlockFace face) {
		return true;
	}


	@Override
	public byte getItemInputAccessFlags() {
		return 0x3F;
	}


	@Override
	public void setItemInputAccessFlags(byte flags) {}


	@Override
	public boolean canAccessItemOutputs(BlockFace face) {
		return true;
	}


	@Override
	public byte getItemOutputAccessFlags() {
		return 0x3F;
	}


	@Override
	public void setItemOutputAccessFlags(byte flags) {}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		int slot = event.getSlot();
		if(slot == 4) {
			player.closeInventory();
			return true;
		} else if(slot == 47) {
			int pages = this.getItemProviders().size() / PAGE_SIZE;
			if(pages > 0) {
				if(--this.page < 0)
					this.page = pages - 1;
				this.updateInventory(gui, null);
			}
			return true;
		} else if(slot == 51) {
			int pages = this.getItemProviders().size() / PAGE_SIZE;
			if(pages > 0) {
				if(++this.page >= pages)
					this.page = 0;
				this.updateInventory(gui, null);
			}
			return true;
		}

		int row = slot / 9 - 1;
		int column = slot - row * 9 - 10;
		if(row < 0 || 4 <= row || column < 0 || 7 <= column)
			return true;

		int index = row * 7 + column;
		if(index < 0 || index >= this.contents.length)
			return true;

		ClickType click = event.getClick();
		boolean removeAll = click.isLeftClick();
		if(click == ClickType.MIDDLE) {
			BigItemStack stack = this.contents[index];
			stack.toggleLocked();
			if(stack.isEmpty() && !stack.getType().getType().isAir())
				stack.setType(new ItemStack(Material.AIR));
		} else if(removeAll || click.isRightClick()) {
			ItemUtils.giveItemToPlayer(player, this.removeStack(index, removeAll));
		} else {
			return true;
		}
		this.updateSlot(index, slot);
		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player player, PlayerInventory gui, InventoryClickEvent event) {
		ItemStack stack = event.getCurrentItem();

		if(ItemUtils.isEmpty(stack))
			return true;

		ClickType click = event.getClick();
		if(click.isLeftClick()) {
			gui.setItem(event.getSlot(), this.addStack(stack));
		} else if(click.isRightClick()) {
			gui.setItem(event.getSlot(), this.addStack(stack, 1));
		} else {
			return true;
		}

		this.updateInventory(event.getInventory(), stack);
		return true;
	}


	private ItemStack createIcon(Provider<ItemStack> provider, boolean locked) {
		ItemStack type = provider.getType();
		ItemMeta meta;
		String name = "§fEmpty";
		int amount = 0;
		if(provider.getType().getType().isAir() || provider.isEmpty() && !locked) {
			type = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
			meta = type.getItemMeta();
		} else {
			meta = type.getItemMeta();
			name = meta.hasDisplayName() ? meta.getDisplayName() : "§f" + Utils.toCapitalWords(type.getType());
			amount = provider.amount();
		}
		if(locked)
			name += " §8🔒";
		meta.setDisplayName(name);
		List<String> lore = meta.getLore();
		if(lore == null)
			lore = new ArrayList<>();
		lore.add(0, "");
		lore.add(1, "§7Amount: §b" + amount);
		lore.add(2, "§7Stacks: §b" + (amount / type.getMaxStackSize()) + " §8/ §3" + this.customBlock.getMaxStackSize());
		meta.setLore(lore);
		type.setItemMeta(meta);
		return type;
	}


	private void updateSlot(int index, int slot) {
		Inventory inventory = this.getInventory();
		index += this.page * PAGE_SIZE;
		List<Provider<ItemStack>> providers = this.getItemProviders();
		if(index >= providers.size())
			return;

		inventory.setItem(slot, this.createIcon(providers.get(index), this.contents[index].isLocked()));
	}


	private ItemStack removeStack(int index, boolean removeStack) {
		index += this.page * PAGE_SIZE;
		List<Provider<ItemStack>> providers = this.getItemProviders();
		if(index >= providers.size())
			return null;

		Provider<ItemStack> provider = providers.get(index);
		return removeStack ? ItemProvider.get(provider) : ItemProvider.get(provider, 1);
	}


	@Override
	public UUID getOwner() {
		return this.owner;
	}


	@Override
	public void setOwner(UUID uuid) {
		this.owner = uuid;
	}


	@Override
	public List<Provider<ItemStack>> getItemProviders() {
		return Arrays.stream(this.contents).<Provider<ItemStack>>map(ItemProvider::fromBigItemStack).toList();
	}


	@Override
	public boolean isTicking() {
		return true;
	}


	@Override
	public void tick() {
		super.tick();

		this.updateInventory(this.getInventory(), null);
	}


	private void updateInventory(Inventory inventory, ItemStack type) {
		int index = this.page * PAGE_SIZE;
		List<Provider<ItemStack>> providers = this.getItemProviders();
		for(int r = 1; r <= 4 && index < providers.size(); r++) {
			for(int c = 1; c <= 7 && index < providers.size(); c++, index++) {
				Provider<ItemStack> provider = providers.get(index);
				if(type != null && !provider.match(type))
					continue;

				inventory.setItem(r * 9 + c, this.createIcon(provider, this.contents[index].isLocked()));
			}
		}
	}


	@Override
	public ItemStorageCrate castTileEntity() {
		return this;
	}


	@Override
	public BlockInventoryProvider getInventoryProvider() {
		return this.customBlock;
	}

}
