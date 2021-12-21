package com.gmail.lilllung09.twipr.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandRunner implements CommandExecutor {
	private final Map<String, DefaultCommand> registered_commands = new HashMap<>();

	public CommandRunner(Plugin plugin) {
		registered_commands.put("help", new CommandHelpMsg());
		registered_commands.put("st", new CommandStreamer(plugin));

		registered_commands.put("reload", new CommandReload());
		registered_commands.put("state", new CommandConnectState());
		registered_commands.put("test", new CommandTestONOFF());
		registered_commands.put("queue", new CommandQueue(plugin));

		registered_commands.put("force", new CommandForceSlotEvent());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {
		if ( 0 == args.length ) {
			registered_commands.get("help").execCommand(sender, args);
			return false;
		}

		if (!registered_commands.containsKey(args[0])) {
			new CommandMissMatchArgs().execCommand(sender, args);
			return false;
		}

		DefaultCommand defaultCommand = registered_commands.get(args[0]);
		defaultCommand.execCommand(sender, args);

		
		return true;
	}

	public List<String> getSubCommands(CommandSender commandSender, String[] args) {
		return this.registered_commands.get(args[0]).getSubCommands(commandSender, args);
	}
}

