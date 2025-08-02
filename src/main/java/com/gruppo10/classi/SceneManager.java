package com.gruppo10.classi;

import java.io.IOException;
import java.util.function.Consumer;

import com.gruppo10.controller.LoginController;
import com.gruppo10.controller.PaginaPrincipaleController;
import com.gruppo10.controller.ProfiloClienteController;
import com.gruppo10.controller.ProfiloRistoratoreController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    // Generalizzazione carica tessere e configurazione controller con interfaccia ControllerConfigurator
    // public static <T> void caricaTessere(List<T> lista, VBox contenitoreTessere, Stage stage, boolean paginaPrincipale, String percorsoFXML, ControllerConfigurator<T> configuratore) {
    //     for (T item : lista) {
    //         try {
    //             FXMLLoader loader = new FXMLLoader(YourLoaderClass.class.getResource(percorsoFXML));
    //             HBox card = loader.load();

    //             Object controller = loader.getController();
    //             configuratore.configure(controller, item, stage, paginaPrincipale);

    //             contenitoreTessere.getChildren().add(card);
    //         } catch (IOException e) {
    //             System.err.println("Errore nel caricamento della scheda: " + e.getMessage());
    //         }
    //     }
    // }

    public static void reload(Stage stage) {
        Scene currentScene = stage.getScene();
        if (currentScene != null) {
            Parent root = currentScene.getRoot();
            if (root != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(root.getClass().getResource(currentScene.getRoot().getId() + ".fxml"));
                    Parent newRoot = loader.load();
                    currentScene.setRoot(newRoot);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
