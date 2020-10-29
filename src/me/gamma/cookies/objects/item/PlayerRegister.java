
package me.gamma.cookies.objects.item;


import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.gamma.cookies.setup.CustomItemSetup;



public interface PlayerRegister {

	Set<UUID> getPlayers();

	boolean shouldRegisterPlayer(Player player);


	default void onRegisterPlayer(Player player, boolean success) {
	}


	default void onUnregisterPlayer(Player player, boolean success) {
	}


	default boolean registerPlayer(Player player) {
		boolean success = this.getPlayers().add(player.getUniqueId());
		this.onRegisterPlayer(player, success);
		return success;
	}


	default boolean unregisterPlayer(Player player) {
		boolean success = this.getPlayers().remove(player.getUniqueId());
		this.onUnregisterPlayer(player, success);
		return success;
	}


	default boolean isRegistered(Player player) {
		return this.getPlayers().contains(player.getUniqueId());
	}


	public static void preRegisterPlayers() {
		Bukkit.getOnlinePlayers().forEach(player -> {
			CustomItemSetup.customItems.stream().filter(item -> item instanceof PlayerRegister).map(item -> (PlayerRegister) item).filter(register -> register.shouldRegisterPlayer(player)).forEach(register -> register.registerPlayer(player));
		});
	}

}
