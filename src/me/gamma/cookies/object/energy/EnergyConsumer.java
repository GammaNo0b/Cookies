
package me.gamma.cookies.object.energy;


import java.util.Arrays;

import org.bukkit.block.Block;

import me.gamma.cookies.object.Consumer;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.object.tile.TileEntityStorage;



/**
 * Represents an energy drain.
 * 
 * @author gamma
 *
 */
public interface EnergyConsumer {

	/**
	 * Returns the {@link EnergyProvider} to consume energy.
	 * 
	 * @return the energy provider
	 */
	EnergyProvider getEnergyInput();


	/**
	 * Stores the given amount of energy. Returns the amount unable to be stored.
	 * 
	 * @param amount the amount of energy to be consumed
	 * @return the amount of energy that couldn't be consumed
	 */
	default int storeEnergy(int amount) {
		return Consumer.consumeTypeless(amount, Arrays.asList(this.getEnergyInput()));
	}


	/**
	 * Returns the {@link EnergyConsumer} from the given block.
	 * 
	 * @param block the block
	 * @return the energy consumer or null
	 */
	public static EnergyConsumer getEnergyConsumer(Block block) {
		AbstractCustomTileEntity<?, ?> tileEntity = TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(block);
		return tileEntity instanceof EnergyConsumer consumer ? consumer : null;
	}

}
