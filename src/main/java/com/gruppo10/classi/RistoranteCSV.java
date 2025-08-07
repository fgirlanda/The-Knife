package com.gruppo10.classi;

public class RistoranteCSV extends GestoreCSV<Ristorante> {
    static String f = "ristoranti.csv";

    public RistoranteCSV() {
        super(f);
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
        Ristorante r = new Ristorante();
        r.setId(id);
        r.setNomeRistorante(nome);
        r.setIndirizzo(indirizzo);
        r.setDelivery(delivery);
        r.setPrenotazioneOnline(prenotazione);
        r.setCucina(cucina);
        r.setPrezzo(prezzo);
        r.setDescrizione(descrizione);
        r.setCords(new Coordinate(lat, lon));
        r.setIdproprietario(idproprietario);

        return r;
    }

    @Override
    protected String[] estraiDati(Ristorante r) {
        String[] dati = new String[11];
        dati[0] = String.valueOf(ultimoID());
        dati[1] = r.getNomeRistorante();
        dati[2] = r.getIndirizzo();
        dati[3] = String.valueOf(r.isDelivery());
        dati[4] = String.valueOf(r.isPrenotazioneOnline());
        dati[5] = r.getTipoCucina().toString();
        dati[6] = r.getPrezzo();
        dati[7] = r.getDescrizione();
        Double lat = r.getCords().getLat();
        Double lon = r.getCords().getLon();
        dati[8] = lat.toString();
        dati[9] = lon.toString();
        dati[10] = String.valueOf(r.getIdproprietario());
        return dati;
    }

    @Override
    protected String[] getHeader() {
        String[] header = { "Id", "Nome", "Indirizzo", "Delivery", "Prenotazione online", "Tipo Cucina",
                "Prezzo", "Descrizione", "Latitudine", "Longitudine", "Proprietario" };

        return header;
    }
}
