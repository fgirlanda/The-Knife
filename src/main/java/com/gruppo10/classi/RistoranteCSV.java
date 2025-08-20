/*
 * Francesco Girlanda 760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Gestisce le operazioni di lettura e scrittura per il file CSV
 * che contiene i dati dei ristoranti.
 * Estende la classe {@link GestoreCSV} per fornire un'implementazione specifica
 * per gli oggetti di tipo {@link Ristorante}.
 * Carica anche le recensioni associate a ciascun ristorante.
 */
public class RistoranteCSV extends GestoreCSV<Ristorante> {
    static String f = "ristoranti.csv";
    private HashMap<Integer, List<Recensione>> mappaRecensioni = new HashMap<>();
    private HashMap<Integer, Ristorante> mappaRistoranti = new HashMap<Integer, Ristorante>();

    /**
     * Costruttore della classe. Inizializza il nome del file CSV da gestire.
     */
    public RistoranteCSV() {
        super(f);
    }

    /**
     * Restituisce l'array di stringhe che rappresenta l'header del file CSV.
     * L'header definisce i nomi delle colonne per i dati dei ristoranti.
     *
     * @return un array di stringhe contenente i nomi delle colonne.
     */
    @Override
    protected String[] getHeader() {
        String[] header = { "Id", "Nome", "Indirizzo", "Delivery", "Prenotazione online", "Tipo Cucina",
                "Prezzo", "Descrizione", "Latitudine", "Longitudine", "Proprietario" };

        return header;
    }

    /**
     * Estrae i dati da un oggetto {@link Ristorante} per scriverli su una riga del file CSV.
     *
     * @param r l'oggetto {@link Ristorante} da cui estrarre i dati.
     * @return un array di stringhe contenente i dati dell'oggetto.
     */
    @Override
    protected String[] estraiDati(Ristorante r) {
        String[] dati = new String[11];
        dati[0] = String.valueOf(ultimoID());
        dati[1] = r.getNomeRistorante();
        dati[2] = r.getIndirizzo();
        dati[3] = r.getDelivery().toString();
        dati[4] = r.getPrenotazione().toString();
        dati[5] = r.getTipoCucina().toString();
        dati[6] = r.getPrezzo().toString();
        dati[7] = r.getDescrizione();
        Double lat = r.getCords().getLat();
        Double lon = r.getCords().getLon();
        dati[8] = lat.toString();
        dati[9] = lon.toString();
        dati[10] = String.valueOf(r.getIdproprietario());
        return dati;
    }

    /**
     * Parsifica una riga di dati dal file CSV e crea un nuovo oggetto {@link Ristorante}.
     *
     * @param dati l'array di stringhe che rappresenta una riga del file CSV.
     * @return un nuovo oggetto {@link Ristorante} con i dati parsificati e le recensioni associate.
     */
    @Override
    protected Ristorante parseRiga(String[] dati) {
        int id = Integer.parseInt(dati[0]);
        String nome = dati[1];
        String indirizzo = dati[2];
        Boolean delivery = Boolean.parseBoolean(dati[3]);
        Boolean prenotazione = Boolean.parseBoolean(dati[4]);
        String cucina = dati[5];
        String prezzo = dati[6];
        String descrizione = dati[7];
        double lat = Double.parseDouble(dati[8]);
        double lon = Double.parseDouble(dati[9]);
        int idproprietario = Integer.parseInt(dati[10]);
        Ristorante ristorante = new Ristorante();
        ristorante.setId(id);
        ristorante.setNomeRistorante(nome);
        ristorante.setIndirizzo(indirizzo);
        ristorante.setDelivery(delivery);
        ristorante.setPrenotazioneOnline(prenotazione);
        ristorante.setCucina(cucina);
        ristorante.setPrezzo(prezzo.length());
        ristorante.setDescrizione(descrizione);
        ristorante.setCords(new Coordinate(lat, lon));
        ristorante.setIdproprietario(idproprietario);

        List<Recensione> listaFiltrata = mappaRecensioni.getOrDefault(id, new ArrayList<>());
        for (Recensione recensione : listaFiltrata) {
            ristorante.aggiungiRecensione(recensione);
        }

        return ristorante;
    }

    /**
     * Esegue un caricamento aggiuntivo di dati, in questo caso le recensioni,
     * e le organizza in una mappa per ID ristorante prima che venga avviato il parsing
     * delle righe dei ristoranti. Questo metodo viene chiamato automaticamente
     * prima di {@code caricaCSV}.
     */
    @Override
    public void caricamentoExtra() {
        RecensioneCSV recensioneCSV = new RecensioneCSV();
        List<Recensione> listaRecensioni = recensioneCSV.caricaCSV();
        for (Recensione recensione : listaRecensioni) {
            int idRis = recensione.getIdRistorante();
            if (!mappaRecensioni.containsKey(idRis)) {
                List<Recensione> listaRecRistorante = new ArrayList<>();
                listaRecRistorante.add(recensione);
                mappaRecensioni.put(idRis, listaRecRistorante);
            } else {
                mappaRecensioni.get(idRis).add(recensione);
            }
        }
    }

    /**
     * Aggiunge un ristorante alla mappa interna.
     *
     * @param ristorante il ristorante da aggiungere.
     */
    public void aggiungiRistorante(Ristorante ristorante) {
        mappaRistoranti.put(ristorante.getId(), ristorante);
    }

    /**
     * Cerca un ristorante nella mappa interna in base al suo ID.
     *
     * @param idRistorante l'ID del ristorante da cercare.
     * @return l'oggetto {@link Ristorante} corrispondente all'ID, o {@code null} se non trovato.
     */
    public Ristorante cercaRistorante(int idRistorante) {
        return mappaRistoranti.get(idRistorante);
    }

    /**
     * Popola la mappa interna dei ristoranti a partire da una lista esistente.
     *
     * @param ristoranti la lista di oggetti {@link Ristorante} da inserire nella mappa.
     */
    public void creaMappa(List<Ristorante> ristoranti) {
        for (Ristorante ristorante : ristoranti) {
            mappaRistoranti.put(ristorante.getId(), ristorante);
        }
    }
}