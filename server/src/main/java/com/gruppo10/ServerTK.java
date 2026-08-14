package com.gruppo10;

import java.io.IOException;

import com.gruppo10.controller.PannelloAdminController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerTK extends Application{

    ServerContext serverContext;
    ServerPublisher serverPublisher;

    @Override
    public void start(Stage stage) throws Exception {

        // Creare ServerContext
        serverContext = new ServerContext();
        serverPublisher = new ServerPublisher(serverContext);

        mostraPannello(stage);
    }

    @Override
    public void stop() throws Exception {
        if (serverPublisher != null) {
            serverPublisher.arresta();
        }
    }

    private void mostraPannello(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/pannello_admin.fxml"));
        Parent root = loader.load();

        PannelloAdminController controller = loader.getController();
        controller.setStage(stage);
        controller.setServerContext(serverContext);
        controller.setServerPublisher(serverPublisher);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Knife - Login");
        stage.show();
    }
    
}
