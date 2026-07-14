
package me.gamma.cookies.object.tile.network.pipes.item;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.pipes.item.ItemDirectionalPipeBlock;
import me.gamma.cookies.object.tile.network.pipes.DirectionalPipe;



public class ItemDirectionalPipe extends DirectionalPipe<ItemPacket, ItemDirectionalPipe, ItemDirectionalPipeBlock> {

	public ItemDirectionalPipe(ItemDirectionalPipeBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected ItemPacket createPacket() {
		return new ItemPacket();
	}


	@Override
	public ItemDirectionalPipe castTileEntity() {
		return this;
	}

}
