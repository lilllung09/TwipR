package com.gmail.lilllung09.twipr;

import com.gmail.lilllung09.twipr.twipevent.EventProcessType;
import com.gmail.lilllung09.twipr.twipevent.EventSlotMachine;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;

import java.io.FileWriter;
import java.net.URI;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;

import java.util.*;

import javax.net.ssl.SSLContext;

import org.bukkit.Bukkit;

public class Streamer {

    private boolean connect;
    private String key;
    private String token;
    private String minecraft_name;
    private String preset;
    private String[] worlds;

    private WebSocket ws;

    private Queue<EventSlotMachine> slotMachineQueue = new LinkedList<>();
    private Map<String, EventSlotMachine> slotMachineMap = new HashMap<>();

    JsonParser parser = new JsonParser();

    public Streamer(boolean connect, String alertbox_key, String alertbox_token, String minecraft_name, String preset, String[] worlds) {
        this.connect = connect;
        this.key = alertbox_key;
        this.token = alertbox_token;
        this.minecraft_name = minecraft_name;
        this.preset = preset;
        this.worlds = worlds;

        if (this.key.replace(" ", "").length() < 2
                || this.token.replace(" ", "").length() < 2) {

            this.key = "null";
            this.token = "null";
        }

        StringBuilder url = new StringBuilder();
        try {
            url.append("wss://io.mytwip.net/socket.io/");
            url.append("?alertbox_key=" + this.key);
            url.append("&version=1.1.62");
            url.append("&token=" + URLEncoder.encode(this.token, StandardCharsets.UTF_8.toString()));
            url.append("&EIO=3&transport=websocket");

            WebSocketFactory socket = new WebSocketFactory();

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);

            socket.setSSLContext(sslContext);
            socket.setVerifyHostname(false);

            this.ws = socket.createSocket(new URI(url.toString()));
            this.ws.addListener(this.webSocketListener);

            if (this.connect) this.ws.connect();

        } catch (Exception e) {
            TwipRMessage.sendMsgConsol(minecraft_name + " <- disconnected with error");
        }
    }

    private WebSocketListener webSocketListener = new WebSocketAdapter() {
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
            TwipRMessage.sendMsgConsol(minecraft_name + " <- connected...");
            Bukkit.getScheduler().runTaskTimer(TwipR.plugin, () -> websocket.sendText("2"), 100L, 100L);
        }

        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
            TwipRMessage.sendMsgConsol(minecraft_name + " <- disconnected...");
        }

        public void onTextMessage(WebSocket websocket, String message) {

            // 불필요 정보
            if (!message.startsWith("42")) return;

            // 토큰 관련 정보보
            if (message.contains("TOKEN_NOT_FOUND")) { // 토큰 정보 없음
                TwipRMessage.sendWanConsol(minecraft_name + " -> TOKEN_NOT_FOUND");

            } else if (message.contains("TOKEN_EXPIRED")) { // 토큰 만료
                TwipRMessage.sendWanConsol(minecraft_name + " -> TOKEN_EXPIRED");
            }

            JsonArray jsonArray = parser.parse(message.substring(2)).getAsJsonArray();

            // 내용없음
            if (jsonArray.size() < 2) return;
            String messageType = jsonArray.get(0).getAsString();
            JsonObject messageContents = jsonArray.get(1).getAsJsonObject();

            if (messageType.equals("new donate")) { //일반후원, 영상, 음성, 룰렛 발생시
                String donateId = messageContents.get("_id").getAsString();

                if (messageContents.getAsJsonObject("slotmachine_data") != null) { // 룰렛
                    EventSlotMachine slotEvent = new EventSlotMachine(minecraft_name, messageContents);

                    switch (TwipConnection.processType) {
                        case PROCESS_TYPE_QUEUE:
                            slotMachineQueue.add(slotEvent);
                            break;

                        case PROCESS_TYPE_REALTIME:
                            slotMachineMap.put(donateId, slotEvent);

                        case PROCESS_TYPE_IMMEDIATE:
                            slotEvent.consume();
                            break;
                    }
                }

            } else if (message.contains("now") && TwipConnection.processType == EventProcessType.PROCESS_TYPE_REALTIME) {
                String donateNowId = messageContents.get("_id").getAsString();

                if (slotMachineMap.containsKey(donateNowId)) {
                    slotMachineMap.get(donateNowId);
                    slotMachineMap.remove(donateNowId);
                }

            }
        }
    };

    public EventSlotMachine getSlotMachineQueuePeek() {
        return this.slotMachineQueue.poll();
    }
    public Queue<EventSlotMachine> getSlotMachineQueue() {
        return this.slotMachineQueue;
    }
    public int slotMachineQueueSize() {
        return this.slotMachineQueue.size();
    }
    public String[] applyWorlds() {
        return this.worlds;
    }


    public boolean getConnect() {
        return this.connect;
    }

    public String getKey() {
        return this.key;
    }

    public String getToken() {
        return this.token;
    }

    public String getPreset() {
        return this.preset;
    }

    public boolean isConnect() {
        return this.ws.isOpen();
    }

    public void disconnect() {
        this.ws.disconnect();
    }

    public void connect() {
        try {
            this.ws.connect();
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    private void logFile(String minecraft_name, String message) {
        try {
            FileWriter fw = new FileWriter(TwipR.plugin.getDataFolder().getPath() + "/log_" + minecraft_name + ".json", StandardCharsets.UTF_8, true);
            fw.append("["+new Date() + "]: " + message + System.lineSeparator());
            fw.flush();
            fw.close();
        } catch (Exception exception) {
            exception.printStackTrace();}
    }

}
