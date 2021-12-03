package com.gmail.lilllung09.twipr.command;

import com.gmail.lilllung09.twipr.TwipRMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandMissMatchArgs implements DefaultCommand {

	@Override
	public void execCommand(CommandSender commandSender, String[] args) {
		TwipRMessage.runCmd("tellraw " + commandSender.getName() + " "
						+ "[" +
						"{\"text\":\"[TwipR] \",\"color\":\"aqua\"},{\"text\":\"[WARN] \",\"color\":\"red\"}" +
						",{\"text\":\"잘못된 명령어 입니다. - /twipr help\"" +
						",\"color\":\"red\"" +
						",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 실행\"}]}" +
						",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr help\"}}" +

						"]"
				, 0);
		
	}

}
