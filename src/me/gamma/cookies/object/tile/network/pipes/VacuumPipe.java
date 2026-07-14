
package me.gamma.cookies.object.tile.network.pipes;


import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;

import me.gamma.cookies.object.block.network.pipes.VacuumPipeBlock;
import me.gamma.cookies.util.EntityUtils;



public abstract class VacuumPipe<P extends PipePacket, T extends VacuumPipe<P, T, B>, B extends VacuumPipeBlock<P, B, T>> extends Pipe<P, T, B> {

	private static final double MIN_ATTRACTION_RADIUS = 1.0D;
	private static final double MAX_ATTRACTION_RADIUS = 10.0D;

	public VacuumPipe(B customBlock, Block block) {
		super(customBlock, block);
	}


	/**
	 * Returns whether the given entity should be attracted towards this vacuum pipe.
	 * 
	 * @param entity the entity
	 * @return whether it should be attracted
	 */
	protected abstract boolean attractsEntity(Entity entity);

	/**
	 * Creates a new packet from the given entity.
	 * 
	 * @param entity the entity
	 * @return the packet
	 */
	protected abstract P createPacket(Entity entity);


	protected void attractEntities() {
		final BlockFace direction = this.customBlock.getFacing(this.block).getOppositeFace();
		final Location center = this.getBlock().getLocation().add(0.5D, 0.5D, 0.5D);
		for(Entity entity : this.block.getWorld().getNearbyEntities(this.block.getLocation(), MAX_ATTRACTION_RADIUS, MAX_ATTRACTION_RADIUS, MAX_ATTRACTION_RADIUS, this::attractsEntity)) {
			if(entity.getLocation().distanceSquared(center) < MIN_ATTRACTION_RADIUS * MIN_ATTRACTION_RADIUS) {
				P packet = this.createPacket(entity);
				if(packet != null) {
					this.enterPacket(packet, direction);
					entity.remove();
				}
			} else {
				EntityUtils.attract(entity, center, 0.3D, MAX_ATTRACTION_RADIUS);
			}
		}
	}


	@Override
	public void tick() {
		this.attractEntities();

		super.tick();
	}

}
