
package me.gamma.cookies.object.block.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.HoneyExtractor;
import me.gamma.cookies.util.InventoryUtils;



public class HoneyExtractorBlock extends AbstractProcessingMachineBlock<HoneyExtractorBlock, HoneyExtractor> {

	public HoneyExtractorBlock() {
		super(null);
	}


	@Override
	public String getTitle() {
		return "§fHoney Extractor";
	}


	@Override
	public String getMachineRegistryName() {
		return "honey_extractor";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.HONEY_EXTRACTOR;
	}


	@Override
	public int getBlockFaceConfigSlot() {
		return 1;
	}


	@Override
	public int getUpgradeSlot() {
		return 10;
	}


	@Override
	public int getProgressSlot() {
		return 19;
	}


	@Override
	public int getRedstoneModeSlot() {
		return 28;
	}


	@Override
	public int getEnergyLevelSlot() {
		return 37;
	}


	@Override
	public Inventory createGui(Block block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		ItemStack filler = InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE);
		InventoryUtils.fillBorder(gui, filler);
		gui.setItem(11, filler);
		gui.setItem(20, filler);
		gui.setItem(29, filler);
		return gui;
	}


	@Override
	public HoneyExtractorBlock castCustomBlock() {
		return this;
	}


	@Override
	public HoneyExtractor createNewTileEntity(Block block) {
		return new HoneyExtractor(this, block);
	}

}
