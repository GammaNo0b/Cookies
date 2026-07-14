
package me.gamma.cookies.object.tile.machine;


import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Consumer;
import me.gamma.cookies.object.block.machine.ObsidianGeneratorBlock;
import me.gamma.cookies.object.fluid.FluidConsumer;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.collection.Holder;
import me.gamma.cookies.util.collection.PersistentDataObject;
import me.gamma.cookies.util.math.MathHelper;



public class ObsidianGenerator extends AbstractItemGenerationMachine<ObsidianGenerator, ObsidianGeneratorBlock> implements FluidConsumer {

	private static final String KEY_LAVA = "lava";

	private static final int[] output_slots = { 24, 25, 33, 34 };

	private static final Material[] progress_materials = { Material.LIGHT_BLUE_STAINED_GLASS, Material.ICE, Material.PACKED_ICE, Material.BLUE_ICE };
	private static final Material[] lava_materials = { Material.RED_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS_PANE, Material.WHITE_STAINED_GLASS_PANE };

	private byte fluidInputAccessFlags = 0x3f;
	private int lava = 0;

	public ObsidianGenerator(ObsidianGeneratorBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		FluidConsumer.super.load(chunk, data);

		this.lava = data.getInteger(KEY_LAVA, 0);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		FluidConsumer.super.save(chunk, data);

		data.setInteger(KEY_LAVA, this.lava);

		return true;
	}


	@Override
	public void listBlockFaceProperties(List<BlockFaceConfig.Config> config) {
		super.listBlockFaceProperties(config);
		config.add(this.createFluidInputBlockFaceConfig());
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		if(!super.onMainInventoryInteract(player, gui, event))
			return false;

		int slot = event.getSlot();
		for(int i : output_slots)
			if(i == slot)
				return false;

		return true;
	}


	@Override
	public void tick() {
		super.tick();
		this.updateLavaTank();
	}


	@Override
	protected int createNextProcess() {
		this.tryPullFluid();

		if(this.lava < 1000)
			return 0;

		this.lava -= 1000;
		return this.customBlock.getObsidianCoolingTime();
	}


	@Override
	protected boolean finishProcess() {
		while(Consumer.consume(new ItemStack(Material.OBSIDIAN), 1, this.getItemOutputs()) > 0)
			if(!this.tryPushItems())
				return false;

		this.tryPushItems();

		return true;
	}


	/**
	 * Updates the lava level in the inventory of the given block.
	 */
	protected void updateLavaTank() {
		int fill = (int) Math.round(this.lava * 4.0D / this.customBlock.getCapacity());
		Inventory gui = this.getInventory();
		int i = 0;
		for(; i < fill; i++) {
			ItemStack stack = new ItemBuilder(lava_materials[i]).setName("§6Lava: §e" + this.lava + " mb").build();
			gui.setItem(37 - i * 9, stack);
			gui.setItem(38 - i * 9, stack);
		}
		for(; i < 4; i++) {
			ItemStack stack = new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setName("§6Lava: §e" + this.lava + " mb").build();
			gui.setItem(37 - i * 9, stack);
			gui.setItem(38 - i * 9, stack);
		}
	}


	@Override
	protected int[] getOutputSlots() {
		return output_slots;
	}


	@Override
	public List<FluidProvider> getFluidInputs() {
		return List.of(FluidProvider.fromHolder(FluidType.LAVA, Holder.create(() -> this.lava, i -> { this.lava = i; }), this.customBlock.getInternalCapacity()));
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
	protected Material getProgressMaterial(double progress) {
		return progress_materials[MathHelper.clamp(0, 3, (int) (progress * 4))];
	}


	@Override
	public ObsidianGenerator castTileEntity() {
		return this;
	}

}
