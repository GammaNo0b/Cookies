package me.gamma.cookies.objects.block;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.objects.property.Properties;
import me.gamma.cookies.objects.property.UUIDProperty;

public interface Ownable {
	
	public static final UUIDProperty OWNER = Properties.OWNER;

	default void setOwner(PersistentDataHolder holder, Player owner) {
		OWNER.store(holder, owner.getUniqueId());
	}


	default Player getOwner(PersistentDataHolder holder) {
		return Bukkit.getPlayer(OWNER.fetch(holder));
	}


	default boolean isOwner(PersistentDataHolder holder, Player player) {
		return player.getUniqueId().equals(this.getOwner(holder).getUniqueId());
	}

}
