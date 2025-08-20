/*
 * Francesco Girlanda  760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.fileJava;

import com.gruppo10.controller.TestController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe di avvio per l'interfaccia di test.
 * Estende {@link javafx.application.Application} e serve come punto di ingresso
 * per avviare l'applicazione in modalità di test, caricando la vista
 * e il controller associati.
 */
public class Test extends Application{
    /**
     * Il metodo start è il punto di ingresso principale per tutte le applicazioni JavaFX.
     * In questa implementazione, carica l'interfaccia FXML per la pagina di test,
     * imposta il controller e visualizza lo stage.
     *
     * @param stage lo stage principale per questa applicazione, su cui viene impostata la scena.
     * @throws Exception se si verifica un errore durante il caricamento del file FXML.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/test.fxml"));
        Parent root = loader.load();

        TestController controller = loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Knife - Test");
        stage.show();
    }

    /**
     * Il metodo main non è strettamente necessario per le applicazioni JavaFX
     * ma è una pratica comune per permettere l'avvio da ambienti di sviluppo
     * che non supportano direttamente il lancio di JavaFX.
     *
     * @param args gli argomenti della riga di comando.
     */
    public static void main(String[] args) {
        launch(args);
    }
}