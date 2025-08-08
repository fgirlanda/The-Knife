package com.gruppo10.fileJava;

import com.gruppo10.controller.TestController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Test extends Application{
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

    // test main
    public static void main(String[] args) {
        launch(args);
    }
}
