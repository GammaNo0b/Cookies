
package me.gamma.cookies.object.block.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.OilPump;
import me.gamma.cookies.util.InventoryUtils;



public class OilPumpBlock extends AbstractGuiMachineBlock<OilPumpBlock, OilPump> {

	private int waterConsumption;
	private int oilAttempts;

	public OilPumpBlock() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.waterConsumption = config.getInt("waterConsumption", 10);
		this.oilAttempts = config.getInt("oilAttempts", 20);
	}


	public int getWaterConsumption() {
		return this.waterConsumption;
	}


	public int getOilAttempts() {
		return this.oilAttempts;
	}


	@Override
	public String getTitle() {
		return "§fOil Pump";
	}


	@Override
	public String getMachineRegistryName() {
		return "oil_pump";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.OIL_PUMP;
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
	public OilPumpBlock castCustomBlock() {
		return this;
	}


	@Override
	public OilPump createNewTileEntity(Block block) {
		return new OilPump(this, block);
	}

}
