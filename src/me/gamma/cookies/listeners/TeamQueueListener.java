
package me.gamma.cookies.listeners;


import java.util.Set;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.gamma.cookies.team.Team;



public class TeamQueueListener implements Listener {

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
