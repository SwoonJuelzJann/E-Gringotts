package com.example.egringotts;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class security {

    private static final String SALTCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int SALT_LENGTH = 2;

    public static String generateSalt() {
        StringBuilder saltBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < SALT_LENGTH; i++) {
            int index = random.nextInt(SALTCHARS.length());
            saltBuilder.append(SALTCHARS.charAt(index));
        }

        return saltBuilder.toString();
    }

    public static String hashPassword(String salt, String password) {
        String saltedPassword = salt + password;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(saltedPassword.getBytes());
            byte[] resultByteArray = messageDigest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte b : resultByteArray) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found");
        }
    }

    public static String getSaltedHash(String password) {
        String salt = generateSalt();
        String hash = hashPassword(salt, password);
        return salt + ":" + hash;
    }

    public static boolean verifyPassword(String password, String storedSaltedHash) {
        String[] parts = storedSaltedHash.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("The stored salted hash must have the format 'salt:hash'");
        }
        String salt = parts[0];
        String storedHash = parts[1];
        String hashOfInput = hashPassword(salt, password);
        return hashOfInput.equals(storedHash);
    }

}

