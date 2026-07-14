
package me.gamma.cookies.object.tile.machine;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.block.machine.FluidPumpBlock;
import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidSupplier;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class FluidPump extends AbstractProcessingMachine<FluidPump, FluidPumpBlock> implements FluidSupplier, BlockFaceConfigurable {

	private static final int INFINITY_POOL_SIZE = 1000;

	private static final String KEY_FLUID = "fluid";

	private final int capacity;

	private byte fluidOutputAccessFlags = 0x3f;
	private Fluid fluid = new Fluid(FluidType.EMPTY);
	private int processing = 0;
	private int infinityPoolUpdateTicks = 0;
	private boolean hasInfinityPool = false;

	public FluidPump(FluidPumpBlock customBlock, Block block) {
		super(customBlock, block);

		this.capacity = this.customBlock.getCapacity();
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		FluidSupplier.super.load(chunk, data);

		PersistentDataUtils.getFluid(data, KEY_FLUID, this.fluid);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		FluidSupplier.super.save(chunk, data);

		PersistentDataUtils.setFluid(data, KEY_FLUID, this.fluid);

		return true;
	}


	@Override
	public void listBlockFaceProperties(List<BlockFaceConfig.Config> configs) {
		configs.add(this.createFluidOutputBlockFaceConfig());
	}


	private void updateInfinityPool() {
		Block below = this.block.getRelative(0, -1, 0);
		if(below.getBlockData() instanceof Levelled levelled && levelled.getLevel() == 0) {
			Material type = below.getType();
			if(type == Material.WATER || type == Material.LAVA) {
				this.hasInfinityPool = this.isInfinityPool(below, type, new HashSet<>());
				return;
			}
		}

		this.hasInfinityPool = false;
	}


	private boolean isInfinityPool(Block block, Material type, Set<Location> visited) {
		if(visited.size() >= INFINITY_POOL_SIZE)
			return true;

		for(BlockFace face : new BlockFace[] { BlockFace.DOWN, BlockFace.SOUTH, BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST }) {
			Block relative = block.getRelative(face);
			if(visited.contains(relative.getLocation()))
				continue;

			if(relative.getType() != type)
				continue;

			visited.add(relative.getLocation());
			if(this.isInfinityPool(relative, type, visited))
				return true;
		}

		return false;
	}


	private Fluid getFluidForPumping() {
		Block below = this.block.getRelative(0, -1, 0);
		if(below.getBlockData() instanceof Levelled levelled && levelled.getLevel() == 0) {
			if(below.getType() == Material.WATER) {
				return new Fluid(FluidType.WATER, 1000);
			} else if(below.getType() == Material.LAVA) {
				return new Fluid(FluidType.LAVA, 1000);
			}
		}
		return new Fluid(FluidType.EMPTY, 0);
	}


	@Override
	public void tick() {
		if(this.infinityPoolUpdateTicks-- <= 0) {
			this.updateInfinityPool();
			this.infinityPoolUpdateTicks = 20;
		}

		super.tick();
		this.tryPushFluid();
		this.updateTank();
	}


	private void updateTank() {
		Inventory gui = this.getInventory();
		int fill = (int) Math.round(this.fluid.getMillibuckets() * 3.0D / this.capacity);
		int i = 0;
		ItemStack stack = new ItemBuilder(this.fluid.getType().createIcon()).addLore("§f" + fluid.getMillibuckets() + " §7mb").build();
		for(; i < fill; i++)
			for(int j = 3; j < 8; j++)
				gui.setItem((3 - i) * 9 + j, stack);
		stack.setType(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(; i < 3; i++)
			for(int j = 3; j < 8; j++)
				gui.setItem((3 - i) * 9 + j, stack);
		gui.setItem(18, new ItemBuilder(Material.BROWN_STAINED_GLASS_PANE).setName("§6Infinity Pool: " + (this.hasInfinityPool ? "§afound" : "§cmissing")).build());
	}


	@Override
	protected int createNextProcess() {
		Fluid pump = this.getFluidForPumping();
		if(pump.isEmpty())
			return 0;

		if(this.fluid.isEmpty()) {
			this.fluid.setType(pump.getType());
			this.fluid.setMillibuckets(0);
			this.processing = pump.getMillibuckets();
		} else if(this.fluid.getType() == pump.getType() && this.fluid.getMillibuckets() + pump.getMillibuckets() <= this.capacity) {
			this.processing = pump.getMillibuckets();
			if(!this.hasInfinityPool)
				this.block.getRelative(0, -1, 0).setType(Material.AIR);
		} else {
			return 0;
		}

		return 200;
	}


	@Override
	protected boolean finishProcess() {
		this.fluid.addMillibuckets(this.processing);
		this.processing = 0;

		this.tryPushFluid();

		return true;
	}


	@Override
	public List<FluidProvider> getFluidOutputs() {
		return List.of(FluidProvider.fromFluid(this.fluid));
	}


	@Override
	public byte getFluidOutputAccessFlags() {
		return this.fluidOutputAccessFlags;
	}


	@Override
	public void setFluidOutputAccessFlags(byte flags) {
		this.fluidOutputAccessFlags = flags;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.BUCKET;
	}


	@Override
	public FluidPump castTileEntity() {
		return this;
	}

}
