package com.gmail.lilllung09.twipr;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class TwipRMessage {
    public static final String PLUGIN_TAG = "[TwipR]";
    public static final ConsoleCommandSender consol = Bukkit.getConsoleSender();


    public static void sendMsgConsol(String msg) {
        consol.sendMessage(ChatColor.AQUA + PLUGIN_TAG + " " + msg);
    }
    public static void sendWanConsol(String msg) {
        sendMsgConsol(ChatColor.RED + "[WARN] " + msg);
    }


    public static void sendMsgTo(CommandSender commandSender, String msg) {
        commandSender.sendMessage(ChatColor.AQUA + PLUGIN_TAG + " " + msg);
    }
    public static void sendWanTo(CommandSender commandSender, String msg) {
        sendMsgTo(commandSender, ChatColor.RED + "[WARN] " + msg);
    }

    public static void runCmd(String cmd, long delay) {
        try {
            Bukkit.getScheduler().scheduleSyncDelayedTask(TwipR.plugin, () -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }, delay);

        } catch (Exception e) {
            e.printStackTrace();
            TwipRMessage.sendWanConsol("run failed -> " + cmd);
        }
    }

}
