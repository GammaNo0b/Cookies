
package me.gamma.cookies.object.block.network.pipes.item;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.pipes.ExtractionPipeBlock;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.pipes.item.ItemExtractionPipe;
import me.gamma.cookies.object.tile.network.pipes.item.ItemPacket;



public class ItemExtractionPipeBlock extends ExtractionPipeBlock<ItemPacket, ItemExtractionPipeBlock, ItemExtractionPipe> {

	@Override
	public String getIdentifier() {
		return "item_extraction_pipe";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ITEM_EXTRACTION_PIPE;
	}


	@Override
	public int extractionTicks() {
		return 20;
	}


	@Override
	public ItemExtractionPipe createNewTileEntity(Block block) {
		return new ItemExtractionPipe(this, block);
	}


	@Override
	public ItemExtractionPipeBlock castCustomBlock() {
		return this;
	}

}
