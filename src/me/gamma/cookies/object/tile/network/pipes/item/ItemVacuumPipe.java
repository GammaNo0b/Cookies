
package me.gamma.cookies.object.tile.network.pipes.item;


import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

import me.gamma.cookies.object.block.network.pipes.item.ItemVacuumPipeBlock;
import me.gamma.cookies.object.tile.network.pipes.VacuumPipe;



public class ItemVacuumPipe extends VacuumPipe<ItemPacket, ItemVacuumPipe, ItemVacuumPipeBlock> {

	public ItemVacuumPipe(ItemVacuumPipeBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected boolean attractsEntity(Entity entity) {
		return entity instanceof Item;
	}


	@Override
	protected ItemPacket createPacket(Entity entity) {
		if(!(entity instanceof Item item))
			return null;

		return new ItemPacket(item.getItemStack());
	}


	@Override
	protected ItemPacket createPacket() {
		return new ItemPacket();
	}


	@Override
	public ItemVacuumPipe castTileEntity() {
		return this;
	}

}
