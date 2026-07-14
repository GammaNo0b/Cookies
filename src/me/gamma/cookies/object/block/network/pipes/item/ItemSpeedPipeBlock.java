
package me.gamma.cookies.object.block.network.pipes.item;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.pipes.SpeedPipeBlock;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.pipes.item.ItemPacket;
import me.gamma.cookies.object.tile.network.pipes.item.ItemSpeedPipe;



public class ItemSpeedPipeBlock extends SpeedPipeBlock<ItemPacket, ItemSpeedPipeBlock, ItemSpeedPipe> {

	private final double speed;

	public ItemSpeedPipeBlock(double speed) {
		this.speed = speed;
	}


	@Override
	public String getIdentifier() {
		return "item_speed_pipe";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ITEM_SPEED_PIPE;
	}


	@Override
	public double getPacketSpeed() {
		return this.speed;
	}


	@Override
	public ItemSpeedPipeBlock castCustomBlock() {
		return this;
	}


	@Override
	public ItemSpeedPipe createNewTileEntity(Block block) {
		return new ItemSpeedPipe(this, block);
	}

}
