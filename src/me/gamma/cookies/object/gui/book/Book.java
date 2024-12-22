
package me.gamma.cookies.object.gui.book;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.object.gui.History;
import me.gamma.cookies.object.gui.InventoryProvider;
import me.gamma.cookies.object.gui.book.Book.BookData;
import me.gamma.cookies.object.gui.task.InventoryTask;
import me.gamma.cookies.object.gui.task.StaticInventoryTask;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.ItemUtils;



public interface Book<D> extends InventoryProvider<BookData<D>> {

	ItemStack WOODEN_BUTTON_PREVIOUS = new ItemBuilder(Material.PLAYER_HEAD).setName("§6<").setTexture(HeadTextures.WOODEN_ARROW_BACKWARDS).build();
	ItemStack WOODEN_BUTTON_NEXT = new ItemBuilder(Material.PLAYER_HEAD).setName("§6>").setTexture(HeadTextures.WOODEN_ARROW_FORWARDS).build();
	ItemStack WOODEN_BUTTON_BACK = new ItemBuilder(Material.PLAYER_HEAD).setName("§6<--- Back").setTexture(HeadTextures.WOODEN_ARROW_LEFT).build();

	int[] BASIC_ONE_ROW_SLOT_LAYOUT = basicSlotLayout(1);
	int[] BASIC_TWO_ROW_SLOT_LAYOUT = basicSlotLayout(2);
	int[] BASIC_THREE_ROW_SLOT_LAYOUT = basicSlotLayout(3);
	int[] BASIC_FOUR_ROW_SLOT_LAYOUT = basicSlotLayout(4);

	static int[] basicSlotLayout(int rows) {
		int[] slots = new int[rows * 7];
		int i = 0;
		for(int r = 1; r <= rows; r++)
			for(int c = 1; c < 8; c++)
				slots[i++] = r * 9 + c;
		return slots;
	}

	String KEY_PAGE = "book_page";
	String KEY_ITEM = "book_item";

	/**
	 * Returns the title for this book with the given data.
	 * 
	 * @param page the page number
	 * @param data the book data
	 * @return the title.
	 */
	String getTitle(int page, D data);


	@Override
	default String getTitle(BookData<D> data) {
		return this.getTitle(data.page, data.data);
	}


	/**
	 * Returns the number of pages this book has with the given data.
	 * 
	 * @param data the extra data
	 * @return the number of pages
	 */
	int pages(D data);


	@Override
	default Sound getSound() {
		return Sound.ITEM_BOOK_PAGE_TURN;
	}


	/**
	 * Loads the data from the given inventory.
	 * 
	 * @param inventory the inventory
	 * @return the loaded dat
	 */
	D loadData(Inventory inventory);

	/**
	 * Saves the given data in the given inventory.
	 * 
	 * @param inventory the inventory
	 * @param data      the data
	 */
	void saveData(Inventory inventory, D data);


	@Override
	default void storeData(Inventory inventory, BookData<D> data) {
		this.saveData(inventory, data.data);
		InventoryUtils.storeIntInStack(this.getIdentifierStack(inventory), KEY_PAGE, data.page);
	}


	@Override
	default BookData<D> fetchData(Inventory inventory) {
		D data = this.loadData(inventory);
		int page = InventoryUtils.getIntFromStack(this.getIdentifierStack(inventory), KEY_PAGE, 0);
		return new BookData<>(page, data);
	}


	/**
	 * Returns the slot index at which the data should be stored inside the inventory page. Can be negative to specify slots from the end of the page. For
	 * example -1 would be the last slot, -2 the slot before that and so on.
	 * 
	 * @return the identifier slot index
	 */
	@Override
	default int getIdentifierSlot() {
		return 0;
	}


	/**
	 * Returns the item stack stored in the identifier slot of the given inventory.
	 * 
	 * @param inventory the inventory
	 * @return the stack
	 */
	default ItemStack getIdentifierStack(Inventory inventory) {
		return inventory.getItem(this.getSlot(this.getIdentifierSlot(), inventory.getSize()));
	}


	/**
	 * Returns the slot index that will turn the page left over on right click. Can be negative to specify slots from the end of the page. For example -1
	 * would be the last slot, -2 the slot before that and so on.
	 * 
	 * @return the identifier slot index
	 */
	default int getTurnLeftSlot() {
		return -7;
	}


	/**
	 * Returns the slot index that will turn the page right over on right click. Can be negative to specify slots from the end of the page. For example -1
	 * would be the last slot, -2 the slot before that and so on.
	 * 
	 * @return the identifier slot index
	 */
	default int getTurnRightSlot() {
		return -3;
	}


	/**
	 * Returns the slot index that will close the page on right click. Can be negative to specify slots from the end of the page. For example -1 would be
	 * the last slot, -2 the slot before that and so on.
	 * 
	 * @return the identifier slot index
	 */
	default int getCloseSlot() {
		return 4;
	}


