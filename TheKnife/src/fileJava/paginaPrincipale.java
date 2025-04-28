package fileJava;

import controller.PaginaPrincipaleController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PaginaPrincipale extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../bin/GUI/pagina_principale.fxml"));
        Parent root = loader.load();;

        PaginaPrincipaleController controller = loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("The Knife");
        // LoginController controller = new LoginController();
        // controller.setStage(stage); 
        stage.show();
    }
}