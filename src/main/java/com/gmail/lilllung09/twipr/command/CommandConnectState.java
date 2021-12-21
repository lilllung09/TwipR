package com.gmail.lilllung09.twipr.command;

import com.gmail.lilllung09.twipr.Permissions;
import com.gmail.lilllung09.twipr.TwipConnection;
import com.gmail.lilllung09.twipr.TwipRMessage;
import org.bukkit.command.CommandSender;

public class CommandConnectState extends DefaultCommand {
    @Override
    public void execCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Permissions.COMMANDS_STATE.getValue())) {
            new CommandNoPermission().execCommand(sender, args);
            return ;

        }

        TwipRMessage.runCmd("tellraw " + sender.getName() + " "
                        + "[" +
                        "{\"text\":\"\\n\"},{\"text\":\"==========TwipR Connect State=======[Red is OFF]===\\n\"}" +
                        parse() +

                        "]"
                , 0);

    }

    private String parse() {
        StringBuilder sb = new StringBuilder();

        TwipConnection.TwipStreamers.forEach((k, s) -> {
            if (s.isConnect()) {
                sb.append(",{\"text\":\"" + k + " \",\"color\":\"green\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"" + s.getPreset() + "\"}]}}");
            } else {
                sb.append(",{\"text\":\"" + k + " \",\"color\":\"red\",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"" + s.getPreset() + "\"}]}}");
            }
        });

        return sb.toString();
    }
}
