
package me.gamma.cookies.object.block.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.TradingMachine;
import me.gamma.cookies.util.InventoryUtils;



public class TradingMachineBlock extends AbstractItemProcessingMachineBlock<TradingMachineBlock, TradingMachine> {

	public TradingMachineBlock() {
		super(null);
	}


	@Override
	public String getTitle() {
		return "§fTrading Machine";
	}


	@Override
	public String getMachineRegistryName() {
		return "trading_machine";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.TRADING_MACHINE;
	}


	@Override
	public int rows() {
		return 6;
	}


	@Override
	public Inventory createGui(Block block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		InventoryUtils.fillTopBottom(gui, MachineConstants.BORDER_MATERIAL);
		for(int r = 1; r < 5; ++r)
			for(int c = 0; c < 9; ++c)
				gui.setItem(9 * r + c, MachineConstants.FILLER_MATERIAL);
		for(int i : new int[] { 22, 23, 26, 31, 40 })
			gui.setItem(i, MachineConstants.BORDER_MATERIAL);
		ItemStack filler = InventoryUtils.filler(Material.LIME_STAINED_GLASS_PANE);
		gui.setItem(21, filler);
		gui.setItem(30, filler);
		for(int i : new int[] { 14, 17 })
			gui.setItem(i, MachineConstants.INPUT_BORDER_MATERIAL);
		for(int i : new int[] { 32, 35, 41, 44 })
			gui.setItem(i, MachineConstants.OUTPUT_BORDER_MATERIAL);
		return gui;
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public int getEnergyLevelSlot() {
		return 49;
	}


	@Override
	public int getRedstoneModeSlot() {
		return 25;
	}


	@Override
	public int getProgressSlot() {
		return 24;
	}


	@Override
	public int getUpgradeSlot() {
		return 13;
	}


	@Override
	public int getBlockFaceConfigSlot() {
		return 4;
	}


	@Override
	public int getInputModeSlot() {
		return 7;
	}


	@Override
	public int getOutputModeSlot() {
		return 52;
	}


	@Override
	public TradingMachineBlock castCustomBlock() {
		return this;
	}


	@Override
	public TradingMachine createNewTileEntity(Block block) {
		return new TradingMachine(this, block);
	}

}
