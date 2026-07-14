
package me.gamma.cookies.object.block.generator;


import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.block.machine.MachineConstants;
import me.gamma.cookies.object.block.machine.MachineTier;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.tile.generator.FluidGenerator;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.Utils;



public class FluidGeneratorBlock extends AbstractGuiGeneratorBlock<FluidGeneratorBlock, FluidGenerator> {

	private final String name;
	private final String identifier;
	private final String texture;
	private final FluidType fluidType;

	private int capacity;
	private int fluidConsumed;
	private int duration;

	public FluidGeneratorBlock(String name, String texture, MachineTier tier, FluidType fluidType) {
		super(tier);
		this.name = name;
		this.identifier = Utils.getIdentifierFromName(name);
		this.texture = texture;
		this.fluidType = fluidType;
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.capacity = config.getInt("capacity", 0);
		this.fluidConsumed = config.getInt("fluidConsumed", 0);
		this.duration = config.getInt("duration", 0);
	}


	public int getCapacity() {
		return this.capacity;
	}


	public int getFluidConsumed() {
		return this.fluidConsumed;
	}


	public int getDuration() {
		return this.duration;
	}


	public FluidType getFluidType() {
		return this.fluidType;
	}


	@Override
	public String getTitle() {
		return this.tier == null ? this.name : this.tier.getName() + " " + this.name;
	}


	@Override
	protected String getGeneratorRegistryName() {
		return this.identifier;
	}


	@Override
	public String getBlockTexture() {
		return this.texture;
	}


	@Override
	public Inventory createGui(Block data) {
		Inventory gui = super.createGui(data);
		InventoryUtils.fillBorder(gui, MachineConstants.BORDER_MATERIAL);
		for(int slot : new int[] { 12, 14, 21, 23, 30, 32 })
			gui.setItem(slot, MachineConstants.BORDER_MATERIAL);
		return gui;
	}


	@Override
	public FluidGeneratorBlock castCustomBlock() {
		return this;
	}


	@Override
	public FluidGenerator createNewTileEntity(Block block) {
		return new FluidGenerator(this, block);
	}

}
