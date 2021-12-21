package com.gmail.lilllung09.twipr.command;

import com.gmail.lilllung09.twipr.Permissions;
import com.gmail.lilllung09.twipr.TwipR;
import com.gmail.lilllung09.twipr.TwipRMessage;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandTestONOFF extends DefaultCommand{

    @Override
    public void execCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission(Permissions.COMMANDS_TEST.getValue())) {
            new CommandNoPermission().execCommand(sender, args);
            return;
        }

        boolean test = false;
        if (args.length >= 2) {
            test = Boolean.parseBoolean(args[1]);

        } else if (args.length == 1) {
            test = !TwipR.RUN_TEST_RESULT;
        }
        
        TwipR.RUN_TEST_RESULT = test;
        
        if (TwipR.RUN_TEST_RESULT) {
            TwipRMessage.sendMsgTo(sender, "룰렛 테스트 결과가 실제로 적용됩니다.");
        } else {
            TwipRMessage.sendMsgTo(sender, "룰렛 테스트 결과는 적용되지 않습니다.");
        }

    }

    @Override
    public List<String> getSubCommands(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        list.add("true");
        list.add("false");

        return super.getMatchingSubCommands(list, args[args.length-1]);
    }
}
