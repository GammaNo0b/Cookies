
package me.gamma.cookies.object.fluid;


import java.util.List;

import org.bukkit.block.TileState;



/**
 * Provides fluid storage that can consume as well as supply fluids.
 * 
 * @author gamma
 *
 */
public interface FluidStorage extends FluidConsumer, FluidSupplier {

	/**
	 * Returns the list of {@link FluidProvider} of the given block that act as inputs and as outputs at the same time.
	 * 
	 * @param block the block
	 * @return the list of fluid providers
	 */
	List<FluidProvider> getFluidProviders(TileState block);


	@Override
	default List<FluidProvider> getFluidInputs(TileState block) {
		return this.getFluidProviders(block);
	}


	@Override
	default List<FluidProvider> getFluidOutputs(TileState block) {
		return this.getFluidProviders(block);
	}

}
