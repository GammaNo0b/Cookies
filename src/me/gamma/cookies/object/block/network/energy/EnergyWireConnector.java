
package me.gamma.cookies.object.block.network.energy;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.TileBlockRegister;
import me.gamma.cookies.object.block.network.Wire;
import me.gamma.cookies.object.block.network.WireComponentType;
import me.gamma.cookies.object.block.network.WireConnector;
import me.gamma.cookies.object.energy.EnergyProvider;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.resources.WireItem;
import me.gamma.cookies.object.property.ListProperty;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.object.property.StringProperty;
import me.gamma.cookies.object.property.VectorProperty;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class EnergyWireConnector extends AbstractCustomBlock implements TileBlockRegister, EnergyStorageBlock, WireConnector<Void> {

	private static final ListProperty<Vector, VectorProperty> WIRES = Properties.WIRE_POSITIONS;
	private static final ListProperty<String, StringProperty> WIRE_ITEMS = Properties.WIRE_ITEMS;

	private final HashSet<Location> locations = new HashSet<>();
	private final HashMap<Location, List<Wire<Void>>> wires = new HashMap<>();

	private final int maximumWireCount;
	private final int capacity;

	public EnergyWireConnector(int maximumWireCount, int capacity) {
		this.maximumWireCount = maximumWireCount;
		this.capacity = capacity;
	}


	@Override
	public boolean load(TileState block, PersistentDataObject data) {
		List<Vector> poslist = WIRES.fetch(data.getContainer());
		List<String> itemlist = WIRE_ITEMS.fetch(data.getContainer());

		for(int i = 0; i < Math.min(poslist.size(), itemlist.size()); i++) {
			String identifier = itemlist.get(i);
			AbstractCustomItem item = Items.getCustomItemFromIdentifier(identifier);
			if(item == null || !(item instanceof WireItem wire))
				continue;

			Vector v = poslist.get(i);
			Location pos = v.toLocation(block.getWorld());
			this.createWire(block, pos, wire);
		}

		return true;
	}


	@Override
	public boolean save(TileState block, PersistentDataObject data) {
		Location pos = block.getLocation();
		List<Vector> poslist = new ArrayList<>();
		List<String> itemlist = new ArrayList<>();
		for(Wire<Void> wire : this.getConnectedWires(block)) {
			Location opposite = wire.getOpposite(pos);
			if(opposite != null) {
				poslist.add(opposite.toVector());
				itemlist.add(wire.getWireItem().getIdentifier());
			}
		}

		while(this.removeWire(block) != null);

		WIRES.store(data.getContainer(), poslist);
		WIRE_ITEMS.store(data.getContainer(), itemlist);

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
	public List<Wire<Void>> getConnectedWires(TileState block) {
		List<Wire<Void>> wires = this.wires.get(block.getLocation());
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
	public int getEnergyCapacity() {
		return this.capacity;
	}


	@Override
	public EnergyProvider getWireProvider(TileState block) {
		return this.getEnergyProvider(block);
	}


	@Override
	public WireComponentType getWireComponentType() {
		return WireComponentType.STORAGE;
	}


	@Override
	public boolean onBlockBreak(Player player, TileState block, BlockBreakEvent event) {
		if(super.onBlockBreak(player, block, event))
			return true;

		Wire<Void> wire;
		while((wire = this.removeWire(block)) != null)
			ItemUtils.dropItem(wire.getWireItem().get(), block);

		return false;
	}

}
