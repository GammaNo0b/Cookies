
package me.gamma.cookies.object.gui.book;


import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.BookInit;
import me.gamma.cookies.init.MultiBlockInit;
import me.gamma.cookies.object.multiblock.MultiBlock;
import me.gamma.cookies.util.CollectionUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;



public class MultiBlockBook implements Book<Void> {

	@Override
	public String getIdentifier() {
		return "multi_block_book";
	}


	@Override
	public String getTitle(int page, Void data) {
		return "§dMulti Block Book";
	}


	@Override
	public int pages(Void data) {
		return (MultiBlockInit.MULTIBLOCKS.size() + 27) / 28;
	}


	@Override
	public int rows() {
		return 5;
	}


	@Override
	public Inventory createGui(BookData<Void> data) {
		Inventory gui = Book.super.createGui(data);
		ItemStack filler = InventoryUtils.filler(Material.BROWN_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(gui.getSize() - 1 - i, filler);
		}
		filler = InventoryUtils.filler(Material.ORANGE_STAINED_GLASS_PANE);
		for(int i = 0; i < 3; i++) {
			gui.setItem(i * 9 + 9, filler);
			gui.setItem(i * 9 + 17, filler);
		}
		return gui;
	}


	@Override
	public Void loadData(Inventory inventory) {
		return null;
	}


	@Override
	public void saveData(Inventory inventory, Void data) {}


	@Override
	public int[] getItemSlots() {
		return BASIC_THREE_ROW_SLOT_LAYOUT;
	}


	@Override
	public ItemStack[] getItems(int page, Inventory inventory, int cycle, Void data) {
		return CollectionUtils.subList(MultiBlockInit.MULTIBLOCKS.asList(), page * 28, 28).stream().map(multiblock -> new ItemBuilder(multiblock.getTriggerMaterial()).setName(multiblock.getName()).build()).toArray(ItemStack[]::new);
	}


	@Override
	public void onItemClick(HumanEntity player, Inventory gui, ItemStack stack, InventoryClickEvent event, int page) {
		MultiBlock multiBlock = MultiBlockInit.getMultiBlockByName(stack.getItemMeta().getDisplayName());
		if(multiBlock != null)
			MultiBlockBuildingBook.openBook(player, multiBlock);
	}


	public static void openBook(HumanEntity player) {
		BookInit.MULTI_BLOCK_BOOK.open(player, null);
	}

}
