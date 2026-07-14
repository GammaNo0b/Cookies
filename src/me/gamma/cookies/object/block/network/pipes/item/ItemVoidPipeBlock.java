
package me.gamma.cookies.object.block.network.pipes.item;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.pipes.VoidPipeBlock;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.pipes.item.ItemPacket;
import me.gamma.cookies.object.tile.network.pipes.item.ItemVoidPipe;



public class ItemVoidPipeBlock extends VoidPipeBlock<ItemPacket, ItemVoidPipeBlock, ItemVoidPipe> {

	@Override
	public String getIdentifier() {
		return "item_void_pipe";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ITEM_VOID_PIPE;
	}


	@Override
	public ItemVoidPipeBlock castCustomBlock() {
		return this;
	}


	@Override
	public ItemVoidPipe createNewTileEntity(Block block) {
		return new ItemVoidPipe(this, block);
	}

}
