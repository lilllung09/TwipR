package com.gmail.lilllung09.twipr.command;

import com.gmail.lilllung09.twipr.Permissions;
import com.gmail.lilllung09.twipr.Streamer;
import com.gmail.lilllung09.twipr.TwipConnection;
import com.gmail.lilllung09.twipr.TwipRMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandQueue extends DefaultCommand {
    private Plugin plugin;

    public CommandQueue(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execCommand(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            new CommandMissMatchArgs().execCommand(sender, args);
            return ;
        }

        if (sender.hasPermission(Permissions.COMMANDS_QUEUE.getValue()))

        if (args[1].equals("start")) {
            if (TwipConnection.queueTaskState == TwipConnection.QUEUE_STATE_RUN) {
                TwipRMessage.sendMsgTo(sender, "큐 형식 룰렛 결과 적용이 이미 실행중입니다. (상태: " + getState() + ")");
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
                for (Map.Entry<String, Streamer> map : TwipConnection.TwipStreamers.entrySet()) {
                    if (Bukkit.getPlayer(map.getKey()) == null) continue;
                    if (!Bukkit.getPlayer(map.getKey()).isOnline()) continue;
                    if (map.getValue().slotMachineQueueSize() < 1) continue;

                    for (String world : map.getValue().applyWorlds()) {
                        if (!Bukkit.getPlayer(map.getKey()).getWorld().getName().equals(world)) continue;

                        map.getValue().getSlotMachineQueuePeek().consume();
                    }
                }

            }, 0L, sec * 20L);
            TwipConnection.queuePeriod = (int) sec;

            if (TwipConnection.queueTaskID == -1) {
                TwipConnection.queueTaskState = TwipConnection.QUEUE_STATE_STOP;
                TwipRMessage.sendMsgTo(sender, "큐 형식 룰렛 테스크 생성에 실패했습니다.");
                TwipRMessage.sendMsgTo(sender, "룰렛 결과가 바로 적용됩니다.");

            } else {
                TwipConnection.queueTaskState = TwipConnection.QUEUE_STATE_RUN;
                TwipRMessage.sendMsgTo(sender, "룰렛 결과가 " + sec + "초 마다 적용됩니다.");
            }


        } else if (args[1].equals("pause")) {
            if (TwipConnection.queueTaskState == TwipConnection.QUEUE_STATE_RUN) {
                TwipConnection.queueTaskState = TwipConnection.QUEUE_STATE_PAUSE;
                this.plugin.getServer().getScheduler().cancelTask(TwipConnection.queueTaskID);

                TwipRMessage.sendMsgTo(sender, "큐 형식 룰렛 결과 적용이 잠시 멈춥니다. (룰렛 결과는 계속 쌓입니다)");

            } else if (TwipConnection.queueTaskState != TwipConnection.QUEUE_STATE_RUN) {
                TwipRMessage.sendMsgTo(sender, "큐 형식 룰렛 결과 적용이 이미 멈춰 있습니다. (상태: " + getState() + ")");
            }


        } else if (args[1].equals("stop")) {
            boolean remain = false;

            for(Map.Entry<String, Streamer> map : TwipConnection.TwipStreamers.entrySet()) {
                remain = remain | (map.getValue().slotMachineQueueSize() > 0);
            }

            if (remain) {
                TwipRMessage.sendMsgTo(sender, "큐에 룰렛 결과가 남아있어 멈추지 못합니다.");

            } else  {
                TwipConnection.queueTaskState = TwipConnection.QUEUE_STATE_STOP;
                this.plugin.getServer().getScheduler().cancelTask(TwipConnection.queueTaskID);

                TwipRMessage.sendMsgTo(sender, "룰렛 결과가 바로 적용됩니다.");
            }

        } else if (args[1].equals("check") || args[1].equals("check o")) {
            boolean only = false;
            if (args.length == 2) {
                only = true;
            }

            for (Map.Entry<String, Streamer> map : TwipConnection.TwipStreamers.entrySet()) {
                Player p = Bukkit.getPlayer(map.getKey());
                if (p == null) { continue; }
                if (p.getGameMode() != GameMode.SURVIVAL) { continue; }

                int remain = TwipConnection.TwipStreamers.get(map.getKey()).slotMachineQueueSize();

                if (remain != 0 && !only) {
                    p.sendMessage("[" + ChatColor.GREEN + "룰렛" + ChatColor.WHITE + "] 남은 룰렛 결과: " + remain + "개, (적용 간격: " + TwipConnection.queuePeriod + "초)");
                }
                sender.sendMessage("[" + ChatColor.GREEN + "룰렛" + ChatColor.WHITE + "] " + map.getKey() + "의 남은 룰렛 결과: " + remain + "개, (적용 간격: " + TwipConnection.queuePeriod  + "초)");
            }

        }
    }

    @Override
    public List<String> getSubCommands(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        list.add("start");
        list.add("pause");
        list.add("stop");
        list.add("check");
        list.add("check o");

        return super.getMatchingSubCommands(list, args[args.length-1]);
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
