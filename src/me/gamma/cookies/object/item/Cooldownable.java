
package me.gamma.cookies.object.item;


import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.property.LongProperty;
import me.gamma.cookies.object.property.Properties;



public interface Cooldownable {

	LongProperty LAST_USED = Properties.LAST_USED;

	/**
	 * Returns the amount of ticks this item is on cooldown.
	 * 
	 * @param holder the item
	 * @return the number of ticks
	 */
	long getCooldown(PersistentDataHolder holder);


	/**
	 * Sets the item on cooldown for the number of ticks specified by {@link Cooldownable#getCooldown(PersistentDataHolder)}.
	 * 
	 * @param world the world in which the cooldown is set
	 * @param stack the item
	 */
	default void initCooldown(World world, ItemStack stack) {
		ItemMeta meta = stack.getItemMeta();
		LAST_USED.store(meta, world.getGameTime() + this.getCooldown(meta));
		stack.setItemMeta(meta);
	}


	/**
	 * Returns whether or not the given item is on cooldown.
	 * 
	 * @param world the world in which the cooldown should be checked
	 * @param stack the item
	 * @return if it is on cooldown
	 */
	default boolean isOnCooldown(World world, ItemStack stack) {
		return world.getGameTime() < LAST_USED.fetchEmpty(stack.getItemMeta());
	}

}