	/**
	 * Calculates the slot id given the slot specification, which can be negative and the size of the gui the slot is addressing in.
	 * 
	 * @param slot the slot specification
	 * @param size the size of the gui
	 * @return
	 */
	default int getSlot(int slot, int size) {
		return slot < 0 ? size + slot : slot;
	}


	/**
	 * Returns the icon for the turn left slot.
	 * 
	 * @return the icon
	 */
	default ItemStack getTurnLeftIcon() {
		return WOODEN_BUTTON_PREVIOUS.clone();
	}


	/**
	 * Returns the icon for the turn right slot.
	 * 
	 * @return the icon
	 */
	default ItemStack getTurnRightIcon() {
		return WOODEN_BUTTON_NEXT.clone();
	}


	/**
	 * Returns the icon for the go back slot.
	 * 
	 * @return the icon
	 */
	default ItemStack getCloseIcon() {
		return WOODEN_BUTTON_BACK.clone();
	}


	/**
	 * Returns an array containing the slots for one page.
	 * 
	 * @return the array
	 */
	int[] getItemSlots();

	/**
	 * Returns the items for the given page.
	 * 
	 * @param page      the page
	 * @param inventory the inventory
	 * @param cycle     the cycle
	 * @param data      the extra data
	 * @return the items
	 */
	ItemStack[] getItems(int page, Inventory inventory, int cycle, D data);


	/**
	 * Whether an {@link InventoryTask} should be created to repeatedly update the items on the page.
	 * 
	 * @return if the items should be updated
	 */
	default boolean updateItems() {
		return false;
	}


	/**
	 * Returns the stack to fill empty slots with no items.
	 * 
	 * @return the item stack
	 */
	default ItemStack getItemFiller() {
		return InventoryUtils.filler(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
	}


	@Override
	default Inventory createGui(BookData<D> data) {
		return Bukkit.createInventory(null, this.rows() * 9, this.getTitle(data.page, data.data) + " §9Page: §3" + data.page);
	}


	@Override
	default Inventory getGui(BookData<D> data) {
		int page = data.page;
		int pages = this.pages(data.data);
		page = pages == 0 ? 0 : (page + pages) % pages;

		Inventory gui = InventoryProvider.super.getGui(data);
		this.setupControls(gui, page, data.data);
		if(!this.updateItems())
			this.fillItems(page, gui, 0, data.data);

		ItemStack identifier = this.getIdentifierStack(gui);
		InventoryUtils.storeIntInStack(identifier, KEY_PAGE, page);

		return gui;
	}


	@Override
	default InventoryTask createInventoryTask(Inventory inventory, BookData<D> data) {
		return this.updateItems() ? new InventoryTask(inventory, 20) {

			@Override
			protected void updateInventory() {
				Book.this.fillItems(data.page, this.inventory, this.cycle, data.data);
			}

		} : new StaticInventoryTask(inventory);
	}


	/**
	 * Opens the book at the first page with the given data.
	 * 
	 * @param player the player
	 * @param data   the data
	 */
	default void open(HumanEntity player, D data) {
		this.open(player, 0, data);
	}


	/**
	 * Opens the book at the given page with the given data.
	 * 
	 * @param player the player
	 * @param page   the page
	 * @param data   the data
	 */
	default void open(HumanEntity player, int page, D data) {
		this.openGui(player, new BookData<>(page, data));
	}


	/**
	 * Sets up all the controls for the given inventory page.
	 * 
	 * @param gui  the inventory page
	 * @param page the page
	 * @param data the extra data
	 */
	default void setupControls(Inventory gui, int page, D data) {
		int size = gui.getSize();

		if(this.pages(data) > 1) {
			gui.setItem(this.getSlot(this.getTurnLeftSlot(), size), this.getTurnLeftIcon());
			gui.setItem(this.getSlot(this.getTurnRightSlot(), size), this.getTurnRightIcon());
		}

		gui.setItem(this.getSlot(this.getCloseSlot(), size), this.getCloseIcon());
	}


	/**
	 * Fills the given inventory page with the items at the given page.
	 * 
	 * @param gui   the inventory page
	 * @param page  the page number
	 * @param cycle the cycle
	 * @param data  the extra data
	 */
	default void fillItems(int page, Inventory gui, int cycle, D data) {
		int index = 0;
		int[] slots = this.getItemSlots();
		ItemStack[] items = this.getItems(page, gui, cycle, data);
		for(; index < slots.length; index++) {
			if(index >= items.length)
				break;

			this.setItem(gui, slots[index], items[index].clone());
		}

		ItemStack filler = this.getItemFiller();
		if(!ItemUtils.isEmpty(filler))
			for(; index < slots.length; index++)
				gui.setItem(slots[index], filler);
	}


	/**
	 * Stores the given item at the given slot in the inventory and marks it as an book item.
	 * 
	 * @param gui   the inventory
	 * @param slot  the slot
	 * @param stack the item
	 */
	default void setItem(Inventory gui, int slot, ItemStack stack) {
		InventoryUtils.markItem(stack, KEY_ITEM);
		gui.setItem(slot, stack);
	}


	/**
	 * Fetches the item at the given slot in the inventory and unmarks it as book item.
	 * 
	 * @param gui  the inventory
	 * @param slot the slot
	 * @return the item
	 */
	default ItemStack getItem(Inventory gui, int slot) {
		ItemStack stack = gui.getItem(slot).clone();
		InventoryUtils.unmarkItem(stack, KEY_ITEM);
		return stack;
	}


	/**
	 * Returns the page number for the given inventory page.
	 * 
	 * @param inventory the inventory page
	 * @return the page number
	 */
	default int getPage(Inventory inventory) {
		return InventoryUtils.getIntFromStack(inventory.getItem(this.getSlot(this.getIdentifierSlot(), inventory.getSize())), Book.KEY_PAGE);
	}


	@Override
	default boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		Inventory inventory = event.getInventory();
		int size = inventory.getSize();
		int slot = event.getSlot();
		int page = this.getPage(inventory);
		ItemStack stack = event.getCurrentItem();

		this.onInventoryClick(player, inventory, stack, slot, event, page);

		if(slot == this.getSlot(this.getTurnLeftSlot(), size)) {
			this.turnLeftOver(player, inventory);
		} else if(slot == this.getSlot(this.getTurnRightSlot(), size)) {
			this.turnRightOver(player, inventory);
		} else if(slot == this.getSlot(this.getCloseSlot(), size)) {
			this.close(player, inventory);
		} else if(InventoryUtils.isMarked(stack, Book.KEY_ITEM)) {
			this.onItemClick(player, inventory, stack, event, page);
		}
		return true;
	}


