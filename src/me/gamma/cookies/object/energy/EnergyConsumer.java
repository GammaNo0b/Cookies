
package me.gamma.cookies.object.energy;


import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;

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
	 * Returns the {@link EnergyProvider} of the given block to consume energy.
	 * 
	 * @param block the block
	 * @return the energy provider
	 */
	EnergyProvider getEnergyInput(TileState block);


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
	 * in {@link BlockUtils#cartesian}.
	 * 
	 * @param block the block
	 * @return the access flags
	 */
	default byte getEnergyInputAccessFlags(TileState block) {
		return ENERGY_INPUT_ACCESS_FLAGS.fetch(block);
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
	 * Stores the given amount of energy into the given block.
	 * 
	 * @param block  the block
	 * @param amount the amount of energy to be consumed
	 * @return the amount of energy that couldn't be consumed
	 */
	default int storeEnergy(TileState block, int amount) {
		return this.storeEnergy(block, null, false, amount);
	}


	/**
	 * Stores the given amount of energy into the given block on the given face.
	 * 
	 * @param block  the block
	 * @param face   the block face
	 * @param amount the amount of energy to be consumed
	 * @return the amount of energy that couldn't be consumed
	 */
	default int storeEnergy(TileState block, BlockFace face, boolean checkAccess, int amount) {
		if(checkAccess && !this.canAccessEnergyInput(block, face))
			return amount;

		return Consumer.consumeTypeless(amount, Arrays.asList(this.getEnergyInput(block)));
	}


	/**
	 * Returns the {@link EnergyConsumer} from the given block.
	 * 
	 * @param block the block
	 * @return the energy consumer or null
	 */
	public static EnergyConsumer getEnergyConsumer(TileState block) {
		AbstractCustomBlock custom = Blocks.getCustomBlockFromBlock(block);
		return custom instanceof EnergyConsumer ? (EnergyConsumer) custom : null;
	}

}
