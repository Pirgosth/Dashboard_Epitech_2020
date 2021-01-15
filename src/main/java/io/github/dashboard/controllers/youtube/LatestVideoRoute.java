package io.github.dashboard.controllers.youtube;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.dashboard.models.services.youtube.ChannelInfos;
import io.github.dashboard.models.services.youtube.latestVideo.VideoPlayer;
import io.github.dashboard.models.widgets.WidgetCacheResult;
import io.github.dashboard.utils.WidgetUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import static io.github.dashboard.utils.HttpRequestUtils.makeRequest;
import static io.github.dashboard.utils.YoutubeUtils.getChannelInfos;
import static io.github.dashboard.utils.YoutubeUtils.getLatestVideoId;

@WebServlet(urlPatterns = {"/youtube/latest"})
public class LatestVideoRoute extends HttpServlet {
    //Set your Youtube API key here
    private final static String API_KEY = "";

    protected VideoPlayer getVideoPlayer(String body) {
        JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
        Gson gson = new GsonBuilder().create();
        VideoPlayer player;
        player = gson.fromJson(obj.get("items").getAsJsonArray().get(0).getAsJsonObject().get("player").getAsJsonObject(), VideoPlayer.class);
        return player;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        String channelParameter = req.getParameter("channelParameter");

        JsonObject cacheParameters = new JsonObject();
        cacheParameters.addProperty("channelParameters", channelParameter);

        Gson gson = new Gson();
        String jsonCacheParameters = gson.toJson(cacheParameters);

        WidgetCacheResult cacheResult = WidgetUtils.getWidgetCachedData("YOUTUBE_LATEST_VIDEO", jsonCacheParameters);

        if(cacheResult != null && cacheResult.foundValid){
            resp.getWriter().print(cacheResult.response);
            return;
        }

        if (channelParameter == null){
            resp.sendError(400);
            return;
        }
        try {
            ChannelInfos channelInfos = getChannelInfos(channelParameter);
            String lastVideoId = getLatestVideoId(channelInfos.id);
            Map<String, String> parameters = new HashMap<>();
            parameters.put("part", "player");
            parameters.put("key",  API_KEY);
            parameters.put("id",  lastVideoId);
            String body = makeRequest("www.googleapis.com", "youtube/v3/videos", parameters);
            VideoPlayer player = getVideoPlayer(body);
            JsonObject response = new JsonObject();
            response.addProperty("channelName", channelInfos.snippet.title);
            response.addProperty("videoPlayer", player.embedHtml);

            String jsonResponse = gson.toJson(response);
            WidgetUtils.updateWidgetCache("YOUTUBE_LATEST_VIDEO", jsonCacheParameters, jsonResponse, 12*60*60*1000);
            resp.getWriter().print(jsonResponse);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
