package com.gruppo10.classi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cryptatore {

    public String crypta(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        byte[] hashBytes = digest.digest(input.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
    
}
