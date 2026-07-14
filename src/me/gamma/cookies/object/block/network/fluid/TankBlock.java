
package me.gamma.cookies.object.block.network.fluid;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.AbstractCustomTileBlock;
import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.block.UpdatingGuiProvider;
import me.gamma.cookies.object.block.machine.MachineTier;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.ContainerTile;
import me.gamma.cookies.object.tile.network.fluid.Tank;
import me.gamma.cookies.util.InventoryUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class TankBlock extends AbstractCustomTileBlock<TankBlock, Tank> implements UpdatingGuiProvider {

	public static final int BLOCK_FACE_CONFIG_SLOT = 4;

	private final MachineTier tier;
	private final int capacity;

	public TankBlock(MachineTier tier, int capacity) {
		this.tier = tier;
		this.capacity = capacity;
	}


	public MachineTier getTier() {
		return this.tier;
	}


	public int getCapacity() {
		return this.capacity;
	}


	public String getTitle() {
		return this.tier.getName() + " Tank";
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
	protected void transferCustomData(PersistentDataObject tileData, PersistentDataObject itemData) {
		super.transferCustomData(tileData, itemData);

		PersistentDataUtils.setFluid(itemData, Tank.KEY_FLUID, PersistentDataUtils.getFluid(tileData, Tank.KEY_FLUID));
	}


	@Override
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, block, stack, event))
			return false;

		this.openGui(player, block, true, true);

		return true;
	}


	@Override
	public TankBlock castCustomBlock() {
		return this;
	}


	@Override
	public Tank createNewTileEntity(Block block) {
		return new Tank(this, block);
	}


	@Override
	public String getTitle(Block data) {
		return this.getTitle();
	}


	@Override
	public int getIdentifierSlot() {
		return 0;
	}


	@Override
	public int rows() {
		return 6;
	}


	@Override
	public ContainerTile getContainer(Block block) {
		return this.getTileEntity(block);
	}


	@Override
	public Inventory createGui(Block data) {
		Inventory gui = UpdatingGuiProvider.super.createGui(data);
		InventoryUtils.fillBorder(gui, InventoryUtils.filler(Material.GRAY_STAINED_GLASS_PANE));
		gui.setItem(BLOCK_FACE_CONFIG_SLOT, BlockFaceConfigurable.BLOCK_FACE_CONFIG_ICON);
		return gui;
	}

}
