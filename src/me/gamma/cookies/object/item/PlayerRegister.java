
package me.gamma.cookies.object.item;


import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.gamma.cookies.init.Items;
import me.gamma.cookies.init.Registry;



public interface PlayerRegister {

	Registry<PlayerRegister> PLAYER_REGISTER = new Registry<>();

	Set<UUID> getPlayers();


	default void registerPlayerRegister() {
		PLAYER_REGISTER.register(this);
	}


	default boolean shouldRegisterPlayer(Player player) {
		return !this.isRegistered(player);
	}
	
	default boolean registerOnStart() {
		return true;
	}


	default void onRegisterPlayer(Player player, boolean success) {}


	default void onUnregisterPlayer(Player player, boolean success) {}


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


	public static void init() {
		Items.getPlayerRegister().forEach(PLAYER_REGISTER::register);
		Bukkit.getOnlinePlayers().forEach(player -> PLAYER_REGISTER.stream().filter(PlayerRegister::registerOnStart).filter(register -> register.shouldRegisterPlayer(player)).forEach(register -> register.registerPlayer(player)));
	}

}
