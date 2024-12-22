
package me.gamma.cookies.object.energy;


import java.util.Arrays;

import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.Supplier;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.property.ByteProperty;
import me.gamma.cookies.util.BlockUtils;



/**
 * Represents a block that can provide energy from it.
 * 
 * @author gamma
 *
 */
public interface EnergySupplier {

	/**
	 * Property to store the energy output access flags in a block.
	 */
	ByteProperty ENERGY_OUTPUT_ACCESS_FLAGS = new ByteProperty("energyoutputaccessflags");

	/**
	 * Returns the {@link EnergyProvider} of the given block to supply energy.
	 * 
	 * @param block the block
	 * @return the energy provider
	 */
	EnergyProvider getEnergyOutput(TileState block);


	/**
	 * Returns the list of {@link EnergyProvider} of the given block on the given face to supply energy.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @return the energy provider
	 */
	default EnergyProvider getEnergyOutput(TileState block, BlockFace face) {
		return this.canAccessEnergyOutput(block, face) ? this.getEnergyOutput(block) : null;
	}


	/**
	 * Returns the flags for each side of the block to be able to yield energy from that side. The first six bits correspond to the six different sides in
	 * {@link BlockUtils#cartesian}.
	 * 
	 * @param block the block
	 * @return the access flags
	 */
	default byte getEnergyOutputAccessFlags(TileState block) {
		return ENERGY_OUTPUT_ACCESS_FLAGS.fetch(block);
	}


	/**
	 * Checks if the given block can yield energy from the given block face.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @return if it can accept items
	 */
	default boolean canAccessEnergyOutput(TileState block, BlockFace face) {
		byte flags = this.getEnergyOutputAccessFlags(block);
		for(int i = 0; i < BlockUtils.cartesian.length; i++)
			if(BlockUtils.cartesian[i] == face)
				return ((flags >> i) & 1) == 1;
		return false;
	}


	/**
	 * Releases {@code max} amount of energy from the given block.
	 * 
	 * @param block the block
	 * @param max   the maximum amount of energy to be released
	 * @return the removed amount of energy
	 */
	default int releaseEnergy(TileState block, int max) {
		return Supplier.supplyTypeless(max, Arrays.asList(this.getEnergyOutput(block)));
	}


	/**
	 * Returns the energy supplier of the given block or null if the block has no energy supplier.
	 * 
	 * @param block the block
	 * @return the supplier or null
	 */
	public static EnergySupplier getEnergySupplier(TileState block) {
		AbstractCustomBlock custom = Blocks.getCustomBlockFromBlock(block);
		return custom instanceof EnergySupplier supplier ? supplier : null;
	}

}
