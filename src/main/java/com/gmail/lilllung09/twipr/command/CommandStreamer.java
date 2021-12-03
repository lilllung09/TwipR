package com.gmail.lilllung09.twipr.command;

import com.gmail.lilllung09.twipr.Streamer;
import com.gmail.lilllung09.twipr.TwipConnection;
import com.gmail.lilllung09.twipr.TwipR;
import com.gmail.lilllung09.twipr.TwipRMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.bukkit.command.CommandSender;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

public class CommandStreamer implements DefaultCommand {
    private CommandSender commandSender;
    @Override
    public void execCommand(CommandSender commandSender, String[] args) {
        this.commandSender = commandSender;
        String[] parsedArgs = this.setArgs(args, commandSender.isOp());

        if (parsedArgs.length == 0) {
            new CommandNoPermission().execCommand(commandSender, args);
            return;
        }
        if (parsedArgs.length == 1) {
            new CommandMissMatchArgs().execCommand(commandSender, args);
            return;
        }

        Streamer s;

        // return new String[]{sub, minecraftID} -> add, info
        // return new String[]{sub, arg, minecraftID} -> connect, key, token, preset
        switch (parsedArgs[0]) {
            case "add":
                if ((s = getStreamer(parsedArgs[1])) != null) {
                    TwipRMessage.sendWanTo(commandSender, parsedArgs[1] + " 은(는) 이미 등록된 스트리머입니다.");
                    break;
                }

                this.registeStreamer(parsedArgs[1]);
                this.saveToFile();
                break;

            case "del":
                if ((s = getStreamer(parsedArgs[1])) == null) {
                    TwipRMessage.sendWanTo(commandSender, parsedArgs[1] + " 은(는) 등록되지 않은 스트리머입니다.");
                    break;
                }

                s.disconnect();

                TwipConnection.STREAMERSJSON.getAsJsonObject("streamers").remove(parsedArgs[1]);
                this.saveToFile();
                //this.reloadConfig(parsedArgs[1]);
                TwipConnection.TwipStreamers.remove(parsedArgs[1]);
                break;

            case "info":
                if ((s = getStreamer(parsedArgs[1])) == null) {
                    TwipRMessage.sendWanTo(commandSender, parsedArgs[1] + " 은(는) 등록되지 않은 스트리머입니다.");
                    break;
                }

                TwipRMessage.runCmd("tellraw " + commandSender.getName() + " "
                + "[" +
                    "{\"text\":\"\\n\"}" +
                    ",{\"text\":\"========[" + parsedArgs[1] + "]======[" + (s.isConnect() ? "ON" : "OFF") + "]=\\n\",\"bold\":true}" +

                    ",{\"text\":\"AutoConnect : " + (s.getConnect() ? "사용" : "미사용") + "\\n\",\"bold\":false" +
                        ",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"" + (s.getConnect() ? "미사용 및 연결 끊기(클릭)" : "사용 및 연결 하기(클릭)") + "\"}]}" +
                        ",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" +
                        (s.getConnect() ?
                                "/twipr st connect " + (commandSender.isOp() ? parsedArgs[1] + " " : "") + "false"
                                : "/twipr st connect " + (commandSender.isOp() ? parsedArgs[1] + " " : "") + "true") +
                        "\"}" +
                    "}" +


                    ",{\"text\":\"API_KEY : \",\"bold\":false" +
                        ",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"/twipr st key YOUR_KEY\"}]}" +
                        ",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr help st\"}" +
                    "}" +

                    ",{\"text\":\"[ 보 기 ]\\n\",\"bold\":false" +
                        ",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"" + s.getKey() + "\"}]}" +
                    "}" +


                    ",{\"text\":\"API_TOKEN : \",\"bold\":false" +
                        ",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"/twipr st token YOUR_TOKEN\"}]}" +
                        ",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr help st\"}" +
                    "}" +

                    ",{\"text\":\"[ 보 기 ]\\n\",\"bold\":false" +
                        ",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"" + s.getToken() + "\"}]}" +
                    "}" +


                    ",{\"text\":\"Preset : " + s.getPreset() + "\\n\",\"bold\":false" +
                        ",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"/twipr st preset PRESET\"}]}" +
                        ",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr help st\"}" +
                    "}" +
                "]", 0);

                break;

            case "alertbox_key":
            case "alertbox_token":
            case "slotmachine":
                if ((s = getStreamer(parsedArgs[2])) == null) {
                    TwipRMessage.sendWanTo(commandSender, parsedArgs[2] + " 은(는) 등록되지 않은 스트리머입니다.");
                    break;
                }

                TwipConnection.STREAMERSJSON.getAsJsonObject("streamers").getAsJsonObject(parsedArgs[2]).addProperty(parsedArgs[0], parsedArgs[1]);
                this.saveToFile();
                this.reloadConfig(parsedArgs[2]);
                break;


            case "connect":
                if ((s = getStreamer(parsedArgs[2])) == null) {
                    TwipRMessage.sendWanTo(commandSender, parsedArgs[2] + " 은(는) 등록되지 않은 스트리머입니다.");
                    break;
                }

                boolean connect = false;

                if (parsedArgs[1].equals("true")) {
                    connect = true;
                } else if (parsedArgs[1].equals("false")) {
                    connect = false;
                } else {
                    new CommandMissMatchArgs().execCommand(commandSender, args);
                    break;
                }

                if (s.getConnect() == connect) {
                    TwipRMessage.sendMsgTo(commandSender, "이미 " + (connect ? "켜져" : "꺼져") + "있습니다.");
                    break;
                }

                if (connect) {
                    s.connect();
                } else {
                    s.disconnect();
                }

                TwipConnection.STREAMERSJSON.getAsJsonObject("streamers").getAsJsonObject(parsedArgs[2]).addProperty("connect", Boolean.valueOf(connect));
                this.saveToFile();
                this.reloadConfig(parsedArgs[2]);
                break;

            default:
                new CommandMissMatchArgs().execCommand(commandSender, args);
                return ;
        }


    }


