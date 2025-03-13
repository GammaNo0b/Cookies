
package me.gamma.cookies.object.block.network.fluid;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.block.AbstractWorkBlock;
import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.block.UpdatingGuiProvider;
import me.gamma.cookies.object.block.machine.MachineTier;
import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidStorage;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.FluidProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.math.MathHelper;



public class Tank extends AbstractWorkBlock implements FluidStorage, UpdatingGuiProvider, BlockFaceConfigurable {

	public static final FluidProperty FLUID = Properties.FLUID;

	private final Set<Location> locations = new HashSet<>();
	private final Map<Location, Inventory> inventories = new HashMap<>();

	private final MachineTier tier;
	private final int capacity;

	public Tank(MachineTier tier) {
		this.tier = tier;
		this.capacity = 1000 * MathHelper.intpow(4, this.tier.getTier());
	}


	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public Map<Location, Inventory> getInventoryMap() {
		return this.inventories;
	}


	@Override
	public String getTitle(TileState data) {
		return this.tier.getName() + " Tank";
	}


	public int getCapacity() {
		return this.capacity;
	}


	@Override
	public int rows() {
		return 6;
	}


	@Override
	public String getIdentifier() {
		return "tank_tier_" + this.tier.name().toLowerCase();
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.COPPER_TANK;
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	protected int getWorkPeriod() {
		return 20;
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(FLUID).add(FLUID_INPUT_ACCESS_FLAGS, (byte) 0x3F).add(FLUID_OUTPUT_ACCESS_FLAGS, (byte) 0x3F);
	}


	@Override
	public void listBlockFaceProperties(List<Config> configs) {
		configs.add(this.createFluidInputBlockFaceConfig());
		configs.add(this.createFluidOutputBlockFaceConfig());
	}


	@Override
	public Inventory createGui(TileState data) {
		Inventory gui = UpdatingGuiProvider.super.createGui(data);
		InventoryUtils.fillBorder(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		gui.setItem(4, BLOCK_FACE_CONFIG_ICON);
		return gui;
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		this.openGui(player, block, true, true);
		return true;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, TileState data, Inventory gui, InventoryClickEvent event) {
		if(event.getSlot() == 4)
			this.openBlockFaceConfig(player, data);

		return true;
	}


	@Override
	public boolean onPlayerInventoryInteract(Player player, TileState data, PlayerInventory gui, InventoryClickEvent event) {
		return true;
	}


	@Override
	public void tick(TileState block) {
		this.tryPushFluid(block);
		this.tryPullFluid(block);
		this.updateTank(block);
	}


	@Override
	public List<FluidProvider> getFluidProviders(PersistentDataHolder holder) {
		return Arrays.asList(FluidProvider.fromProperty(FLUID, holder, this.capacity));
	}


	private void updateTank(TileState block) {
		Fluid fluid = FLUID.fetch(block);
		Inventory gui = this.getGui(block);
		int millibuckets = fluid.getMillibuckets();
		int fill = (int) Math.round(millibuckets * 4.0D / this.capacity);
		int i = 0;
		ItemStack stack = new ItemBuilder(fluid.getType().createIcon()).addLore("§f" + millibuckets + " §7mb").build();
		for(; i < fill; i++)
			for(int j = 1; j < 8; j++)
				gui.setItem((4 - i) * 9 + j, stack);
		stack.setType(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(; i < 4; i++)
			for(int j = 1; j < 8; j++)
				gui.setItem((4 - i) * 9 + j, stack);
	}

}
