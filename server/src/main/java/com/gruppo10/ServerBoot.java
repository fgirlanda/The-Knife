package com.gruppo10;

import javafx.application.Application;

/**
 * Classe di bootstrap del server JavaFX dell'applicazione.
 * Avvia la schermata di amministrazione {@link ServerTK}.
 */
public class ServerBoot {
    /**
     * Metodo principale di avvio del server.
     *
     * @param args argomenti della riga di comando
     */
    public static void main(String[] args) {
        Application.launch(ServerTK.class, args);
    }
}
