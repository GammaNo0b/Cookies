
package me.gamma.cookies.object.tile.network.pipes.item;


import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.network.pipes.item.ItemExtractionPipeBlock;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.object.tile.network.pipes.ExtractionPipe;



public class ItemExtractionPipe extends ExtractionPipe<ItemPacket, ItemExtractionPipe, ItemExtractionPipeBlock> {

	public ItemExtractionPipe(ItemExtractionPipeBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	protected ItemPacket extractResource(Block target) {
		ItemSupplier supplier = ItemSupplier.getItemSupplier(target);
		if(supplier == null)
			return null;

		ItemStack stack = supplier.removeItem();
		if(stack == null)
			return null;

		return new ItemPacket(stack);
	}


	@Override
	protected ItemPacket createPacket() {
		return new ItemPacket();
	}


	@Override
	public ItemExtractionPipe castTileEntity() {
		return this;
	}

}
