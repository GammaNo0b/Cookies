
package me.gamma.cookies.object.energy;


import org.bukkit.block.TileState;



/**
 * Provides an energy storage that can consume as well as supply energy.
 * 
 * @author gamma
 *
 */
public interface EnergyStorage extends EnergyConsumer, EnergySupplier {

	/**
	 * Returns the list of {@link EnergyProvider} of the given block that act as inputs and as outputs at the same time.
	 * 
	 * @param block the block
	 * @return the list of item providers
	 */
	EnergyProvider getEnergyProvider(TileState block);


	@Override
	default EnergyProvider getEnergyInput(TileState block) {
		return this.getEnergyProvider(block);
	}


	@Override
	default EnergyProvider getEnergyOutput(TileState block) {
		return this.getEnergyProvider(block);
	}

}
