
package me.gamma.cookies.object.block;


import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.block.machine.MachineUpgrade;



public interface Upgradeable {

	/**
	 * Returns the number of upgrade slots this machine of the given data holder has.
	 * 
	 * @param holder the data holder
	 * @return the number of slots
	 */
	int getUpgradeSlots(PersistentDataHolder holder);

	/**
	 * Returns the display name for this machine of the given data holder.
	 * 
	 * @param holder the data holder
	 * @return the display name
	 */
	String getDisplayName(PersistentDataHolder holder);

	/**
	 * Returns the icon for this machine of the given data holder.
	 * 
	 * @param holder the data holder
	 * @return the icon
	 */
	ItemStack getIcon(PersistentDataHolder holder);


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
	 * Returns the level the given data holder has of the give upgrade.
	 * 
	 * @param holder  the data holder
	 * @param upgrade the upgrade
	 * @return the level
	 */
	default int getUpgradeLevel(PersistentDataHolder holder, MachineUpgrade upgrade) {
		return upgrade.fetch(holder);
	}


	/**
	 * Sets the level for the data holder for the given upgrade.
	 * 
	 * @param holder  the data holder
	 * @param upgrade the upgrade
	 * @param level   the new level
	 */
	default void setUpgradeLevel(PersistentDataHolder holder, MachineUpgrade upgrade, int level) {
		upgrade.store(holder, level);
	}


	/**
	 * Returns the value for the given upgrade of the given data holder.
	 * 
	 * @param holder  the data holder
	 * @param upgrade the upgrade
	 * @return the value
	 */
	default double getUpgradeValue(PersistentDataHolder holder, MachineUpgrade upgrade) {
		return upgrade.getValue(holder);
	}

}
