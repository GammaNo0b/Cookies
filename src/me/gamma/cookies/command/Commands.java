
package me.gamma.cookies.command;


import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import me.gamma.cookies.Cookies;



public class Commands {

	public static void registerCommands() {
		registerCommand("cookie", new CookieCommand());
		registerCommand("debug", new DebugCommand());
		registerCommand("god", new GodCommand());
	}


	public static void registerCommand(String name, CommandExecutor executor) {
		PluginCommand command = Cookies.INSTANCE.getCommand(name);
		command.setExecutor(executor);
		if(executor instanceof TabCompleter)
			command.setTabCompleter((TabCompleter) executor);
	}

}
