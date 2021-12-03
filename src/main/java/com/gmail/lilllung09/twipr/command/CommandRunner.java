package com.gmail.lilllung09.twipr.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class CommandRunner implements CommandExecutor {
	private Plugin plugin;
	private static final Map<String, DefaultCommand> REPOSITORY_COMMANDS = new HashMap<>();
	
	static {
		REPOSITORY_COMMANDS.put("help", new CommandHelpMsg());
		REPOSITORY_COMMANDS.put("reload", new CommandReload());
		REPOSITORY_COMMANDS.put("st", new CommandStreamer());
		REPOSITORY_COMMANDS.put("missmatch", new CommandMissMatchArgs());
		REPOSITORY_COMMANDS.put("state", new CommandConnectState());
	}

	public CommandRunner(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	/*
	 * sender = 명령어 실행 주체
	 * command = run to command
	 * str = 명령어 -> td4m
	 * args = 명령어 파라매타
	 */
	public boolean onCommand(CommandSender sender, Command command, String str, String[] args) {

		if ( 0 == args.length ) {
			REPOSITORY_COMMANDS.get("help").execCommand(sender, args);
			return false;
		}

		if (!REPOSITORY_COMMANDS.containsKey(args[0])) {
			REPOSITORY_COMMANDS.get("missmatch").execCommand(sender, args);
			return false;
		}

		DefaultCommand defaultCommand = REPOSITORY_COMMANDS.get(args[0]);
		defaultCommand.execCommand(sender, args);

		
		return true;
	}



}

