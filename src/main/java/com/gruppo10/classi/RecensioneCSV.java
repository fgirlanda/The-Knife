package com.gruppo10.classi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class RecensioneCSV extends GestoreCSV<Recensione> {

    static String f = "recensioni.csv";

    public RecensioneCSV() {
        super(f);
    }

    @Override
    protected Recensione parseRiga(String[] dati) {
        String nomeUtente = dati[1];
        int idUtente = Integer.parseInt(dati[2]);
        int idRis = Integer.parseInt(dati[3]);
        int stelle = Integer.parseInt(dati[4]);
        String testo = dati[5];
        String risposta = dati[6];
        Recensione r = new Recensione();
        r.setUsername(nomeUtente);
        r.setIdUtente(idUtente);
        r.setIdRis(idRis);
        r.setStelle(stelle);
        r.setTesto(testo);
        r.setRisposta(risposta);

        return r;
    }

    @Override
    protected String[] estraiDati(Recensione r) {
        String[] dati = new String[7];

        dati[0] = Integer.toString(ultimoID());
        dati[1] = r.getUsername();
        dati[2] = Integer.toString(r.getIdUtente());
        dati[3] = Integer.toString(r.getIdRis());
        dati[4] = Integer.toString(r.getStelle());
        dati[5] = r.getTesto();
        dati[6] = r.getRisposta();

        return dati;
    }

    @Override
    protected String[] getHeader() {
        String[] header = { "ID Recensione", "Username", "ID Cliente", "ID Ristorante", "Voto", "Testo",
                "Risposta" };

        return header;
    }

    public void aggiungiRisposta(Recensione recensione, String risposta) {
        List<String[]> listaTemp = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] dati;
            try {
                reader.readNext(); // Salta header

                while ((dati = reader.readNext()) != null) {
                    try {
                        int idUt = Integer.parseInt(dati[2]);
                        int idRis = Integer.parseInt(dati[3]);

                        if (idUt == recensione.getIdUtente() && idRis == recensione.getIdRis()) {
                            dati[6] = risposta;
                        }

                        listaTemp.add(dati);

                    } catch (NumberFormatException e) {
                        GestioneEccezioni.errore("Errore parsing ID", "Riga: " + Arrays.toString(dati), false, null);
                    }
                }

            } catch (CsvValidationException e) {
                GestioneEccezioni.errore("Errore formato csv", e.getMessage(), false, null);
            }

        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                GestioneEccezioni.errore("Errore caricamento file", e.getMessage(), true,
                        nuovoFile -> file = nuovoFile);
                return;
            }
        }
        sovrascrivi(listaTemp);
    }

    public void modificaRecensione(Recensione recensione, String testoModificato, int nuovoVoto) {
        List<String[]> listaTemp = new ArrayList<>();

        // Lettura del CSV
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] dati;
            reader.readNext(); // Salta l'header

            while ((dati = reader.readNext()) != null) {
                try {
                    int idUt = Integer.parseInt(dati[2]);
                    int idRis = Integer.parseInt(dati[3]);

                    if (idUt == recensione.getIdUtente() && idRis == recensione.getIdRis()) {
                        dati[4] = Integer.toString(nuovoVoto);
                        dati[5] = testoModificato;
                    }

                    listaTemp.add(dati);

                } catch (NumberFormatException e) {
                    GestioneEccezioni.errore("Errore parsing ID", "Riga: " + Arrays.toString(dati), false, null);
                    return;
                }

            }
        } catch (IOException e) {
            GestioneEccezioni.errore("Errore caricamento file", e.getMessage(), true, nuovoFile -> file = nuovoFile);
            return;

        } catch (CsvValidationException e) {
            GestioneEccezioni.errore("Errore di validazione CSV", e.getMessage(), true, nuovoFile -> file = nuovoFile);
            return;
        }
        sovrascrivi(listaTemp);
    }

    public void rimuoviRecensione(Recensione recensione) {
        List<String[]> listaTemp = new ArrayList<>();

        // Lettura del CSV
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] dati;
            reader.readNext(); // Salta l'header

            while ((dati = reader.readNext()) != null) {
                try {
                    int idUt = Integer.parseInt(dati[2]);
                    int idRis = Integer.parseInt(dati[3]);

                    // Aggiungi tutte le righe tranne quella da rimuovere
                    if (!(idUt == recensione.getIdUtente() && idRis == recensione.getIdRis())) {
                        listaTemp.add(dati);
                    }
                } catch (NumberFormatException e) {
                    GestioneEccezioni.errore("Errore parsing ID", "Riga: " + Arrays.toString(dati), false, null);
                }
            }

        } catch (IOException e) {
            GestioneEccezioni.errore("Errore caricamento file", e.getMessage(), true, nuovoFile -> file = nuovoFile);
            return;

        } catch (CsvValidationException e) {
            GestioneEccezioni.errore("Errore di validazione CSV", e.getMessage(), true, nuovoFile -> file = nuovoFile);
            return;
        }

        sovrascrivi(listaTemp);
    }
}
