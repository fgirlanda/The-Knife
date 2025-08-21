/*
 * Francesco Girlanda  760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10;

import com.gruppo10.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale dell'applicazione "The Knife".
 * <p>
 * Questa classe estende {@link javafx.application.Application} e rappresenta
 * il punto di ingresso dell'applicazione JavaFX. Si occupa di caricare
 * la schermata principale e avviare la GUI.
 * </p>
 */
public class TheKnife extends Application {

    /**
     * Avvia la GUI dell'applicazione.
     * <p>
     * Questo metodo viene chiamato automaticamente dal framework JavaFX
     * dopo {@link #main(String[])}. Carica il file FXML della schermata
     * principale, imposta il controller e mostra la finestra principale.
     * </p>
     *
     * @param stage il {@link Stage} principale su cui costruire la scena
     * @throws Exception se si verifica un errore durante il caricamento del FXML
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/main.fxml"));
        Parent root = loader.load();

        MainController controller = loader.getController();
        controller.setStage(stage);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Knife - Login");
        stage.show();
    }
}