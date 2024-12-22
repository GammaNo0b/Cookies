
package me.gamma.cookies.object.block.network.item;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Cable;
import me.gamma.cookies.object.Provider;
import me.gamma.cookies.object.item.ItemConsumer;
import me.gamma.cookies.object.item.ItemFilter;
import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.object.item.ItemStorage;
import me.gamma.cookies.object.item.ItemSupplier;
import me.gamma.cookies.util.BlockUtils;
import me.gamma.cookies.util.collection.Triple;



/**
 * Transfers energy between {@link ItemSupplier}, {@link ItemConsumer} and {@link ItemStorage} blocks.
 * 
 * @author gamma
 *
 */
public interface ItemCable {

	/**
	 * Returns the transfer mode of the given block to transfer items with this cable.
	 * 
	 * @param block the block
	 * @return the transfer mode
	 */
	Cable.TransferMode getTransferMode(TileState block);

	/**
	 * Returns the amount of items that can be transferred per tick.
	 * 
	 * @param block the block state of the cable
	 * @return the transfer rate
	 */
	int getTransferRate(TileState block);

	/**
	 * Returns the item buffer for this cable to store items temporarily.
	 * 
	 * @param block the block state of the cable
	 * @return the item buffer of the given block
	 */
	ItemProvider getBuffer(TileState block);

	/**
	 * Returns the filter for this cable to filter the item flow.
	 * 
	 * @param block the block state of the cable
	 * @return the item filter of the given block
	 */
	ItemFilter getFilter(TileState block);


	/**
	 * Returns a triple containing maps with the adjacent {@link ItemSupplier}, {@link ItemConsumer} and {@link ItemStorage} of this block with their
	 * corresponding {@link TileState}.
	 * 
	 * @param block the item cable block
	 * @return the triple
	 */
	default Triple<Map<ItemSupplier, TileState>, Map<ItemConsumer, TileState>, Map<ItemStorage, TileState>> getNerbyComponents(TileState block) {
		Map<ItemSupplier, TileState> suppliers = new HashMap<>();
		Map<ItemConsumer, TileState> consumers = new HashMap<>();
		Map<ItemStorage, TileState> storages = new HashMap<>();
		for(BlockFace face : BlockUtils.cartesian) {
			for(int i = 0; i < 4; i++) {
				Block relative = block.getBlock().getRelative(face, i + 1);
				BlockState state = relative.getState();
				if(!(state instanceof TileState tile))
					continue;

				ItemSupplier supplier = ItemSupplier.getItemSupplier(tile);
				ItemConsumer consumer = ItemConsumer.getItemConsumer(tile);
				if(supplier != null) {
					if(supplier instanceof ItemStorage storage) {
						storages.put(storage, tile);
					} else {
						suppliers.put(supplier, tile);
					}
				}
				if(consumer != null) {
					if(!(consumer instanceof ItemStorage)) {
						consumers.put(consumer, tile);
					}
				}
			}
		}
		return new Triple<>(suppliers, consumers, storages);
	}


	/**
	 * Transmits items between the suppliers to the consumers.
	 * 
	 * @param block    the item cable block
	 * @param supplier Map of {@link ItemSupplier} and {@link TileState} that supply the items
	 * @param consumer Map of {@link ItemConsumer} and {@link TileState} that consume the items
	 * @param storage  Map of {@link ItemStorage} and {@link TileState} that supply and consume the rest items
	 */
	default void transmitItems(TileState block, Map<ItemSupplier, TileState> supplier, Map<ItemConsumer, TileState> consumer, Map<ItemStorage, TileState> storage) {
		LinkedList<Provider<ItemStack>> supplierlist = new LinkedList<>();
		supplier.forEach((sup, state) -> supplierlist.addAll(sup.getItemOutputs(state)));
		LinkedList<Provider<ItemStack>> consumerlist = new LinkedList<>();
		consumer.forEach((con, state) -> consumerlist.addAll(con.getItemInputs(state)));
		LinkedList<Provider<ItemStack>> storagelist = new LinkedList<>();
		storage.forEach((sto, state) -> storagelist.addAll(sto.getItemProviders(state)));
		Cable.transfer(this.getTransferMode(block), this.getBuffer(block), this.getFilter(block), this.getTransferRate(block), supplierlist, consumerlist, storagelist);
	}


	/**
	 * Transmits items between the suppliers to the consumers.
	 * 
	 * @param block the item cable block
	 */
	default void transmitItems(TileState block) {
		Triple<Map<ItemSupplier, TileState>, Map<ItemConsumer, TileState>, Map<ItemStorage, TileState>> triple = this.getNerbyComponents(block);
		this.transmitItems(block, triple.left, triple.middle, triple.right);
	}

}
