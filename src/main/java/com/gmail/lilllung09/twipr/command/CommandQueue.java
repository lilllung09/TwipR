package com.gmail.lilllung09.twipr.command;

import com.gmail.lilllung09.twipr.TwipConnection;
import com.gmail.lilllung09.twipr.TwipRMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class CommandQueue implements DefaultCommand {
    private Plugin plugin;

    public CommandQueue(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execCommand(CommandSender commandSender, String[] args) {
        if (args[1].equals("start")) {
            if (this.plugin.getServer().getScheduler().isCurrentlyRunning(TwipConnection.queueTaskID)) {
                TwipRMessage.sendMsgTo(commandSender, "큐 형식 룰렛 결과 적용이 이미 되어있습니다.");
                return;
            }

            long sec = 4;
            if (args.length == 3) {
                try {
                    sec = Integer.parseInt(args[2]);

                } catch (NumberFormatException e) {
                    sec = 4;
                }
            }

            TwipConnection.queueTaskID = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, () -> {
                TwipConnection.TwipStreamers.forEach((minecraft_name, streamer) -> {
                    if (streamer.slotMachineQueueSize() > 0) {
                        for (String world : streamer.applyWorlds()) {
                            if (Bukkit.getPlayer(minecraft_name).getWorld().getName().equals(world)) {
                                streamer.getSlotMachineQueuePeek().consume();
                            }
                        }
                    }
                });

            }, 0L, sec * 20L);

            TwipRMessage.sendMsgTo(commandSender, "룰렛 결과가 " + sec + "초 마다 적용됩니다.");



        } else if (args[1].equals("pause")) {
            if (this.plugin.getServer().getScheduler().isCurrentlyRunning(TwipConnection.queueTaskID)) {
                this.plugin.getServer().getScheduler().cancelTask(TwipConnection.queueTaskID);
                TwipRMessage.sendMsgTo(commandSender, "큐 형식 룰렛 결과 적용이 잠시 멈춥니다. (룰렛 결과는 계속 쌓입니다)");
            } else {
                TwipRMessage.sendMsgTo(commandSender, "큐 형식 룰렛 결과 적용이 이미 멈춰 있습니다.");

            }


        } else if (args[1].equals("stop")) {
            TwipConnection.queueTaskID = -1;
            TwipRMessage.sendMsgTo(commandSender, "룰렛 결과가 바로 적용됩니다.");
        }
    }
}
