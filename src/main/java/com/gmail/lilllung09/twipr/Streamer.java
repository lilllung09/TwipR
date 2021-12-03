package com.gmail.lilllung09.twipr;

import com.gmail.lilllung09.twipr.twipevent.EventSlotMachine;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;

import java.net.URI;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;

import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.bukkit.Bukkit;

public class Streamer {

    private boolean connect = false;
    private String key = null;
    private String token = null;
    private String minecraft_name = null;
    private String preset = null;

    private WebSocket ws;

    JsonParser parser = new JsonParser();

    public Streamer(boolean connect, String alertbox_key, String alertbox_token, String minecraft_name, String preset) {
        this.connect = connect;
        this.key = alertbox_key;
        this.token = alertbox_token;
        this.minecraft_name = minecraft_name;
        this.preset = preset;



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

            if (this.key.replace(" ", "").length() == 0) {
                return;
            }
            if (this.token.replace(" ", "").length() == 0) {
                return;
            }

            if (this.connect)
                this.ws.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WebSocketListener webSocketListener = new WebSocketAdapter() {
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            TwipRMessage.sendMsgConsol(minecraft_name + " <- connected...");
            Bukkit.getScheduler().runTaskTimer(TwipR.plugin, () -> websocket.sendText("2"), 100L, 100L);
        }

        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
            TwipRMessage.sendMsgConsol(minecraft_name + " <- disconnected...");
        }

        public void onTextMessage(WebSocket websocket, String message) {
            if (!message.startsWith("42"))
                return;
            if (message.contains("TOKEN_NOT_FOUND")) {
                TwipRMessage.sendWanConsol(minecraft_name + " -> TOKEN_NOT_FOUND");
                return;
            }
            if (message.contains("TOKEN_EXPIRED")) {
                TwipRMessage.sendWanConsol(minecraft_name + " -> TOKEN_EXPRIED");
                return;
            }
            if (message.contains("new donate")) {
                JsonObject jsonObject = parser.parse(message.substring(2)).getAsJsonArray().get(1).getAsJsonObject();
                if (jsonObject.getAsJsonObject("slotmachine_data") != null)
                    new EventSlotMachine(minecraft_name, jsonObject);
            }
        }
    };

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

}
