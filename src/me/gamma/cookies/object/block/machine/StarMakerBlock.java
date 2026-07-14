
package me.gamma.cookies.object.block.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.StarMaker;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.InventoryUtils;



public class StarMakerBlock extends AbstractItemProcessingMachineBlock<StarMakerBlock, StarMaker> {

	public StarMakerBlock() {
		super(null);
	}


	@Override
	public String getTitle() {
		return "§eStar Maker";
	}


	@Override
	public String getMachineRegistryName() {
		return "star_maker";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.STAR_MAKER;
	}


	@Override
	public int getUpgradeSlot() {
		return 14;
	}


	@Override
	public int getEnergyLevelSlot() {
		return 41;
	}


	@Override
	public int getRedstoneModeSlot() {
		return 32;
	}


	@Override
	public int getProgressSlot() {
		return 23;
	}


	@Override
	public int getBlockFaceConfigSlot() {
		return 5;
	}


	@Override
	public Inventory createGui(Block block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		gui.setItem(21, MachineConstants.INPUT_BORDER_MATERIAL);
		for(int i : ArrayUtils.array(15, 16, 17, 24, 26, 33, 34, 35))
			gui.setItem(i, MachineConstants.OUTPUT_BORDER_MATERIAL);
		return gui;
	}


	@Override
	public StarMakerBlock castCustomBlock() {
		return this;
	}


	@Override
	public StarMaker createNewTileEntity(Block block) {
		return new StarMaker(this, block);
	}

}
