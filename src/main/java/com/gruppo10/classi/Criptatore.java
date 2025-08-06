package com.gruppo10.classi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Criptatore {

    public static String cripta(String input) {     
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            GestioneEccezioni.errore("Algoritmo SHA-256 non disponibile: " + e);
            return null;
        }
    }
}
