
package me.gamma.cookies.object.block.network;


import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.entity.LivingEntity;

import me.gamma.cookies.manager.WireManager;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.item.resources.WireItem;



public class Wire<T> {

	private final Location pos1;
	private final Location pos2;
	private final WireHolder<T> holder1;
	private final WireHolder<T> holder2;
	private final LivingEntity holder;
	private final LivingEntity held;

	private final AbstractCustomItem wireItem;

	private final int transfer;

	public Wire(TileState state1, TileState state2, WireHolder<T> holder1, WireHolder<T> holder2, BlockFace face1, BlockFace face2, WireItem wireItem) {
		this.pos1 = state1.getLocation();
		this.pos2 = state2.getLocation();
		this.holder1 = holder1;
		this.holder2 = holder2;
		this.holder = WireManager.spawnWireHook(holder1.getWireLocation(state1, face1), WireManager.Y_OFFSET_HOLDER);
		this.held = WireManager.spawnWireHook(holder2.getWireLocation(state2, face2), WireManager.Y_OFFSET_HELD);

		this.wireItem = wireItem;
		this.transfer = wireItem.getTransfer();

		this.holder1.addWire(state1, this);
		this.holder2.addWire(state2, this);
		this.held.setLeashHolder(this.holder);
	}


	public Location getOpposite(Location pos) {
		if(this.pos1.equals(pos))
			return this.pos2;

		if(this.pos2.equals(pos))
			return this.pos1;

		return null;
	}


	public void transfer() {
		WireComponentType type1 = this.holder1.getWireComponentType();
		WireComponentType type2 = this.holder2.getWireComponentType();

		boolean atob = type1.canTransferTo(type2);
		boolean btoa = type2.canTransferTo(type1);

		if(!(this.pos1.getBlock().getState() instanceof TileState state1))
			return;
		if(!(this.pos2.getBlock().getState() instanceof TileState state2))
			return;

		Provider<T> provider1 = this.holder1.getWireProvider(state1);
		Provider<T> provider2 = this.holder2.getWireProvider(state2);

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
		if(this.pos1.getBlock().getState() instanceof TileState state1)
			this.holder1.removeWire(state1, this);

		if(this.pos2.getBlock().getState() instanceof TileState state2)
			this.holder2.removeWire(state2, this);

		this.holder.remove();
		this.held.remove();

		WireManager.removeWire(this);
	}


	@Override
	public int hashCode() {
		return this.holder.hashCode() ^ this.held.hashCode();
	}


	public AbstractCustomItem getWireItem() {
		return this.wireItem;
	}

}
