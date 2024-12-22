
package me.gamma.cookies.object.block.machine;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.block.data.Levelled;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidSupplier;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.BooleanProperty;
import me.gamma.cookies.object.property.FluidProperty;
import me.gamma.cookies.object.property.IntegerProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;



public class FluidPump extends AbstractProcessingMachine implements FluidSupplier, BlockFaceConfigurable {

	private static final int INFINITY_POOL_SIZE = 1000;

	public static final IntegerProperty INFINITY_POOL_TICKER = new IntegerProperty("infpoolcheck");
	public static final BooleanProperty HAS_INFINITY_POOL = new BooleanProperty("hasinfpool");
	public static final FluidProperty FLUID = Properties.FLUID;
	public static final FluidProperty PROCESSING = new FluidProperty("processing");

	private int capacity;

	public FluidPump() {
		super(null);
	}


	@Override
	public void configure(ConfigurationSection config) {
		super.configure(config);

		this.capacity = config.getInt("capacity", 0);
	}


	private void updateInfinityPool(TileState block) {
		Block below = block.getBlock().getRelative(0, -1, 0);
		if(below.getBlockData() instanceof Levelled levelled && levelled.getLevel() == 0) {
			Material type = below.getType();
			if(type == Material.WATER || type == Material.LAVA) {
				HAS_INFINITY_POOL.store(block, this.isInfinityPool(below, type, new HashSet<>()));
				return;
			}
		}

		HAS_INFINITY_POOL.store(block, false);
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

			if(!(relative.getBlockData() instanceof Levelled levelled) || levelled.getLevel() != 0)
				continue;

			visited.add(block.getLocation());
			if(this.isInfinityPool(relative, type, visited))
				return true;
		}

		return false;
	}


	private Fluid getFluidForPumping(TileState block) {
		Block below = block.getBlock().getRelative(0, -1, 0);
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
	protected int createNextProcess(TileState block) {
		Fluid pump = this.getFluidForPumping(block);
		if(pump.isEmpty())
			return 0;

		Fluid stored = FLUID.fetch(block);
		if(stored.isEmpty() || stored.getType() == pump.getType() && stored.getMillibuckets() + pump.getMillibuckets() <= this.capacity) {
			PROCESSING.store(block, pump);
			if(!HAS_INFINITY_POOL.fetch(block))
				block.getBlock().getRelative(0, -1, 0).setType(Material.AIR);
			return 200;
		}

		return 0;
	}


	@Override
	protected void proceed(TileState block, int progress, int goal) {}


	@Override
	protected boolean finishProcess(TileState block) {
		Fluid pumped = PROCESSING.fetch(block);
		Fluid stored = FLUID.fetch(block);
		stored.setType(pumped.getType());
		stored.addMillibuckets(pumped.getMillibuckets());
		FLUID.store(block, stored);

		this.tryPushFluid(block);

		return true;
	}


	@Override
	protected Material getProgressMaterial(double progress) {
		return Material.BUCKET;
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
	public List<FluidProvider> getFluidOutputs(TileState block) {
		return List.of(FluidProvider.fromProperty(FLUID, block, this.capacity));
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder).add(INFINITY_POOL_TICKER).add(HAS_INFINITY_POOL);
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(FLUID).add(PROCESSING).add(FLUID_OUTPUT_ACCESS_FLAGS, (byte) 0x3F);
	}


	@Override
	public void listBlockFaceProperties(List<BlockFaceConfig.Config> configs) {
		configs.add(this.createEnergyInputBlockFaceConfig());
		configs.add(this.createFluidOutputBlockFaceConfig());
	}


	@Override
	protected int getBlockFaceConfigSlot() {
		return 1;
	}


	@Override
	protected int getUpgradeSlot() {
		return 10;
	}


	@Override
	protected int getProgressSlot() {
		return 19;
	}


	@Override
	protected int getRedstoneModeSlot() {
		return 28;
	}


	@Override
	protected int getEnergyLevelSlot() {
		return 37;
	}


	@Override
	public Inventory createGui(TileState block) {
		Inventory gui = InventoryUtils.createBasicInventoryProviderGui(this, block);
		ItemStack filler = InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE);
		InventoryUtils.fillBorder(gui, filler);
		gui.setItem(11, filler);
		gui.setItem(20, filler);
		gui.setItem(29, filler);
		return gui;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player player, TileState data, PlayerInventory gui, InventoryClickEvent event) {
		return true;
	}


	@Override
	public void tick(TileState block) {
		int infpoolticks = INFINITY_POOL_TICKER.fetch(block);
		if(infpoolticks <= 0) {
			this.updateInfinityPool(block);
			INFINITY_POOL_TICKER.store(block, 20);
		} else {
			INFINITY_POOL_TICKER.store(block, infpoolticks - 1);
		}

		super.tick(block);
		this.updateTank(block);
	}


	private void updateTank(TileState block) {
		Fluid fluid = FLUID.fetch(block);
		Inventory gui = this.getGui(block);
		int fill = (int) Math.round(fluid.getMillibuckets() * 3.0D / this.capacity);
		int i = 0;
		ItemStack stack = new ItemBuilder(fluid.getType().createIcon()).addLore("§f" + fluid.getMillibuckets() + " §7mb").build();
		for(; i < fill; i++)
			for(int j = 3; j < 8; j++)
				gui.setItem((3 - i) * 9 + j, stack);
		stack.setType(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(; i < 3; i++)
			for(int j = 3; j < 8; j++)
				gui.setItem((3 - i) * 9 + j, stack);
	}

}