	@Override
	default boolean onPlayerInventoryInteract(Player player, PlayerInventory gui, InventoryClickEvent event) {
		this.onPlayerInventoryClick(player, gui, event.getCurrentItem(), event.getSlot(), event, this.getPage(event.getInventory()));
		return true;
	}


	@Override
	default boolean onInventoryClose(Player player, Inventory gui, InventoryCloseEvent event) {
		this.onPageClose(player, gui, event, this.getIdentifierSlot());
		return false;
	}


	/**
	 * Get's executed when the player clicks in the inventory on a inventory page.
	 * 
	 * @param player the player
	 * @param gui    the clicked inventory
	 * @param stack  the clicked item
	 * @param slot   the clicked slot
	 * @param event  the fired event
	 * @param page   the page number
	 */
	default void onInventoryClick(HumanEntity player, Inventory gui, ItemStack stack, int slot, InventoryClickEvent event, int page) {}


	/**
	 * Get's executed when the player clicks in his player inventory.
	 * 
	 * @param player    the player
	 * @param inventory the clicked inventory
	 * @param stack     the clicked item
	 * @param slot      the clicked slot
	 * @param event     the fired event
	 * @param page      the page number
	 */
	default void onPlayerInventoryClick(HumanEntity player, PlayerInventory inventory, ItemStack stack, int slot, InventoryClickEvent event, int page) {}


	/**
	 * Get's executed when the player clicks an item on a inventory page.
	 * 
	 * @param player the player
	 * @param gui    the clicked inventory
	 * @param stack  the clicked item
	 * @param event  the fired event
	 * @param page   the page number
	 */
	default void onItemClick(HumanEntity player, Inventory gui, ItemStack stack, InventoryClickEvent event, int page) {}


	/**
	 * Get's executed when a player closes a inventory page.
	 * 
	 * @param player the player
	 * @param gui    the closed inventory
	 * @param event  the fired event
	 * @param page   the page number
	 */
	default void onPageClose(HumanEntity player, Inventory gui, InventoryCloseEvent event, int page) {}


	/**
	 * Turns the current page left over for the given player.
	 * 
	 * @param player    the player
	 * @param inventory the old inventory page
	 */
	default void turnLeftOver(HumanEntity player, Inventory inventory) {
		BookData<D> bookdata = this.fetchData(inventory);
		D data = bookdata.data;
		int pages = this.pages(data);
		if(pages <= 1)
			return;

		int page = this.getPage(inventory) - 1;
		if(page < 0)
			page = pages - 1;

		this.openGui(player, new BookData<>(page, data), false, true);
	}


	/**
	 * Turns the current page right over for the given player.
	 * 
	 * @param player    the player
	 * @param inventory the old inventory page
	 */
	default void turnRightOver(HumanEntity player, Inventory inventory) {
		BookData<D> bookdata = this.fetchData(inventory);
		D data = bookdata.data;
		int pages = this.pages(data);
		if(pages <= 1)
			return;

		int page = this.getPage(inventory) + 1;
		if(page >= pages)
			page = 0;

		this.openGui(player, new BookData<>(page, data), false, true);
	}


	/**
	 * Closes this page when the player presses the close button.
	 * 
	 * @param player    the player
	 * @param inventory the inventory page
	 */
	default void close(HumanEntity player, Inventory inventory) {
		player.closeInventory();
		History.clear(player);
	}

	record BookData<D>(int page, D data) {}

}
