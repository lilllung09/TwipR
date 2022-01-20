package com.gmail.lilllung09.twipr.twipevent;

public enum EventProcessType {

    PROCESS_TYPE_QUEUE("PROCESS_TYPE_QUEUE"),
    PROCESS_TYPE_REALTIME("PROCESS_TYPE_REALTIME"),
    PROCESS_TYPE_IMMEDIATE("PROCESS_TYPE_IMMEDIATE");


    private String value;
    private EventProcessType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value.substring(13);
    }
}
