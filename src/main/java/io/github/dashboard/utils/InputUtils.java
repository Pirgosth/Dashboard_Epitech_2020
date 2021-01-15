package io.github.dashboard.utils;

import java.util.regex.Pattern;

public class InputUtils {
    private static final Pattern patternUsername = Pattern.compile("^[A-Za-z0-9]{4,20}$");
    private static final Pattern patternMail = Pattern.compile("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$");
    private static final Pattern patternPassword = Pattern.compile("^[a-zA-Z0-9-\\w~@#$%^&*+=`|{}:;!.?\"()\\[\\]-]{4,15}$");

    public static boolean isUsernameValid(String username) {
        return InputUtils.patternUsername.matcher(username).find();
    }

    public static boolean isEmailValid(String email) {
        return InputUtils.patternMail.matcher(email).find();
    }

    public static boolean isPasswordValid(String password) {
        return InputUtils.patternPassword.matcher(password).find();
    }

    public static boolean isUsernameFree(String username) {
        //Check in db
        return true;
    }

    public static boolean isEmailFree(String email) {
        //Check in db
        return true;
    }
}
