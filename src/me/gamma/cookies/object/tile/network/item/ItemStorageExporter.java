
package me.gamma.cookies.object.tile.network.item;


import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.network.item.ItemStorageExporterBlock;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemFilter;
import me.gamma.cookies.object.tile.network.AbstractStorageInterface;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class ItemStorageExporter extends AbstractStorageInterface<ItemStack, ItemFilter, ItemStorageExporter, ItemStorageExporterBlock> {

	public ItemStorageExporter(ItemStorageExporterBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public List<Provider<ItemStack>> getInputs() {
		return List.of();
	}


	@Override
	public Filter<ItemStack> getInputFiler() {
		return Filter.empty();
	}


	@Override
	public List<Provider<ItemStack>> getOutputs() {
		BlockFace facing = this.customBlock.getFacing(this.block);
		Block target = this.block.getRelative(facing.getOppositeFace());

		ItemConsumer consumer = ItemConsumer.getItemConsumer(target);
		return consumer == null ? List.of() : consumer.getItemInputs(facing);
	}


	@Override
	public Filter<ItemStack> getOutputFilter() {
		return this.getFilter();
	}


	@Override
	protected ItemFilter emptyFilter() {
		return new ItemFilter();
	}


	@Override
	protected ItemFilter loadFilter(PersistentDataObject data) {
		return PersistentDataUtils.loadItemFilter(data);
	}


	@Override
	protected void saveFilter(PersistentDataObject data, ItemFilter filter) {
		PersistentDataUtils.saveItemFilter(data, filter);
	}


	@Override
	public ItemStorageExporter castTileEntity() {
		return this;
	}

}
