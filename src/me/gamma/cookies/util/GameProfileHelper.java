
package me.gamma.cookies.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;



public class GameProfileHelper {

	public static final String UUID_ADRESS = "https://api.mojang.com/users/profiles/minecraft/";
	public static final String GAME_PROFILE_ADRESS = "https://sessionserver.mojang.com/session/minecraft/profile/";

	private static final HashMap<String, UUID> UUID_CACHE = new HashMap<>();
	private static final HashMap<UUID, GameProfile> GAME_PROFILE_CACHE = new HashMap<>();

	public static UUID getUUID(String name) {
		return getUUID(name, false);
	}


	public static UUID getUUID(String name, boolean force) {
		if(force || !UUID_CACHE.containsKey(name)) {
			try {
				URL url = new URL(UUID_ADRESS + name);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				int code = connection.getResponseCode();
				if(code == HttpURLConnection.HTTP_OK) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					StringBuilder response = new StringBuilder();
					String line;
					while((line = reader.readLine()) != null)
						response.append(line);
					System.out.println(response.toString());
					JsonElement element = new JsonParser().parse(response.toString());
					String id = element.getAsJsonObject().get("id").getAsString();
					char[] chars = new char[36];
					int index = 0;
					for(int i = 0; i < 32; i++) {
						if(i == 8 || i == 12 || i == 16 || i == 20)
							chars[index++] = '-';
						chars[index++] = id.charAt(i);
					}
					UUID uuid = UUID.fromString(new String(chars));
					UUID_CACHE.put(name, uuid);
					return uuid;
				} else {
					return null;
				}
			} catch(IOException | IllegalStateException e) {
				return null;
			}
		} else {
			return UUID_CACHE.get(name);
		}
	}


	public static GameProfile getProfile(UUID uuid) {
		return getProfile(uuid, false);
	}


	public static GameProfile getProfile(UUID uuid, boolean force) {
		if(force || !GAME_PROFILE_CACHE.containsKey(uuid)) {
			try {
				URL url = new URL(GAME_PROFILE_ADRESS + uuid.toString().replace("-", ""));
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				int code = connection.getResponseCode();
				if(code == HttpURLConnection.HTTP_OK) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					StringBuilder response = new StringBuilder();
					String line;
					while((line = reader.readLine()) != null)
						response.append(line);
					System.out.println(response.toString());
					JsonObject element = new JsonParser().parse(response.toString()).getAsJsonObject();
					String name = element.get("name").getAsString();
					JsonArray properties = element.get("properties").getAsJsonArray();
					for(int i = 0; i < properties.size(); i++) {
						JsonObject property = properties.get(i).getAsJsonObject();
						if(property.get("name").getAsString().equals("textures")) {
							String texture = property.get("value").getAsString();
							GameProfile profile = new GameProfile(uuid, name);
							Property textures = new Property("textures", texture);
							profile.getProperties().put("textures", textures);
							return profile;
						}
					}
					return null;
				} else {
					return null;
				}
			} catch(IOException | IllegalStateException e) {
				return null;
			}
		} else {
			return GAME_PROFILE_CACHE.get(uuid);
		}
	}

}
