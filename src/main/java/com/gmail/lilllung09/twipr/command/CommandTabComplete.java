package com.gmail.lilllung09.twipr.command;

import com.gmail.lilllung09.twipr.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CommandTabComplete extends DefaultCommand implements TabCompleter {

    private CommandRunner commandRunner;

    public CommandTabComplete(Plugin plugin) {
        commandRunner = (CommandRunner) plugin.getServer().getPluginCommand("twipr").getExecutor();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();

        if ((args.length == 1)) {
            list.add("help");
            list.add("st");

            if (sender.hasPermission(Permissions.COMMANDS_RELOAD.getValue())) { list.add("reload"); }
            if (sender.hasPermission(Permissions.COMMANDS_STATE.getValue())) { list.add("state"); }
            if (sender.hasPermission(Permissions.COMMANDS_TEST.getValue())) { list.add("test"); }
            if (sender.hasPermission(Permissions.COMMANDS_QUEUE.getValue())) { list.add("queue"); }
            if (sender.hasPermission(Permissions.COMMANDS_FORCE_SLOT.getValue())) { list.add("force"); }

            list = super.getMatchingSubCommands(list, args[0]);

        } else if (args.length <= 2) {
            list = this.commandRunner.getSubCommands(sender, args);
        }

        return list;
    }
}
