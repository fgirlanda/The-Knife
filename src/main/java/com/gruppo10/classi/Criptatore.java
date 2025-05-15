package com.gruppo10.classi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Criptatore {

    public static String cripta(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
    
}
