
package me.gamma.cookies.object.energy;


import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.init.Blocks;
import me.gamma.cookies.object.Consumer;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.BlockFaceConfigurable;
import me.gamma.cookies.object.gui.BlockFaceConfig;
import me.gamma.cookies.object.property.ByteProperty;
import me.gamma.cookies.util.BlockUtils;



/**
 * Represents a block that can store energy in it.
 * 
 * @author gamma
 *
 */
public interface EnergyConsumer {

	/**
	 * Property to store the energy input access flags in a block.
	 */
	ByteProperty ENERGY_INPUT_ACCESS_FLAGS = new ByteProperty("energyinputaccessflags");

	/**
	 * Returns the {@link EnergyProvider} of the given data holder to consume energy.
	 * 
	 * @param holder the data holder
	 * @return the energy provider
	 */
	EnergyProvider getEnergyInput(PersistentDataHolder holder);


	/**
	 * Returns the {@link EnergyProvider} of the given block on the given face to consume energy.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @return the energy provider
	 */
	default EnergyProvider getEnergyInput(TileState block, BlockFace face) {
		return this.canAccessEnergyInput(block, face) ? this.getEnergyInput(block) : null;
	}


	/**
	 * Returns the flags for each side of the block to be able to accept energy from that side. The first six bits correspond to the six different sides
	 * in {@link BlockUtils#cartesian}. The seventh bit controlls automatic energy transfer.
	 * 
	 * @param holder the data holder
	 * @return the access flags
	 */
	default byte getEnergyInputAccessFlags(PersistentDataHolder holder) {
		return ENERGY_INPUT_ACCESS_FLAGS.fetch(holder);
	}


	/**
	 * Checks if the given block can energy items from the given block face.
	 * 
	 * @param block the block
	 * @param face  the block face
	 * @return if it can accept items
	 */
	default boolean canAccessEnergyInput(TileState block, BlockFace face) {
		return BlockFaceConfigurable.isFaceEnabled(this.getEnergyInputAccessFlags(block), block, face);
	}


	/**
	 * Creates the block face config.
	 * 
	 * @return the config
	 */
	default BlockFaceConfig.Config createEnergyInputBlockFaceConfig() {
		return new BlockFaceConfig.Config("§cEnergy Input Configuration", ENERGY_INPUT_ACCESS_FLAGS, false, Material.RED_STAINED_GLASS_PANE);
	}


	/**
	 * Stores the given amount of energy into the given data holder.
	 * 
	 * @param holder the data holder
	 * @param amount the amount of energy to be consumed
	 * @return the amount of energy that couldn't be consumed
	 */
	default int storeEnergy(PersistentDataHolder holder, int amount) {
		return Consumer.consumeTypeless(amount, Arrays.asList(this.getEnergyInput(holder)));
	}


	/**
	 * Stores the given amount of energy into the given block on the given face.
	 * 
	 * @param block  the block
	 * @param face   the block face
	 * @param amount the amount of energy to be consumed
	 * @return the amount of energy that couldn't be consumed
	 */
	default int storeEnergy(TileState block, BlockFace face, int amount) {
		if(!this.canAccessEnergyInput(block, face))
			return amount;

		return this.storeEnergy(block, amount);
	}


	/**
	 * Returns the {@link EnergyConsumer} from the given data holder.
	 * 
	 * @param holder the data holder
	 * @return the energy consumer or null
	 */
	public static EnergyConsumer getEnergyConsumer(PersistentDataHolder holder) {
		AbstractCustomBlock custom = Blocks.getCustomBlockFromHolder(holder);
		return custom instanceof EnergyConsumer ? (EnergyConsumer) custom : null;
	}

}
