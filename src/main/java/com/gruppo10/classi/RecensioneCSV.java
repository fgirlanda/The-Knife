package com.gruppo10.classi;

import java.io.File;

public class RecensioneCSV extends GestoreCSV<Recensione> {

    static File dir = new File("fileCSV");
    static File fileRecensioni = new File(dir, "recensioni.csv");

    public RecensioneCSV() {
        super(fileRecensioni);
    }

    @Override
    protected Recensione parseLine(String[] dati) {
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
}
