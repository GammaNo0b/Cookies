
package me.gamma.cookies.object.tile.network.item;


import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Filter;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.network.item.ItemStorageImporterBlock;
import me.gamma.cookies.object.item.ItemFilter;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.object.tile.network.AbstractStorageInterface;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class ItemStorageImporter extends AbstractStorageInterface<ItemStack, ItemFilter, ItemStorageImporter, ItemStorageImporterBlock> {

	public ItemStorageImporter(ItemStorageImporterBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public List<Provider<ItemStack>> getInputs() {
		BlockFace facing = this.customBlock.getFacing(this.block);
		Block target = this.block.getRelative(facing.getOppositeFace());

		ItemSupplier supplier = ItemSupplier.getItemSupplier(target);
		return supplier == null ? List.of() : supplier.getItemOutputs(facing);
	}


	@Override
	public Filter<ItemStack> getInputFiler() {
		return this.getFilter();
	}


	@Override
	public List<Provider<ItemStack>> getOutputs() {
		return List.of();
	}


	@Override
	public Filter<ItemStack> getOutputFilter() {
		return Filter.empty();
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
	public ItemStorageImporter castTileEntity() {
		return this;
	}

}
