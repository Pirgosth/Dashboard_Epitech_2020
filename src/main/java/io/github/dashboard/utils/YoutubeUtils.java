package io.github.dashboard.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.dashboard.models.services.youtube.ChannelInfos;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static io.github.dashboard.utils.HttpRequestUtils.makeRequest;

public class YoutubeUtils {

    private static final String API_KEY = "";

    public static String getLatestVideoId(String channelId) throws IOException, URISyntaxException {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("part", "snippet");
        parameters.put("key", API_KEY);
        parameters.put("channelId", channelId);
        parameters.put("order", "date");
        parameters.put("maxResults", "1");

        String body = makeRequest("www.googleapis.com", "/youtube/v3/search", parameters);
        JsonObject obj = JsonParser.parseString(body).getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsJsonObject();
        return obj.get("videoId").getAsString();
    }

    public static ChannelInfos getChannelInfos(String param) throws IOException, URISyntaxException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("part", "snippet");
        parameters.put("key", API_KEY);
        ChannelInfos infos;
        String body;
        JsonObject response;
        Gson gson = new GsonBuilder().create();

        if (param.startsWith("https://www.youtube.com/channel/")) {
            String id = param.substring(param.length() - 24 - ((param.endsWith("/")) ? 1 : 0), param.length() - ((param.endsWith("/")) ? 1 : 0));
            parameters.put("id", id);
        } else {
            String username = param.startsWith("https://www.youtube.com/user/") ? param.substring(29, param.length() - ((param.endsWith("/")) ? 1 : 0)) : param;
            parameters.put("forUsername", username);
        }
        body = makeRequest("www.googleapis.com", "/youtube/v3/channels", parameters);
        response = JsonParser.parseString(body).getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject();
        infos = gson.fromJson(response, ChannelInfos.class);
        return infos;
    }
}
