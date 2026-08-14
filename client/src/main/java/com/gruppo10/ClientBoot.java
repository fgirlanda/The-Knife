package com.gruppo10;

import javafx.application.Application;

/**
 * Classe di bootstrap del client JavaFX dell'applicazione.
 * Avvia l'applicazione principale {@link ClientTK}.
 */
public class ClientBoot {
    /**
     * Metodo principale di avvio del client.
     *
     * @param args argomenti della riga di comando passati all'avvio
     */
    public static void main(String[] args) {
        Application.launch(ClientTK.class, args);
    }
}