
package me.gamma.cookies.object.block.network.pipes.item;


import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.network.pipes.FilterPipeBlock;
import me.gamma.cookies.object.gui.BlockFaceGui;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.tile.network.pipes.item.ItemFilterPipe;
import me.gamma.cookies.object.tile.network.pipes.item.ItemPacket;



public class ItemFilterPipeBlock extends FilterPipeBlock<ItemPacket, ItemFilterPipeBlock, ItemFilterPipe> implements BlockFaceGui<ItemFilterPipe.FilterData> {

	@Override
	public String getIdentifier() {
		return "item_filter_pipe";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.ITEM_FILTER_PIPE;
	}


	@Override
	public boolean onBlockRightClick(Player player, Block block, ItemStack stack, PlayerInteractEvent event) {
		if(!super.onBlockRightClick(player, block, stack, event))
			return false;

		ItemFilterPipe pipe = this.getTileEntity(block);
		if(pipe != null)
			this.openGui(player, pipe.new FilterData(), false, true);

		return true;
	}


	@Override
	public ItemFilterPipe createNewTileEntity(Block block) {
		return new ItemFilterPipe(this, block);
	}


	@Override
	public ItemFilterPipeBlock castCustomBlock() {
		return this;
	}

}
