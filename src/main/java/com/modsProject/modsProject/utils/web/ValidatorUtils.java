package com.modsProject.modsProject.utils.web;

import java.util.regex.Pattern;

public class ValidatorUtils {
    public static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_-]{6,32}$");
    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^[A-Za-z0-9_\\-!@#$%^&*()+={}\\[\\]|\\\\:;\"'<>,.?/~`]{6,32}$");

    public static boolean usernameIsValid(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean passwordIsValid(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
