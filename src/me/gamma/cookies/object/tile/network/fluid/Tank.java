
package me.gamma.cookies.object.tile.network.fluid;


import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.block.BlockInventoryProvider;
import me.gamma.cookies.object.block.network.fluid.TankBlock;
import me.gamma.cookies.object.fluid.Fluid;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidStorage;
import me.gamma.cookies.object.fluid.FluidType;
import me.gamma.cookies.object.gui.BlockFaceConfig.Config;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.object.tile.ContainerTile;
import me.gamma.cookies.util.ItemBuilder;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class Tank extends AbstractCustomTileEntity<Tank, TankBlock> implements FluidStorage, ContainerTile, BlockFaceConfigurable {

	public static final String KEY_FLUID = "fluid";

	private byte fluidInputAccessFlags = 0x3F;
	private byte fluidOutputAccessFlags = 0x3F;

	private final Fluid fluid = new Fluid(FluidType.EMPTY);

	public Tank(TankBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		PersistentDataUtils.getFluid(data, KEY_FLUID, this.fluid);

		FluidStorage.super.load(chunk, data);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		PersistentDataUtils.setFluid(data, KEY_FLUID, this.fluid);

		FluidStorage.super.save(chunk, data);

		return true;
	}


	@Override
	public void setupInventory(Inventory inventory) {
		ContainerTile.super.setupInventory(inventory);
		this.updateTank();
	}


	@Override
	public void listBlockFaceProperties(List<Config> configs) {
		configs.add(this.createFluidInputBlockFaceConfig());
		configs.add(this.createFluidOutputBlockFaceConfig());
	}


	@Override
	public boolean isTicking() {
		return true;
	}


	@Override
	public boolean onMainInventoryInteract(Player player, Inventory gui, InventoryClickEvent event) {
		if(event.getSlot() == TankBlock.BLOCK_FACE_CONFIG_SLOT)
			this.openBlockFaceConfig(player);

		return true;
	}


	@Override
	public List<FluidProvider> getFluidProviders() {
		return List.of(FluidProvider.fromFluid(this.fluid, this.customBlock.getCapacity()));
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
	public byte getFluidOutputAccessFlags() {
		return this.fluidOutputAccessFlags;
	}


	@Override
	public void setFluidOutputAccessFlags(byte flags) {
		this.fluidOutputAccessFlags = flags;
	}


	@Override
	public void tick() {
		this.tryPushFluid();
		this.tryPullFluid();
		this.updateTank();
	}


	private void updateTank() {
		Inventory gui = this.getInventory();
		int millibuckets = this.fluid.getMillibuckets();
		int fill = (int) Math.round(millibuckets * 4.0D / this.customBlock.getCapacity());
		int i = 0;
		ItemStack stack = new ItemBuilder(this.fluid.getType().createIcon()).addLore("§f" + millibuckets + " §7mb").build();
		for(; i < fill; i++)
			for(int j = 1; j < 8; j++)
				gui.setItem((4 - i) * 9 + j, stack);
		stack.setType(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		for(; i < 4; i++)
			for(int j = 1; j < 8; j++)
				gui.setItem((4 - i) * 9 + j, stack);
	}


	@Override
	public Tank castTileEntity() {
		return this;
	}


	@Override
	public BlockInventoryProvider getInventoryProvider() {
		return this.customBlock;
	}

}
