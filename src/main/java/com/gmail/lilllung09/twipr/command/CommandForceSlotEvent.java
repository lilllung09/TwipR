package com.gmail.lilllung09.twipr.command;

import com.gmail.lilllung09.twipr.Permissions;
import com.gmail.lilllung09.twipr.Streamer;
import com.gmail.lilllung09.twipr.TwipConnection;
import com.gmail.lilllung09.twipr.twipevent.EventSlotMachine;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class CommandForceSlotEvent extends DefaultCommand{
    private Plugin plugin;

    public CommandForceSlotEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execCommand(CommandSender commandSender, String[] args) {
        if (args.length < 4) {
            new CommandMissMatchArgs().execCommand(commandSender, args);
            return ;
        }

        if (!commandSender.hasPermission(Permissions.COMMANDS_FORCE_SLOT.getValue())) {
            new CommandNoPermission().execCommand(commandSender, args);
            return ;
        }

        String sender = args[1];
        String target = args[2];
        String slot_item = "";

        int amount = 5000;
        String message = "";

        for (int i = 3; i < args.length; i++) {
            slot_item = slot_item + " " + args[i];
        }
        slot_item = slot_item.substring(1, slot_item.length());

        Streamer s = TwipConnection.TwipStreamers.get(target);

        JsonObject jsonObject = new JsonObject();


        jsonObject.addProperty("_id", "FORCE");
        jsonObject.addProperty("nickname", sender);
        jsonObject.addProperty("amount", amount);
        jsonObject.addProperty("comment", message);

        /////////////////////slotmachine_data
        JsonObject slotmachine_data = new JsonObject();

        JsonArray items = new JsonArray();
        items.add(slot_item);
        slotmachine_data.add("items", items);

        slotmachine_data.addProperty("gotcha", 1);

        JsonObject config = new JsonObject();
        config.addProperty("duration", 2);
        slotmachine_data.add("config", config);
        /////////////////////
        jsonObject.add("slotmachine_data", slotmachine_data);


        if (TwipConnection.queueTaskState == TwipConnection.QUEUE_STATE_STOP) {
            s.getSlotMachineQueue().add(new EventSlotMachine(target, jsonObject));
            s.getSlotMachineQueuePeek().consume();
        } else {
            s.getSlotMachineQueue().add(new EventSlotMachine(target, jsonObject));
        }
    }
}
