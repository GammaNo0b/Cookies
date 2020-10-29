package me.gamma.cookies.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.gamma.cookies.objects.block.AbstractTileStateBlock;
import me.gamma.cookies.objects.item.AbstractCustomItem;
import me.gamma.cookies.objects.item.CookieCookBook;
import me.gamma.cookies.setup.CustomBlockSetup;
import me.gamma.cookies.setup.CustomItemSetup;
import me.gamma.cookies.util.Utilities;


public class CookieCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] parameters) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(parameters.length == 0) {
				if(player.isOp()) {
					CookieCookBook.openRecipeCategoryList(player, 1, true);
				} else {
					player.sendMessage("§cYou have to be OP to do that!");
				}
			} else if(parameters.length == 2) {
				if(parameters[0].equals("give")) {
					String name = parameters[1];
					for(AbstractTileStateBlock block : CustomBlockSetup.customBlocks) {
						if(block.getIdentifier().equals(name)) {
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
				}
			}
		} else {
			sender.sendMessage("§cYou have to be a player to do that!");
		}
		return false;
	}
	
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] parameters) {
		List<String> values = new ArrayList<>();
		
		if(parameters.length == 1) {
			if("give".contains(parameters[0]))
				values.add("give");
		} else if(parameters.length == 2) {
			CustomBlockSetup.customBlocks.stream().map(AbstractTileStateBlock::getIdentifier).filter(id -> id.startsWith(parameters[1])).forEach(values::add);
			CustomItemSetup.customItems.stream().map(AbstractCustomItem::getIdentifier).filter(id -> id.startsWith(parameters[1])).forEach(values::add);
			values.sort(String::compareTo);
		}
		
		return values;
	}

}
