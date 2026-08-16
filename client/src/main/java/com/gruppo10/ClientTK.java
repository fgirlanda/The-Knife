package com.gruppo10;

import java.io.IOException;

import com.gruppo10.controller.MainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point JavaFX del client dell'applicazione.
 * Si occupa di inizializzare il contesto RMI e mostrare la finestra iniziale.
 */
public class ClientTK extends Application {

    /** Contesto del client con i service remoti collegati al server. */
    ClientContext clientContext;

    /**
     * Metodo invocato da JavaFX all'avvio dell'applicazione.
     *
     * @param stage finestra principale dell'applicazione
     * @throws Exception se si verifica un errore durante l'avvio o il caricamento iniziale
     */
    @Override
    public void start(Stage stage) throws Exception {
        clientContext = new ClientContext();
        clientContext.connetti();

        mostraFinestra(stage);
    }

    /**
     * Carica la schermata iniziale di login.
     *
     * @param stage finestra principale da aggiornare
     * @throws IOException se il file FXML della schermata iniziale non viene caricato correttamente
     */
    private void mostraFinestra(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/main.fxml"));
        Parent root = loader.load();
        root.getStylesheets().add(getClass().getResource("/GUI/theme.css").toExternalForm());

        MainController controller = loader.getController();
        controller.setStage(stage);
        controller.setClientContext(clientContext);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Knife - Login");
        stage.show();
    }
}
