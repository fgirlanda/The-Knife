package com.gruppo10.classi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class UtenteReader {

    private static HashMap<String, Utente> utentiMap = new HashMap<>();

    public static boolean caricaCSV() {
        File dir = new File("fileCSV");
        File fileUtente = new File(dir, "utenti.csv");
        try (CSVReader cr = new CSVReader(new FileReader(fileUtente))) {
            String[] dati;
            cr.readNext(); // Salta l'header
            while ((dati = cr.readNext()) != null) {

                // Crea un nuovo oggetto Utente e popola i campi
                Utente utente = new Utente();
                utente.setId(Integer.parseInt(dati[0]));
                utente.setNome(dati[1]);
                utente.setCognome(dati[2]);
                utente.setUsername(dati[3]);
                utente.setPassword(dati[4]);
                utente.setDataDiNascita(dati[5]);
                utente.setIndirizzo(dati[6]);
                utente.setRuolo(dati[7]);
                utente.setCords(Double.parseDouble(dati[8]), Double.parseDouble(dati[9]));
                aggiungiUtente(utente);
            }

        } catch (CsvValidationException e) {
            System.err.println("Errore format csv utenti: " + e.getMessage());
            return false;

        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                System.err.println("Errore caricamento file: " + fileUtente);
                return false;
            }
        }
        return true;
    }

    public static void aggiungiUtente(Utente utente) {
        utentiMap.put(utente.getUsername(), utente);
    }

    public static Utente cercaUtente(String username) {
        return utentiMap.get(username);
    }
}
