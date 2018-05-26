package com.gghouse.wardah.wardahba.util;

/**
 * Created by michaelhalim on 1/25/17.
 */

public abstract class WBAValidation {
    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public static boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public static boolean isPasswordMatch(String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            return true;
        } else {
            return false;
        }
    }
}
