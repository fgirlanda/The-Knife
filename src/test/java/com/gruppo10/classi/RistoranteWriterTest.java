package com.gruppo10.classi;

import org.junit.jupiter.api.Test;

public class RistoranteWriterTest {
    @Test
    void testScriviRistorante() throws Exception {
        Ristorante ristorante = new Ristorante();
        ristorante.setNomeRistorante("Test Ristorante");
        ristorante.setIndirizzo("Via santa maria 8, villadosia");
        ristorante.setDelivery(true);
        ristorante.setPrenotazioneOnline(false);
        ristorante.setCucina("Italiana");
        ristorante.setPrezzo("€€");
        ristorante.setDescrizione("Test Descrizione lungaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        Coordinate coordinate = new Coordinate(ristorante.getIndirizzo());
        ristorante.setCords(coordinate);

        RistoranteWriter writer = new RistoranteWriter();
        try {
            writer.scriviRistorante(ristorante);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
