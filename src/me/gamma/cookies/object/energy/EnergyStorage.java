
package me.gamma.cookies.object.energy;


/**
 * Provides an energy storage that can consume as well as supply energy.
 * 
 * @author gamma
 *
 */
public interface EnergyStorage extends EnergyConsumer, EnergySupplier {

	/**
	 * Returns the list of {@link EnergyProvider} of this energy storage that act as inputs and as outputs at the same time.
	 * 
	 * @return the list of item providers
	 */
	EnergyProvider getEnergyProvider();


	@Override
	default EnergyProvider getEnergyInput() {
		return this.getEnergyProvider();
	}


	@Override
	default EnergyProvider getEnergyOutput() {
		return this.getEnergyProvider();
	}

}
