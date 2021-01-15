package io.github.dashboard.controllers.authentication.github;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.dashboard.models.github.GithubProfile;
import io.github.dashboard.models.github.GithubToken;
import io.github.dashboard.utils.SqlUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

@WebServlet(urlPatterns = {"/github/auth/callback"})
public class AuthCallback extends HttpServlet {
    //Set your own github developer id and secret here
    //See https://github.com/settings/tokens to create your api access token
    private final static String clientId = "";
    private final static String clientSecret = "";

    protected String getAccessToken(String code) throws IOException {
        String githubUrl = "https://github.com/login/oauth/access_token";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(githubUrl);

        StringBuilder builder = new StringBuilder().append("{")
                .append("\"client_id\":\"").append(clientId).append("\",")
                .append("\"client_secret\":\"").append(clientSecret).append("\",")
                .append("\"code\":\"").append(code).append("\"}");
        StringEntity entity = new StringEntity(builder.toString());
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = httpclient.execute(httpPost);
        String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
        Gson gson = new GsonBuilder().create();
        GithubToken token = gson.fromJson(obj, GithubToken.class);
        return token.access_token;
    }

    protected GithubProfile getGithubUser(String access_token) throws IOException {
        HttpGet request = new HttpGet("https://api.github.com/user");
        String auth = "Bearer " + access_token;
        request.setHeader(HttpHeaders.AUTHORIZATION, auth);
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(request);
        String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(obj, GithubProfile.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        if (session.getAttribute("isConnected") != null && (boolean) session.getAttribute("isConnected")) {
            resp.sendRedirect("/index");
            return;
        }

        String code = req.getParameter("code");
        String access_token = getAccessToken(code);
        GithubProfile profile = getGithubUser(access_token);

        try {
            ResultSet userExistsSet = SqlUtils.executeSelect("SELECT * FROM github_users WHERE username = ?;", Collections.singletonList(profile.login));
            if (!userExistsSet.first()) {
                ResultSet insertUserResult = SqlUtils.executeInsert("INSERT INTO users(usertype_id) VALUES(?);", Collections.singletonList(2));
                if (!insertUserResult.first()) {
                    throw new IllegalStateException("User creation failed!");
                }
                session.setAttribute("userId", insertUserResult.getInt("id"));
                SqlUtils.executeInsert("INSERT INTO github_users(username, user_id) VALUES(?, ?);", Arrays.asList(profile.login, insertUserResult.getInt("id")));
            }
            else{
                session.setAttribute("userId", userExistsSet.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        session.setAttribute("isConnected", true);
        session.setAttribute("username", profile.login);
        session.setAttribute("accessToken", access_token);
        resp.sendRedirect("/index");
    }
}
