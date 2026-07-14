
package me.gamma.cookies.object.block.machine;


import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.machine.BioPress;
import me.gamma.cookies.util.InventoryUtils;



public class BioPressBlock extends AbstractProcessingMachineBlock<BioPressBlock, BioPress> {

	private int capacity;

	public BioPressBlock(MachineTier tier) {
		super(tier);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.capacity = config.getInt("capacity", 0);
	}


	@Override
	public String getTitle() {
		return this.tier.getName() + " Bio Press";
	}


	@Override
	public String getMachineRegistryName() {
		return "bio_press";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.BIO_PRESS;
	}


	@Override
	public Inventory createGui(Block block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		InventoryUtils.fillTopBottom(gui, MachineConstants.BORDER_MATERIAL);
		for(int slot : new int[] { 14, 17, 23, 26, 32, 35 })
			gui.setItem(slot, MachineConstants.BORDER_MATERIAL);
		for(int slot : MachineConstants.inputBorderSlots[this.tier.ordinal()])
			gui.setItem(slot, MachineConstants.INPUT_BORDER_MATERIAL);
		for(int slot : MachineConstants.fillerSlots[this.tier.ordinal()])
			gui.setItem(slot, MachineConstants.FILLER_MATERIAL);
		return gui;
	}


	@Override
	public BioPressBlock castCustomBlock() {
		return this;
	}


	@Override
	public BioPress createNewTileEntity(Block block) {
		return new BioPress(this, block);
	}


	/**
	 * Returns the fluid capacity of this block.
	 * 
	 * @return the capacity
	 */
	public int getBiomassCapacity() {
		return this.capacity;
	}

}
