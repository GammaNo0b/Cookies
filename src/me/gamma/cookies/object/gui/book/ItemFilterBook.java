
package me.gamma.cookies.object.gui.book;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.init.BookInit;
import me.gamma.cookies.object.block.FilterBlock;
import me.gamma.cookies.object.gui.book.ItemFilterBook.ItemFilterInformation;
import me.gamma.cookies.object.item.ItemFilter;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;



public class ItemFilterBook implements Book<ItemFilterInformation> {

	public static final String WORLD_KEY = "world";
	public static final String LOCATION_KEY = "location";
	public static final int LOCATION_SLOT = 6;

	private static final int LISTTYPE_SLOT = 26;
	private static final int NBT_SLOT = 35;

	private static final ItemStack WHITELIST_ICON = InventoryUtils.markFiller(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setName("§fWhitelist").build());
	private static final ItemStack BLACKLIST_ICON = InventoryUtils.markFiller(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("§fBlacklist").build());
	private static final ItemStack FILTER_NBT_ICON = InventoryUtils.markFiller(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName("§aFilter NBT").build());
	private static final ItemStack IGNORE_NBT_ICON = InventoryUtils.markFiller(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§cIgnore NBT").build());

	private static final ItemStack EMPTY_SLOT = InventoryUtils.markFiller(new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName(" ").build());

	@Override
	public String getIdentifier() {
		return "item_filter_book";
	}


	@Override
	public String getTitle(int page, ItemFilterInformation data) {
		return "§9Item Filter";
	}


	@Override
	public int pages(ItemFilterInformation data) {
		return (ItemFilter.SIZE + 27) / 28;
	}


	@Override
	public int rows() {
		return 6;
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public int getTurnLeftSlot() {
		return 47;
	}


	@Override
	public int getTurnRightSlot() {
		return 51;
	}


	@Override
	public int getCloseSlot() {
		return 4;
	}


	@Override
	public ItemStack getTurnLeftIcon() {
		return new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("§9<---").build();
	}


	@Override
	public ItemStack getTurnRightIcon() {
		return new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("§9--->").build();
	}


	@Override
	public ItemStack getCloseIcon() {
		return new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE).setName("§5Close").build();
	}


	@Override
	public Inventory createGui(BookData<ItemFilterInformation> data) {
		Inventory gui = Book.super.createGui(data);
		ItemStack filler = InventoryUtils.filler(Material.CYAN_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(45 + i, filler);
		}
		for(int i = 1; i < 5; i++) {
			gui.setItem(9 * i, filler);
			gui.setItem(9 * i + 8, filler);
		}
		return gui;
	}


	@Override
	public void setupControls(Inventory gui, int page, ItemFilterInformation data) {
		Book.super.setupControls(gui, page, data);
		gui.setItem(LISTTYPE_SLOT, data.getFilter().isWhitelisted() ? WHITELIST_ICON : BLACKLIST_ICON);
		gui.setItem(NBT_SLOT, data.getFilter().isIgnoreNBT() ? IGNORE_NBT_ICON : FILTER_NBT_ICON);
	}


	@Override
	public int[] getItemSlots() {
		return BASIC_FOUR_ROW_SLOT_LAYOUT;
	}


	@Override
	public ItemStack[] getItems(int page, Inventory inventory, int cycle, ItemFilterInformation data) {
		int start = page * 28;
		int length = Math.min(28, ItemFilter.SIZE - start);
		ItemStack[] items = new ItemStack[length];
		for(int i = 0; i < length; i++) {
			ItemStack item = data.getFilter().getFilterItem(start + i);
			items[i] = ItemUtils.isEmpty(item) ? EMPTY_SLOT : item;
			items[i].setAmount(1);
		}
		return items;
	}


	@Override
	public ItemStack getItemFiller() {
		return InventoryUtils.filler(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
	}


	@SuppressWarnings("unchecked")
	@Override
	public ItemFilterInformation loadData(Inventory inventory) {
		Location location = InventoryUtils.getLocationFromStack(inventory.getItem(LOCATION_SLOT), LOCATION_KEY, WORLD_KEY);
		if(!(location.getBlock().getState() instanceof TileState state))
			return null;

		try {
			return new ItemFilterInformation(state, (FilterBlock<ItemStack, ItemFilter>) Blocks.getCustomBlockFromBlock(state));
		} catch(ClassCastException e) {
			return null;
		}
	}


	@Override
	public void saveData(Inventory inventory, ItemFilterInformation data) {
		InventoryUtils.storeLocationInStack(inventory.getItem(LOCATION_SLOT), LOCATION_KEY, WORLD_KEY, data.block.getLocation());
	}


	@Override
	public void onInventoryClick(HumanEntity player, Inventory gui, ItemStack stack, int slot, InventoryClickEvent event, int page) {
		ItemFilterInformation data = this.loadData(gui);
		if(data == null)
			return;

		if(slot == LISTTYPE_SLOT) {
			ItemFilter filter = data.getFilter();
			filter.setWhitelisted(stack.getType() != WHITELIST_ICON.getType());
			data.setFilter(filter);
			this.openGui(player, new BookData<>(page, data), false, false);
		} else if(slot == NBT_SLOT) {
			ItemFilter filter = data.getFilter();
			filter.setIgnoreNBT(stack.getType() != IGNORE_NBT_ICON.getType());
			data.setFilter(filter);
			this.openGui(player, new BookData<>(page, data), false, false);
		} else {
			int row = slot / 9;
			int col = slot % 9;
			if(1 <= row && row <= 4 && 1 <= col && col <= 7) {
				ItemFilter filter = data.getFilter();
				int offset = page * 28;
				int index = offset + row * 7 + col - 8;
				if(index < ItemFilter.SIZE) {
					filter.setFilterItem(index, new ItemStack(Material.AIR));
					data.setFilter(filter);
					this.openGui(player, new BookData<>(page, data), false, false);
				}
			}
		}
	}


	@Override
	public void onPlayerInventoryClick(HumanEntity player, PlayerInventory inventory, ItemStack stack, int slot, InventoryClickEvent event, int page) {
		ItemFilterInformation data = this.loadData(event.getInventory());
		if(data == null)
			return;

		if(ItemUtils.isEmpty(stack))
			return;

		int offset = page * 28;
		ItemFilter filter = data.getFilter();
		for(int i = 0; i < ItemFilter.SIZE; i++) {
			if(!ItemUtils.isEmpty(filter.getFilterItem(offset + i)))
				continue;

			filter.setFilterItem(offset + i, stack.clone());
			break;
		}
		data.setFilter(filter);
		this.openGui(player, new BookData<>(page, data), false, false);
	}


	public static void openBook(HumanEntity player, TileState block, FilterBlock<ItemStack, ItemFilter> filter) {
		BookInit.ITEM_FILTER_BOOK.open(player, new ItemFilterInformation(block, filter));
	}

	public static class ItemFilterInformation {

		private final TileState block;
		private final FilterBlock<ItemStack, ItemFilter> filter;

		public ItemFilterInformation(TileState block, FilterBlock<ItemStack, ItemFilter> filter) {
			this.block = block;
			this.filter = filter;
		}


		private ItemFilter getFilter() {
			return this.filter.getFilter(this.block);
		}


		private void setFilter(ItemFilter filter) {
			this.filter.setFilter(this.block, filter);
			this.block.update();
		}

	}

}
