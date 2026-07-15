
package me.gamma.cookies.command;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import me.gamma.cookies.Cookies;
import me.gamma.cookies.feature.CookieFeature;
import me.gamma.cookies.init.Registries;
import me.gamma.cookies.object.block.AbstractCustomBlock;
import me.gamma.cookies.object.block.CustomBlockStorage;
import me.gamma.cookies.object.gui.book.CookieMenuBook;
import me.gamma.cookies.object.item.AbstractCustomItem;
import me.gamma.cookies.object.team.Team;
import me.gamma.cookies.object.tile.AbstractCustomTileEntity;
import me.gamma.cookies.object.tile.TileEntityStorage;
import me.gamma.cookies.util.ItemUtils;
import me.gamma.cookies.util.NBTUtils;
import me.gamma.cookies.util.collection.PersistentDataObject;



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
				player.sendMessage("§cYou don't have the required permission to do that!");
				return false;
			}

			if(parameters.length != 1) {
				player.sendMessage("§cSyntax: §e/cookies §6cheat");
				return false;
			}

			CookieMenuBook.openBook(player, true);
			return true;
		} else if(parameters[0].equals("data")) {
			if(!player.hasPermission("cookies.data")) {
				player.sendMessage("§cYou don't have the required permission to do that!");
				return false;
			}

			Block block = null;
			if(parameters.length == 1) {
				RayTraceResult result = player.rayTraceBlocks(4.0D);
				block = result == null ? null : result.getHitBlock();
			} else if(parameters.length == 4) {
				try {
					int x = Integer.parseInt(parameters[1]);
					int y = Integer.parseInt(parameters[2]);
					int z = Integer.parseInt(parameters[3]);

					block = player.getWorld().getBlockAt(x, y, z);
				} catch(NumberFormatException _) {}
			}

			if(block == null) {
				player.sendMessage("§cSyntax: §e/cookies §6data <X> <Y> <Z>");
				return false;
			}

			AbstractCustomBlock custom = CustomBlockStorage.BLOCK_STORAGE.getCustomBlock(block);
			if(custom == null) {
				player.sendMessage("§cThis block is not a custom block.");
				return true;
			}

			player.sendMessage("§7Custom Block: §6" + custom.getClass().getSimpleName() + " §8[§7" + custom.getIdentifier() + "§8]");

			AbstractCustomTileEntity<?, ?> tile = TileEntityStorage.TILE_ENTITY_STORAGE.getTileEntity(block);
			if(tile == null) {
				player.sendMessage("§cThis block does not have a custom tile entity.");
				return true;
			}

			PersistentDataObject object = new PersistentDataObject(player.getPersistentDataContainer().getAdapterContext());
			tile.save(null, object);
			String nbt = NBTUtils.convertPersistentDataToNBT(object.getContainer()).toString();
			player.sendMessage(nbt);
		} else if(parameters[0].equals("feature")) {
			if(!player.hasPermission("cookies.feature")) {
				player.sendMessage("§cYou don't have the required permission to do that!");
				return false;
			}

			if(parameters.length == 2) {
				if(parameters[1].equals("list")) {
					if(Cookies.INSTANCE.features.isEmpty()) {
						player.sendMessage("§cNo registered cookie features.");
						return true;
					}

					player.sendMessage("§6Cookie Features:");
					Cookies.INSTANCE.features.stream().sorted(Comparator.comparing(CookieFeature::getName)).forEach(feature -> player.sendMessage("  §e" + feature.getName() + "§6: " + (feature.isEnabled() ? "§aenabled" : "§cdisabled")));

					return true;
				}

				player.sendMessage("§cSyntax: §e/cookies §6[disable | enable | list]");
				return false;
			} else if(parameters.length == 3) {
				boolean disable = parameters[1].equals("disable");
				boolean enable = parameters[1].equals("enable");
				if(!disable && !enable) {
					player.sendMessage("§cSyntax: §e/cookies §6[disable | enable] <name>");
					return false;
				}

				String name = parameters[2];
				CookieFeature feature = Cookies.INSTANCE.features.getFeature(name);
				if(feature == null) {
					player.sendMessage("§cUnknown feature \"" + name + "\".");
					return false;
				}

				feature.setEnabled(enable);
				return true;
			}
		} else if(parameters[0].equals("give")) {
			if(!player.hasPermission("cookies.cheat")) {
				player.sendMessage("§cYou don't have the required permission to do that!");
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
					} catch(NumberFormatException _) {}
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
			Arrays.asList("cheat", "data", "feature", "give", "team").stream().filter(val -> val.contains(parameters[0])).forEach(values::add);
		} else if(parameters.length == 2) {
			if(parameters[0].equals("data")) {
				if(sender instanceof LivingEntity entity) {
					RayTraceResult result = entity.rayTraceBlocks(10.0D);
					if(result != null) {
						String s = String.valueOf(result.getHitBlock().getX());
						if(s.startsWith(parameters[1]))
							values.add(s);
					}
				}
			} else if(parameters[0].equals("feature")) {
				Arrays.asList("disable", "enable", "list").stream().filter(val -> val.contains(parameters[1])).forEach(values::add);
			} else if(parameters[0].equals("give")) {
				Registries.ITEMS.stream().map(AbstractCustomItem::getIdentifier).filter(id -> id.contains(parameters[1])).forEach(values::add);
				values.sort(String::compareTo);
			} else if(parameters[0].equals("team")) {
				Arrays.asList("accept", "add", "create", "delete", "deny", "leave", "list", "members", "remove").stream().filter(val -> val.contains(parameters[1])).forEach(values::add);
			}
		} else if(parameters.length == 3) {
			if(parameters[0].equals("data")) {
				if(sender instanceof LivingEntity entity) {
					RayTraceResult result = entity.rayTraceBlocks(10.0D);
					if(result != null) {
						String s = String.valueOf(result.getHitBlock().getY());
						if(s.startsWith(parameters[2]))
							values.add(s);
					}
				}
			} else if(parameters[0].equals("feature")) {
				if(parameters[1].equals("disable") || parameters[1].equals("enable")) {
					Cookies.INSTANCE.features.stream().map(CookieFeature::getName).filter(id -> id.contains(parameters[2])).forEach(values::add);
				}
			}
		} else if(parameters.length == 4) {
			if(parameters[0].equals("data")) {
				if(sender instanceof LivingEntity entity) {
					RayTraceResult result = entity.rayTraceBlocks(10.0D);
					if(result != null) {
						String s = String.valueOf(result.getHitBlock().getZ());
						if(s.startsWith(parameters[3]))
							values.add(s);
					}
				}
			}
		}

		return values;
	}

}
