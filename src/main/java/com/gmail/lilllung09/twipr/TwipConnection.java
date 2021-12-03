package com.gmail.lilllung09.twipr;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.MalformedJsonException;

import java.io.File;
import java.io.FileReader;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class TwipConnection {

	public static JsonObject STREAMERSJSON = null;
	public static Map<String, Streamer> TwipStreamers = new HashMap<>();
	public static Map<String, JsonObject> SlotMachinePreset = new HashMap<>();
	
	private Plugin plugin = TwipR.plugin;

	public TwipConnection() {
		
		if (!(new File(this.plugin.getDataFolder().getPath() + "/streamer.json")).exists()) {
			TwipRMessage.sendWanConsol("stremaers.json 파일이 존재하지 않습니다!");
			return;
		}

		try (FileReader fr = new FileReader(this.plugin.getDataFolder().getPath() + "/streamer.json", StandardCharsets.UTF_8)) {
			Gson gson = new Gson();
			STREAMERSJSON = gson.fromJson(fr, JsonObject.class);

			List<String> presetList = new ArrayList<>();
			JsonObject jsonPreset = STREAMERSJSON.getAsJsonObject("preset");
			Set<Map.Entry<String, JsonElement>> presets = jsonPreset.entrySet();
			presets.forEach(entry -> {
				presetList.add(entry.getKey());
				SlotMachinePreset.put(entry.getKey(), jsonPreset.getAsJsonObject(entry.getKey()));
			});
			TwipRMessage.sendMsgConsol("등록된 프리셋 (" + presets.size() + "): " + presetList);


			List<String> stremaerList = new ArrayList<>();
			JsonObject jsonStreamer = STREAMERSJSON.getAsJsonObject("streamers");
			Set<Map.Entry<String, JsonElement>> streamers = jsonStreamer.entrySet();
			streamers.forEach(entry -> {
				stremaerList.add(entry.getKey());
				Bukkit.getScheduler().runTaskLater(TwipR.plugin, () -> {
					Streamer s = initStreamer(entry.getKey(), jsonStreamer.getAsJsonObject(entry.getKey()));
					TwipStreamers.put(entry.getKey(), s);
				}, 60L);
			});
			TwipRMessage.sendMsgConsol("등록된 스트리머 (" + streamers.size() + "): " + stremaerList);
			
		} catch (MalformedJsonException e) {
			TwipRMessage.sendWanConsol("stremaers.json 파일이 json 형식이 아닙니다!");
			e.printStackTrace();

		}  catch (Exception e) {
			TwipRMessage.sendWanConsol("stremaers.json 파일 관련 알 수 없는 오류 발생");
			e.printStackTrace();
			
		}

	}

	private Streamer initStreamer(String minecraft_name, JsonObject o) {
		boolean connect = o.get("connect").getAsBoolean();
		String key = o.get("alertbox_key").getAsString();
		String token = o.get("alertbox_token").getAsString();
		String presset = o.get("slotmachine").getAsString();
		return new Streamer(connect, key, token, minecraft_name, presset);
	}

	public void disconnectAllStremaer() {
		TwipStreamers.forEach((k,v) -> v.disconnect());
		TwipStreamers.clear();

	}

}
