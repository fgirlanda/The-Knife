package com.gruppo10.classi;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class UtenteCSV extends GestoreCSV<Utente> {
    static String f = "utenti.csv";
    private static HashMap<String, Utente> utentiMap = new HashMap<>();

    public UtenteCSV() {
        super(f);
    }

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

    @Override
    protected String[] estraiDati(Utente u) {
        String[] dati = new String[10];
        dati[0] = String.valueOf(ultimoID());
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

    @Override
    protected String[] getHeader() {
        String[] header = { "ID Recensione", "Username", "ID Cliente", "ID Ristorante", "Voto", "Testo",
                "Risposta" };

        return header;
    }

    public void aggiungiUtente(Utente utente) {
        utentiMap.put(utente.getUsername(), utente);
    }

    public Utente cercaUtente(String username) {
        return utentiMap.get(username);
    }

    public void creaMappa(List<Utente> lista){
        for(Utente u: lista){
            aggiungiUtente(u);
        }
    }
}
