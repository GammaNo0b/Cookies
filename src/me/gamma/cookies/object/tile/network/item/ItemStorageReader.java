
package me.gamma.cookies.object.tile.network.item;


import java.util.HashMap;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Powerable;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.FilterBlock;
import me.gamma.cookies.object.block.network.item.ItemStorageReaderBlock;
import me.gamma.cookies.object.item.ItemFilter;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemStorage;
import me.gamma.cookies.object.tile.network.AbstractStorageComponent;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.PersistentDataUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



public class ItemStorageReader extends AbstractStorageComponent<ItemStack, ItemStorageReader, ItemStorageReaderBlock> implements FilterBlock<ItemStack, ItemFilter> {

	private static final String KEY_COOLDOWN = "cooldown";
	private static final String KEY_FILTER = "filter";

	private int cooldown = 0;
	private ItemFilter filter;

	public ItemStorageReader(ItemStorageReaderBlock customBlock, Block block) {
		super(customBlock, block);
	}


	@Override
	public boolean load(Chunk chunk, PersistentDataObject data) {
		if(!super.load(chunk, data))
			return false;

		this.cooldown = data.getInteger(KEY_COOLDOWN, 0);
		this.filter = PersistentDataUtils.getItemFilter(data, "filter");
		if(this.filter == null)
			this.filter = new ItemFilter();

		return true;
	}


	@Override
	public boolean save(Chunk chunk, PersistentDataObject data) {
		if(!super.save(chunk, data))
			return false;

		data.setInteger(KEY_COOLDOWN, this.cooldown);
		PersistentDataUtils.setItemFilter(data, KEY_FILTER, this.filter);

		return true;
	}


	@Override
	public String getFilterTitle() {
		return "§8Storage Reader";
	}


	@Override
	public ItemFilter getFilter() {
		return this.filter;
	}


	@Override
	public void setFilter(ItemFilter filter) {
		this.filter = filter;
	}


	@Override
	public boolean isTicking() {
		return true;
	}


	@Override
	public void tick() {
		if(--this.cooldown > 0)
			return;

		this.cooldown = 20;

		BlockFace facing = this.customBlock.getFacing(this.block);

		// find lever
		Block redstone = this.block.getRelative(facing);
		if(!(redstone.getBlockData() instanceof Powerable powerable))
			return;

		// find storage
		Block target = this.block.getRelative(facing.getOppositeFace());
		ItemStorage storage = ItemStorage.getItemStorage(target);
		if(storage == null)
			return;

		// collect resources
		Map<ItemStack, Integer> resources = new HashMap<>();
		for(Provider<ItemStack> provider : storage.getItemProviders()) {
			ItemStack stack = ItemProvider.getStack(provider);
			if(ItemUtils.isEmpty(stack))
				continue;

			ItemStack type = stack.clone();
			type.setAmount(1);
			resources.put(type, stack.getAmount() + resources.getOrDefault(type, 0));
		}

		ItemFilter filter = this.getFilter();
		boolean filtered = false;
		for(Map.Entry<ItemStack, Integer> entry : resources.entrySet()) {
			if(filter.filter(entry.getKey(), entry.getValue()) == entry.getValue()) {
				filtered = true;
				break;
			}
		}

		powerable.setPowered(filtered);
		redstone.setBlockData(powerable);
	}


	@Override
	public ItemStorageReader castTileEntity() {
		return this;
	}

}
