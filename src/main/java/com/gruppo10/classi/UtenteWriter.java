package com.gruppo10.classi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.CSVWriter;

public class UtenteWriter {


    public void scriviUtente(Utente utente) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, NoSuchAlgorithmException {
        File dir = new File("fileCSV");
        if (!dir.exists()) dir.mkdirs();

        File fileUtente = new File(dir, "utenti.csv");
        
        boolean fileEsiste = fileUtente.exists();

        try (Writer writer = new FileWriter(fileUtente, true)) {
            
            // Se il file non esiste, scrivi l'header
            CSVWriter csvWriter = new CSVWriter(writer);
            if (!fileEsiste) {
                String[] header = { "ID","Nome", "Cognome", "Username", "Password", "Data di nascita", "Indirizzo", "Ruolo", "Latitudine", "Longitudine" }; // Sostituisci con i nomi dei campi della classe Utente
                csvWriter.writeNext(header);
                csvWriter.flush();
            }
            
            // Crea lista dati
            String[] dati = estraiDati(utente, fileUtente);

            // Scrivi i dati dell'utente
            csvWriter.writeNext(dati);
            csvWriter.close();
            writer.close();
        }
    }

    private String[] estraiDati(Utente utente, File file) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        String[] dati = new String[10];
        dati[0] = String.valueOf(RistoranteWriter.ultimoID(file));
        dati[1] = utente.getNome();
        dati[2] = utente.getCognome();
        dati[3] = utente.getUsername();        
        dati[4] = utente.getPassword();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        dati[5] = utente.getDataDiNascita().format(formatter).toString();
        dati[6] = utente.getIndirizzo();
        dati[7] = utente.getRuolo().toString();
        Double lat = utente.getCords().getLat();
        Double lon = utente.getCords().getLon();
        dati[8] = lat.toString();
        dati[9] = lon.toString();
        return dati;
    }
}


