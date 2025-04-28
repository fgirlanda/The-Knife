package fileJava;

import controller.cardRistoranteController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class cardRistorante extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../bin/GUI/card_ristorante.fxml"));
        Parent root = loader.load();;

        cardRistoranteController controller = loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Knife - test");
        // LoginController controller = new LoginController();
        // controller.setStage(stage); 
        stage.show();
    }
}