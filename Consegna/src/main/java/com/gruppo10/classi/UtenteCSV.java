/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

/**
 * Gestisce le operazioni di lettura e scrittura per il file CSV
 * che contiene i dati degli utenti.
 * Estende la classe {@link GestoreCSV} per fornire un'implementazione specifica
 * per gli oggetti di tipo {@link Utente}.
 * Mantiene anche una cache interna degli utenti in una mappa per un accesso più rapido.
 */
public class UtenteCSV extends GestoreCSV<Utente> {
    static String f = "utenti.csv";
    private static HashMap<String, Utente> utentiMap = new HashMap<>();

    /**
     * Costruttore della classe. Inizializza il nome del file CSV da gestire.
     */
    public UtenteCSV() {
        super(f);
    }

    /**
     * Restituisce l'array di stringhe che rappresenta l'header del file CSV.
     * L'header definisce i nomi delle colonne per i dati degli utenti.
     *
     * @return un array di stringhe contenente i nomi delle colonne.
     */
    @Override
    protected String[] getHeader() {
        String[] header = { "ID", "Nome", "Cognome", "Username", "Password", "Data di nascita",
                "Indirizzo", "Ruolo", "Latitudine", "Longitudine" };

        return header;
    }

    /**
     * Estrae i dati da un oggetto {@link Utente} per scriverli su una riga del file CSV.
     *
     * @param u l'oggetto {@link Utente} da cui estrarre i dati.
     * @return un array di stringhe contenente i dati dell'oggetto.
     */
    @Override
    protected String[] estraiDati(Utente u) {
        String[] dati = new String[10];
        dati[0] = String.valueOf(u.getId());
        dati[1] = u.getNome();
        dati[2] = u.getCognome();
        dati[3] = u.getUsername();
        dati[4] = u.getPassword();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        dati[5] = u.getDataDiNascita().format(formatter).toString();
        dati[6] = u.getIndirizzo();
        dati[7] = u.getRuolo().toString();
        Double lat = u.getCords().getLat();
        Double lon = u.getCords().getLon();
        dati[8] = lat.toString();
        dati[9] = lon.toString();
        return dati;
    }

    /**
     * Parsifica una riga di dati dal file CSV e crea un nuovo oggetto {@link Utente}.
     *
     * @param dati l'array di stringhe che rappresenta una riga del file CSV.
     * @return un nuovo oggetto {@link Utente} con i dati parsificati.
     */
    @Override
    protected Utente parseRiga(String[] dati) {
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

        return utente;
    }

    /**
     * Aggiunge un utente alla mappa interna.
     *
     * @param utente l'utente da aggiungere.
     */
    public void aggiungiUtente(Utente utente) {
        utentiMap.put(utente.getUsername(), utente);
    }

    /**
     * Cerca un utente nella mappa interna in base al suo username.
     *
     * @param username l'username dell'utente da cercare.
     * @return l'oggetto {@link Utente} corrispondente all'username, o {@code null} se non trovato.
     */
    public Utente cercaUtente(String username) {
        return utentiMap.get(username);
    }

    /**
     * Popola la mappa interna degli utenti a partire da una lista esistente.
     *
     * @param lista la lista di oggetti {@link Utente} da inserire nella mappa.
     */
    public void creaMappa(List<Utente> lista) {
        for (Utente u : lista) {
            aggiungiUtente(u);
        }
    }
}