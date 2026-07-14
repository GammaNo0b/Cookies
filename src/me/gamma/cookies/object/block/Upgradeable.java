
package me.gamma.cookies.object.block;


import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.block.machine.MachineUpgrade;



public interface Upgradeable {

	/**
	 * Returns the number of upgrade slots of this upgradeable.
	 * 
	 * @return the number of slots
	 */
	int getUpgradeSlots();

	/**
	 * Returns the display name for this upgradeable.
	 * 
	 * @return the display name
	 */
	String getDisplayName();

	/**
	 * Returns the icon for this upgradeable.
	 * 
	 * @return the icon
	 */
	ItemStack getIcon();

	/**
	 * Returns if the given upgrade is allowed for this upgradeable.
	 * 
	 * @param upgrade the upgrade
	 * @return if valid
	 */
	boolean isAllowedUpgrade(MachineUpgrade upgrade);

	/**
	 * Returns the level of the given upgrade.
	 * 
	 * @param upgrade the upgrade
	 * @return the level
	 */
	int getUpgradeLevel(MachineUpgrade upgrade);

	/**
	 * Sets the level of the given upgrade.
	 * 
	 * @param upgrade the upgrade
	 * @param level   the new level
	 */
	void setUpgradeLevel(MachineUpgrade upgrade, int level);


	/**
	 * Returns the value of the given upgrade.
	 * 
	 * @param upgrade the upgrade
	 * @return the value
	 */
	default double getUpgradeValue(MachineUpgrade upgrade) {
		return upgrade.getValue(this.getUpgradeLevel(upgrade));
	}

}
