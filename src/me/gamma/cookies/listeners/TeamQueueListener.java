
package me.gamma.cookies.listeners;


import java.util.HashSet;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.gamma.cookies.team.Team;



public class TeamQueueListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final UUID uuid = event.getPlayer().getUniqueId();
		if(Team.requests.containsKey(uuid)) {
			Team.requests.getOrDefault(uuid, new HashSet<>()).forEach(string -> {
				Team team = Team.TEAM_REGISTRY.getTeamByName(string);
				if(team != null) {
					team.sendRequest(uuid);
				}
			});
			Team.requests.remove(uuid);
		}
	}

}
