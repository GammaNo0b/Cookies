
package me.gamma.cookies.objects.block;


import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataHolder;

import me.gamma.cookies.objects.property.UUIDProperty;
import me.gamma.cookies.team.Team;



public interface Ownable {

	public static final UUIDProperty OWNER = new UUIDProperty("owner");

	default void setOwner(PersistentDataHolder holder, UUID owner) {
		OWNER.store(holder, owner);
	}


	default UUID getOwner(PersistentDataHolder holder) {
		return OWNER.fetch(holder);
	}


	default boolean isOwner(PersistentDataHolder holder, UUID player) {
		return player.equals(getOwner(holder));
	}


	default boolean canAccess(PersistentDataHolder holder, Player player) {
		if(player.hasPermission("cookies.ownable")) {
			return true;
		}
		if(isOwner(holder, player.getUniqueId())) {
			return true;
		}
		final Team team = Team.TEAM_REGISTRY.getTeamFromPlayer(getOwner(holder));
		if(team == null) {
			return false;
		}
		if(team.isMember(player.getUniqueId())) {
			return true;
		}
		return false;
	}

}
