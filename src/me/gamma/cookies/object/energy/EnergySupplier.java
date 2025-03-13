
package me.gamma.cookies.object.energy;


import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.Supplier;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.gui.BlockFaceConfig;
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
	 * Returns the {@link EnergyProvider} of the given data holder to supply energy.
	 * 
	 * @param holder the data holder
	 * @return the energy provider
	 */
	EnergyProvider getEnergyOutput(PersistentDataHolder holder);


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
	 * {@link BlockUtils#cartesian}. The seventh bit controlls automatic energy transfer.
	 * 
	 * @param holder the data holder
	 * @return the access flags
	 */
	default byte getEnergyOutputAccessFlags(PersistentDataHolder holder) {
		return ENERGY_OUTPUT_ACCESS_FLAGS.fetch(holder);
	}


	/**
	 * Checks if the given block can yield energy from the given block face.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @return if it can accept items
	 */
	default boolean canAccessEnergyOutput(TileState block, BlockFace face) {
		return BlockFaceConfigurable.isFaceEnabled(this.getEnergyOutputAccessFlags(block), block, face);
	}


	/**
	 * Creates the block face config.
	 * 
	 * @return the config
	 */
	default BlockFaceConfig.Config createEnergyOutputBlockFaceConfig() {
		return new BlockFaceConfig.Config("§5Energy Input Configuration", ENERGY_OUTPUT_ACCESS_FLAGS, false, Material.PURPLE_STAINED_GLASS_PANE);
	}


	/**
	 * Releases at most {@code max} amount of energy from the given data holder.
	 * 
	 * @param holder the data holder
	 * @param max    the maximum amount of energy to be released
	 * @return the released amount of energy
	 */
	default int releaseEnergy(PersistentDataHolder holder, int max) {
		return Supplier.supplyTypeless(max, Arrays.asList(this.getEnergyOutput(holder)));
	}


	/**
	 * Releases at most {@code max} of energy from the given block on the given face.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @param max   the maximum amount of energy to be released
	 * @return the released amount of energy
	 */
	default int releaseEnergy(TileState block, BlockFace face, int max) {
		if(!this.canAccessEnergyOutput(block, face))
			return 0;

		return this.releaseEnergy(block, max);
	}


	/**
	 * Returns the {@link EnergySupplier}from the given data holder.
	 * 
	 * @param holder the block
	 * @return the energy supplier or null
	 */
	public static EnergySupplier getEnergySupplier(PersistentDataHolder holder) {
		AbstractCustomBlock custom = Blocks.getCustomBlockFromHolder(holder);
		return custom instanceof EnergySupplier supplier ? supplier : null;
	}

}
