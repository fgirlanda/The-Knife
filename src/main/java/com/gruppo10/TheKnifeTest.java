/*
 * Francesco Girlanda  760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10;

import com.gruppo10.fileJava.Test;

import javafx.application.Application;

/**
 * Classe di avvio per la modalità di test dell'applicazione "The Knife".
 * Questa classe contiene il metodo {@code main} che serve come punto di ingresso
 * per avviare l'applicazione in una modalità di prova, bypassando il normale
 * flusso di login e di caricamento dati per facilitare i test.
 */
public class TheKnifeTest {
    /**
     * Il metodo principale che avvia l'applicazione in modalità di test.
     * <p>
     * Lancia l'interfaccia JavaFX specificata dalla classe {@link Test},
     * che permette agli sviluppatori di accedere rapidamente a una sessione di prova
     * senza dover passare dalla schermata di login e dalla validazione dei dati.
     * </p>
     * @param args gli argomenti della riga di comando passati all'applicazione.
     */
    public static void main(String[] args) {
        Application.launch(Test.class, args);
    }
}