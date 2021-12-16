package com.gmail.lilllung09.twipr.command;

import com.gmail.lilllung09.twipr.TwipR;
import com.gmail.lilllung09.twipr.TwipRMessage;
import org.bukkit.command.CommandSender;

public class CommandTestONOFF implements DefaultCommand{
    @Override
    public void execCommand(CommandSender commandSender, String[] args) {
        if (!commandSender.isOp()) return;

        Boolean test = false;
        if (args.length >= 2) {
            test = Boolean.parseBoolean(args[1]);
            test = (test != null) ? test : false;

        } else if (args.length == 1) {
            test = !TwipR.RUN_TEST_RESULT;
        }
        
        TwipR.RUN_TEST_RESULT = test;
        
        if (TwipR.RUN_TEST_RESULT) {
            TwipRMessage.sendMsgTo(commandSender, "룰렛 테스트 결과가 실제로 적용됩니다.");
        } else {
            TwipRMessage.sendMsgTo(commandSender, "룰렛 테스트 결과는 적용되지 않습니다.");
        }

    }
}
