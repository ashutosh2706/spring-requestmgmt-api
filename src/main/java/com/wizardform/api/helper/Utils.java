package com.wizardform.api.helper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    public static String generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
            byte[] hashBytes = digest.digest(inputBytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }
}
