/* 
Francesco Girlanda 760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import java.util.List;

import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;
import com.gruppo10.classi.UtenteCSV;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 * Controller per la schermata di lancio dell'applicazione.
 * Gestisce la selezione della modalità di avvio dell'applicazione
 * tra "The Knife" e "Test", e avvia la scena appropriata.
 */
public class MainController extends BasicController {

    /**
     * Gruppo di toggle che gestisce la selezione tra le modalità disponibili.
     */
    @FXML
    private ToggleGroup visualGroup;

    /**
     * RadioButton per selezionare la modalità "The Knife".
     */
    @FXML
    private RadioButton radioTheKnife;

    /**
     * RadioButton per selezionare la modalità "Test".
     */
    @FXML
    private RadioButton radioTest;

    /**
     * Avvia l'applicazione in modalità "The Knife".
     * <p>
     * Carica la lista degli utenti dal file CSV e, se il caricamento è
     * riuscito, apre la schermata di login.
     * </p>
     */
    private void theKnife() {
        UtenteCSV utenteCSV = new UtenteCSV();
        List<Utente> listaUtenti = utenteCSV.caricaCSV();
        utenteCSV.creaMappa(listaUtenti);
        if (listaUtenti == null) {
            System.err.println("Errore caricamento utenti");
        } else {
            SceneManager.apriLogin(stage);
        }
    }

    /**
     * Avvia l'applicazione in modalità "Test".
     * <p>
     * Apre la schermata di test dell'applicazione.
     * </p>
     */
    private void test() {
        SceneManager.apriTest(stage);
    }

    /**
     * Gestisce l'evento di selezione della modalità di avvio.
     * <p>
     * In base alla modalità selezionata, avvia l'applicazione
     * nella modalità corrispondente.
     * </p>
     */
    @FXML
    private void apri() {
        RadioButton selectedVisual = (RadioButton) visualGroup.getSelectedToggle();
        if (selectedVisual == radioTheKnife)
            theKnife();
        else
            test();
    }
}