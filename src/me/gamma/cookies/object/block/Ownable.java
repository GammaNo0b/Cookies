
package me.gamma.cookies.object.block;


import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;

import me.gamma.cookies.object.team.Team;



/**
 * An ownable represents something that can be owned by a player by storing the player's uuid.
 * 
 */
public interface Ownable {

	/**
	 * Returns the uuid of the owning player.
	 * 
	 * @return the uuid
	 */
	UUID getOwner();

	/**
	 * Sets the uuid of the owner.
	 * 
	 * @param uuid the uuid
	 */
	void setOwner(UUID uuid);


	/**
	 * Returns the owning player if online.
	 * 
	 * @return the player
	 */
	default Player getOwningPlayer() {
		UUID uuid = this.getOwner();
		return uuid == null ? null : Bukkit.getPlayer(uuid);
	}


	/**
	 * Checks if the given uuid owns this.
	 * 
	 * @param uuid the uuid
	 * @return if owned
	 */
	default boolean isOwner(UUID uuid) {
		return uuid.equals(this.getOwner());
	}


	/**
	 * Checks if the given player can access this ownable.
	 * 
	 * First checks if the player owns this ownable, otherwise it checks whether the owner and the player are in the same team.
	 * 
	 * @param player the player
	 * @return if the player can access
	 */
	default boolean canAccess(OfflinePlayer player) {
		if(player.isOp() || player instanceof Permissible perm && perm.hasPermission("cookies.ownable"))
			return true;

		if(this.isOwner(player.getUniqueId()))
			return true;

		final Team team = Team.getTeamFromPlayer(this.getOwner());
		return team != null && team.isMember(player.getUniqueId());
	}

}
