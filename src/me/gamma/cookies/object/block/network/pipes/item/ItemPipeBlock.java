
package me.gamma.cookies.object.block.network.pipes.item;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.pipes.PipeBlock;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.pipes.item.ItemPacket;
import me.gamma.cookies.object.tile.network.pipes.item.ItemPipe;



public class ItemPipeBlock extends PipeBlock<ItemPacket, ItemPipeBlock, ItemPipe> {

	@Override
	public String getIdentifier() {
		return "item_pipe";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ITEM_PIPE;
	}


	@Override
	public ItemPipe createNewTileEntity(Block block) {
		return new ItemPipe(this, block);
	}


	@Override
	public ItemPipeBlock castCustomBlock() {
		return this;
	}

}
