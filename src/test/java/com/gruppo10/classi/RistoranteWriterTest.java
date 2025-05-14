package com.gruppo10.classi;

import org.junit.jupiter.api.Test;

public class RistoranteWriterTest {
    @Test
    void testScriviRistorante() {
        Ristorante ristorante = new Ristorante();
        ristorante.setNomeRistorante("Test Ristorante");
        ristorante.setIndirizzo("Test Indirizzo");
        ristorante.setDelivery(true);
        ristorante.setPrenotazioneOnline(false);
        ristorante.setCucina("Italiana");
        ristorante.setPrezzo("€€");
        ristorante.setDescrizione("Test Descrizione lungaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        Coordinate coordinate = new Coordinate();
        coordinate.setLat(45.0);
        coordinate.setLon(9.0);
        ristorante.setCords(coordinate);

        RistoranteWriter writer = new RistoranteWriter();
        try {
            writer.scriviRistorante(ristorante);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
