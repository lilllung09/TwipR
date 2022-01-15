package com.gmail.lilllung09.twipr.command;

import com.gmail.lilllung09.twipr.Permissions;
import com.gmail.lilllung09.twipr.TwipR;
import com.gmail.lilllung09.twipr.TwipRMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandReload extends DefaultCommand {
    @Override
    public void execCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Permissions.COMMANDS_RELOAD.getValue())) {
            new CommandNoPermission().execCommand(sender, args);
            return ;
        }

        TwipRMessage.sendWanConsol("[!플러그인이 재시작 됩니다!]");

        if (sender instanceof Player)
            TwipRMessage.sendWanTo(sender, "[!플러그인을 재시작 합니다!]");

        TwipR.reload();

    }
}
