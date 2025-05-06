package com.gruppo10.fileJava;

import com.gruppo10.controller.RegistrazioneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Registrazione extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/registrazione.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        RegistrazioneController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(scene);
        stage.setTitle("TheKnife - Registrazione");
        stage.show();
    }
}