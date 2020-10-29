package me.gamma.cookies.objects.item;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ItemTicker extends PlayerRegister {
	
	long getDelay();
	
	ItemStack getStackFromPlayer(Player player);
	
	void tick(Player player, ItemStack stack);
	
	default void tickPlayers() {
		for(UUID uuid : this.getPlayers()) {
			Player player = Bukkit.getPlayer(uuid);
			if(player != null) {
				this.tick(player, this.getStackFromPlayer(player));
			}
		}
	}

}
