
package me.gamma.cookies.object.tile.generator;


import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.generator.FluidGeneratorBlock;
import me.gamma.cookies.object.fluid.FluidConsumer;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.util.ColorUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utils;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class FluidGenerator extends AbstractGuiGenerator<FluidGenerator, FluidGeneratorBlock> implements FluidConsumer {

	private static final int PROGRESS_SLOT = 22;

	private static final String KEY_FLUID = "fluid";
	private static final String KEY_GENERATING_TICKS = "generatingticks";

	private byte fluidInputAccessFlags = 0x3f;
	private int fluid = 0;
	private int generatingTicks = 0;

	public FluidGenerator(FluidGeneratorBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		FluidConsumer.super.load(chunk, data);

		this.fluid = data.getInteger(KEY_FLUID, 0);
		this.generatingTicks = data.getInteger(KEY_GENERATING_TICKS, 0);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		FluidConsumer.super.save(chunk, data);

		data.setInteger(KEY_FLUID, this.fluid);
		data.setInteger(KEY_GENERATING_TICKS, this.generatingTicks);

		return true;
	}


	@Override
	public void setupInventory(Inventory inventory) {
		super.setupInventory(inventory);
		this.updateProgress();
		this.updateTank();
	}


	private void updateTank() {
		Inventory gui = this.getInventory();
		int fill = (int) Math.round(this.fluid * 3.0D / this.customBlock.getCapacity());
		int i = 0;
		ItemStack stack = new ItemBuilder(this.customBlock.getFluidType().createIcon()).addLore("§f" + this.fluid + " §7mb").build();
		for(; i < fill; i++)
			for(int j : new int[] { 1, 2, 6, 7 })
				gui.setItem((3 - i) * 9 + j, stack);
		stack.setType(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(; i < 3; i++)
			for(int j : new int[] { 1, 2, 6, 7 })
				gui.setItem((3 - i) * 9 + j, stack);
	}


	/**
	 * Updates the progress icon in the inventory of the given block.
	 */
	protected void updateProgress() {
		String name;
		if(this.generatingTicks == 0) {
			name = "§8Idle";
		} else {
			double d = 1.0D * this.generatingTicks / this.customBlock.getDuration();
			name = String.format("§%c%s", ColorUtils.getProgressColor(d, ColorUtils.STOPLIGHT_PROGRESS), Utils.formatTicks(this.generatingTicks));
		}

		ItemBuilder builder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(name);
		builder.addLore("  §8Generating: §7" + this.getEnergyGeneration() + " §8CC/t");
		this.getInventory().setItem(PROGRESS_SLOT, builder.build());
	}


	@Override
	public void listBlockFaceProperties(List<Config> configs) {
		super.listBlockFaceProperties(configs);
		configs.add(this.createFluidInputBlockFaceConfig());
	}


	@Override
	public List<FluidProvider> getFluidInputs() {
		return List.of(FluidProvider.fromHolder(this.customBlock.getFluidType(), Holder.create(() -> this.fluid, i -> { this.fluid = i; }), this.customBlock.getCapacity()));
	}


	@Override
	public byte getFluidInputAccessFlags() {
		return this.fluidInputAccessFlags;
	}


	@Override
	public void setFluidInputAccessFlags(byte flags) {
		this.fluidInputAccessFlags = flags;
	}


	@Override
	public void tick() {
		super.tick();
		this.tryPullFluid();
		this.updateProgress();
		this.updateTank();
	}


	@Override
	protected boolean fullfillsGeneratingConditions() {
		if(--this.generatingTicks == 0)
			return true;

		final int fluidConsumed = this.customBlock.getFluidConsumed();
		if(this.fluid < fluidConsumed)
			return false;

		this.fluid -= fluidConsumed;
		this.generatingTicks = this.customBlock.getDuration();

		return true;
	}


	@Override
	public FluidGenerator castTileEntity() {
		return this;
	}

}
