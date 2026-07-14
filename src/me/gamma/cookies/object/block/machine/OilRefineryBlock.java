
package me.gamma.cookies.object.block.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.OilRefinery;
import me.gamma.cookies.util.InventoryUtils;



public class OilRefineryBlock extends AbstractGuiMachineBlock<OilRefineryBlock, OilRefinery> {

	private double refiningEfficiency = 0.0D;

	public OilRefineryBlock() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.refiningEfficiency = config.getDouble("refiningEfficiency", 1.0);
	}


	public double getRefiningEfficiency() {
		return this.refiningEfficiency;
	}


	@Override
	public String getTitle() {
		return "§fOil Refinery";
	}


	@Override
	public String getMachineRegistryName() {
		return "oil_refinery";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.REFINERY;
	}


	@Override
	public int rows() {
		return 6;
	}


	@Override
	public Inventory createGui(Block block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		InventoryUtils.fillTopBottom(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		for(int i : new int[] { 9, 12, 18, 21, 27, 30, 36, 39 })
			gui.setItem(i, MachineConstants.INPUT_BORDER_MATERIAL);
		for(int i : new int[] { 14, 17, 23, 26, 32, 35, 41, 44 })
			gui.setItem(i, MachineConstants.OUTPUT_BORDER_MATERIAL);
		return gui;
	}


	@Override
	public OilRefineryBlock castCustomBlock() {
		return this;
	}


	@Override
	public OilRefinery createNewTileEntity(Block block) {
		return new OilRefinery(this, block);
	}

}
