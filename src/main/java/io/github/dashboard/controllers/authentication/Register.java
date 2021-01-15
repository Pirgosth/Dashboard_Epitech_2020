package io.github.dashboard.controllers.authentication;


import com.google.gson.Gson;
import io.github.dashboard.models.InputField;
import io.github.dashboard.models.FormInputResult;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

@WebServlet(urlPatterns = {"/register"})
public class Register extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession currentSession = req.getSession();
        if (currentSession.getAttribute("isConnected") != null && (boolean) currentSession.getAttribute("isConnected")) {
            resp.sendRedirect("/index");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    protected FormInputResult checkRegisterInput(String username, String mail, String password, String confirmPassword) {
        FormInputResult result = new FormInputResult();
        result.success = true;
        InputField usernameField = new InputField("username", true, null);
        InputField mailField = new InputField("email", true, null);
        InputField passwordField = new InputField("password", true, null);
        InputField confirmPasswordField = new InputField("confirm_password", true, null);

        if (!InputUtils.isUsernameValid(username)) {
            result.success = false;
            usernameField.validity = false;
            usernameField.cause = "Username is invalid";
        }

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

        if (!confirmPassword.equals(password)) {
            result.success = false;
            confirmPasswordField.validity = false;
            confirmPasswordField.cause = "Passwords does not match";
        }

        try {
            ResultSet rs = SqlUtils.executeSelect("SELECT * FROM local_users WHERE email = ?", Collections.singletonList(mail));
            if (rs.first()) {
                result.success = false;
                mailField.validity = false;
                mailField.cause = "Email already exists";
            }

        } catch (SQLException e) {
            result.success = false;
            e.printStackTrace();
        }

        result.fields.add(usernameField);
        result.fields.add(mailField);
        result.fields.add(passwordField);
        result.fields.add(confirmPasswordField);

        return result;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession currentSession = req.getSession();

        String username = req.getParameter("username");
        String mail = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirm_password");

        FormInputResult result = this.checkRegisterInput(username, mail, password, confirmPassword);

        if (result.success) {
            String encryptedPassword = HashUtils.hashString(password);
            try {
                ResultSet insertResult = SqlUtils.executeInsert("INSERT INTO users(usertype_id) VALUES(?);", Collections.singletonList(1));
                if(!insertResult.first()){
                    throw new IllegalStateException("User creation failed!");
                }
                currentSession.setAttribute("userId", insertResult.getInt("id"));
                SqlUtils.executeInsert("INSERT INTO local_users(username, email, password, user_id) VALUES(?, ?, ?, ?);",
                        Arrays.asList(username, mail, encryptedPassword, insertResult.getInt("id")));

            } catch (SQLException | IllegalStateException e) {
                e.printStackTrace();
                return;
            }
            currentSession.setAttribute("isConnected", true);
            currentSession.setAttribute("username", username);
        }
        resp.setContentType("application/json");
        resp.getWriter().print(new Gson().toJson(result, FormInputResult.class));
    }
}

