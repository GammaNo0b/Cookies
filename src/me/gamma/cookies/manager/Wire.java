
package me.gamma.cookies.manager;


import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.entity.LivingEntity;

import me.gamma.cookies.object.block.network.WireHolder;



public class Wire {

	private final Location pos1;
	private final Location pos2;
	private final WireHolder holder1;
	private final WireHolder holder2;
	private final LivingEntity holder;
	private final LivingEntity held;

	public Wire(TileState state1, TileState state2, WireHolder holder1, WireHolder holder2, BlockFace face1, BlockFace face2) {
		this.pos1 = state1.getLocation();
		this.pos2 = state2.getLocation();
		this.holder1 = holder1;
		this.holder2 = holder2;
		this.holder = WireManager.spawnWireHook(holder1.getWireLocation(state1, face1), WireManager.Y_OFFSET_HOLDER);
		this.held = WireManager.spawnWireHook(holder2.getWireLocation(state2, face2), WireManager.Y_OFFSET_HELD);

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


	public void destroy() {
		if(this.pos1.getBlock().getState() instanceof TileState state1)
			this.holder1.removeWire(state1, this);

		if(this.pos2.getBlock().getState() instanceof TileState state2)
			this.holder2.removeWire(state2, this);

		this.holder.remove();
		this.held.remove();
	}


	@Override
	public int hashCode() {
		return this.holder.hashCode() ^ this.held.hashCode();
	}

}
