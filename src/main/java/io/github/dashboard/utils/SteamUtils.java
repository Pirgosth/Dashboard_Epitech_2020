package io.github.dashboard.utils;

import com.google.gson.*;
import io.github.dashboard.models.services.steam.SteamInfo;
import io.github.dashboard.models.services.steam.profile.Players;
import io.github.dashboard.models.services.steam.profile.ProfileInfos;
import io.github.dashboard.models.widgets.WidgetCacheResult;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static io.github.dashboard.utils.HttpRequestUtils.makeRequest;

public class SteamUtils {

    //Set your Steam API_KEY here
    public static final String API_KEY = "";

    public static int getNbrFriends(String steamId) throws IOException, URISyntaxException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("key", API_KEY);
        parameters.put("steamid", steamId);
        String body = makeRequest("api.steampowered.com", "/ISteamUser/GetFriendList/v1/", parameters);
        JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
        if(obj.get("friendslist") == null){
            return -1;
        }
        JsonArray array = obj.get("friendslist").getAsJsonObject().get("friends").getAsJsonArray();
        return array.size();
    }

    public static int getPlayerLevel(String steamId) throws IOException, URISyntaxException {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("key", API_KEY);
        parameters.put("steamid", steamId);
        String body = makeRequest("api.steampowered.com", "/IPlayerService/GetSteamLevel/v1/", parameters);
        JsonObject obj = JsonParser.parseString(body).getAsJsonObject().get("response").getAsJsonObject();
        return obj.get("player_level") != null ? obj.get("player_level").getAsInt() : -1;
    }

    public static ProfileInfos getProfile(String steamId) throws IOException, URISyntaxException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("key", API_KEY);
        parameters.put("steamids", steamId);
        String body = makeRequest("api.steampowered.com", "/ISteamUser/GetPlayerSummaries/v2/", parameters);
        JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
        Gson gson = new GsonBuilder().create();
        Players players;
        players = gson.fromJson(obj.get("response"), Players.class);
        return players.players.get(0);
    }

    public static SteamInfo getGames(String steamId) throws IOException, URISyntaxException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("key", API_KEY);
        parameters.put("steamid", steamId);
        String body = makeRequest("api.steampowered.com", "/IPlayerService/GetOwnedGames/v1/", parameters);
        JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(obj.get("response"), SteamInfo.class);
    }

    public static String getSteamIdFromUserName(String username) {

        JsonObject cacheParameters = new JsonObject();
        cacheParameters.addProperty("username", username);

        Gson gson = new Gson();
        String jsonParameters = gson.toJson(cacheParameters);

        WidgetCacheResult cacheResult = WidgetUtils.getWidgetCachedData("STEAM_ID", jsonParameters);
        if(cacheResult != null && cacheResult.foundValid){
            JsonObject response = JsonParser.parseString(cacheResult.response).getAsJsonObject();
            return response.get("steamid").getAsString();
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("key", API_KEY);
        parameters.put("vanityurl", username);
        try {
            String body = makeRequest("api.steampowered.com", "/ISteamUser/ResolveVanityURL/v0001/", parameters);
            JsonObject response = JsonParser.parseString(body).getAsJsonObject().get("response").getAsJsonObject();
            if(response.get("success").getAsInt() != 1){
                return null;
            }

            String steamId = response.get("steamid").getAsString();

            JsonObject cacheResponse = new JsonObject();
            cacheResponse.addProperty("steamid", steamId);
            WidgetUtils.updateWidgetCache("STEAM_ID", jsonParameters, gson.toJson(cacheResponse), 60*60*1000);

            return steamId;

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String fromParameterToId(String username) {
        String steamId;
        if (username.startsWith("https://steamcommunity.com/profiles/"))
            steamId = username.substring(username.length() - 17 - ((username.endsWith("/")) ? 1 : 0), username.length() - ((username.endsWith("/")) ? 1 : 0));
        else if (username.startsWith("https://steamcommunity.com/id/")) {
            String raw_username = username.substring(30);
            steamId = SteamUtils.getSteamIdFromUserName(raw_username);
        }
        else
            steamId = SteamUtils.getSteamIdFromUserName(username);
        return steamId;
    }
}
