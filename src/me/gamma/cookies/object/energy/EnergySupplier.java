
package me.gamma.cookies.object.energy;


import java.util.Arrays;

import org.bukkit.block.Block;

import me.gamma.cookies.object.Supplier;
import me.gamma.cookies.object.tile.TileEntityStorage;



/**
 * Represents a energy source.
 * 
 * @author gamma
 *
 */
public interface EnergySupplier {

	/**
	 * Returns the {@link EnergyProvider} to supply energy.
	 * 
	 * @return the energy provider
	 */
	EnergyProvider getEnergyOutput();


	/**
	 * Releases at most {@code max} amount of energy.
	 * 
	 * @param max the maximum amount of energy to be released
	 * @return the released amount of energy
	 */
	default int releaseEnergy(int max) {
		return Supplier.supplyTypeless(max, Arrays.asList(this.getEnergyOutput()));
	}


	/**
	 * Returns the {@link EnergySupplier} from the given block.
	 * 
	 * @param holder the block
	 * @return the energy supplier or null
	 */
	public static EnergySupplier getEnergySupplier(Block block) {
		return TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(block);
	}

}
