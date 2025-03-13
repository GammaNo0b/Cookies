
package me.gamma.cookies.object.block.generator;


import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.block.machine.MachineConstants;
import me.gamma.cookies.object.block.machine.MachineTier;
import me.gamma.cookies.object.fluid.FluidConsumer;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.ColorUtils;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.Utils;



public class FluidGenerator extends AbstractGuiGenerator implements FluidConsumer {

	public static final int PROGRESS_SLOT = 22;

	public static final IntegerProperty FLUID = new IntegerProperty("fluid");
	public static final IntegerProperty GENERATING = new IntegerProperty("generating", 0, Integer.MAX_VALUE);

	private final String name;
	private final String identifier;
	private final String texture;
	private final FluidType fluidType;

	private int capacity;
	private int fluidConsumed;
	private int duration;

	public FluidGenerator(String name, String texture, MachineTier tier, FluidType fluidType) {
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


	@Override
	public void setupInventory(TileState block, Inventory inventory) {
		super.setupInventory(block, inventory);
		this.updateProgress(block);
		this.updateTank(block);
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(GENERATING);
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(FLUID).add(FLUID_INPUT_ACCESS_FLAGS, (byte) 0x3F);
	}


	@Override
	public void listBlockFaceProperties(List<Config> configs) {
		super.listBlockFaceProperties(configs);
		configs.add(this.createFluidInputBlockFaceConfig());
	}


	@Override
	public String getTitle() {
		return this.tier == null ? "§f" + this.name : this.tier.getName() + " " + this.name;
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
	public String getTitle(TileState data) {
		return this.getTitle();
	}


	@Override
	public Inventory createGui(TileState data) {
		Inventory gui = super.createGui(data);
		InventoryUtils.fillBorder(gui, MachineConstants.BORDER_MATERIAL);
		for(int slot : new int[] { 12, 14, 21, 23, 30, 32 })
			gui.setItem(slot, MachineConstants.BORDER_MATERIAL);
		return gui;
	}


	@Override
	public void tick(TileState block) {
		super.tick(block);
		this.tryPullFluid(block);
		this.updateProgress(block);
		this.updateTank(block);
	}


	@Override
	protected boolean fullfillsGeneratingConditions(TileState block) {
		if(GENERATING.decrease(block, 1) == 0)
			return true;

		int fluid = FLUID.fetch(block);
		if(fluid < this.fluidConsumed)
			return false;

		FLUID.store(block, fluid - this.fluidConsumed);
		GENERATING.store(block, this.duration);
		block.update();
		return true;
	}


	@Override
	public List<FluidProvider> getFluidInputs(PersistentDataHolder holder) {
		return List.of(FluidProvider.fromProperty(this.fluidType, FLUID, holder, this.capacity));
	}


	private void updateTank(TileState block) {
		int fluid = FLUID.fetch(block);
		Inventory gui = this.getGui(block);
		int fill = (int) Math.round(fluid * 3.0D / this.capacity);
		int i = 0;
		ItemStack stack = new ItemBuilder(this.fluidType.createIcon()).addLore("§f" + fluid + " §7mb").build();
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
	 * 
	 * @param block the block
	 */
	protected void updateProgress(TileState block) {
		int generating = GENERATING.fetch(block);
		String name;
		if(generating == 0) {
			name = "§8Idle";
		} else {
			double d = 1.0D * generating / this.duration;
			name = String.format("§%c%s", ColorUtils.getProgressColor(d, ColorUtils.STOPLIGHT_PROGRESS), Utils.formatTicks(generating));
		}

		ItemBuilder builder = new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(name);
		builder.addLore("  §8Generating: §7" + this.getEnergyGeneration(block) + " §8CC/t");
		this.getGui(block).setItem(PROGRESS_SLOT, builder.build());
	}

}
