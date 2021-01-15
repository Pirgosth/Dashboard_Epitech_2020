package io.github.dashboard.controllers.authentication.github;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/github/auth"})
public class Auth extends HttpServlet {
    //Same as AuthCallbck.java, set your oauth client_id from github access token
    private final static String clientId = "";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession currentSession = req.getSession();
        if (currentSession.getAttribute("isConnected") != null && (boolean) currentSession.getAttribute("isConnected")) {
            resp.sendRedirect("/index");
            return;
        }
        StringBuilder githubUrl = new StringBuilder("https://github.com/login/oauth/authorize")
                .append("?client_id=").append(clientId)
                .append("&redirect_uri=").append("http://localhost:8080/github/auth/callback")
                .append("&scope=repo");
        resp.sendRedirect(githubUrl.toString());
    }
}
