
package me.gamma.cookies.object.block.network.energy;


import org.bukkit.block.TileState;

import me.gamma.cookies.object.energy.EnergyProvider;
import me.gamma.cookies.object.energy.EnergyStorage;
import me.gamma.cookies.object.property.EnergyProperty;



public interface EnergyStorageBlock extends EnergyStorage {

	EnergyProperty INTERNAL_STORAGE = new EnergyProperty("internalstorage", 0, Integer.MAX_VALUE);

	/**
	 * Amount of energy this energy storage block can store at once.
	 * 
	 * @return the capacity
	 */
	int getEnergyCapacity();


	@Override
	default EnergyProvider getEnergyProvider(TileState block) {
		return EnergyProvider.fromProperty(INTERNAL_STORAGE, block, this.getEnergyCapacity());
	}

}
