
package me.gamma.cookies.listener;


import java.util.Set;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.gamma.cookies.object.team.Team;



public class TeamQueueListener implements Listener {

	/**
	 * Checks if a joining player has any team requests that will be display if so.
	 * 
	 * @param event {@link PlayerJoinEvent}
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final UUID uuid = event.getPlayer().getUniqueId();
		Set<String> requests = Team.requests.remove(uuid);
		if(requests != null) {
			requests.forEach(string -> {
				Team team = Team.TEAM_REGISTRY.getTeamByName(string);
				if(team != null) {
					team.sendRequest(uuid);
				}
			});
		}
	}

}
