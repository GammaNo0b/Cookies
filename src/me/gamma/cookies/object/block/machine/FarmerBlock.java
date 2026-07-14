
package me.gamma.cookies.object.block.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.Farmer;
import me.gamma.cookies.util.InventoryUtils;



public class FarmerBlock extends AbstractItemProcessingMachineBlock<FarmerBlock, Farmer> {

	public FarmerBlock(MachineTier tier) {
		super(tier);
	}


	@Override
	public String getTitle() {
		return this.tier.getDescription() + " Farmer";
	}


	@Override
	public String getMachineRegistryName() {
		return "farmer";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.FARMER;
	}


	@Override
	public int getBlockFaceConfigSlot() {
		return 3;
	}


	@Override
	public int getUpgradeSlot() {
		return 12;
	}


	@Override
	public int getProgressSlot() {
		return 21;
	}


	@Override
	public int getRedstoneModeSlot() {
		return 30;
	}


	@Override
	public int getEnergyLevelSlot() {
		return 39;
	}


	@Override
	public int getOutputModeSlot() {
		return 42;
	}


	@Override
	public Inventory createGui(Block block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		for(int i : new int[] { 9, 10, 11, 18, 20, 27, 28, 29 })
			gui.setItem(i, MachineConstants.INPUT_BORDER_MATERIAL);

		for(int i : new int[] { 13, 17, 22, 26, 31, 35 })
			gui.setItem(i, MachineConstants.OUTPUT_BORDER_MATERIAL);

		return gui;
	}


	@Override
	public FarmerBlock castCustomBlock() {
		return this;
	}


	@Override
	public Farmer createNewTileEntity(Block block) {
		return new Farmer(this, block);
	}

}
