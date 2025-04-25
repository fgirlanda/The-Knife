package fileJava;

import controller.ProfiloClienteController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProfiloCliente extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../bin/GUI/profilo_cliente.fxml"));
        Parent root = loader.load();;

        ProfiloClienteController controller = loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Knife - Profilo");
        // LoginController controller = new LoginController();
        // controller.setStage(stage); 
        stage.show();
    }
}