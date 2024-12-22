
package me.gamma.cookies.object.block;


import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.object.property.Properties;
import me.gamma.cookies.object.property.UUIDProperty;
import me.gamma.cookies.object.team.Team;



/**
 * An Ownable representsa block that can be owned by a player. The owning player's uuid is stored inside the block data.
 * 
 * The {@link AbstractCustomBlock} will automatically store the placing player's uuid inside the block and perforn check on breaking if the owning
 * player is the breaking player or a teammate.
 * 
 */
public interface Ownable {

	public static final UUIDProperty OWNER = Properties.OWNER;

	default void setOwner(PersistentDataHolder holder, UUID owner) {
		OWNER.store(holder, owner);
	}


	default UUID getOwner(PersistentDataHolder holder) {
		return OWNER.fetch(holder);
	}


	default Player getOwningPlayer(PersistentDataHolder holder) {
		return Bukkit.getPlayer(this.getOwner(holder));
	}


	default boolean isOwner(PersistentDataHolder holder, UUID player) {
		return player.equals(this.getOwner(holder));
	}


	default boolean canAccess(PersistentDataHolder holder, OfflinePlayer player) {
		if(player.isOp() || player instanceof Permissible perm && perm.hasPermission("cookies.ownable"))
			return true;

		if(this.isOwner(holder, player.getUniqueId()))
			return true;

		final Team team = Team.getTeamFromPlayer(this.getOwner(holder));
		return team != null && team.isMember(player.getUniqueId());
	}

}
