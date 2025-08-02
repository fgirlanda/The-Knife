package com.gruppo10.classi;

import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.gruppo10.controller.LoginController;
import com.gruppo10.controller.PaginaPrincipaleController;
import com.gruppo10.controller.ProfiloClienteController;
import com.gruppo10.controller.ProfiloRistoratoreController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SceneManager {
    
    public static <T> void cambioScena(Stage stage, String fxmlPath, String title, Consumer<T> controllerConsumer) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

            // Applica azioni personalizzate sul controller, se richieste
            T controller = loader.getController();
            if (controllerConsumer != null) {
                controllerConsumer.accept(controller);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void cambioScena(Stage stage, String fxmlPath, String title) {
        cambioScena(stage, fxmlPath, title, null);
    }


    public static <T> void finestraDialogo(String fxmlPath, String title, Stage owner, Consumer<T> controllerConsumer) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(owner);
            dialogStage.setScene(new Scene(root));

            T controller = loader.getController();
            if (controllerConsumer != null) {
                controllerConsumer.accept(controller);
            }

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void tornaPaginaPrincipale(Stage stage){
        SceneManager.cambioScena(stage, "/GUI/pagina_principale.fxml", "The Knife", 
        (PaginaPrincipaleController controller) -> controller.setStage(stage));
    }


    public static void tornaProfilo(Stage stage, Utente utente){
        if(utente.getRuolo() == Ruolo.CLIENTE){
            SceneManager.cambioScena(stage, "/GUI/profilo_cliente.fxml", "The Knife", 
            (ProfiloClienteController controller) -> controller.setStage(stage));
        }else{
            SceneManager.cambioScena(stage, "/GUI/profilo_ristoratore.fxml", "The Knife", 
            (ProfiloRistoratoreController controller) -> controller.setStage(stage));
        }
    }


    public static void annulla(Button annulla) {
        Stage dialogue = (Stage) annulla.getScene().getWindow();
        dialogue.close();
    }

    public static void logOut(Stage stage){
        SceneManager.cambioScena(stage, "/GUI/login.fxml", "The Knife - Login", 
            (LoginController controller) -> controller.setStage(stage));
    }


    public static <T> void caricaTessere(List<T> lista, VBox contenitoreTessere, Stage stage, String fxmlPath, BiConsumer<CardController<T>, T> extraConfig){
        for (T r : lista) {
            try {
                FXMLLoader loader = new FXMLLoader(RistoranteReader.class.getResource(fxmlPath));
                HBox card = loader.load();

                CardController<T> controller = loader.getController();
                controller.setStage(stage);
                controller.setItem(r);
                controller.setDati();

                if (extraConfig != null) {
                    extraConfig.accept(controller, r);
                }
                
                contenitoreTessere.getChildren().add(card);
            } catch (IOException e) {
                System.err.println("Errore nel caricamento della scheda del ristorante: " + e.getMessage());
            }
        }
    }
    

    public static void reload(Stage stage, String path) {
        Scene currentScene = stage.getScene();
        if (currentScene != null) {
            Parent root = currentScene.getRoot();
            if (root != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(path));
                    Parent newRoot = loader.load();
                    currentScene.setRoot(newRoot);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
