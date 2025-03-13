
package me.gamma.cookies.object.energy;


import org.bukkit.persistence.PersistentDataHolder;



/**
 * Provides an energy storage that can consume as well as supply energy.
 * 
 * @author gamma
 *
 */
public interface EnergyStorage extends EnergyConsumer, EnergySupplier {

	/**
	 * Returns the list of {@link EnergyProvider} of the given data holder that act as inputs and as outputs at the same time.
	 * 
	 * @param holder the data holder
	 * @return the list of item providers
	 */
	EnergyProvider getEnergyProvider(PersistentDataHolder holder);


	@Override
	default EnergyProvider getEnergyInput(PersistentDataHolder holder) {
		return this.getEnergyProvider(holder);
	}


	@Override
	default EnergyProvider getEnergyOutput(PersistentDataHolder holder) {
		return this.getEnergyProvider(holder);
	}

}
