/*
 * Francesco Girlanda  760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10;

import java.util.List;

import com.gruppo10.classi.Utente;
import com.gruppo10.classi.UtenteCSV;
import com.gruppo10.fileJava.Login;
import javafx.application.Application;

/**
 * Classe principale dell'applicazione "The Knife".
 * Questa classe contiene il metodo {@code main} che funge da punto di ingresso
 * per l'applicazione. È responsabile del caricamento iniziale dei dati degli
 * utenti e dell'avvio dell'interfaccia grafica di login.
 */
public class TheKnife {
    /**
     * Il metodo principale che avvia l'applicazione.
     * <p>
     * Carica gli utenti da un file CSV all'avvio. Se il caricamento ha successo,
     * avvia l'applicazione JavaFX, partendo dalla schermata di login.
     * Se il caricamento fallisce, stampa un messaggio di errore e l'applicazione
     * non viene avviata.
     * </p>
     * @param args gli argomenti della riga di comando passati all'applicazione.
     */
    public static void main(String[] args) {
        // Carica gli utenti dal file CSV
        UtenteCSV utenteCSV = new UtenteCSV();
        List<Utente> listaUtenti = utenteCSV.caricaCSV();
        utenteCSV.creaMappa(listaUtenti);
        if(listaUtenti == null){
            System.err.println("Errore caricamento utenti");
        }else{
            Application.launch(Login.class, args);
        }
    }
}