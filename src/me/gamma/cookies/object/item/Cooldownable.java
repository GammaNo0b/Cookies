
package me.gamma.cookies.object.item;


import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;



public interface Cooldownable {

	String KEY_LAST_USED = "lastused";

	/**
	 * Returns the number of ticks the given item requires to cool down.
	 * 
	 * @param stack the item stack
	 * @return the cool down ticks
	 */
	long getCooldown(ItemStack stack);


	/**
	 * Returns the time the given item was last used.
	 * 
	 * @param stack the item
	 * @return the time of the last usage
	 */
	default long getLastUsed(ItemStack stack) {
		CustomItemData data = AbstractCustomItem.getCustomData(stack);
		if(data == null)
			return 0;

		return data.getData().getLong(KEY_LAST_USED, 0);
	}


	/**
	 * Sets the item on cooldown for the number of ticks specified by {@link Cooldownable#getLastUsed(PersistentDataHolder)}.
	 * 
	 * @param world the world in which the cooldown is set
	 * @param stack the item
	 */
	default void setLastUsed(World world, ItemStack stack) {
		CustomItemData data = AbstractCustomItem.getCustomData(stack);
		if(data == null)
			return;

		data.getData().setLong(KEY_LAST_USED, world.getGameTime());
		data.save();
	}


	/**
	 * Returns whether or not the given item is on cooldown.
	 * 
	 * @param world the world in which the cooldown should be checked
	 * @param stack the item
	 * @return if it is on cooldown
	 */
	default boolean isOnCooldown(World world, ItemStack stack) {
		return world.getGameTime() < this.getLastUsed(stack) + this.getCooldown(stack);
	}

}
