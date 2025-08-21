/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

/**
 * Gestisce le operazioni di lettura, scrittura e modifica per il file CSV
 * che contiene le recensioni dei ristoranti.
 * Estende la classe {@link GestoreCSV} per fornire un'implementazione specifica
 * per gli oggetti di tipo {@link Recensione}.
 */
public class RecensioneCSV extends GestoreCSV<Recensione> {

    static String f = "recensioni.csv";

    /**
     * Costruttore della classe. Inizializza il nome del file CSV da gestire.
     */
    public RecensioneCSV() {
        super(f);
    }

    /**
     * Restituisce l'array di stringhe che rappresenta l'header del file CSV.
     * L'header definisce i nomi delle colonne per i dati delle recensioni.
     *
     * @return un array di stringhe contenente i nomi delle colonne.
     */
    @Override
    protected String[] getHeader() {
        String[] header = { "ID Cliente", "ID Ristorante", "ID Recensione", "Username", "Voto", "Testo",
                "Risposta" };

        return header;
    }

    /**
     * Estrae i dati da un oggetto {@link Recensione} per scriverli su una riga del file CSV.
     *
     * @param r l'oggetto {@link Recensione} da cui estrarre i dati.
     * @return un array di stringhe contenente i dati dell'oggetto.
     */
    @Override
    protected String[] estraiDati(Recensione r) {
        String[] dati = new String[7];

        dati[0] = Integer.toString(r.getIdUtente());
        dati[1] = Integer.toString(r.getIdRistorante());
        dati[2] = Integer.toString(ultimoID());
        dati[3] = r.getUsername();
        dati[4] = Integer.toString(r.getStelle());
        dati[5] = r.getTesto();
        dati[6] = r.getRisposta();

        return dati;
    }

    /**
     * Parsifica una riga di dati dal file CSV e crea un nuovo oggetto {@link Recensione}.
     *
     * @param dati l'array di stringhe che rappresenta una riga del file CSV.
     * @return un nuovo oggetto {@link Recensione} con i dati parsificati.
     */
    @Override
    protected Recensione parseRiga(String[] dati) {
        int idUtente = Integer.parseInt(dati[0]);
        int idRis = Integer.parseInt(dati[1]);
        int idRec = Integer.parseInt(dati[2]);
        String nomeUtente = dati[3];
        int stelle = Integer.parseInt(dati[4]);
        String testo = dati[5];
        String risposta = dati[6];
        Recensione r = new Recensione();
        r.setIdRec(idRec);
        r.setUsername(nomeUtente);
        r.setIdUtente(idUtente);
        r.setIdRistorante(idRis);
        r.setStelle(stelle);
        r.setTesto(testo);
        r.setRisposta(risposta);

        return r;
    }

    /**
     * Aggiunge una risposta a una recensione esistente nel file CSV.
     * Legge il file, trova la recensione corrispondente a {@code recensione},
     * aggiorna il campo della risposta e riscrive il file.
     *
     * @param recensione l'oggetto {@link Recensione} da aggiornare.
     * @param risposta la stringa di risposta da aggiungere.
     */
    public void aggiungiRisposta(Recensione recensione, String risposta) {
        List<String[]> listaTemp = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] dati;
            try {
                reader.readNext();

                while ((dati = reader.readNext()) != null) {
                    try {
                        int idUt = Integer.parseInt(dati[0]);
                        int idRis = Integer.parseInt(dati[1]);

                        if (idUt == recensione.getIdUtente() && idRis == recensione.getIdRistorante()) {
                            dati[6] = risposta;
                        }

                        listaTemp.add(dati);

                    } catch (NumberFormatException e) {
                        GestioneEccezioni.errore("Errore parsing ID\nRiga: " + Arrays.toString(dati), e, false, null);
                    }
                }

            } catch (CsvValidationException e) {
                GestioneEccezioni.errore("Errore formato csv: " + file, e, false, null);
            }

        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                GestioneEccezioni.errore("Errore caricamento file: " + file, e, true,
                        nuovoFile -> file = nuovoFile);
                return;
            }
        }
        sovrascrivi(listaTemp);
    }

    /**
     * Modifica il testo e il voto di una recensione esistente nel file CSV.
     * Legge il file, trova la recensione corrispondente a {@code recensione},
     * aggiorna il testo e le stelle e riscrive il file.
     *
     * @param recensione l'oggetto {@link Recensione} da modificare.
     * @param testoModificato il nuovo testo della recensione.
     * @param nuovoVoto il nuovo voto (numero di stelle) della recensione.
     */
    public void modificaRecensione(Recensione recensione, String testoModificato, int nuovoVoto) {
        List<String[]> listaTemp = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] dati;
            reader.readNext();

            while ((dati = reader.readNext()) != null) {
                try {
                    int idUt = Integer.parseInt(dati[0]);
                    int idRis = Integer.parseInt(dati[1]);

                    if (idUt == recensione.getIdUtente() && idRis == recensione.getIdRistorante()) {
                        dati[4] = Integer.toString(nuovoVoto);
                        dati[5] = testoModificato;
                    }

                    listaTemp.add(dati);

                } catch (NumberFormatException e) {
                    GestioneEccezioni.errore("Errore parsing ID\nRiga: " + Arrays.toString(dati), e, false, null);
                    return;
                }

            }
        } catch (IOException e) {
            GestioneEccezioni.errore("Errore caricamento file: " + file, e, true, nuovoFile -> file = nuovoFile);
            return;

        } catch (CsvValidationException e) {
            GestioneEccezioni.errore("Errore di validazione CSV: " + file, e, true, nuovoFile -> file = nuovoFile);
            return;
        }
        sovrascrivi(listaTemp);
    }
}