
package me.gamma.cookies.object.block.network.pipes.item;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.pipes.InsertionPipeBlock;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.pipes.item.ItemInsertionPipe;
import me.gamma.cookies.object.tile.network.pipes.item.ItemPacket;



public class ItemInsertionPipeBlock extends InsertionPipeBlock<ItemPacket, ItemInsertionPipeBlock, ItemInsertionPipe> {

	@Override
	public String getIdentifier() {
		return "item_insertion_pipe";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ITEM_INSERTION_PIPE;
	}


	@Override
	public ItemInsertionPipe createNewTileEntity(Block block) {
		return new ItemInsertionPipe(this, block);
	}


	@Override
	public ItemInsertionPipeBlock castCustomBlock() {
		return this;
	}

}
