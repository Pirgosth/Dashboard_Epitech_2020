package io.github.dashboard.controllers.authentication;


import com.google.gson.GsonBuilder;
import io.github.dashboard.models.FormInputResult;
import io.github.dashboard.models.InputField;
import io.github.dashboard.utils.HashUtils;
import io.github.dashboard.utils.InputUtils;
import io.github.dashboard.utils.SqlUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

@WebServlet(urlPatterns = {"/login"})
public class Login extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession currentSession = req.getSession();
        if (currentSession.getAttribute("isConnected") != null && (boolean) currentSession.getAttribute("isConnected")) {
            resp.sendRedirect("/index");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    protected FormInputResult checkLoginInput(String mail, String password) {
        FormInputResult result = new FormInputResult();
        result.success = true;
        InputField mailField = new InputField("email", true, null);
        InputField passwordField = new InputField("password", true, null);

        if (!InputUtils.isEmailValid(mail)) {
            result.success = false;
            mailField.validity = false;
            mailField.cause = "Email is invalid";
        }

        if (!InputUtils.isPasswordValid(password)) {
            result.success = false;
            passwordField.validity = false;
            passwordField.cause = "Password is not valid";
        }

        if (mailField.validity && passwordField.validity) {
            try {
                ResultSet rs = SqlUtils.executeSelect("SELECT * FROM local_users WHERE email = ?;", Collections.singletonList(mail));
                if (!rs.first()) {
                    result.success = false;
                    mailField.validity = false;
                    mailField.cause = "Email does not exist";
                } else {
                    String encryptedPassword = HashUtils.hashString(password);
                    if (!encryptedPassword.equals(rs.getString("password"))) {
                        result.success = false;
                        passwordField.validity = false;
                        passwordField.cause = "Password is incorrect";
                    }
                    if (result.success) {
                        result.setLocalAccountId(rs.getInt("id"));
                    }
                }

            } catch (SQLException e) {
                result.success = false;
                e.printStackTrace();
            }
        }


        result.fields.add(mailField);
        result.fields.add(passwordField);

        return result;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession currentSession = req.getSession();

        FormInputResult result = checkLoginInput(req.getParameter("email"), req.getParameter("password"));
        if (result.success) {
            int localUserId = result.getLocalAccountId();
            try {
                ResultSet rs = SqlUtils.executeSelect("SELECT user_id, username, email FROM local_users where id = ?", Collections.singletonList(localUserId));
                rs.first();
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                currentSession.setAttribute("isConnected", true);
                currentSession.setAttribute("userId", userId);
                currentSession.setAttribute("username", username);
                currentSession.setAttribute("email", email);
            } catch (SQLException e) {
                e.printStackTrace();
                resp.sendError(500, "Database read error");
                return;
            }
        }

        resp.setContentType("application/json");
        resp.getWriter().print(new GsonBuilder().excludeFieldsWithModifiers(Modifier.PRIVATE).create().toJson(result, FormInputResult.class));

    }
}
