
package me.gamma.cookies.object.block.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.RocketAssembler;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.InventoryUtils;



public class RocketAssemblerBlock extends AbstractItemProcessingMachineBlock<RocketAssemblerBlock, RocketAssembler> {

	public RocketAssemblerBlock() {
		super(null);
	}


	@Override
	public String getTitle() {
		return "§eRocket Assembler";
	}


	@Override
	public String getMachineRegistryName() {
		return "rocket_assembler";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ROCKET_ASSEMBLER;
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
	public int getInputModeSlot() {
		return -1;
	}


	@Override
	public int getOutputModeSlot() {
		return -1;
	}


	@Override
	public Inventory createGui(Block block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		for(int i : ArrayUtils.array(9, 13, 18, 22, 27, 28, 29, 30, 31))
			gui.setItem(i, MachineConstants.INPUT_BORDER_MATERIAL);
		for(int i : ArrayUtils.array(15, 16, 17, 24, 26, 33, 34, 35))
			gui.setItem(i, MachineConstants.OUTPUT_BORDER_MATERIAL);
		return gui;
	}


	@Override
	public RocketAssemblerBlock castCustomBlock() {
		return this;
	}


	@Override
	public RocketAssembler createNewTileEntity(Block block) {
		return new RocketAssembler(this, block);
	}

}
