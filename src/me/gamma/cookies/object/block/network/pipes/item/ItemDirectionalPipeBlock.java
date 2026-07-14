
package me.gamma.cookies.object.block.network.pipes.item;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.pipes.DirectionalPipeBlock;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.pipes.item.ItemDirectionalPipe;
import me.gamma.cookies.object.tile.network.pipes.item.ItemPacket;



public class ItemDirectionalPipeBlock extends DirectionalPipeBlock<ItemPacket, ItemDirectionalPipeBlock, ItemDirectionalPipe> {

	@Override
	public String getIdentifier() {
		return "item_directional_pipe";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ITEM_DIRECTIONAL_PIPE;
	}


	@Override
	public ItemDirectionalPipe createNewTileEntity(Block block) {
		return new ItemDirectionalPipe(this, block);
	}


	@Override
	public ItemDirectionalPipeBlock castCustomBlock() {
		return this;
	}

}
