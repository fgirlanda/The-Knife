package fileJava;

import controller.registrazioneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Registrazione extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../GUI/registrazione.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        registrazioneController controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(scene);
        stage.setTitle("TheKnife - Registrazione");
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}