
package me.gamma.cookies.command;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import me.gamma.cookies.util.Debug;



public class DebugCommand implements TabExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.hasPermission("cookies.debug")) {
			sender.sendMessage("§cYou are unable to do that!");
			return false;
		}

		if(args.length == 0) {
			sender.sendMessage("§cSyntax: §6/debug [var|script] ...");
			return false;
		}

		if("var".equals(args[0])) {
			if(args.length == 1) {
				sender.sendMessage("§cSyntax: §6/debug var [get|set] ...");
				return false;
			}
			if("get".equals(args[1])) {
				if(args.length != 3) {
					sender.sendMessage("§cSyntax: §6/debug var get <name>");
					return false;
				}

				Object object = Debug.getVariable(args[2]);
				sender.sendMessage("§7" + Objects.toString(object));

				return true;
			} else if("set".equals(args[1])) {
				if(args.length != 5) {
					sender.sendMessage("§cSyntax: §6/debug var set <name> <value> <type>");
					return false;
				}

				DataType type = types.get(args[4].toUpperCase());
				if(type == null) {
					sender.sendMessage("§cUnknown data type '§7" + args[4] + "§c'.");
					return false;
				}

				Object value = type.parse(args[3]);
				Debug.setVariable(args[2], value);

				return true;
			} else {
				sender.sendMessage("§cSyntax: §6/debug var [get|set] ...");
				return false;
			}
		} else if("script".equals(args[0])) {
			if(args.length != 3 || !args[1].equals("execute")) {
				sender.sendMessage("§cSyntax: §6/debug script execute <script>");
				return false;
			}

			Debug.executeScript(args[2]);

			return true;
		} else {
			sender.sendMessage("§cSyntax: §6/debug [var|script] ...");
			return false;
		}
	}


	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 1) {
			return Arrays.asList("script", "var").stream().filter(val -> val.contains(args[0])).toList();
		} else if(args.length == 2) {
			if(args[0].equals("var")) {
				return Arrays.asList("get", "set").stream().filter(val -> val.contains(args[1])).toList();
			} else if(args[0].equals("script")) {
				return Arrays.asList("execute").stream().filter(val -> val.contains(args[1])).toList();
			}
		} else if(args.length == 3) {
			if(args[0].equals("var")) {
				if(args[1].equals("get") || args[1].equals("set")) {
					return Debug.getVariables().stream().filter(val -> val.contains(args[2])).sorted().toList();
				}
			} else if(args[0].equals("script") && args[1].equals("execute")) {
				return Debug.getScripts().stream().filter(val -> val.contains(args[2])).sorted().toList();
			}
		} else if(args.length == 5) {
			if(args[0].equals("var") && args[1].equals("set")) {
				return Stream.of(DataType.values()).map(DataType::name).map(String::toLowerCase).filter(val -> val.contains(args[4].toLowerCase())).sorted().toList();
			}
		}

		return List.of();
	}

	private static final HashMap<String, DataType> types = new HashMap<>();

	private static enum DataType {

		NULL(str -> null),
		BOOL(Boolean::parseBoolean),
		INT(Integer::parseInt),
		LONG(Long::parseLong),
		FLOAT(Float::parseFloat),
		DOUBLE(Double::parseDouble),
		STRING(str -> str);

		private final Function<String, Object> parser;

		private DataType(Function<String, Object> parser) {
			this.parser = parser;

			types.put(this.name(), this);
		}


		Object parse(String arg) {
			try {
				return this.parser.apply(arg);
			} catch(Exception e) {
				return null;
			}
		}

	}

}
