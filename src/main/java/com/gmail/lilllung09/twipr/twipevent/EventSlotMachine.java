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

    //private JsonArray cmdArray;
    private String minecraft_name;
    private JsonObject messageContents;

    public EventSlotMachine(String minecraft_name, JsonObject messageContents) {
        this.minecraft_name = minecraft_name;
        this.messageContents = messageContents;
    }

    public void consume() {
        Streamer streamer = TwipConnection.TwipStreamers.get(this.minecraft_name);
        JsonObject preset = TwipConnection.SlotMachinePreset.get(streamer.getPreset());
        // 등록되지 않은 프리셋 사용중
        if (preset == null ) {
            TwipRMessage.sendWanConsol(this.minecraft_name + " -> using unregistered preset");
            return;
        }

        // 슬롯머신에 등록된 아이템 리스트
        JsonArray itemsList = this.messageContents.getAsJsonObject("slotmachine_data").getAsJsonArray("items");

        // 뽑힌 아이템 위치
        int gotchaIndex = this.messageContents.getAsJsonObject("slotmachine_data").get("gotcha").getAsInt() - 1;

        // 뽑힌 아이템 이름
        String itemName = itemsList.get(gotchaIndex).getAsString();

        // 룰렛 결과가 프리셋에 등록된 아이템이 아님
        if (!preset.has(itemName)) {
            TwipRMessage.sendWanConsol(this.minecraft_name + " ->  item[" + itemName + "] was not registered in preset[" + streamer.getPreset() + "]");
            return;
        }

        // 슬롯머신 테스트 중
        if (messageContents.get("_id").getAsString().equals("TEST") && !TwipR.RUN_TEST_RESULT) {
            TwipRMessage.runCmd("tellraw " + minecraft_name
                            + " [{\"text\":\"[\",\"color\":\"white\"}" + ",{\"text\":\"룰렛\",\"color\":\"green\"}"
                            + ",{\"text\":\"] \",\"color\":\"white\"},{\"text\":\"" + messageContents.get("nickname").getAsString()  + "\"}"
                            + ",{\"text\":\" 진짜인 줄 알았는데, 알고보니 룰렛 테스트 중이었네요.\",\"color\":\"white\"}]"
                    , 0L);

            return;


        }
        // 슬롯머신 가짜가 아니면 파일로 기록
        if (!messageContents.get("_id").getAsString().equals("FORCE")) {
            logFile(minecraft_name, itemName);
        }

        // 프리셋에 등록된 아이템 정보
        JsonObject itemConfig = preset.getAsJsonObject(itemName);
        // 프리셋 작성 오류
        if (!itemConfig.has("commands")) {
            TwipRMessage.sendWanConsol(this.minecraft_name + " ->  item[" + itemName + "] has no commands");
            return;

        // 실행할 명령어가 없음
        } else if (itemConfig.getAsJsonArray("commands").size() < 1) {
            TwipRMessage.sendWanConsol(this.minecraft_name + " ->  item[" + itemName + "] has no runnable commands");
            return;
        }

        // 아이템에 등록된 실행할 명령어
        JsonArray cmdArray = itemConfig.getAsJsonArray("commands");

        // 슬롯머신 딜레이(돌아가는 효과 기간)
        int duration = messageContents.getAsJsonObject("slotmachine_data").getAsJsonObject("config").get("duration").getAsInt() * 20;
        cmdArray.forEach(cmd -> {
            TwipRMessage.runCmd(cmdParser(cmd.getAsString()
                            , itemName)
                    , duration);
        });

        TwipRMessage.sendMsgConsol(minecraft_name
                + ", " + (duration / 20) + "초 뒤"
                + ", 남음: " + (TwipConnection.TwipStreamers.get(minecraft_name).slotMachineQueueSize()+1)
                + "개, " + itemName
        );
    }

    private String cmdParser(String cmd, String itemName) {
        String parsedCmd = cmd;
        if (parsedCmd != null) {
            parsedCmd = parsedCmd.replace("%minecraft_name%", this.minecraft_name);
            parsedCmd = parsedCmd.replace("%comment%", this.messageContents.get("comment").getAsString());
            parsedCmd = parsedCmd.replace("%sender%", this.messageContents.get("nickname").getAsString());
            parsedCmd = parsedCmd.replace("%amount%", this.messageContents.get("amount").getAsString());
            parsedCmd = parsedCmd.replace("%slot_item_name%", itemName);
            parsedCmd = parsedCmd.replace("%slot_result_remain%", "" + (TwipConnection.TwipStreamers.get(this.minecraft_name).slotMachineQueueSize()+1));
        }
        return parsedCmd;
    }
    private void logFile(String minecraft_name, String itemName) {
        try {
            FileWriter fw = new FileWriter(TwipR.plugin.getDataFolder().getPath() + "/" + minecraft_name + ".json", StandardCharsets.UTF_8, true);
            fw.append("[").append(new Date().toString()).append("]: ");
            fw.append(this.messageContents.get("nickname").getAsString());
            fw.append(", ").append(this.messageContents.get("amount").getAsString());
            fw.append(", ").append(itemName).append(System.lineSeparator());

            fw.flush();
            fw.close();

        } catch (Exception e) {
            TwipRMessage.sendMsgConsol(minecraft_name + " <- error was occurred to log in File");
        }
    }
}
