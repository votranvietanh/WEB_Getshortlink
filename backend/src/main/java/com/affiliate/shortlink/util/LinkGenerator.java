package com.affiliate.shortlink.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Utility class for generating short codes
 */
public class LinkGenerator {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int DEFAULT_LENGTH = 8;
    private static final Random RANDOM = new SecureRandom();

    /**
     * Generate a random short code
     */
    public static String generateShortCode() {
        return generateShortCode(DEFAULT_LENGTH);
    }

    /**
     * Generate a random short code with specified length
     */
    public static String generateShortCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    /**
     * Generate a short code from a number (base62 encoding)
     */
    public static String generateShortCodeFromId(long id) {
        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            sb.append(CHARACTERS.charAt((int) (id % CHARACTERS.length())));
            id /= CHARACTERS.length();
        }
        return sb.reverse().toString();
    }

    /**
     * Validate short code format
     */
    public static boolean isValidShortCode(String shortCode) {
        if (shortCode == null || shortCode.length() < 4 || shortCode.length() > 20) {
            return false;
        }
        for (char c : shortCode.toCharArray()) {
            if (CHARACTERS.indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }
}
