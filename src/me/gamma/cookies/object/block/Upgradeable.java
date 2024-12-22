
package me.gamma.cookies.object.block;


import java.util.ArrayList;

import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.machine.MachineUpgrade;



public interface Upgradeable {

	/**
	 * Returns the number of upgrade slots this machine of the given block has.
	 * 
	 * @param block the block
	 * @return the number of slots
	 */
	int getUpgradeSlots(TileState block);

	/**
	 * Returns the display name for this machine of the given block.
	 * 
	 * @param block the block
	 * @return the display name
	 */
	String getDisplayName(TileState block);

	/**
	 * Returns the icon for this machine of the given block.
	 * 
	 * @param block the block
	 * @return the icon
	 */
	ItemStack getIcon(TileState block);


	/**
	 * Returns the list of allowed upgrades.
	 * 
	 * @return the list
	 */
	default ArrayList<MachineUpgrade> getAllowedUpgrades() {
		ArrayList<MachineUpgrade> upgrades = new ArrayList<>();
		this.getAllowedUpgrades(upgrades);
		return upgrades;
	}


	/**
	 * Fills the given list with upgrades.
	 * 
	 * @param upgrades the list of allowed upgrades
	 */
	default void getAllowedUpgrades(ArrayList<MachineUpgrade> upgrades) {}


	/**
	 * Returns the level the given block has of the give upgrade.
	 * 
	 * @param block   the block
	 * @param upgrade the upgrade
	 * @return the level
	 */
	default int getUpgradeLevel(TileState block, MachineUpgrade upgrade) {
		return upgrade.fetch(block);
	}


	/**
	 * Sets the level for the block for the given upgrade.
	 * 
	 * @param block   the block
	 * @param upgrade the upgrade
	 * @param level   the new level
	 */
	default void setUpgradeLevel(TileState block, MachineUpgrade upgrade, int level) {
		upgrade.store(block, level);
	}


	/**
	 * Returns the value for the given upgrade of the given block.
	 * 
	 * @param block   the block
	 * @param upgrade the upgrade
	 * @return the value
	 */
	default double getUpgradeValue(TileState block, MachineUpgrade upgrade) {
		return upgrade.getValue(block);
	}

}
