
package me.gamma.cookies.object.tile.network.pipes.item;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.pipes.item.ItemSpeedPipeBlock;
import me.gamma.cookies.object.tile.network.pipes.SpeedPipe;



public class ItemSpeedPipe extends SpeedPipe<ItemPacket, ItemSpeedPipe, ItemSpeedPipeBlock> {

	public ItemSpeedPipe(ItemSpeedPipeBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected ItemPacket createPacket() {
		return new ItemPacket();
	}


	@Override
	public ItemSpeedPipe castTileEntity() {
		return this;
	}

}
