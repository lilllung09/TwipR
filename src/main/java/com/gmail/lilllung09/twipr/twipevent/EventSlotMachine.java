package com.gmail.lilllung09.twipr.twipevent;

import com.gmail.lilllung09.twipr.Streamer;
import com.gmail.lilllung09.twipr.TwipConnection;
import com.gmail.lilllung09.twipr.TwipR;
import com.gmail.lilllung09.twipr.TwipRMessage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class EventSlotMachine {

    public EventSlotMachine(String minecraft_name, JsonObject o) {
        String sender = o.get("nickname").getAsString();
        String comment = o.get("comment").getAsString();
        String amount = o.get("amount").getAsString();

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
        int duraiton = slotmachineData.getAsJsonObject("config").get("duration").getAsInt() * 20;

        String itemName = itemsList.get(gotchaIndex - 1).getAsString();
        if (!j.has(itemName)) {
            TwipRMessage.sendWanConsol(minecraft_name + " ->  item[" + itemName + "] was not registered in preset[" + s.getPreset() + "]");
            return;
        }

        //it was just test
        if (o.get("_id").getAsString().equals("TEST") && !TwipR.RUN_TEST_RESULT) {
            TwipRMessage.sendMsgConsol(minecraft_name + " -> 룰렛 테스트 [" + minecraft_name + "]");
            TwipRMessage.runCmd("title " + minecraft_name + " title [{\"text\":\"[\",\"color\":\"white\"},{\"text\":\"룰렛\",\"color\":\"green\"},{\"text\":\"] \",\"color\":\"white\"},{\"text\":\"" + sender + "\"}]", 0L);
            TwipRMessage.runCmd("title " + minecraft_name + " subtitle {\"text\":\"진짜인 줄 알았는데, 알고보니 룰렛 테스트 중이었네요.\",\"color\":\"white\"}", 0L);
            return;
        }

        JsonObject itemConfig = j.getAsJsonObject(itemName);
        if (itemConfig.has("commands")) {
            JsonArray cmdArray = itemConfig.getAsJsonArray("commands");
            cmdArray.forEach(cmd -> TwipRMessage.runCmd(cmdParser(cmd.getAsString(), minecraft_name, sender, comment, amount, itemName), duraiton));
        }

        logging(new String[] { minecraft_name, "after " + duraiton / 20 + "sec", itemName });
        logFile(minecraft_name, sender, amount, itemName);

    }

    private String cmdParser(String cmd, String minecraft_name, String sender, String comment, String amount, String itemName) {
        String parsedCmd = cmd;
        if (parsedCmd != null) {
            parsedCmd = parsedCmd.replace("%minecraft_name%", minecraft_name);
            parsedCmd = parsedCmd.replace("%comment%", comment);
            parsedCmd = parsedCmd.replace("%sender%", sender);
            parsedCmd = parsedCmd.replace("%amount%", amount);
            parsedCmd = parsedCmd.replace("%slot_item_name%", itemName);
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
        TwipRMessage.sendMsgConsol(args[0] + ", " + args[1] + ", " + args[2]);
    }
}
