/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.classi;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.gruppo10.controller.LoginController;
import com.gruppo10.controller.PaginaPrincipaleController;
import com.gruppo10.controller.PaginaRistoranteController;
import com.gruppo10.controller.ProfiloClienteController;
import com.gruppo10.controller.ProfiloRistoratoreController;
import com.gruppo10.controller.RegistrazioneController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SceneManager {

    public static void apriLogin(Stage stage) {

        SceneManager.cambioScena(stage, "login.fxml", "The Knife - Login",
                (LoginController controller) -> controller.setStage(stage));
    }

    public static void apriPaginaPrincipale(Stage stage) {
        SceneManager.cambioScena(stage, "pagina_principale.fxml", "The Knife",
                (PaginaPrincipaleController controller) -> {
                    controller.setStage(stage);
                    controller.setRistoranti();
                });
    }

    public static void apriPaginaRistorante(Stage stage, Ristorante ristorante, boolean paginaPrincipale, int indiceTab) {
        SceneManager.cambioScena(stage, "pagina_ristorante.fxml", "The Knife",
                (PaginaRistoranteController controller) -> {
                    controller.setStage(stage);
                    controller.setPrincipale(paginaPrincipale);
                    controller.setRistorante(ristorante);
                    controller.setDati();
                    controller.setIndiceTab(indiceTab);
                });
    }

    public static void apriRegistrati(Stage stage, boolean paginaPrincipale) {
        SceneManager.cambioScena(stage, "registrazione.fxml", "The Knife - Registrazione",
                (RegistrazioneController controller) -> {
                    controller.setStage(stage);
                    controller.setPrincipale(paginaPrincipale);
                });
    }

    public static void apriProfilo(Stage stage, int tab) {
        SceneManager.cambioScena(stage, "profilo_cliente.fxml", "The Knife - Profilo",
                (ProfiloClienteController controller) -> {
                    controller.setStage(stage);
                    controller.caricaDati();
                    controller.setTab(tab);
                });
    }

    public static void apriProfiloRistoratore(Stage stage, int tab) {
        SceneManager.cambioScena(stage, "profilo_ristoratore.fxml", "The Knife - Profilo",
                (ProfiloRistoratoreController controller) -> {
                    controller.setStage(stage);
                    controller.caricaDati();
                    controller.setTab(tab);
                });
    }

    public static void chiudi(Button annulla) {
        Stage dialogue = (Stage) annulla.getScene().getWindow();
        dialogue.close();
    }

    public static <T> void caricaTessere(List<T> lista, VBox contenitoreTessere, Stage stage, String fxmlPath,
            BiConsumer<Card<T>, T> extraConfig) {
        contenitoreTessere.getChildren().clear();
        for (T r : lista) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        Paths.get("src", "main", "resources", "GUI", fxmlPath).toUri().toURL());
                HBox card = loader.load();

                Card<T> controller = loader.getController();
                controller.setStage(stage);

                if (extraConfig != null) {
                    extraConfig.accept(controller, r);
                }
                controller.setItem(r, contenitoreTessere);
                controller.setDati();

                contenitoreTessere.getChildren().add(card);
            } catch (IOException e) {
                GestioneEccezioni.errore("Errore caricamento file: " + fxmlPath, e, false, null);
                return;
            }
        }
    }

    public static <T> void cambioScena(Stage stage, String fxmlPath, String title, Consumer<T> controllerConsumer) {
        try {
            FXMLLoader loader = new FXMLLoader(Paths.get("src", "main", "resources", "GUI", fxmlPath).toUri().toURL());
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

        } catch (IOException e) {
            GestioneEccezioni.errore("Errore caricamento file: " + fxmlPath, e, false, null);
            // e.printStackTrace();
        }
    }

    public static <T> Stage finestraDialogo(String fxmlPath, String title, Stage owner,
            Consumer<T> controllerConsumer) {
        Stage dialogStage = null;
        try {
            FXMLLoader loader = new FXMLLoader(Paths.get("src", "main", "resources", "GUI", fxmlPath).toUri().toURL());
            Parent root = loader.load();

            dialogStage = new Stage();
            dialogStage.setTitle(title);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            if (owner != null)
                dialogStage.initOwner(owner);
            dialogStage.setScene(new Scene(root));

            T controller = loader.getController();
            if (controllerConsumer != null) {
                controllerConsumer.accept(controller);
            }

            dialogStage.showAndWait();

        } catch (IOException e) {
            GestioneEccezioni.errore("Errore caricamento file: " + fxmlPath, e, false, null);
        }

        return dialogStage;
    }
}
