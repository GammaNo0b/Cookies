
package me.gamma.cookies.object.tile.network.pipes;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import me.gamma.cookies.object.block.network.pipes.PipeBlock;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.object.tile.TileEntityStorage;
import me.gamma.cookies.object.tile.network.pipes.PipePacket.Phase;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class Pipe<P extends PipePacket, T extends Pipe<P, T, B>, B extends PipeBlock<P, B, T>> extends AbstractCustomTileEntity<T, B> {

	private static final String KEY_PACKETS = "packets";

	private final List<P> packets = new ArrayList<>();

	public Pipe(B customBlock, Block block) {
		super(customBlock, block);
	}


	/**
	 * Creates a new empty packet.
	 * 
	 * @return the packet
	 */
	protected abstract P createPacket();


	/**
	 * Returns the speed packets are traversing through this pipe.
	 * 
	 * @return the speed
	 */
	protected double getPacketSpeed() {
		return 1.0D;
	}


	/**
	 * Enters the given packet from the given side into this pipe.
	 * 
	 * @param packet the packet
	 * @param face   the face
	 */
	protected void enterPacket(P packet, BlockFace face) {
		this.updatePacketPhysicsRelative(packet, this.customBlock.getFacePos(this.block, face), this.customBlock.getCenterOffset(this.block));
		packet.phase = Phase.ENTER;
		packet.face = face;
		this.packets.add(packet);
	}


	/**
	 * Handles this packet and returns the direction in which to leave. If {@link BlockFace#SELF} is returned, the packet is removed.
	 * 
	 * @param packet the packet
	 * @param face   the face the packet came from
	 * @return the exit location
	 */
	protected BlockFace handlePacket(P packet, BlockFace face) {
		Map<BlockFace, Pipe<P, ?, ?>> neighbors = this.getNeighboringPipes();
		for(BlockFace f : neighbors.keySet()) {
			if(f == face)
				continue;

			return f;
		}

		return face;
	}


	/**
	 * Is called when the given packet exits this pipe.
	 * 
	 * @param packet the packet
	 * @param face   the face
	 */
	protected void exitPacket(P packet, BlockFace face) {
		Pipe<P, ?, ?> pipe = this.getNeighboringPipe(face);
		if(pipe != null) {
			pipe.enterPacket(packet, face.getOppositeFace());
		} else {
			packet.drop();
		}
	}


	/**
	 * Updates position and velocity of the packet.
	 * 
	 * @param packet   the packet
	 * @param position the relative block position
	 * @param target   the relative target position
	 */
	protected void updatePacketPhysicsRelative(P packet, Vector position, Vector target) {
		packet.ticksLeft += 20.0D / this.getPacketSpeed();
		packet.setPosition(this.block.getLocation().add(position));
		packet.setTarget(this.block.getLocation().add(target));
	}


	@SuppressWarnings("unchecked")
	private Pipe<P, ?, ?> getNeighboringPipe(BlockFace face) {
		AbstractCustomTileEntity<?, ?> pipe = TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(this.block.getRelative(face));
		try {
			return (Pipe<P, ?, ?>) pipe;
		} catch(ClassCastException _) {
			return null;
		}
	}


	/**
	 * Returns neighboring pipes.
	 * 
	 * @return the neighboring pipes
	 */
	protected Map<BlockFace, Pipe<P, ?, ?>> getNeighboringPipes() {
		Map<BlockFace, Pipe<P, ?, ?>> neighbors = new HashMap<>();
		for(BlockFace face : BlockUtils.cartesian) {
			Pipe<P, ?, ?> pipe = this.getNeighboringPipe(face);
			if(pipe != null)
				neighbors.put(face, pipe);

		}
		return neighbors;
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.packets.clear();
		List<PersistentDataObject> packets = data.getObjectList(KEY_PACKETS);
		if(packets != null) {
			for(PersistentDataObject o : packets) {
				P packet = this.createPacket();
				if(packet.load(chunk, o))
					this.packets.add(packet);
			}
		}

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		List<PersistentDataObject> packets = new ArrayList<>();
		for(PipePacket packet : this.packets) {
			PersistentDataObject o = new PersistentDataObject(data.getAdapterContext());
			packet.save(chunk, o);
			packets.add(o);
		}
		data.setObjectList(KEY_PACKETS, packets);

		return true;
	}


	@Override
	public boolean isTicking() {
		return true;
	}


	@Override
	public void tick() {
		ListIterator<P> iterator = this.packets.listIterator();
		List<P> exit = new ArrayList<>();
		while(iterator.hasNext()) {
			P packet = iterator.next();
			if(--packet.ticksLeft >= 1) {
				packet.update();
				continue;
			}

			if(packet.phase == Phase.ENTER) {
				BlockFace face = this.handlePacket(packet, packet.face);
				if(face == BlockFace.SELF) {
					packet.remove();
					iterator.remove();
				} else {
					packet.phase = Phase.EXIT;
					packet.face = face;
					this.updatePacketPhysicsRelative(packet, this.customBlock.getCenterOffset(this.block), this.customBlock.getFacePos(this.block, face));
				}
			} else {
				exit.add(packet);
				iterator.remove();
			}
		}
		exit.forEach(packet -> this.exitPacket(packet, packet.face));
	}


	@Override
	public void destroy() {
		super.destroy();

		for(P packet : this.packets)
			packet.drop();
	}

}
