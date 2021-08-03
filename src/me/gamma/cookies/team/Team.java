
package me.gamma.cookies.team;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;



public class Team {

	public static final Map<UUID, Set<String>> requests = new HashMap<>();
	public static final TeamRegistry TEAM_REGISTRY = new TeamRegistry();

	private String name;
	private UUID owner;
	private Set<UUID> members = new HashSet<>();

	private Team(String name, UUID creator) {
		this.name = name;
		this.owner = creator;
		this.members.add(creator);
	}


	private Team() {}


	public String getName() {
		return name;
	}


	public boolean isEmpty() {
		return this.members.isEmpty();
	}


	public boolean addMember(Player executor, UUID player) {
		if(!isOwner(executor.getUniqueId())) {
			executor.sendMessage("§cYou have to be the Owner of that Team!");
			return false;
		}
		if(TEAM_REGISTRY.getTeamFromPlayer(player) != null) {
			executor.sendMessage("§cThis Player already has a Team!");
			return false;
		}
		if(Bukkit.getOfflinePlayer(player).isOnline()) {
			executor.sendMessage("§aJoin Request sent!");
			sendRequest(player);
			return true;
		} else {
			executor.sendMessage("§aJoin Request queued!");
			return queueRequest(player);
		}
	}


	public boolean queueRequest(UUID player) {
		Set<String> teams = requests.getOrDefault(player, new HashSet<>());
		boolean b = teams.add(name);
		requests.put(player, teams);
		return b;
	}


	public void sendRequest(UUID player) {
		TextComponent component = new TextComponent("§eDo you want to join " + name + "? ");
		TextComponent accept = new TextComponent("[Accept]");
		accept.setColor(ChatColor.GREEN);
		accept.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/cookie team accept " + name));
		TextComponent deny = new TextComponent("[Deny]");
		deny.setColor(ChatColor.RED);
		deny.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/cookie team deny " + name));
		component.addExtra(accept);
		component.addExtra(" ");
		component.addExtra(deny);
		OfflinePlayer offline = Bukkit.getOfflinePlayer(player);
		if(offline.isOnline() && offline instanceof Player) {
			((CraftPlayer) offline).spigot().sendMessage(component);
		}
	}


	public boolean removeMember(Player executor, UUID member) {
		if(!isOwner(executor.getUniqueId()) && !executor.getUniqueId().equals(member)) {
			executor.sendMessage("§cYou have to be the Owner of that Team!");
			return false;
		}
		executor.sendMessage("§aRemoved Teammember!");
		return members.remove(member);
	}


	public boolean isMember(UUID player) {
		return members.contains(player);
	}


	public boolean setOwner(Player executor, UUID player) {
		if(!isOwner(executor.getUniqueId())) {
			executor.sendMessage("§cYou have to be the Owner of that Team!");
			return false;
		}
		if(isOwner(player)) {
			executor.sendMessage("§eYou are already the Owner of this Team!");
			return false;
		}
		this.owner = player;
		executor.sendMessage("§aYou changed the Owner of that Team!");
		return true;
	}


	public boolean isOwner(UUID player) {
		return this.owner.equals(player);
	}


	public boolean deleteTeam(Player executor) {
		if(!isOwner(executor.getUniqueId())) {
			executor.sendMessage("§cYou have to be the Owner of that Team!");
			return false;
		}
		if(TEAM_REGISTRY.unregister(executor, name)) {
			executor.sendMessage("§aTeam " + name + " removed!");
			return true;
		} else {
			return false;
		}
	}


	public void load(NBTTagCompound compound) {
		name = compound.getString("name");
		long owner_most = compound.getLong("owner_most");
		long owner_least = compound.getLong("owner_least");
		owner = new UUID(owner_most, owner_least);
		NBTTagList list = compound.getList("members", 10);
		list.stream().filter(nbt -> nbt instanceof NBTTagCompound).map(nbt -> (NBTTagCompound) nbt).forEach(member -> members.add(new UUID(member.getLong("most"), member.getLong("least"))));
	}


