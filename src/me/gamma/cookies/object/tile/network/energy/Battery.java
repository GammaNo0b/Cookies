
package me.gamma.cookies.object.tile.network.energy;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

import me.gamma.cookies.manager.WireManager;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.network.Wire;
import me.gamma.cookies.object.block.network.WireComponentType;
import me.gamma.cookies.object.block.network.WireConnector;
import me.gamma.cookies.object.block.network.WireHolder;
import me.gamma.cookies.object.block.network.energy.BatteryBlock;
import me.gamma.cookies.object.item.resources.WireItem;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class Battery extends AbstractCustomTileEntity<Battery, BatteryBlock> implements EnergyStorageBlock, WireConnector<Void> {

	public static final String KEY_ENERGY = "energy";
	public static final String KEY_WIRE_ITEM = "wireitem";

	private int energy = 0;
	private final List<Wire<Void>> wires = new ArrayList<>();

	public Battery(BatteryBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		this.energy = data.getInteger(KEY_ENERGY, 0);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		data.setInteger(KEY_ENERGY, this.energy);

		return true;
	}


	@Override
	public void destroy() {
		super.destroy();

		for(Wire<?> wire : this.getConnectedWires())
			ItemUtils.dropItem(wire.getWireItem().get(), this.block);

		this.removeWires();
	}


	@Override
	public int getEnergy() {
		return this.energy;
	}


	@Override
	public void setEnergy(int energy) {
		this.energy = energy;
	}


	@Override
	public Provider<Void> getWireProvider() {
		return this.getEnergyProvider();
	}


	@Override
	public WireComponentType getWireComponentType() {
		return WireComponentType.STORAGE;
	}


	@Override
	public boolean createWire(WireHolder<Void> holder, Block other, WireItem wireItem) {
		return WireManager.WIRE_MANAGER.createWire(this, this.block, holder, other, wireItem) != null;
	}


	@Override
	public List<Wire<Void>> getConnectedWires() {
		return this.wires;
	}


	@Override
	public int getMaximumWireCount() {
		return this.customBlock.getMaximumWireCount();
	}


	@Override
	public int getEnergyCapacity() {
		return this.customBlock.getCapacity();
	}


	@Override
	public Battery castTileEntity() {
		return this;
	}

}
