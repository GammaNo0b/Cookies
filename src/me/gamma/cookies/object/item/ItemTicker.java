
package me.gamma.cookies.object.item;


import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.object.Ticker;
import me.gamma.cookies.util.ItemUtils;



public interface ItemTicker extends PlayerRegister, Ticker {

	/**
	 * Returns a list of stacks from the player to be ticked. Per default it returns the stack from {@link ItemTicker#getStackFromPlayer(Player)}.
	 * 
	 * @param player the player
	 * @return the list of stacks to be ticked
	 */
	List<ItemStack> getStacksFromPlayer(Player player);

	void tick(Player player, ItemStack stack);


	@Override
	default void tick() {
		for(UUID uuid : this.getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			if(player != null)
				for(ItemStack stack : this.getStacksFromPlayer(player))
					if(!ItemUtils.isEmpty(stack))
						this.tick(player, stack);
		}
	}


	default void registerItemTicker() {
		PlayerRegister.super.registerPlayerRegister();
		Ticker.super.registerTicker();
	}

}
