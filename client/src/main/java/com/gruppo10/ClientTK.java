package com.gruppo10;

import java.io.IOException;

import com.gruppo10.controller.MainController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientTK extends Application {

    ClientContext clientContext;

    @Override
    public void start(Stage stage) throws Exception {
        clientContext = new ClientContext();
        clientContext.connetti();

        mostraFinestra(stage);
    }

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
