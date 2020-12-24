
package me.gamma.cookies.command;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.gamma.cookies.managers.RecipeManager;
import me.gamma.cookies.objects.block.AbstractTileStateBlock;
import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.team.Team;
import me.gamma.cookies.util.Utilities;



public class CookieCommand implements CommandExecutor, TabCompleter {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] parameters) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("§cYou have to be a player to do that!");
			return false;
		}
		Player player = (Player) sender;

		if(parameters.length == 0) {
			RecipeManager.openRecipeCategoryList(player, 1, false);
			return true;
		}

		if(parameters[0].equals("cheat")) {

			if(!player.hasPermission("cookies.cheat")) {
				player.sendMessage("§cYou don't have the required Permission to do that!");
				return false;
			}

			if(parameters.length != 1) {
				player.sendMessage("§cSyntax: §e/cookies §6cheat");
				return false;
			}

			RecipeManager.openRecipeCategoryList(player, 1, true);
			return true;
		} else if(parameters[0].equals("give")) {
			if(!player.hasPermission("cookies.cheat")) {
				player.sendMessage("§cYou don't have the required Permission to do that!");
				return false;
			}

			if(parameters.length != 2) {
				player.sendMessage("§cSyntax: §e/cookies §6give <item>");
				return false;
			}

			String name = parameters[1];
			for(AbstractTileStateBlock block : CustomBlockSetup.customBlocks) {
				if(block.getRegistryName().equals(name)) {
					Utilities.giveItemToPlayer(player, block.createDefaultItemStack());
					return true;
				}
			}
			for(AbstractCustomItem item : CustomItemSetup.customItems) {
				if(item.getIdentifier().equals(name)) {
					Utilities.giveItemToPlayer(player, item.createDefaultItemStack());
					return true;
				}
			}
			return false;
		} else if(parameters[0].equals("team")) {
			if(parameters.length < 2) {
				player.sendMessage("§cSyntax: §e/cookies §6[ add | accept | create | delete | deny | leave | list | members | remove ]");
				return false;
			}
			String subcmd = parameters[1];
			if(subcmd.equals("accept")) {
				if(parameters.length != 3) {
					player.sendMessage("§cSyntax: §e/cookies §6team accept <name>");
					return false;
				}
				return Team.accept(player, parameters[2]);
			} else if(subcmd.equals("add")) {
				if(parameters.length != 3) {
					player.sendMessage("§cSyntax: §e/cookies §6team add <player>");
					return false;
				}
				return Team.add(player, Bukkit.getOfflinePlayer(parameters[2]).getUniqueId());
			} else if(subcmd.equals("create")) {
				if(parameters.length != 3) {
					player.sendMessage("§cSyntax: §e/cookies §6team create <name>");
					return false;
				}
				return Team.create(player, parameters[2]) != null;
			} else if(subcmd.equals("delete")) {
				if(parameters.length != 3) {
					player.sendMessage("§cSyntax: §e/cookies §6team delete");
					return false;
				}
				return Team.delete(player);
			} else if(subcmd.equals("deny")) {
				if(parameters.length != 3) {
					player.sendMessage("§cSyntax: §e/cookies §6team deny <name>");
					return false;
				}
				return Team.deny(player, parameters[2]);
			} else if(subcmd.equals("leave")) {
				if(parameters.length != 2) {
					player.sendMessage("§cSyntax: §e/cookies §6team leave");
					return false;
				}
				return Team.leave(player);
			} else if(subcmd.equals("list")) {
				if(parameters.length != 2) {
					player.sendMessage("§cSyntax: §e/cookies §6team list");
					return false;
				}
				Team.list(player);
				return true;
			} else if(subcmd.equals("members")) {
				if(parameters.length != 2) {
					player.sendMessage("§cSyntax: §e/cookies §6team members");
					return false;
				}
				return Team.members(player);
			} else if(subcmd.equals("remove")) {
				if(parameters.length != 3) {
					player.sendMessage("§cSyntax: §e/cookies §6team remove <player>");
					return false;
				}
				return Team.remove(player, Bukkit.getOfflinePlayer(parameters[2]).getUniqueId());
			}
		}

		return false;
	}


	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] parameters) {
		List<String> values = new ArrayList<>();

		if(parameters.length == 1) {
			Arrays.asList("cheat", "give", "team").stream().filter(val -> val.contains(parameters[0])).forEach(values::add);
		} else if(parameters.length == 2) {
			if(parameters[0].equals("give")) {
				CustomBlockSetup.customBlocks.stream().map(AbstractTileStateBlock::getRegistryName).filter(id -> id.contains(parameters[1])).forEach(values::add);
				CustomItemSetup.customItems.stream().map(AbstractCustomItem::getIdentifier).filter(id -> id.contains(parameters[1])).forEach(values::add);
				values.sort(String::compareTo);
			} else if(parameters[0].equals("team")) {
				Arrays.asList("accept", "add", "create", "delete", "deny", "leave", "list", "members", "remove").stream().filter(val -> val.contains(parameters[1])).forEach(values::add);
			}
		}

		return values;
	}

}
