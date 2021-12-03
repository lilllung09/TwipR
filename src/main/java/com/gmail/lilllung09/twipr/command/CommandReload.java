package com.gmail.lilllung09.twipr.command;

import com.gmail.lilllung09.twipr.TwipR;
import com.gmail.lilllung09.twipr.TwipRMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandReload implements DefaultCommand {
    @Override
    public void execCommand(CommandSender commandSender, String[] args) {
        if (!commandSender.isOp()) {
            new CommandNoPermission().execCommand(commandSender, args);
            return ;
        }

        TwipRMessage.sendWanConsol("[!플러그인이 재시작 됩니다!]");

        if (commandSender instanceof Player)
            TwipRMessage.sendWanTo(commandSender, "[!플러그인을 재시작 합니다!]");

        TwipR.reload();

    }
}
