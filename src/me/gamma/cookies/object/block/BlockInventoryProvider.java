
package me.gamma.cookies.object.block;


import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.inventory.Inventory;

import me.gamma.cookies.object.gui.InventoryProvider;
import me.gamma.cookies.util.InventoryUtils;



public interface BlockInventoryProvider extends InventoryProvider<TileState> {

	String KEY_LOCATION = "location";
	String WORLD_LOCATION = "world";

	@Override
	default void storeData(Inventory inventory, TileState data) {
		InventoryUtils.storeLocationInStack(inventory.getItem(this.getIdentifierSlot()), KEY_LOCATION, WORLD_LOCATION, data.getLocation());
	}


	@Override
	default TileState fetchData(Inventory inventory) {
		Location location = InventoryUtils.getLocationFromStack(inventory.getItem(this.getIdentifierSlot()), KEY_LOCATION, WORLD_LOCATION);
		Block block = location.getBlock();
		return block.getState() instanceof TileState state ? state : null;
	}


	@Override
	default SoundCategory getSoundCategory() {
		return SoundCategory.BLOCKS;
	}

}
