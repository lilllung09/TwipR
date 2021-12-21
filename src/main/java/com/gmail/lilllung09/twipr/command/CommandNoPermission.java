package com.gmail.lilllung09.twipr.command;

import com.gmail.lilllung09.twipr.TwipRMessage;
import org.bukkit.command.CommandSender;

public class CommandNoPermission extends DefaultCommand {
    @Override
    public void execCommand(CommandSender sender, String[] args) {
        TwipRMessage.runCmd("tellraw " + sender.getName() + " "
                        + "[" +
                        "{\"text\":\"[TwipR] \",\"color\":\"aqua\"},{\"text\":\"[WARN] \",\"color\":\"red\"}" +
                        ",{\"text\":\"당신은 해당 명령어에 대한 권한이 없습니다.\"" +
                        ",\"color\":\"red\"" +
                        ",\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭 하여 실행\"}]}" +
                        ",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/twipr help\"}}" +

                        "]"
                , 0);
    }
}
