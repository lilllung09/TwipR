package com.gmail.lilllung09.twipr.command;

import com.gmail.lilllung09.twipr.Permissions;
import com.gmail.lilllung09.twipr.Streamer;
import com.gmail.lilllung09.twipr.TwipConnection;
import com.gmail.lilllung09.twipr.TwipRMessage;
import com.gmail.lilllung09.twipr.twipevent.EventProcessType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandEventProcessType extends DefaultCommand{
    private Plugin plugin;

    public CommandEventProcessType(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Permissions.COMMANDS_EVENT_PROCESS_TYPE.getValue())) {
            new CommandNoPermission().execCommand(sender, args);
            return;
        }
        if (args.length <= 1) {
            new CommandMissMatchArgs().execCommand(sender, args);
            return;
        }
        switch (args[1]) {
            case "queue": { // moved from CommandQueue.java 2022/01/21
                if (args[2].equals("start")) {
                    if (TwipConnection.queueTaskID > 0) {
                        TwipRMessage.sendMsgTo(sender, "룰렛 처리 방식이 이미 큐 방식 입니다.(상태: " + getState() + ")");
                        return;
                    }

                    long sec = 4;
                    try {
                        sec = Integer.parseInt(args[3]);
                    } catch (Exception e) {
                        sec = 4;
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

                    if (TwipConnection.queueTaskID == -1) {
                        TwipRMessage.sendMsgTo(sender, "큐 방식 룰렛 처리 테스크 생성에 실패했습니다.");
                    } else {
                        TwipConnection.processType = EventProcessType.PROCESS_TYPE_QUEUE;
                        TwipRMessage.sendMsgTo(sender, "큐 방식 룰렛 처리가 " + sec + "초 마다 실행합니다.");
                    }
                }
                else if (args[2].equals("pause")) {
                    if (TwipConnection.queueTaskID == -1) {
                        TwipRMessage.sendMsgTo(sender, "큐 형식 룰렛 결과 적용이 이미 멈춰 있습니다. (상태: " + getState() + ")");
                    } else {
                        this.plugin.getServer().getScheduler().cancelTask(TwipConnection.queueTaskID);
                        TwipConnection.queueTaskID = -1;
                        TwipRMessage.sendMsgTo(sender, "큐 방식 룰렛 처리 테스크가 잠시 멈춥니다. (룰렛은 계속 쌓입니다)");
                    }
                }

            } break;

            case "realtime": {
                if (!stopQueue()) {
                    TwipRMessage.sendMsgTo(sender, "큐에 룰렛 결과가 남아있어 바꾸지 못합니다.");
                    return;
                }
                TwipConnection.processType = EventProcessType.PROCESS_TYPE_REALTIME;
                TwipRMessage.sendMsgTo(sender, "실제 방송의 룰렛과 결과 적용 타이밍을 맞춥니다.");

            } break;

            case "immediate": {
                if (!stopQueue()) {
                    TwipRMessage.sendMsgTo(sender, "큐에 룰렛 결과가 남아있어 바꾸지 못합니다.");
                    return;
                }
                TwipConnection.processType = EventProcessType.PROCESS_TYPE_IMMEDIATE;
                TwipRMessage.sendMsgTo(sender, "룰렛을 기다리지 않고 결과를 바로 적용합니다.");

            } break;

            case "stop": {

            } break;

            case "check": {
                /*boolean only = false;
                if (args.length == 2) {
                    only = true;
                }

                for (Map.Entry<String, Streamer> map : TwipConnection.TwipStreamers.entrySet()) {
                    Player p = Bukkit.getPlayer(map.getKey());
                    if (p == null) { continue; }
                    if (p.getGameMode() != GameMode.SURVIVAL) { continue; }

                    int remain = TwipConnection.TwipStreamers.get(map.getKey()).slotMachineQueueSize();

                    if (remain != 0 && !only) {
                        p.sendMessage("[" + ChatColor.GREEN + "룰렛" + ChatColor.WHITE + "] 남은 룰렛 결과: " + remain + "개");
                    }
                    sender.sendMessage("[" + ChatColor.GREEN + "룰렛" + ChatColor.WHITE + "] " + map.getKey() + "의 남은 룰렛 결과: " + remain + "개");
                }*/
            } break;
        }
    }

    @Override
    public List<String> getSubCommands(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 2) {
            list.add("queue");
            list.add("real");
            list.add("immediate");
            list.add("stop");
            list.add("check");

        } else if (args.length == 3) {
            if (args[1].equals("queue")) {
                list.add("start");
                list.add("pause");
            }
        }

        return super.getMatchingSubCommands(list, args[args.length-1]);
    }

    private String getState() {
        return TwipConnection.processType.getValue();
    }

    private boolean stopQueue() {
        boolean remain = false;

        for(Map.Entry<String, Streamer> map : TwipConnection.TwipStreamers.entrySet()) {
            remain = remain | (map.getValue().slotMachineQueueSize() > 0);
        }

        if (!remain && TwipConnection.queueTaskID > 0) {
            this.plugin.getServer().getScheduler().cancelTask(TwipConnection.queueTaskID);
            TwipConnection.queueTaskID = -1;
        }

        return remain;
    }
}
