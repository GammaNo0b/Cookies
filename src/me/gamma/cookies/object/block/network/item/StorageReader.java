
package me.gamma.cookies.object.block.network.item;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.BlockTicker;
import me.gamma.cookies.object.block.Cartesian;
import me.gamma.cookies.object.block.FilterBlock;
import me.gamma.cookies.object.gui.util.ItemFilterGui;
import me.gamma.cookies.object.item.ItemFilter;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemStorage;
import me.gamma.cookies.object.list.HeadTextures;
import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.PropertyBuilder;
import me.gamma.cookies.object.property.PropertyCompound;
import me.gamma.cookies.util.ItemUtils;



public class StorageReader extends AbstractCustomBlock implements BlockTicker, FilterBlock<ItemStack, ItemFilter>, Cartesian {

	public static final PropertyCompound<ItemFilter> ITEM_FILTER = Properties.createItemFilterProperty();

	private final HashSet<Location> locations = new HashSet<>();

	@Override
	public String getIdentifier() {
		return "storage_reader";
	}


	@Override
	public String getBlockTexture() {
		return HeadTextures.STORAGE_READER;
	}


	@Override
	public Set<Location> getLocations() {
		return this.locations;
	}


	@Override
	public long getDelay() {
		return 20;
	}


	@Override
	public void tick(TileState block) {
		BlockFace facing = this.getFacing(block);

		// find lever
		Block redstone = block.getBlock().getRelative(facing);
		if(!(redstone.getBlockData() instanceof Powerable powerable))
			return;

		// find storage
		Block target = block.getBlock().getRelative(facing.getOppositeFace());
		if(!(target.getState() instanceof TileState state))
			return;

		ItemStorage storage = ItemStorage.getItemStorage(state);
		if(storage == null)
			return;

		// collect resources
		Map<ItemStack, Integer> resources = new HashMap<>();
		for(Provider<ItemStack> provider : storage.getItemProviders(block)) {
			ItemStack stack = ItemProvider.getStack(provider);
			if(ItemUtils.isEmpty(stack))
				continue;

			ItemStack type = stack.clone();
			type.setAmount(1);
			resources.put(type, stack.getAmount() + resources.getOrDefault(type, 0));
		}
		
		ItemFilter filter = this.getFilter(block);
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
	public String getFilterTitle() {
		return "§8Storage Reader";
	}


	@Override
	public ItemFilter getFilter(TileState block) {
		return ITEM_FILTER.fetch(block);
	}


	@Override
	public void setFilter(TileState block, ItemFilter filter) {
		ITEM_FILTER.store(block, filter);
		block.update();
	}


	@Override
	public PropertyBuilder buildBlockItemProperties(PropertyBuilder builder) {
		return super.buildBlockItemProperties(builder).add(ITEM_FILTER, new ItemFilter());
	}


	@Override
	public boolean onBlockRightClick(Player player, TileState block, ItemStack stack, PlayerInteractEvent event) {
		if(!player.isSneaking()) {
			ItemFilterGui.open(player, block, this, null, Material.ORANGE_STAINED_GLASS_PANE);
			return true;
		}

		return false;
	}

}
