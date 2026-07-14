
package me.gamma.cookies.object.fluid;


import java.util.List;

import org.bukkit.Chunk;

import me.gamma.cookies.util.collection.PersistentDataObject;



/**
 * Provides fluid storage that can consume as well as supply fluids.
 * 
 * @author gamma
 *
 */
public interface FluidStorage extends FluidConsumer, FluidSupplier {

	@Override
	default boolean load(Chunk chunk, PersistentDataObject data) {
		return FluidConsumer.super.load(chunk, data) & FluidSupplier.super.load(chunk, data);
	}


	@Override
	default boolean save(Chunk chunk, PersistentDataObject data) {
		return FluidConsumer.super.save(chunk, data) & FluidSupplier.super.load(chunk, data);
	}


	/**
	 * Returns the list of {@link FluidProvider} of this fluid storage that act as inputs and as outputs at the same time.
	 * 
	 * @return the list of fluid providers
	 */
	List<FluidProvider> getFluidProviders();


	@Override
	default List<FluidProvider> getFluidInputs() {
		return this.getFluidProviders();
	}


	@Override
	default List<FluidProvider> getFluidOutputs() {
		return this.getFluidProviders();
	}

}
