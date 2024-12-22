
package me.gamma.cookies.command;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.gamma.cookies.object.property.BooleanProperty;
import me.gamma.cookies.util.Utils;



public class GodCommand implements TabExecutor, Listener {

	public static final BooleanProperty GOD = new BooleanProperty("god");

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.isOp()) {
			sender.sendMessage("§cYou have to be OP do switch to God Mode!");
			return false;
		}

		if(args.length == 0) {
			if(!(sender instanceof Player)) {
				sender.sendMessage("§cYou have to be a Player to execute this command.");
				return false;
			}
			this.switchGodMode((Player) sender);
		} else if(args.length == 1) {
			for(Player player : Bukkit.getOnlinePlayers()) {
				if(player.getName().startsWith(args[0])) {
					this.switchGodMode(player);
					break;
				}
			}
		} else {
			sender.sendMessage("§cWrong Syntax! Use §e/god §6[<Player>] §c.");
		}

		return false;
	}


	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> values = new ArrayList<>();

		if(!sender.isOp())
			return values;

		if(args.length == 1)
			for(Player player : Bukkit.getOnlinePlayers())
				if(player.getName().startsWith(args[0]))
					values.add(player.getName());

		return values;
	}


	private void switchGodMode(Player player) {
		if(isGod(player)) {
			removeGod(player);
			GOD.remove(player);
			player.sendMessage("§6You are no longer in God Mode!");
		} else {
			setGod(player);
			GOD.storeEmpty(player);
			player.sendMessage("§6You are now in God Mode!");
		}
	}


	private static void setGod(Player player) {
		player.setAllowFlight(true);
	}


	@SuppressWarnings("deprecation")
	private static void removeGod(Player player) {
		player.setAllowFlight((player.getGameMode().getValue() & 1) == 1);
	}


	public static boolean isGod(Entity entity) {
		return GOD.isPropertyOf(entity);
	}


	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(isGod(player))
			setGod(player);
	}


	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if(isGod(event.getEntity()))
			event.setCancelled(true);
	}


	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Location location = player.getLocation();
		if(isGod(player)) {
			player.spigot().respawn();
			player.teleport(location);
		}
	}


	@EventHandler
	public void onHunger(FoodLevelChangeEvent event) {
		if(isGod(event.getEntity()))
			event.setFoodLevel(20);
	}


	@EventHandler
	public void onGamemodeChange(PlayerGameModeChangeEvent event) {
		final Player player = event.getPlayer();
		final boolean flying = player.isFlying();
		if(isGod(player))
			Utils.runLater(() -> {
				player.setAllowFlight(true);
				player.setFlying(flying);
			});
	}


	public static boolean canFlyByDefault(Player player) {
		GameMode mode = player.getGameMode();
		return mode == GameMode.SPECTATOR || mode == GameMode.CREATIVE || isGod(player);
	}

}
