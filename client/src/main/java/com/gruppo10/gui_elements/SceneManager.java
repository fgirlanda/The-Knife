/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.gui_elements;

import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.gruppo10.ClientContext;
import com.gruppo10.ClientTK;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.Sessione;
import com.gruppo10.controller.LoginController;
import com.gruppo10.controller.PaginaPrincipaleController;
import com.gruppo10.controller.PaginaRistoranteController;
import com.gruppo10.controller.ProfiloClienteController;
import com.gruppo10.controller.ProfiloRistoratoreController;
import com.gruppo10.controller.RegistrazioneController;
import com.gruppo10.controller.TestController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Questa classe gestisce il caricamento e la visualizzazione delle scene e
 * finestre
 * dell'applicazione.
 */
public class SceneManager {

    /** Percorso base delle risorse FXML. */
    private static String guiPath = "/GUI/";

    /**
     * Apre la finestra di test.
     *
     * @param stage finestra principale
     */
    public static void apriTest(Stage stage) {
        SceneManager.cambioScena(stage, "test.fxml", "The Knife - Test",
                (TestController controller) -> controller.setStage(stage));
    }

    /**
     * Apre la finestra di login.
     *
     * @param stage finestra principale
     */
    public static void apriLogin(Stage stage, ClientContext clientContext) {
        SceneManager.cambioScena(stage, "login.fxml", "The Knife - Login",
                (LoginController controller) -> {
                    controller.setStage(stage);
                    controller.setClientContext(clientContext);
                });
    }

    /**
     * Apre la pagina principale.
     *
     * @param stage finestra principale
     */
    public static void apriPaginaPrincipale(Stage stage, ClientContext clientContext, Sessione sessioneCorrente) {
        SceneManager.cambioScena(stage, "pagina_principale.fxml", "The Knife",
                (PaginaPrincipaleController controller) -> {
                    controller.setStage(stage);
                    controller.setClientContext(clientContext);
                    controller.setSessioneCorrente(sessioneCorrente);
                    controller.setRistoranti();
                });
    }

    /**
     * Apre la pagina di un ristorante.
     *
     * @param stage            finestra principale
     * @param ristorante       oggetto ristorante per settare i dati della pagina
     * @param paginaPrincipale true se si proviene dalla pagina principale, false se
     *                         si proviene dal profilo
     * @param indiceTab        indice della tab da aprire in caso si provenga dal
     *                         profilo
     */
    public static void apriPaginaRistorante(Stage stage, Ristorante ristorante,
            boolean paginaPrincipale, int indiceTab, ClientContext clientContext, Sessione sessioneCorrente) {
        SceneManager.cambioScena(stage, "pagina_ristorante.fxml", "The Knife",
                (PaginaRistoranteController controller) -> {
                    controller.setStage(stage);
                    controller.setClientContext(clientContext);
                    controller.setSessioneCorrente(sessioneCorrente);
                    controller.setPrincipale(paginaPrincipale);
                    controller.setRistorante(ristorante);
                    controller.setDati();
                    controller.setIndiceTab(indiceTab);
                });
    }

    /**
     * Apre la finestra di registrazione.
     *
     * @param stage            finestra principale
     * @param paginaPrincipale true se si proviene dalla pagina principale, false se
     *                         si proviene dal profilo
     */
    public static void apriRegistrati(Stage stage, boolean paginaPrincipale, ClientContext clientContext, Sessione sessioneCorrente) {
        SceneManager.cambioScena(stage, "registrazione.fxml", "The Knife - Registrazione",
                (RegistrazioneController controller) -> {
                    controller.setStage(stage);
                    controller.setPrincipale(paginaPrincipale);
                    controller.setClientContext(clientContext);
                    controller.setSessioneCorrente(sessioneCorrente);
                });
    }

    /**
     * Apre la pagina del profilo cliente.
     *
     * @param stage finestra principale
     * @param tab   indice della tab da aprire
     * @param clientContext 
     */
    public static void apriProfilo(Stage stage, int tab, ClientContext clientContext, Sessione sessioneCorrente) {
        SceneManager.cambioScena(stage, "profilo_cliente.fxml", "The Knife - Profilo",
                (ProfiloClienteController controller) -> {
                    controller.setStage(stage);
                    controller.setClientContext(clientContext);
                    controller.setSessioneCorrente(sessioneCorrente);
                    controller.caricaDati();
                    controller.setTab(tab);
                });
    }

