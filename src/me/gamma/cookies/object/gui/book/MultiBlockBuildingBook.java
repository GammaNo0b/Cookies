
package me.gamma.cookies.object.gui.book;


import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.gamma.cookies.init.BookInit;
import me.gamma.cookies.init.MultiBlockInit;
import me.gamma.cookies.object.gui.History;
import me.gamma.cookies.object.multiblock.MultiBlock;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utils;



public class MultiBlockBuildingBook implements Book<MultiBlock> {

	public static final String KEY_MULTIBLOCK = "multiblock";

	@Override
	public String getIdentifier() {
		return "multi_block_building_book";
	}


	@Override
	public String getTitle(int page, MultiBlock data) {
		return data.getName();
	}


	@Override
	public int pages(MultiBlock data) {
		return data.getLength();
	}


	@Override
	public int rows() {
		return 6;
	}


	@Override
	public Inventory createGui(BookData<MultiBlock> data) {
		Inventory gui = Book.super.createGui(data);
		ItemStack filler = InventoryUtils.filler(Material.BROWN_STAINED_GLASS_PANE);
		for(int i = 0; i < 9; i++) {
			gui.setItem(i, filler);
			gui.setItem(gui.getSize() - 1 - i, filler);
		}
		int width = data.data().getWidth();
		int height = data.data().getHeight();
		int start = 4 - width / 2;
		ItemStack filler2 = this.getItemFiller();
		for(int i = 0; i < height; i++) {
			int s = (i - 1 + this.rows() - height) * 9;
			for(int j = 0; j < start; j++)
				gui.setItem(s + j, filler2);
			for(int j = 0; j < width; j++) {
				Material material = data.data().getMaterial(new Vector(j, height - 1 - i, data.page()));
				if(material == null) {
					gui.setItem(s + start + j, filler2);
				} else {
					if(material.isItem()) {
						gui.setItem(s + start + j, new ItemBuilder(material).setName("§6Position: §4X: §c" + j + " §7| §2Y: §a" + (height - 1 - i) + " §7| §1Z: §9" + data.page()).build());
					} else {
						gui.setItem(s + start + j, new ItemBuilder(Material.BARRIER).setName("§8Material: §7" + Utils.toCapitalWords(material) + "§8, §6Position: §4X: §c" + j + " §7| §2Y: §a" + data.page() + " §7| §1Z: §9" + i).build());
					}
				}
			}
			for(int j = start + width; j < 9; j++)
				gui.setItem(s + j, filler2);
		}
		for(int i = 0; i < this.rows() - 2 - height; i++) {
			int s = i * 9 + 9;
			for(int j = 0; j < 9; j++)
				gui.setItem(s + j, filler2);
		}
		return gui;
	}


	@Override
	public int[] getItemSlots() {
		return new int[0];
	}


	@Override
	public ItemStack[] getItems(int page, Inventory inventory, int cycle, MultiBlock data) {
		return new ItemStack[0];
	}


	@Override
	public MultiBlock loadData(Inventory inventory) {
		return MultiBlockInit.getMultiBlockByName(InventoryUtils.getStringFromStack(inventory.getItem(this.getSlot(this.getIdentifierSlot(), inventory.getSize())), KEY_MULTIBLOCK));
	}


	@Override
	public void saveData(Inventory inventory, MultiBlock data) {
		InventoryUtils.storeStringInStack(inventory.getItem(this.getSlot(this.getIdentifierSlot(), inventory.getSize())), KEY_MULTIBLOCK, data.getName());
	}


	@Override
	public void close(HumanEntity player, Inventory inventory) {
		History.travelBack(player);
	}


	public static void openBook(HumanEntity player, MultiBlock block) {
		BookInit.MULTI_BLOCK_BUILDING_BOOK.open(player, block);
	}

}
