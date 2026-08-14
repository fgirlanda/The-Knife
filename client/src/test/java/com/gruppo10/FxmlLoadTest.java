package com.gruppo10;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.Utente;
import com.gruppo10.controller.LoginController;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;

class FxmlLoadTest {

    private static final List<String> VISTE = List.of(
            "main.fxml",
            "login.fxml",
            "registrazione.fxml",
            "pagina_principale.fxml",
            "card_ristorante.fxml",
            "pagina_ristorante.fxml",
            "card_recensione.fxml",
            "aggiungi_recensione.fxml",
            "modifica_recensione.fxml",
            "rispondi_recensione.fxml",
            "profilo_cliente.fxml",
            "profilo_ristoratore.fxml",
            "aggiungi_ristorante.fxml");

    @BeforeAll
    static void avviaJavaFx() throws InterruptedException {
        Utente utenteDiProva = new Utente();
        utenteDiProva.setRuolo(Ruolo.CLIENTE);
        LoginController.utenteLoggato = utenteDiProva;

        CountDownLatch avvio = new CountDownLatch(1);
        Platform.startup(avvio::countDown);
        if (!avvio.await(10, TimeUnit.SECONDS)) {
            fail("JavaFX non si è avviato entro il tempo previsto");
        }
    }

    @Test
    void tutteLeVisteSiCaricanoConIlTema() throws InterruptedException {
        CountDownLatch completato = new CountDownLatch(1);
        AtomicReference<Throwable> errore = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                String tema = ClientTK.class.getResource("/GUI/theme.css").toExternalForm();
                for (String vista : VISTE) {
                    FXMLLoader loader = new FXMLLoader(
                            ClientTK.class.getResource("/GUI/" + vista));
                    Parent root = loader.load();
                    root.getStylesheets().add(tema);
                    new Scene(root);
                    if (root instanceof Region regione) {
                        regione.resize(regione.prefWidth(-1), regione.prefHeight(-1));
                    }
                    root.applyCss();
                    root.layout();
                }
            } catch (Throwable e) {
                errore.set(e);
            } finally {
                completato.countDown();
            }
        });

        if (!completato.await(20, TimeUnit.SECONDS)) {
            fail("Il caricamento delle viste non è terminato entro il tempo previsto");
        }
        if (errore.get() != null) {
            fail("Caricamento FXML non riuscito", errore.get());
        }
    }

}
