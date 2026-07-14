
package me.gamma.cookies.object.block.machine;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.FluidPump;
import me.gamma.cookies.util.InventoryUtils;



public class FluidPumpBlock extends AbstractProcessingMachineBlock<FluidPumpBlock, FluidPump> {

	private int capacity;

	public FluidPumpBlock() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.capacity = config.getInt("capacity", 0);
	}


	public int getCapacity() {
		return this.capacity;
	}


	@Override
	public String getTitle() {
		return "§fFluid Pump";
	}


	@Override
	public String getMachineRegistryName() {
		return "fluid_pump";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.FLUID_PUMP;
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
	public FluidPumpBlock castCustomBlock() {
		return this;
	}


	@Override
	public FluidPump createNewTileEntity(Block block) {
		return new FluidPump(this, block);
	}

}
