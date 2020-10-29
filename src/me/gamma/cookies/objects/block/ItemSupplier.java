package me.gamma.cookies.objects.block;

import java.util.List;
import java.util.function.Supplier;

import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;

public interface ItemSupplier {
	
	List<Supplier<ItemStack>> getOutputStackHolders(TileState block);

}
