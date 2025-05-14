package com.gruppo10.classi;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtenteWriterTest {

    @Test
    public void testScriviUtente() throws Exception {
        // Prepara un oggetto utente di test
        
        Utente utente = new Utente();
        utente.setNome("Mario");
        utente.setCognome("Rossi");
        utente.setUsername("mrossi");
        utente.setPassword("password123");
        utente.setDataDiNascita("01-01-1990"); // Assicurati che il formato della data sia corretto
        utente.setIndirizzo("Via Roma 1");
        utente.setRuolo("cliente"); // Assicurati che Ruolo sia un enum definito correttamente
        Coordinate coordinate = new Coordinate(utente.getIndirizzo());
        utente.setCords(coordinate);

        // Scrive l'utente nel file
        UtenteWriter writer = new UtenteWriter();
        writer.scriviUtente(utente);

        // Verifica che il file sia stato creato
        File file = new File("fileCSV/utenti.csv");
        assertTrue(file.exists(), "Il file utenti.csv dovrebbe essere stato creato");
    }
}
