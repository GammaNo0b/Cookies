
package me.gamma.cookies.object.tile.network.pipes.item;


import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.network.pipes.item.ItemInsertionPipeBlock;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.tile.network.pipes.InsertionPipe;
import me.gamma.cookies.util.ItemUtils;



public class ItemInsertionPipe extends InsertionPipe<ItemPacket, ItemInsertionPipe, ItemInsertionPipeBlock> {

	public ItemInsertionPipe(ItemInsertionPipeBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected boolean insertResource(ItemPacket packet, Block target) {
		ItemConsumer consumer = ItemConsumer.getItemConsumer(target);
		if(consumer == null)
			return false;

		ItemStack rest = consumer.addStack(packet.getStack());
		if(ItemUtils.isEmpty(rest))
			return true;

		packet.setStack(rest);
		return false;
	}


	@Override
	protected ItemPacket createPacket() {
		return new ItemPacket();
	}


	@Override
	public ItemInsertionPipe castTileEntity() {
		return this;
	}

}
