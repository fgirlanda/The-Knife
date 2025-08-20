/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

/**
 * Gestisce le operazioni di lettura e scrittura per il file CSV che contiene i preferiti degli utenti.
 * Estende la classe {@link GestoreCSV} per fornire un'implementazione specifica
 * per gli oggetti di tipo {@link Preferito}.
 */
public class PreferitiCSV extends GestoreCSV<Preferito> {

    static String f = "preferiti.csv";

    /**
     * Costruttore della classe. Inizializza il nome del file CSV da gestire.
     */
    public PreferitiCSV() {
        super(f);
    }

    /**
     * Restituisce l'array di stringhe che rappresenta l'header del file CSV.
     * L'header definisce i nomi delle colonne per i dati dei preferiti.
     *
     * @return un array di stringhe contenente i nomi delle colonne ("ID Utente", "ID Ristorante").
     */
    @Override
    public String[] getHeader() {
        String[] header = { "ID Utente", "ID Ristorante" };
        return header;
    }

    /**
     * Estrae i dati da un oggetto {@link Preferito} per scriverli su una riga del file CSV.
     * Converte l'ID utente e l'ID ristorante in stringhe.
     *
     * @param preferito l'oggetto {@link Preferito} da cui estrarre i dati.
     * @return un array di stringhe contenente i dati dell'oggetto.
     */
    @Override
    public String[] estraiDati(Preferito preferito) {
        String[] dati = new String[2];

        dati[0] = Integer.toString(preferito.getIdUtente());
        dati[1] = Integer.toString(preferito.getIdRistorante());

        return dati;
    }

    /**
     * Parsifica una riga di dati dal file CSV e crea un nuovo oggetto {@link Preferito}.
     *
     * @param dati l'array di stringhe che rappresenta una riga del file CSV.
     * @return un nuovo oggetto {@link Preferito} con i dati parsificati.
     */
    @Override
    public Preferito parseRiga(String[] dati) {
        int idUtente = Integer.parseInt(dati[0]);
        int idRistorante = Integer.parseInt(dati[1]);

        return new Preferito(idUtente, idRistorante);
    }

    /**
     * Controlla se un determinato preferito (combinazione di ID utente e ID ristorante)
     * esiste già nel file CSV.
     *
     * @param idUtente l'ID dell'utente da cercare.
     * @param idRistorante l'ID del ristorante da cercare.
     * @return {@code true} se il preferito esiste, {@code false} altrimenti.
     * Gestisce le eccezioni {@link CsvValidationException} e {@link IOException}
     * durante la lettura del file.
     */
    public boolean controlloPreferito(int idUtente, int idRistorante) {
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] dati;
            reader.readNext(); // Salta header
            while ((dati = reader.readNext()) != null) {
                int idUt = Integer.parseInt(dati[0]);
                int idRis = Integer.parseInt(dati[1]);

                if (idUt == idUtente && idRis == idRistorante) {
                    return true;
                }
            }
        } catch (CsvValidationException e) {
            GestioneEccezioni.errore("Errore format csv in: " + file, e, true, nuovoFile -> file = nuovoFile);
            return false;

        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                GestioneEccezioni.errore("Errore caricamento file: " + file, e, true,
                        nuovoFile -> file = nuovoFile);
                return false;
            }
        }

        return false;
    }
}