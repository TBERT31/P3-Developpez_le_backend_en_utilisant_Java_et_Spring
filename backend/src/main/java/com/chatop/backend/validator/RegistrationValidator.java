package com.chatop.backend.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationValidator {

    public static boolean isValidEmail(String email) {
        // Expression régulière pour valider l'email
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        if (password.length() < 6 || password.length() > 64) {
            return false;
        }

        return true;
    }

    public static boolean isValidName(String name) {
        return !name.isEmpty();
    }
}