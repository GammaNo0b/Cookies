
package me.gamma.cookies.object.tile.network.pipes.item;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.pipes.item.ItemPipeBlock;
import me.gamma.cookies.object.tile.network.pipes.Pipe;



public class ItemPipe extends Pipe<ItemPacket, ItemPipe, ItemPipeBlock> {

	public ItemPipe(ItemPipeBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected ItemPacket createPacket() {
		return new ItemPacket();
	}


	@Override
	public ItemPipe castTileEntity() {
		return this;
	}

}
