
package me.gamma.cookies.object.block.network.energy;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.AbstractCustomTileBlock;
import me.gamma.cookies.object.block.network.Wire;
import me.gamma.cookies.object.tile.network.energy.Battery;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class BatteryBlock extends AbstractCustomTileBlock<BatteryBlock, Battery> {

	private final String identifier;
	private final String texture;
	private final int maximumWireCount;
	private final int capacity;

	public BatteryBlock(String identifier, String texture, int maximumWireCount, int capacity) {
		this.identifier = identifier;
		this.texture = texture;
		this.maximumWireCount = maximumWireCount;
		this.capacity = capacity;
	}


	@Override
	public String getIdentifier() {
		return this.identifier;
	}


	@Override
	public String getBlockTexture() {
		return this.texture;
	}


	public int getMaximumWireCount() {
		return this.maximumWireCount;
	}


	public int getCapacity() {
		return this.capacity;
	}


	@Override
	protected void transferCustomData(PersistentDataObject tileData, PersistentDataObject itemData) {
		super.transferCustomData(tileData, itemData);

		itemData.setInteger(Battery.KEY_ENERGY, tileData.getInteger(Battery.KEY_ENERGY, 0));
	}


	@Override
	public void blockBroken(Block block) {
		super.blockBroken(block);

		Battery connector = this.getTileEntity(block);
		if(connector != null) {
			Wire<Void> wire;
			while((wire = connector.removeWire()) != null)
				ItemUtils.dropItem(wire.getWireItem().get(), block);
		}
	}


	@Override
	public BatteryBlock castCustomBlock() {
		return this;
	}


	@Override
	public Battery createNewTileEntity(Block block) {
		return new Battery(this, block);
	}

}
