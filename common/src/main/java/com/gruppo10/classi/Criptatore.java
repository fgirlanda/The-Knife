/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * La classe {@code Criptatore} fornisce un metodo di utilità per l' hashing
 * (non decrittabile) di stringhe, utilizzando l'algoritmo
 * crittografico {@code SHA-256}.
 *
 * <p>
 * L'algoritmo SHA-256 genera un hash univoco a 256 bit (64 caratteri
 * esadecimali)
 * a partire da una stringa di input.
 *
 * <p>
 * Questa classe non è istanziabile, in quanto fornisce solo un metodo statico.
 * </p>
 *
 * @author Francesco Girlanda
 * @author Mattia Lambertoni
 * @author Gabriele Gallon
 * @version 1.0
 */
public class Criptatore {
    /**
     * Applica l'algoritmo di hashing {@code SHA-256} alla stringa di input.
     *
     * @param input la stringa da convertire in hash.
     * @return la rappresentazione esadecimale a 64 caratteri dell'hash calcolato,
     *         oppure {@code null} se l'algoritmo SHA-256 non è disponibile.
     *
     *
     * Nota: L'output non è reversibile. Una volta generato l'hash, non è
     *           possibile risalire alla stringa originale.
     *
     * @see java.security.MessageDigest
     */
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
            GestioneEccezioni.errore("Algoritmo SHA-256 non disponibile", e, false, null);
            return null;
        }
    }
}
