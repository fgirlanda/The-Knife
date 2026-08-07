/*
 * Francesco Girlanda  760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.client.controller;

import com.gruppo10.common.classi.GestioneEccezioni;
import com.gruppo10.common.classi.Ruolo;
import com.gruppo10.common.classi.SceneManager;
import com.gruppo10.common.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 * Controller di test per l'accesso rapido all'applicazione.
 * Permette di creare un utente di prova con dati predefiniti e di accedere
 * alla pagina principale senza passare per la procedura di login standard,
 * scegliendo un ruolo tra quelli disponibili.
 */
public class TestController extends BasicController {
    /**
     * Gruppo di toggle che gestisce la selezione tra i ruoli disponibili.
     */
    @FXML
    private ToggleGroup ruoloGroup;

    /**
     * Pulsante per accedere al programma una volta scelto il ruolo.
     */
    @FXML
    private Button btnAccedi;

    @FXML
    private void initialize() {
        btnAccedi.setDefaultButton(true);
    }
    /**
     * Gestisce l'evento di clic sul pulsante "Accedi".
     * Crea un utente di test, lo imposta come utente loggato e naviga
     * alla pagina principale dell'applicazione.
     */
    @FXML
    public void accedi() {
        Utente utente = creaUtenteTest();
        LoginController.utenteLoggato = utente;
        try {
            SceneManager.apriPaginaPrincipale(stage);
        } catch (Exception e) {
            GestioneEccezioni.errore("Errore caricamento pagina principale", e, false, null);
        }
    }

    /**
     * Crea un oggetto {@link Utente} con dati predefiniti a scopo di test.
     * I dati sono fissi, tranne il ruolo che viene selezionato tramite
     * l'interfaccia.
     *
     * @return un oggetto {@link Utente} di test.
     */
    private Utente creaUtenteTest() {
        Utente utente = new Utente();
        utente.setNome("Mario");
        utente.setCognome("Rossi");
        utente.setCords(45.5, 9.4);
        utente.setId(404);
        utente.setDataDiNascita("08-02-1999");
        utente.setIndirizzo("Via Santa Maria 8, Casale Litta");
        utente.setPassword("passwordTest");
        utente.setRuolo(getRuolo());
        utente.setUsername("usernameTest");

        return utente;
    }

    /**
     * Determina il ruolo dell'utente di test in base alla selezione
     * del RadioButton.
     *
     * @return il {@link Ruolo} selezionato dall'utente.
     */
    private Ruolo getRuolo() {
        RadioButton select = (RadioButton) ruoloGroup.getSelectedToggle();
        Ruolo ruolo;
        if (select.getText().equals("CLIENTE")) {
            ruolo = Ruolo.CLIENTE;
        } else if (select.getText().equals("RISTORATORE")) {
            ruolo = Ruolo.RISTORATORE;
        } else {
            ruolo = Ruolo.NON_REGISTRATO;
        }

        return ruolo;
    }
}