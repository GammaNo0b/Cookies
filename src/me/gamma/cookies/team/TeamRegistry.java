
package me.gamma.cookies.team;


import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.util.INBTRegistry;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;



public class TeamRegistry implements INBTRegistry<NBTTagList> {

	private Map<String, Team> teams = new HashMap<>();

	public boolean register(Player executor, Team team) {
		if(teams.containsKey(team.getName())) {
			executor.sendMessage("§cThere already exists an Team with such a name!");
			return false;
		}
		return teams.put(team.getName(), team) == null;
	}


	public boolean unregister(Player executor, String name) {
		if(!teams.containsKey(name)) {
			executor.sendMessage("§cThere is no Team with such a name!");
			return false;
		}
		return teams.remove(name) != null;
	}


	public Team getTeamByName(String name) {
		return teams.get(name);
	}


	public Team getTeamFromPlayer(UUID player) {
		for(Team team : teams.values()) {
			if(team.isMember(player)) {
				return team;
			}
		}
		return null;
	}


	public Collection<Team> getTeams() {
		return teams.values();
	}


	public boolean containsTeam(String name) {
		return teams.containsKey(name);
	}


	@Override
	public String getRegistryName() {
		return "teams";
	}


	@Override
	public void load(NBTTagList list) {
		list.forEach(nbt -> {
			if(nbt instanceof NBTTagCompound) {
				Team team = Team.loadTeam((NBTTagCompound) nbt);
				teams.put(team.getName(), team);
			}
		});
	}


	@Override
	public NBTTagList save() {
		NBTTagList list = new NBTTagList();
		teams.values().forEach(team -> list.add(team.save()));
		return list;
	}


	public static File getConfigFile() {
		return new File(Cookies.INSTANCE.getDataFolder(), "teams.dat");
	}

}
