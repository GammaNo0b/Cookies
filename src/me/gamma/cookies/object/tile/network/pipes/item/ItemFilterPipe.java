
package me.gamma.cookies.object.tile.network.pipes.item;


import java.util.function.Predicate;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.FilterBlock;
import me.gamma.cookies.object.block.network.pipes.item.ItemFilterPipeBlock;
import me.gamma.cookies.object.gui.BlockFaceGui;
import me.gamma.cookies.object.gui.util.ItemFilterGui;
import me.gamma.cookies.object.item.ItemFilter;
import me.gamma.cookies.object.tile.network.pipes.FilterPipe;
import me.gamma.cookies.util.ArrayUtils;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.Utils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class ItemFilterPipe extends FilterPipe<ItemPacket, ItemFilterPipe, ItemFilterPipeBlock> {

	private static final String KEY_FILTERS = "filters";

	private final ItemFilter[] filters = ArrayUtils.generate(BlockUtils.cartesian.length, _ -> new ItemFilter(), ItemFilter[]::new);

	public ItemFilterPipe(ItemFilterPipeBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		PersistentDataUtils.getArray(data, KEY_FILTERS, this.filters, PersistentDataUtils::loadItemFilter);

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		PersistentDataUtils.setArray(data, KEY_FILTERS, this.filters, PersistentDataUtils::saveItemFilter);

		return true;
	}


	public ItemFilter getItemFilter(BlockUtils.BlockFaceDirection face) {
		return this.filters[face.ordinal()];
	}


	@Override
	protected Predicate<ItemPacket> getFilter(BlockUtils.BlockFaceDirection face) {
		final ItemFilter filter = this.getItemFilter(face);
		return packet -> filter.filter(packet.getStack()) > 0;
	}


	@Override
	protected ItemPacket createPacket() {
		return new ItemPacket();
	}


	@Override
	public ItemFilterPipe castTileEntity() {
		return this;
	}

	public class FilterData extends BlockFaceGui.BlockFaceData {

		@Override
		protected String getTitle() {
			return "Item Filter Pipe";
		}


		@Override
		protected boolean onBlockFaceClicked(BlockUtils.BlockFaceDirection face, ItemStack stack, Player player) {
			final int i = face.ordinal();
			final String title = "§" + BlockFaceGui.COLORS_ACTIVE[i] + Utils.toCapitalWords(face) + " Filter";
			ItemFilterGui.open(player, new FilterBlock<ItemStack, ItemFilter>() {

				@Override
				public String getFilterTitle() {
					return title;
				}


				@Override
				public void setFilter(ItemFilter filter) {
					ItemFilterPipe.this.filters[i] = filter;
				}


				@Override
				public ItemFilter getFilter() {
					return ItemFilterPipe.this.filters[i];
				}

			}, null, BlockFaceGui.ICONS_ACTIVE[i]);
			return false;
		}

	}

}
