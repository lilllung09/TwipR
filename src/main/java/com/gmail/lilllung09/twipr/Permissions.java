package com.gmail.lilllung09.twipr;

public enum Permissions {

    //COMMANDS
    COMMANDS_STATE("twipr.commands.state"),
    COMMANDS_FORCE_SLOT("twipr.commands.force.slot"),
    COMMANDS_QUEUE("twipr.commands.queue"),
    COMMANDS_RELOAD("twipr.commands.reload"),
    COMMANDS_ST_EDIT("twipr.commands.st.edit"),
    COMMANDS_TEST("twipr.commands.test");

    private String name;
    private Permissions(String name) {
        this.name = name;
    }
    public String getValue() {
        return this.name;
    }
}