    /**
     * Apre la pagina del profilo ristoratore.
     *
     * @param stage finestra principale
     * @param tab   indice della tab da aprire
     * @param clientContext il contesto del client contenente le informazioni e i servizi condivisi
     * @param sessioneCorrente la sessione corrente dell'utente
     */
    public static void apriProfiloRistoratore(Stage stage, int tab, ClientContext clientContext, Sessione sessioneCorrente) {
        SceneManager.cambioScena(stage, "profilo_ristoratore.fxml", "The Knife - Profilo",
                (ProfiloRistoratoreController controller) -> {
                    controller.setStage(stage);
                    controller.setClientContext(clientContext);
                    controller.setSessioneCorrente(sessioneCorrente);
                    controller.caricaDati();
                    controller.setTab(tab);
                });
    }

    /**
     * Chiude la pagina associata al pulsante.
     *
     * @param annulla pulsante di chiusura
     */
    public static void chiudi(Button annulla) {
        Stage dialogue = (Stage) annulla.getScene().getWindow();
        dialogue.close();
    }

    /**
     * Carica una lista di tessere in un contenitore.
     *
     * @param <T>                tipo degli elementi, {@link Ristorante} oppure
     *                           {@link Recensione}
     * @param lista              elementi da visualizzare
     * @param contenitoreTessere contenitore delle tessere
     * @param stage              finestra principale
     * @param fxmlFile           file FXML della tessera
     * @param extraConfig        configurazione aggiuntiva, può essere null
     */
    public static <T> void caricaTessere(List<T> lista, VBox contenitoreTessere,
            Stage stage, String fxmlFile, ClientContext clientContext, Sessione sessioneCorrente,
            BiConsumer<Card<T>, T> extraConfig) {
        contenitoreTessere.getChildren().clear();
        for (T r : lista) {
            try {
                FXMLLoader loader = new FXMLLoader(ClientTK.class.getResource(guiPath + fxmlFile));
                HBox card = loader.load();

                Card<T> controller = loader.getController();
                controller.setStage(stage);
                controller.setClientContext(clientContext);
                controller.setSessioneCorrente(sessioneCorrente);

                if (extraConfig != null) {
                    extraConfig.accept(controller, r);
                }
                controller.setItem(r, contenitoreTessere);
                controller.setDati();

                contenitoreTessere.getChildren().add(card);
            } catch (IOException e) {
                GestioneEccezioni.errore("Errore caricamento file: " + fxmlFile, e, false, null);
                return;
            }
        }
    }

    /**
     * Cambia scena nella finestra principale.
     *
     * @param <T>                tipo del controller
     * @param stage              finestra principale
     * @param fxmlFile           file FXML della scena
     * @param title              titolo della finestra
     * @param controllerConsumer operazioni aggiuntive sul controller, può essere
     *                           null
     */
    public static <T> void cambioScena(Stage stage, String fxmlFile, String title,
            Consumer<T> controllerConsumer) {
        try {
            FXMLLoader loader = new FXMLLoader(ClientTK.class.getResource(guiPath + fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

            T controller = loader.getController();
            if (controllerConsumer != null) {
                controllerConsumer.accept(controller);
            }

        } catch (IOException e) {
            GestioneEccezioni.errore("Errore caricamento file: " + fxmlFile, e, false, null);
        }
    }

    /**
     * Apre una finestra di dialogo modale.
     *
     * @param <T>                tipo del controller
     * @param fxmlFile           file FXML della finestra
     * @param title              titolo della finestra
     * @param owner              finestra genitore, può essere null
     * @param controllerConsumer operazioni aggiuntive sul controller, può essere
     *                           null
     * @return stage della finestra di dialogo, o null se si verifica un errore
     */
    public static <T> Stage finestraDialogo(String fxmlFile, String title, Stage owner, ClientContext clientContext, Sessione sessioneCorrente,
            Consumer<T> controllerConsumer) {
        Stage dialogStage = null;
        try {
            FXMLLoader loader = new FXMLLoader(ClientTK.class.getResource(guiPath + fxmlFile));
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
            GestioneEccezioni.errore("Errore caricamento file: " + fxmlFile, e, false, null);
        }

        return dialogStage;
    }
}
