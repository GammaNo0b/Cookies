
package me.gamma.cookies.object.block.network.fluid;


import java.util.LinkedList;
import java.util.Map;

import org.bukkit.block.TileState;

import me.gamma.cookies.object.Cable;
import me.gamma.cookies.object.fluid.FluidConsumer;
import me.gamma.cookies.object.fluid.FluidFilter;
import me.gamma.cookies.object.fluid.FluidProvider;
import me.gamma.cookies.object.fluid.FluidStorage;
import me.gamma.cookies.object.fluid.FluidSupplier;



/**
 * Transfers fluids between {@link FluidSupplier}, {@link FluidConsumer} and {@link FluidStorage} blocks.
 * 
 * @author gamma
 *
 */
public interface FluidCable {

	/**
	 * Returns the transfer mode of the given block to transfer fluids with this cable.
	 * 
	 * @param block the block
	 * @return the transfer mode
	 */
	Cable.TransferMode getTransferMode(TileState block);

	/**
	 * Returns the amount of fluid that can be transferred per tick.
	 * 
	 * @param block the block state of the cable
	 * @return the transferrate
	 */
	int getTransferRate(TileState block);

	/**
	 * Returns the fluid buffer for this cable to store fluids temporarily.
	 * 
	 * @param block the block state of the cable
	 * @return the fluid buffer of the given block
	 */
	FluidProvider getBuffer(TileState block);

	/**
	 * Returns the filter for this cable to filter the fluid flow.
	 * 
	 * @param block the block state of the cable
	 * @return the fluid filter of the given block
	 */
	FluidFilter getFilter(TileState block);


	/**
	 * Transmits fluids from the suppliers to the consumers.
	 * 
	 * @param block    the fluid cable block
	 * @param supplier Map of {@link FluidSupplier} and {@link TileState} that supply the fluids
	 * @param consumer Map of {@link FluidConsumer} and {@link TileState} that consume the fluids
	 * @param storage  Map of {@link FluidStorage} and {@link TileState} that supply and consume the rest fluids
	 */
	default void transmitFluids(TileState block, Map<FluidSupplier, TileState> supplier, Map<FluidConsumer, TileState> consumer, Map<FluidStorage, TileState> storage) {
		LinkedList<FluidProvider> supplierlist = new LinkedList<>();
		supplier.forEach((sup, state) -> supplierlist.addAll(sup.getFluidOutputs(state)));
		LinkedList<FluidProvider> consumerlist = new LinkedList<>();
		consumer.forEach((con, state) -> consumerlist.addAll(con.getFluidInputs(state)));
		LinkedList<FluidProvider> storagelist = new LinkedList<>();
		storage.forEach((sto, state) -> storagelist.addAll(sto.getFluidProviders(state)));
		Cable.transfer(this.getTransferMode(block), this.getBuffer(block), this.getFilter(block), this.getTransferRate(block), supplierlist, consumerlist, storagelist);
	}

}
