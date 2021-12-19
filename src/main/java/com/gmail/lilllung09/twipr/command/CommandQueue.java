package com.gmail.lilllung09.twipr.command;

import com.gmail.lilllung09.twipr.Streamer;
import com.gmail.lilllung09.twipr.TwipConnection;
import com.gmail.lilllung09.twipr.TwipRMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class CommandQueue implements DefaultCommand {
    private Plugin plugin;

    public CommandQueue(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execCommand(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            return;
        }

        if (args[1].equals("start")) {
            if (TwipConnection.queueTaskState == TwipConnection.QUEUE_STATE_RUN) {
                TwipRMessage.sendMsgTo(commandSender, "큐 형식 룰렛 결과 적용이 이미 실행중입니다. (상태: " + getState() + ")");
                return;

            }

            long sec = 4;
            if (args.length >= 3) {
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

            if (TwipConnection.queueTaskID == -1) {
                TwipConnection.queueTaskState = TwipConnection.QUEUE_STATE_STOP;
                TwipRMessage.sendMsgTo(commandSender, "큐 형식 룰렛 테스크 생성에 실패했습니다.");
                TwipRMessage.sendMsgTo(commandSender, "룰렛 결과가 바로 적용됩니다.");

            } else {
                TwipConnection.queueTaskState = TwipConnection.QUEUE_STATE_RUN;
                TwipRMessage.sendMsgTo(commandSender, "룰렛 결과가 " + sec + "초 마다 적용됩니다.");
            }


        } else if (args[1].equals("pause")) {
            if (TwipConnection.queueTaskState == TwipConnection.QUEUE_STATE_RUN) {
                TwipConnection.queueTaskState = TwipConnection.QUEUE_STATE_PAUSE;
                this.plugin.getServer().getScheduler().cancelTask(TwipConnection.queueTaskID);

                TwipRMessage.sendMsgTo(commandSender, "큐 형식 룰렛 결과 적용이 잠시 멈춥니다. (룰렛 결과는 계속 쌓입니다)");

            } else if (TwipConnection.queueTaskState != TwipConnection.QUEUE_STATE_RUN) {
                TwipRMessage.sendMsgTo(commandSender, "큐 형식 룰렛 결과 적용이 이미 멈춰 있습니다. (상태: " + getState() + ")");
            }


        } else if (args[1].equals("stop")) {
            boolean remain = false;

            for(Map.Entry<String, Streamer> map : TwipConnection.TwipStreamers.entrySet()) {
                remain = remain | (map.getValue().slotMachineQueueSize() > 0);
            }

            if (remain) {
                TwipRMessage.sendMsgTo(commandSender, "큐에 룰렛 결과가 남아있어 멈추지 못합니다.");

            } else  {
                TwipConnection.queueTaskState = TwipConnection.QUEUE_STATE_STOP;
                this.plugin.getServer().getScheduler().cancelTask(TwipConnection.queueTaskID);

                TwipRMessage.sendMsgTo(commandSender, "룰렛 결과가 바로 적용됩니다.");
            }
        }
    }

    private String getState() {
        return switch (TwipConnection.queueTaskState) {
            case TwipConnection.QUEUE_STATE_RUN -> "RUNNING";
            case TwipConnection.QUEUE_STATE_PAUSE -> "PAUSE";
            case TwipConnection.QUEUE_STATE_STOP -> "STOPPED";

            default -> "UNKNOWN";
        };
    }
}
