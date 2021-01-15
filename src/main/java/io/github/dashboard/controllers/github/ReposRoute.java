package io.github.dashboard.controllers.github;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.dashboard.models.services.github.repos.Repository;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static io.github.dashboard.utils.HttpRequestUtils.makeRequest;

@WebServlet(urlPatterns = {"/github/repos"})
public class ReposRoute extends HttpServlet {

    protected Repository[] getRepositories(String body) {
        Gson gson = new GsonBuilder().create();
        Repository[] repositories;
        repositories = gson.fromJson(body, Repository[].class);
        return repositories;
    }

    protected String privateRepos(String token, String caller, String lookedFor) throws IOException {
        String uri;
        if (lookedFor != null && !caller.equals(lookedFor))
            uri = "https://api.github.com/users/" + lookedFor + "/repos";
        else
            uri = "https://api.github.com/user/repos";
        HttpGet request = new HttpGet(uri);
        String auth = "Bearer " + token;
        request.setHeader(HttpHeaders.AUTHORIZATION, auth);
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(request);
        return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String username = req.getParameter("username");

        try {
            String body;
            if (session.getAttribute("accessToken") == null && username != null)
                body = makeRequest("api.github.com", "/users/" + username + "/repos", Collections.emptyMap());
            else if(session.getAttribute("accessToken") != null)
                body = privateRepos((String)session.getAttribute("accessToken"), (String)session.getAttribute("username"), username);
            else{
                resp.sendError(204);
                return;
            }
            Repository[] repositories = getRepositories(body);
            Gson gson = new GsonBuilder().create();
            resp.setContentType("application/json");
            JsonObject res = new JsonObject();
            res.addProperty("username", username);
            res.add("repositories", gson.toJsonTree(repositories));
            resp.getWriter().print(gson.toJson(res));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