    // return new String[]{sub, arg, [minecraftID]};
    private String[] setArgs(String[] args, boolean isOP) {
        if (args.length == 1) {
            return new String[]{""};
        }

        switch(args[1]) {
            case "add":
            case "del" :
            case "info":
                if (args.length == 2) { //자신
                    return new String[]{args[1], commandSender.getName()};
                } else if (args.length == 3 && isOP) { //다른 사람꺼
                    if (isOP) {
                        return new String[]{args[1], args[2]};
                    } else {
                        return new String[]{};
                    }
                }
                break;

            case "connect":
            case "key":
            case "token":
            case "preset":
                String sub;
                if (args[1].equals("key")) {
                    sub = "alertbox_key";
                } else if (args[1].equals("token")) {
                    sub = "alertbox_token";
                } else if (args[1].equals("preset")){
                    sub = "slotmachine";
                } else {
                    sub = "connect";
                }

                if (args.length == 3) { //자신
                    return new String[]{sub, args[2], commandSender.getName()};
                } else if (args.length == 4 ) { //다른 사람꺼
                    if (isOP) {
                        return new String[]{sub, args[3], args[2]};
                    } else {
                        return new String[]{};
                    }
                }
                break;

            default:
                return new String[]{""};
        }
        return new String[]{""};
    }

    private Streamer getStreamer(String minecraftID) {
        return TwipConnection.TwipStreamers.get(minecraftID);
    }

    private Streamer registeStreamer(String minecraftID) {
        Streamer s = new Streamer(false, " ", " ", minecraftID, " ");

        TwipConnection.TwipStreamers.put(minecraftID, s);

        JsonObject streamer = new JsonObject();
        streamer.addProperty("connect", Boolean.valueOf(false));
        streamer.addProperty("alertbox_key", "");
        streamer.addProperty("alertbox_token", "");
        streamer.addProperty("slotmachine", "");

        TwipConnection.STREAMERSJSON.getAsJsonObject("streamers").add(minecraftID, streamer);

        return s;
    }

    private boolean saveToFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter fw = new FileWriter(TwipR.plugin.getDataFolder().getPath() + "/streamer.json", StandardCharsets.UTF_8);
            gson.toJson(TwipConnection.STREAMERSJSON, fw);
            fw.flush();
            fw.close();

        } catch (Exception e) {
            TwipRMessage.sendWanTo(commandSender, "적용 실패!");
            return false;
        }
        TwipRMessage.sendMsgTo(commandSender, "적용 완료!");
        return true;
    }

    private boolean reloadConfig(String minecraftID) {
        Streamer s = getStreamer(minecraftID);
        s.disconnect();

        JsonObject o = TwipConnection.STREAMERSJSON.getAsJsonObject("streamers").getAsJsonObject(minecraftID);

        boolean connect = o.get("connect").getAsBoolean();
        String key = o.get("alertbox_key").getAsString();
        String token = o.get("alertbox_token").getAsString();
        String presset = o.get("slotmachine").getAsString();

        TwipConnection.TwipStreamers.put(minecraftID, new Streamer(connect, key, token, minecraftID, presset));
        return true;
    }

}
