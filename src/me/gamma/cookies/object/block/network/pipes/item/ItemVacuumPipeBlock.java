
package me.gamma.cookies.object.block.network.pipes.item;


import org.bukkit.block.Block;

import me.gamma.cookies.object.block.network.pipes.VacuumPipeBlock;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.pipes.item.ItemPacket;
import me.gamma.cookies.object.tile.network.pipes.item.ItemVacuumPipe;



public class ItemVacuumPipeBlock extends VacuumPipeBlock<ItemPacket, ItemVacuumPipeBlock, ItemVacuumPipe> {

	public ItemVacuumPipeBlock() {
		super();
	}


	@Override
	public String getIdentifier() {
		return "item_vacuum_pipe";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ITEM_VACUUM_PIPE;
	}


	@Override
	public ItemVacuumPipeBlock castCustomBlock() {
		return this;
	}


	@Override
	public ItemVacuumPipe createNewTileEntity(Block block) {
		return new ItemVacuumPipe(this, block);
	}

}
