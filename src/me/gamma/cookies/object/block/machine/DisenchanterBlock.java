
package me.gamma.cookies.object.block.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.Disenchanter;
import me.gamma.cookies.util.InventoryUtils;



public class DisenchanterBlock extends EnchantmentMachineBlock<DisenchanterBlock, Disenchanter> {

	public DisenchanterBlock() {
		super(null);
	}


	@Override
	public String getMachineRegistryName() {
		return "disenchanter";
	}


	@Override
	public String getTitle() {
		return "§bDisenchanter";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.DISENCHANTMENT_TABLE;
	}


	@Override
	public Inventory createGui(Block block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, null);
		ItemStack filler = InventoryUtils.filler(Material.CYAN_STAINED_GLASS_PANE);
		for(int i : new int[] { 0, 1, 7, 8, 9, 17 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.YELLOW_STAINED_GLASS_PANE);
		for(int i : new int[] { 2, 6 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.ORANGE_STAINED_GLASS_PANE);
		for(int i : new int[] { 3, 5 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.BLACK_STAINED_GLASS_PANE);
		for(int i : new int[] { 18, 26, 27, 35, 36, 37, 38, 39, 41, 42, 43, 44 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.GREEN_STAINED_GLASS_PANE);
		for(int i : new int[] { 10, 11, 12, 19, 28, 29, 30 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.LIME_STAINED_GLASS_PANE);
		for(int i : new int[] { 14, 15, 16, 25, 32, 33, 34 })
			gui.setItem(i, filler);
		filler = InventoryUtils.filler(Material.ORANGE_STAINED_GLASS_PANE);
		for(int i : new int[] { 13, 31 })
			gui.setItem(i, filler);
		return gui;
	}


	@Override
	public DisenchanterBlock castCustomBlock() {
		return this;
	}


	@Override
	public Disenchanter createNewTileEntity(Block block) {
		return new Disenchanter(this, block);
	}

}
