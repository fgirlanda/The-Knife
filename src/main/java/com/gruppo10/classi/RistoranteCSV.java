/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RistoranteCSV extends GestoreCSV<Ristorante> {
    static String f = "ristoranti_test.csv";
    private HashMap<Integer, List<Recensione>> mappaRecensioni = new HashMap<>();

    public RistoranteCSV() {
        super(f);
    }

    @Override
    protected String[] getHeader() {
        String[] header = { "Id", "Nome", "Indirizzo", "Delivery", "Prenotazione online", "Tipo Cucina",
                "Prezzo", "Descrizione", "Latitudine", "Longitudine", "Proprietario" };

        return header;
    }

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
}
