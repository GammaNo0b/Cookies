
package me.gamma.cookies.object.tile.network.pipes;


import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import me.gamma.cookies.object.block.network.pipes.ExtractionPipeBlock;
import me.gamma.cookies.util.collection.PersistentDataObject;



public abstract class ExtractionPipe<P extends PipePacket, T extends ExtractionPipe<P, T, B>, B extends ExtractionPipeBlock<P, B, T>> extends Pipe<P, T, B> {

	private static final String KEY_EXTRACTION_TICKS = "extractionticks";

	private int extractionTicks = 0;

	public ExtractionPipe(B customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.extractionTicks = data.getInteger(KEY_EXTRACTION_TICKS, 0);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		data.setInteger(KEY_EXTRACTION_TICKS, this.extractionTicks);

		return true;
	}


	/**
	 * Extracts resources from the given block and returns them inside a new packet if successful.
	 * 
	 * @param target the target block
	 * @return the resource packet
	 */
	protected abstract P extractResource(Block target);


	@Override
	public void tick() {
		super.tick();

		if(--this.extractionTicks > 0)
			return;

		this.extractionTicks = this.customBlock.extractionTicks();
		BlockFace face = this.customBlock.getFacing(this.block).getOppositeFace();
		Block target = this.block.getRelative(face);
		P packet = this.extractResource(target);
		if(packet != null)
			this.enterPacket(packet, face);
	}

}