	public NBTTagCompound save() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("name", name);
		compound.setLong("owner_most", owner.getMostSignificantBits());
		compound.setLong("owner_least", owner.getLeastSignificantBits());
		NBTTagList list = new NBTTagList();
		members.stream().map(member -> {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setLong("most", member.getMostSignificantBits());
			tag.setLong("least", member.getLeastSignificantBits());
			return tag;
		}).forEach(list::add);
		compound.set("members", list);
		return compound;
	}


	public static boolean accept(Player executor, String name) {
		Team team = TEAM_REGISTRY.getTeamByName(name);
		if(team == null) {
			executor.sendMessage("§cThis Team doesn't exist anymore!");
			return false;
		}
		executor.sendMessage("§aYou are now member of the Team " + name + "!");
		return team.members.add(executor.getUniqueId());
	}


	public static boolean add(Player executor, UUID player) {
		Team team = TEAM_REGISTRY.getTeamFromPlayer(executor.getUniqueId());
		if(team == null) {
			executor.sendMessage("§cYou are not in any Team!");
			return false;
		}
		if(!team.isOwner(executor.getUniqueId())) {
			executor.sendMessage("§cYou don't own any Team!");
			return false;
		}
		return team.addMember(executor, player);
	}


	public static Team create(Player creator, String name) {
		if(TEAM_REGISTRY.getTeamFromPlayer(creator.getUniqueId()) != null) {
			creator.sendMessage("§cYou already are in a Team!");
			return null;
		}
		final Team team = new Team(name, creator.getUniqueId());
		if(TEAM_REGISTRY.register(creator, team)) {
			creator.sendMessage("§aCreated new Team with name " + name + "!");
			return team;
		}
		return null;
	}


	public static boolean delete(Player executor) {
		Team team = TEAM_REGISTRY.getTeamFromPlayer(executor.getUniqueId());
		if(team == null) {
			executor.sendMessage("§cYou are not in any Team!");
			return false;
		}
		if(!team.isOwner(executor.getUniqueId())) {
			executor.sendMessage("§cYou don't own any Team!");
			return false;
		}
		return team.deleteTeam(executor);
	}


	public static boolean deny(Player executor, String name) {
		Team team = TEAM_REGISTRY.getTeamByName(name);
		if(team == null) {
			executor.sendMessage("§cThis Team doesn't exist anymore!");
			return false;
		}
		executor.sendMessage("§aYou denied the request from the Team " + name + "!");
		return true;
	}


	public static boolean leave(Player executor) {
		Team team = TEAM_REGISTRY.getTeamFromPlayer(executor.getUniqueId());
		if(team == null) {
			executor.sendMessage("§cYou are not in any Team!");
			return false;
		}
		if(team.isOwner(executor.getUniqueId())) {
			executor.sendMessage("§cYou can't leave a Team as the Owner!");
			return false;
		}
		return team.removeMember(executor, executor.getUniqueId());
	}


	public static void list(Player executor) {
		executor.sendMessage("§6===== §eTeams §6=====");
		TEAM_REGISTRY.getTeams().forEach(team -> executor.sendMessage("  §d" + team.getName()));
		executor.sendMessage("§6===== §eTeams §6=====");
	}


	public static boolean members(Player executor) {
		Team team = TEAM_REGISTRY.getTeamFromPlayer(executor.getUniqueId());
		if(team == null) {
			executor.sendMessage("§cYou are not in any Team!");
			return false;
		}
		executor.sendMessage("§6===== §5Members §6=====");
		team.members.forEach(uuid -> executor.sendMessage("  §d" + Bukkit.getOfflinePlayer(uuid).getName()));
		executor.sendMessage("§6===== §5Members §6=====");
		return false;
	}


	public static boolean remove(Player executor, UUID player) {
		Team team = TEAM_REGISTRY.getTeamFromPlayer(executor.getUniqueId());
		if(team == null) {
			executor.sendMessage("§cYou are not in any Team!");
			return false;
		}
		if(!team.isOwner(executor.getUniqueId())) {
			executor.sendMessage("§cYou don't own any Team!");
			return false;
		}
		boolean b = team.removeMember(executor, player);
		if(b && team.isEmpty())
			TEAM_REGISTRY.unregister(executor, team.getName());
		return b;
	}


	public static Team loadTeam(NBTTagCompound compound) {
		Team team = new Team();
		team.load(compound);
		return team;
	}

}
