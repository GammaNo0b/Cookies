
package me.gamma.cookies.object.block.network.energy;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.manager.Wire;
import me.gamma.cookies.object.Cable.TransferMode;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.BlockTicker;
import me.gamma.cookies.object.block.network.WireConnector;
import me.gamma.cookies.object.energy.EnergyProvider;
import me.gamma.cookies.object.property.ListProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.object.property.VectorProperty;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class EnergyWireConnector extends AbstractCustomBlock implements BlockTicker, EnergyStorageBlock, WireConnector, EnergyCable {

	private static final ListProperty<Vector, VectorProperty> WIRES = Properties.WIRE_POSITIONS;

	private final HashSet<Location> locations = new HashSet<>();
	private final HashMap<Location, List<Wire>> wires = new HashMap<>();

	private final int maximumWireCount;
	private final int capacity;
	private final int transferRate;

	public EnergyWireConnector(int maximumWireCount, int capacity, int transferRate) {
		this.maximumWireCount = maximumWireCount;
		this.capacity = capacity;
		this.transferRate = transferRate;
	}


	@Override
	public boolean load(TileState block, PersistentDataObject data) {
		List<Vector> poslist = WIRES.fetch(data.getContainer());
		for(Vector v : poslist) {
			Location pos = v.toLocation(block.getWorld());
			this.createWire(block, pos);
		}

		return true;
	}


	@Override
	public boolean save(TileState block, PersistentDataObject data) {
		Location pos = block.getLocation();
		List<Vector> poslist = new ArrayList<>();
		for(Wire wire : this.getConnectedWires(block)) {
			Location opposite = wire.getOpposite(pos);
			if(opposite != null)
				poslist.add(opposite.toVector());
		}
		this.removeAllWires(block);
		WIRES.store(data.getContainer(), poslist);

		return true;
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(INTERNAL_STORAGE);
	}


	@Override
	protected PropertyBuilder buildBlockProperties(PropertyBuilder builder) {
		return super.buildBlockProperties(builder);
	}


	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public long getDelay() {
		return 1;
	}


	@Override
	public List<Wire> getConnectedWires(TileState block) {
		List<Wire> wires = this.wires.get(block.getLocation());
		if(wires == null) {
			wires = new ArrayList<>();
			this.wires.put(block.getLocation(), wires);
		}
		return wires;
	}


	@Override
	public int getMaximumWireCount(TileState block) {
		return this.maximumWireCount;
	}


	@Override
	public void tick(TileState block) {
		this.transmitEnergy(block);
	}


	@Override
	public int getEnergyCapacity() {
		return this.capacity;
	}


	@Override
	public TransferMode getTransferMode(TileState block) {
		return TransferMode.ROUND_ROBIN;
	}


	@Override
	public int getTransferRate(TileState block) {
		return this.transferRate;
	}


	@Override
	public EnergyProvider getBuffer(TileState block) {
		return this.getEnergyProvider(block);
	}


	@Override
	public Stream<TileState> getNeighbors(TileState block) {
		return this.getConnectedWires(block).stream().map(wire -> wire.getOpposite(block.getLocation())).map(pos -> pos.getBlock().getState() instanceof TileState state ? state : null).filter(Objects::nonNull);
	}


	@Override
	public boolean onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(super.onBlockBreak(player, block, event))
			return true;

		int amount = this.removeAllWires(block);
		for(int i = 0; i < amount; i++)
			ItemUtils.dropItem(Items.INSULATED_COPPER_WIRE.get(), block);

		return false;
	}

}
