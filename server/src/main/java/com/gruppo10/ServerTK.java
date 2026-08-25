/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10;

import java.io.IOException;

import com.gruppo10.controller.PannelloAdminController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Applicazione JavaFX del server di The Knife.
 * Avvia l'interfaccia amministrativa per configurare la connessione al database
 * e pubblicare i service RMI del sistema.
 */
public class ServerTK extends Application {

    /** Contesto condiviso del server. */
    ServerContext serverContext;

    /** Publisher dei service remoti esposti dal server. */
    ServerPublisher serverPublisher;

    /**
     * Inizializza il contesto del server e apre il pannello di amministrazione.
     *
     * @param stage finestra principale dell'applicazione
     * @throws Exception se si verifica un errore durante l'avvio
     */
    @Override
    public void start(Stage stage) throws Exception {

        serverContext = new ServerContext();
        serverPublisher = new ServerPublisher(serverContext);

        mostraPannello(stage);
    }

    /**
     * Chiude correttamente il server e libera le risorse RMI al termine dell'applicazione.
     *
     * @throws Exception se si verifica un errore durante la chiusura
     */
    @Override
    public void stop() throws Exception {
        if (serverPublisher != null) {
            serverPublisher.arresta();
        }
    }

    /**
     * Carica il pannello di amministrazione del server e collega il controller al contesto.
     *
     * @param stage finestra principale dell'applicazione
     * @throws IOException se il file FXML non può essere caricato
     */
    private void mostraPannello(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/pannello_admin.fxml"));
        Parent root = loader.load();

        PannelloAdminController controller = loader.getController();
        controller.setServerContext(serverContext);
        controller.setServerPublisher(serverPublisher);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Knife - Pannello Server");
        stage.show();
    }
    
}
