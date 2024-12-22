
package me.gamma.cookies.object.gui.book;


import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.network.item.StorageCrateBlock;
import me.gamma.cookies.object.gui.book.StorageBlockBook.StorageInformation;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.util.CollectionUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.Utils;



@Deprecated
public class StorageBlockBook implements Book<StorageInformation> {

	private static final String WORLD_KEY = "world";
	private static final String BLOCK_KEY = "block";

	@Override
	public String getIdentifier() {
		return "storage_block_book";
	}


	@Override
	public String getTitle(int page, StorageInformation data) {
		return "Storage Skull Block";
	}


	@Override
	public int pages(StorageInformation data) {
		return (data.getProviders().size() + 27) / 28;
	}


	@Override
	public int rows() {
		return 6;
	}


	@Override
	public ItemStack getTurnLeftIcon() {
		return new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§8<---").build();
	}


	@Override
	public ItemStack getTurnRightIcon() {
		return new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§8--->").build();
	}


	@Override
	public ItemStack getCloseIcon() {
		return new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("§8Close").build();
	}


	@Override
	public ItemStack getItemFiller() {
		return InventoryUtils.filler(Material.ORANGE_STAINED_GLASS_PANE);
	}


	@Override
	public Inventory createGui(BookData<StorageInformation> data) {
		Inventory gui = Book.super.createGui(data);
		ItemStack filler = InventoryUtils.filler(Material.BROWN_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(i + 45, filler);
		}
		for(int i = 1; i <= 4; i++) {
			gui.setItem(i * 9, filler);
			gui.setItem(i * 9 + 8, filler);
		}
		return gui;
	}


	@Override
	public int[] getItemSlots() {
		return BASIC_FOUR_ROW_SLOT_LAYOUT;
	}


	@Override
	public ItemStack[] getItems(int page, Inventory inventory, int cycle, StorageInformation data) {
		final ItemStack filler = InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		return CollectionUtils.subList(data.getProviders(), page * 28, 28).stream().map(p -> {
			if(p.isEmpty())
				return filler;
			ItemStack type = p.getType().clone();
			ItemMeta meta = type.getItemMeta();
			String name = meta.hasDisplayName() ? meta.getDisplayName() : "§f" + Utils.toCapitalWords(type.getType());
			meta.setDisplayName(name + " §3Amount: §b" + p.amount());
			type.setItemMeta(meta);
			return type;
		}).toArray(ItemStack[]::new);
	}


	@Override
	public void onItemClick(HumanEntity player, Inventory gui, ItemStack stack, InventoryClickEvent event, int page) {
		ClickType click = event.getClick();
		if(!click.isRightClick() && !click.isLeftClick())
			return;

		int slot = event.getSlot();
		int row = slot / 9;
		int column = slot - row * 9;
		int index = page * 28 + row * 7 + column - 8;
		StorageInformation data = this.loadData(gui);
		ItemUtils.giveItemToPlayer(player, data.removeStack(index, click.isLeftClick()));
		this.openGui(player, new BookData<>(page, data), false, false);
	}


	@Override
	public void onPlayerInventoryClick(HumanEntity player, PlayerInventory inventory, ItemStack stack, int slot, InventoryClickEvent event, int page) {
		if(ItemUtils.isEmpty(stack))
			return;

		ClickType click = event.getClick();
		if(!click.isRightClick() && !click.isLeftClick())
			return;

		StorageInformation data = this.loadData(event.getInventory());
		inventory.setItem(slot, data.addStack(stack, click.isLeftClick()));
		this.openGui(player, new BookData<>(page, data), false, false);
	}


	@Override
	public StorageInformation loadData(Inventory inventory) {
		return new StorageInformation((TileState) InventoryUtils.getLocationFromStack(this.getIdentifierStack(inventory), BLOCK_KEY, WORLD_KEY).getBlock().getState());
	}


	@Override
	public void saveData(Inventory inventory, StorageInformation data) {
		InventoryUtils.storeLocationInStack(this.getIdentifierStack(inventory), BLOCK_KEY, WORLD_KEY, data.tile.getLocation());
	}


	public static void openBook(HumanEntity player, TileState block) {
		// BookInit.STORAGE_BLOCK_BOOK.open(player, new StorageInformation(block));
	}

	public static class StorageInformation {

		private final TileState tile;
		private final StorageCrateBlock block;

		StorageInformation(TileState tile) {
			this.tile = tile;
			this.block = (StorageCrateBlock) Blocks.getCustomBlockFromBlock(tile);
		}


		List<Provider<ItemStack>> getProviders() {
			return this.block.getItemProviders(this.tile);
		}


		ItemStack addStack(ItemStack stack, boolean addAll) {
			return addAll ? this.block.addStack(this.tile, stack) : this.block.addStack(this.tile, stack, 1);
		}


		ItemStack removeStack(int index, boolean addAll) {
			Provider<ItemStack> provider = this.getProviders().get(index);
			return addAll ? ItemProvider.get(provider) : ItemProvider.get(provider, 1);
		}

	}

}
