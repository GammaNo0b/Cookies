
package me.gamma.cookies.command;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.gamma.cookies.init.Registries;
import me.gamma.cookies.object.gui.book.CookieMenuBook;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.team.Team;
import me.gamma.cookies.util.ItemUtils;



public class CookieCommand implements TabExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] parameters) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("§cYou have to be a player to do that!");
			return false;
		}
		Player player = (Player) sender;

		if(parameters.length == 0) {
			CookieMenuBook.openBook(player, false);
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

			CookieMenuBook.openBook(player, true);
			return true;
		} else if(parameters[0].equals("give")) {
			if(!player.hasPermission("cookies.cheat")) {
				player.sendMessage("§cYou don't have the required Permission to do that!");
				return false;
			}

			if(parameters.length != 2 && parameters.length != 3) {
				player.sendMessage("§cSyntax: §e/cookies §6give <item> [<amount>]");
				return false;
			}

			String name = parameters[1];
			AbstractCustomItem item = Registries.ITEMS.filterFirst(i -> i.getIdentifier().equals(name));
			if(item != null) {
				ItemStack stack = item.get();
				if(parameters.length == 3) {
					int amount = 1;
					try {
						amount = Integer.parseInt(parameters[2]);
					} catch(NumberFormatException e) {}
					stack.setAmount(amount);
				}
				ItemUtils.giveItemToPlayer(player, stack);
				return true;
			}
			return false;
		} else if(parameters[0].equals("team")) {
			if(parameters.length < 2) {
				player.sendMessage("§cSyntax: §e/cookies team §6[add | accept | create | delete | deny | leave | list | members | remove]");
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
				Team.remove(player, Bukkit.getOfflinePlayer(parameters[2]).getUniqueId());
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
				Registries.ITEMS.stream().map(AbstractCustomItem::getIdentifier).filter(id -> id.contains(parameters[1])).forEach(values::add);
				values.sort(String::compareTo);
			} else if(parameters[0].equals("team")) {
				Arrays.asList("accept", "add", "create", "delete", "deny", "leave", "list", "members", "remove").stream().filter(val -> val.contains(parameters[1])).forEach(values::add);
			}
		}

		return values;
	}

}
