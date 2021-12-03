package com.gmail.lilllung09.twipr.command;

import com.gmail.lilllung09.twipr.TwipConnection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CommandTabComplete implements TabCompleter {

    private Plugin plugin;

    public CommandTabComplete(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("help");

            if (sender.isOp()) {
                list.add("reload");
                list.add("state");
            }

            list.add("st");

        } else if (args.length == 2) {
            switch (args[0]) {
                case "help":
                    list.add("st");
                    break;

                case "st":
                    list.add("add");
                    list.add("del");
                    list.add("info");
                    list.add("connect");
                    list.add("key");
                    list.add("token");
                    list.add("preset");
                    break;
            }

        } else if (args.length == 3) {
            switch (args[1]) {
                case "add": case "del": case "info":
                case "key": case "token":
                    if (sender.isOp()) {
                        plugin.getServer().getOnlinePlayers().forEach(p -> list.add(p.getName()));
                    }
                    break;

                case "connect":
                    list.add("true");
                    list.add("false");
                    if (sender.isOp()) {
                        plugin.getServer().getOnlinePlayers().forEach(p -> list.add(p.getName()));
                    }
                    break;

                case "preset":
                    TwipConnection.SlotMachinePreset.keySet().forEach(k -> list.add(k));
                    if (sender.isOp()) {
                        plugin.getServer().getOnlinePlayers().forEach(p -> list.add(p.getName()));
                    }
                    break;
            }
        } else if (sender.isOp() && args.length == 4) {
            switch (args[1]) {
                case "connect":
                    list.add("true");
                    list.add("false");
                    break;

                case "preset":
                    TwipConnection.SlotMachinePreset.keySet().forEach(k -> list.add(k));
                    break;
            }
        }

        return list;
    }
}
