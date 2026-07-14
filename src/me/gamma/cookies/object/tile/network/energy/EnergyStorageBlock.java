
package me.gamma.cookies.object.tile.network.energy;


import me.gamma.cookies.object.energy.EnergyProvider;
import me.gamma.cookies.object.energy.EnergyStorage;
import me.gamma.cookies.util.collection.Holder;



public interface EnergyStorageBlock extends EnergyStorage {

	/**
	 * Amount of energy this energy storage block can store at once.
	 * 
	 * @return the capacity
	 */
	int getEnergyCapacity();

	/**
	 * Returns the energy stored in this energy storage block.
	 * 
	 * @return the energy
	 */
	int getEnergy();

	/**
	 * Sets the energy stored in this energy storage block.
	 * 
	 * @param energy the energy
	 */
	void setEnergy(int energy);


	@Override
	default EnergyProvider getEnergyProvider() {
		return EnergyProvider.fromHolder(Holder.create(this::getEnergy, this::setEnergy), this.getEnergyCapacity());
	}

}
