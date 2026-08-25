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

    @Override
    public void start(Stage stage) throws Exception {

        // Creare ServerContext
        serverContext = new ServerContext();


        mostraPannello(stage);
    }

    private void mostraPannello(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/pannello_admin.fxml"));
        Parent root = loader.load();

        PannelloAdminController controller = loader.getController();
        controller.setStage(stage);
        controller.setServerContext(serverContext);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        // stage.setTitle("The Knife - Pannello Admin");
        // stage.setMinWidth(1040);
        // stage.setMinHeight(760);
        // stage.setWidth(1360);
        // stage.setHeight(880);
        // stage.centerOnScreen();
        stage.show();
    }
    
}
