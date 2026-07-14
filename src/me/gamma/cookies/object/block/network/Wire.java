
package me.gamma.cookies.object.block.network;


import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.manager.WireManager;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.item.resources.WireItem;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.object.tile.TileEntityStorage;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class Wire<T> {

	private static final String TAG_START = "start";
	private static final String TAG_END = "end";
	private static final String TAG_HOLDER = "holder";
	private static final String TAG_HELD = "held";
	private static final String TAG_ITEM = "item";

	private final Location pos1;
	private final Location pos2;
	private final WireHolder<T> holder1;
	private final WireHolder<T> holder2;
	private final UUID holder;
	private final UUID held;

	private final WireItem wireItem;

	private final int transfer;

	public Wire(Location pos1, Location pos2, WireHolder<T> holder1, WireHolder<T> holder2, UUID holder, UUID held, WireItem wireItem) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.holder1 = holder1;
		this.holder2 = holder2;
		this.holder = holder;
		this.held = held;

		this.holder1.addWire(this);
		this.holder2.addWire(this);

		this.wireItem = wireItem;
		this.transfer = wireItem.getTransfer();
	}


	@SuppressWarnings("unchecked")
	public static <T> Wire<T> load(PersistentDataObject data) {
		Location pos1 = PersistentDataUtils.getLocation(data, TAG_START);
		if(pos1 == null)
			return null;

		Location pos2 = PersistentDataUtils.getLocation(data, TAG_END);
		if(pos2 == null)
			return null;

		WireHolder<T> holder1, holder2;

		try {
			AbstractCustomTileEntity<?, ?> tileEntity = TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(pos1.getBlock());
			if(tileEntity == null || !(tileEntity instanceof WireHolder h1))
				return null;

			holder1 = (WireHolder<T>) h1;

			tileEntity = TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(pos2.getBlock());
			if(tileEntity == null || !(tileEntity instanceof WireHolder h2))
				return null;

			holder2 = (WireHolder<T>) h2;
		} catch(ClassCastException _) {
			return null;
		}

		UUID holder = PersistentDataUtils.getUUID(data, TAG_HOLDER);
		if(holder == null)
			return null;

		UUID held = PersistentDataUtils.getUUID(data, TAG_HELD);
		if(held == null)
			return null;

		String identifier = data.getString(TAG_ITEM);
		if(identifier == null || !(Items.getCustomItemFromIdentifier(identifier) instanceof WireItem item))
			return null;

		return new Wire<>(pos1, pos2, holder1, holder2, holder, held, item);
	}


	public void save(PersistentDataObject data) {
		PersistentDataUtils.setLocation(data, TAG_START, this.pos1);
		PersistentDataUtils.setLocation(data, TAG_END, this.pos2);
		PersistentDataUtils.setUUID(data, TAG_HOLDER, this.holder);
		PersistentDataUtils.setUUID(data, TAG_HELD, this.held);
		data.setString(TAG_ITEM, this.wireItem.getIdentifier());
	}


	public Chunk getFirstChunk() {
		return this.pos1.getChunk();
	}


	public Chunk getSecondChunk() {
		return this.pos2.getChunk();
	}


	public Location getOpposite(Location pos) {
		if(this.pos1.equals(pos))
			return this.pos2;

		if(this.pos2.equals(pos))
			return this.pos1;

		return null;
	}


	public boolean isConnectedTo(Location l) {
		return this.pos1.equals(l) || this.pos2.equals(l);
	}


	public void transfer() {
		WireComponentType type1 = this.holder1.getWireComponentType();
		WireComponentType type2 = this.holder2.getWireComponentType();

		boolean atob = type1.canTransferTo(type2);
		boolean btoa = type2.canTransferTo(type1);

		Provider<T> provider1 = this.holder1.getWireProvider();
		Provider<T> provider2 = this.holder2.getWireProvider();

		if(!provider1.match(provider2.getType()))
			return;

		if(atob) {
			if(btoa) {
				int diff = (provider1.amount() - provider2.amount()) / 2;
				if(diff > 0) {
					this.transfer(provider1, provider2, diff);
				} else if(diff < 0) {
					this.transfer(provider2, provider1, -diff);
				}
			} else {
				this.transfer(provider1, provider2, this.transfer);
			}
		} else if(btoa) {
			this.transfer(provider2, provider1, this.transfer);
		}
	}


	private void transfer(Provider<T> from, Provider<T> to, int max) {
		int transfer = from.get(max);
		if(transfer <= 0)
			return;

		int rest = to.set(transfer);
		if(rest <= 0)
			return;

		from.add(rest);
	}


	public void destroy() {
		this.holder1.removeWire(this);
		this.holder2.removeWire(this);

		Entity e;
		e = Bukkit.getEntity(this.holder);
		if(e != null)
			e.remove();
		e = Bukkit.getEntity(this.held);
		if(e != null)
			e.remove();

		WireManager.WIRE_MANAGER.removeWire(this);
	}


	@Override
	public int hashCode() {
		return this.holder.hashCode() ^ this.held.hashCode();
	}


	public WireItem getWireItem() {
		return this.wireItem;
	}

}
