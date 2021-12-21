package com.gmail.lilllung09.twipr.twipevent;

import com.gmail.lilllung09.twipr.Streamer;
import com.gmail.lilllung09.twipr.TwipConnection;
import com.gmail.lilllung09.twipr.TwipR;
import com.gmail.lilllung09.twipr.TwipRMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class EventSlotMachine {

    private JsonArray cmdArray;
    private String minecraft_name;
    private String sender;
    private String comment, amount;
    private String itemName;
    private int duraiton;

    public EventSlotMachine(String minecraft_name, JsonObject o) {
        this.minecraft_name = minecraft_name;
        this.sender = o.get("nickname").getAsString();
        this.comment = o.get("comment").getAsString();
        this.amount = o.get("amount").getAsString();

        JsonObject slotmachineData = o.getAsJsonObject("slotmachine_data");

        //it's donate type was slotmachine
        if (slotmachineData == null) return; //후원, 영상, 음성

        Streamer s = TwipConnection.TwipStreamers.get(minecraft_name);

        JsonObject j = TwipConnection.SlotMachinePreset.get(s.getPreset());
        if (j == null ) {
            TwipRMessage.sendWanConsol(minecraft_name + " -> using unregistered preset");
            return;
        }

        JsonArray itemsList = slotmachineData.getAsJsonArray("items");
        int gotchaIndex = slotmachineData.get("gotcha").getAsInt();
        duraiton = slotmachineData.getAsJsonObject("config").get("duration").getAsInt() * 20;

        this.itemName = itemsList.get(gotchaIndex - 1).getAsString();
        if (!j.has(itemName)) {
            TwipRMessage.sendWanConsol(minecraft_name + " ->  item[" + itemName + "] was not registered in preset[" + s.getPreset() + "]");
            return;
        }

        //it was just test
        if (o.get("_id").getAsString().equals("TEST") && !TwipR.RUN_TEST_RESULT) {
            TwipRMessage.sendMsgConsol(minecraft_name + " -> 룰렛 테스트 [" + minecraft_name + "]");
            TwipRMessage.runCmd("tellraw " + minecraft_name + " [{\"text\":\"[\",\"color\":\"white\"}" +
                    ",{\"text\":\"룰렛\",\"color\":\"green\"}" +
                    ",{\"text\":\"] \",\"color\":\"white\"},{\"text\":\"" + sender + "\"}" +
                    ",{\"text\":\" 진짜인 줄 알았는데, 알고보니 룰렛 테스트 중이었네요.\",\"color\":\"white\"}]", 0L);
            return;
        }

        JsonObject itemConfig = j.getAsJsonObject(itemName);
        if (itemConfig.has("commands")) {
            cmdArray = itemConfig.getAsJsonArray("commands");
        }

        if (!o.get("_id").getAsString().equals("FORCE")) {
            logFile(minecraft_name, sender, amount, itemName);
        }

        logging(new String[] { minecraft_name, "after " + duraiton / 20 + "sec", itemName, "" + (TwipConnection.TwipStreamers.get(minecraft_name).slotMachineQueueSize()+1)});

    }

    public void consume() {
        if (cmdArray != null) {
            cmdArray.forEach(cmd -> TwipRMessage.runCmd(cmdParser(cmd.getAsString(), minecraft_name, sender, comment, amount, itemName), duraiton));
        }
    }

    private String cmdParser(String cmd, String minecraft_name, String sender, String comment, String amount, String itemName) {
        String parsedCmd = cmd;
        if (parsedCmd != null) {
            parsedCmd = parsedCmd.replace("%minecraft_name%", minecraft_name);
            parsedCmd = parsedCmd.replace("%comment%", comment);
            parsedCmd = parsedCmd.replace("%sender%", sender);
            parsedCmd = parsedCmd.replace("%amount%", amount);
            parsedCmd = parsedCmd.replace("%slot_item_name%", itemName);
            parsedCmd = parsedCmd.replace("%slot_result_remain%", "" + (TwipConnection.TwipStreamers.get(minecraft_name).slotMachineQueueSize()+1));
        }
        return parsedCmd;
    }

    private void logFile(String minecraft_name, String sender, String amount, String itemName) {
        try {
            FileWriter fw = new FileWriter(TwipR.plugin.getDataFolder().getPath() + "/" + minecraft_name + ".json", StandardCharsets.UTF_8, true);
            fw.append("[" + new Date() + "]: " + sender + ", " + amount + ", " + itemName + System.lineSeparator());
            fw.flush();
            fw.close();
        } catch (Exception exception) {
            exception.printStackTrace();}
    }

    private void logging(String[] args) {
        TwipRMessage.sendMsgConsol(args[0] + ", " + args[1] + ", 남음: " + args[3] + "개, " + args[2] );
    }
}
