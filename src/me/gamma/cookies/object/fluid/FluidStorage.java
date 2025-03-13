
package me.gamma.cookies.object.fluid;


import java.util.List;

import org.bukkit.persistence.PersistentDataHolder;



/**
 * Provides fluid storage that can consume as well as supply fluids.
 * 
 * @author gamma
 *
 */
public interface FluidStorage extends FluidConsumer, FluidSupplier {

	/**
	 * Returns the list of {@link FluidProvider} of the given data holder that act as inputs and as outputs at the same time.
	 * 
	 * @param holder the data holder
	 * @return the list of fluid providers
	 */
	List<FluidProvider> getFluidProviders(PersistentDataHolder holder);


	@Override
	default List<FluidProvider> getFluidInputs(PersistentDataHolder holder) {
		return this.getFluidProviders(holder);
	}


	@Override
	default List<FluidProvider> getFluidOutputs(PersistentDataHolder holder) {
		return this.getFluidProviders(holder);
	}

}
