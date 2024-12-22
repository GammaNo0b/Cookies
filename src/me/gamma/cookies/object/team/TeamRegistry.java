
package me.gamma.cookies.object.team;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.object.WorldPersistentDataStorage;
import me.gamma.cookies.util.core.MinecraftPersistentDataHelper;



public class TeamRegistry implements WorldPersistentDataStorage {

	public static final String TAG_TEAMS = "this.teams";

	private Map<String, Team> teams = new HashMap<>();

	public boolean register(Player executor, Team team) {
		if(this.teams.containsKey(team.getName())) {
			executor.sendMessage("§cThere already exists an Team with such a name!");
			return false;
		}
		return this.teams.put(team.getName(), team) == null;
	}


	public boolean unregister(Player executor, String name) {
		if(!this.teams.containsKey(name)) {
			executor.sendMessage("§cThere is no Team with such a name!");
			return false;
		}
		return this.teams.remove(name) != null;
	}


	public Team getTeamByName(String name) {
		return this.teams.get(name);
	}


	public Team getTeamFromPlayer(UUID player) {
		for(Team team : this.teams.values()) {
			if(team.isMember(player)) {
				return team;
			}
		}
		return null;
	}


	public Collection<Team> getTeams() {
		return this.teams.values();
	}


	public boolean containsTeam(String name) {
		return this.teams.containsKey(name);
	}


	@Override
	public String getIdentifier() {
		return "this.teams";
	}


	@Override
	public void load(World world, PersistentDataContainer container) {
		List<PersistentDataContainer> containers = container.getOrDefault(new NamespacedKey(Cookies.INSTANCE, TAG_TEAMS), PersistentDataType.LIST.dataContainers(), new ArrayList<>());
		for(PersistentDataContainer c : containers) {
			Team team = Team.loadTeam(c);
			if(team != null)
				this.teams.put(team.getName(), team);
		}
	}


	@Override
	public void save(World world, PersistentDataContainer container) {
		List<PersistentDataContainer> containers = new ArrayList<>(this.teams.size());
		for(Team team : this.teams.values()) {
			PersistentDataContainer c = MinecraftPersistentDataHelper.createNewPersistentDataContainer(container);
			team.save(c);
			containers.add(c);
		}
		container.set(new NamespacedKey(Cookies.INSTANCE, TAG_TEAMS), PersistentDataType.LIST.dataContainers(), containers);
	}

}
