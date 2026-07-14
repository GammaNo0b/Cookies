
package me.gamma.cookies.object.tile.network.pipes.item;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.pipes.item.ItemVoidPipeBlock;
import me.gamma.cookies.object.tile.network.pipes.VoidPipe;



public class ItemVoidPipe extends VoidPipe<ItemPacket, ItemVoidPipe, ItemVoidPipeBlock> {

	public ItemVoidPipe(ItemVoidPipeBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected ItemPacket createPacket() {
		return new ItemPacket();
	}


	@Override
	public ItemVoidPipe castTileEntity() {
		return this;
	}

}
