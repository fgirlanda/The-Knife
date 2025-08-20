/*
 * Francesco Girlanda  760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.fileJava;

import com.gruppo10.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale per l'avvio dell'applicazione.
 * Estende {@link javafx.application.Application} e gestisce il caricamento
 * dell'interfaccia utente di login e l'inizializzazione dello stage principale.
 */
public class Login extends Application {
    /**
     * Il metodo start è il punto di ingresso principale per tutte le applicazioni JavaFX.
     * Viene chiamato dopo che il metodo init() è stato eseguito, e il sistema è pronto
     * per l'esecuzione.
     *
     * @param stage lo stage principale per questa applicazione, su cui viene impostata la scena.
     * @throws Exception se si verifica un errore durante il caricamento del file FXML.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/login.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Knife - Login");
        // LoginController controller = new LoginController();
        // controller.setStage(stage); 
        stage.show();
    }

    /**
     * Il metodo main non è richiesto per le applicazioni JavaFX con JFX,
     * ma è una pratica comune includerlo per avviare l'applicazione in modo standard.
     *
     * @param args gli argomenti della riga di comando.
     */
    public static void main(String[] args) {
        launch(args);
    }
}