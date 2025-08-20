/*
 * Francesco Girlanda 760616 VA
 * Gabriele Gallon 761125 VA
 * Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import com.gruppo10.classi.SceneManager;
import com.gruppo10.classi.Utente;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Classe base per tutti i controller dell'applicazione. Fornisce metodi e proprietà
 * comuni per la gestione dell'interfaccia utente, come la gestione delle finestre,
 * l'accesso all'utente loggato, la gestione dei bottoni e il caricamento dei dati del profilo.
 */
public class BasicController {

    private Utente utenteLoggato = LoginController.utenteLoggato;
    
    protected Stage stage;

    protected boolean paginaPrincipale;

    protected int indiceTab;

    @FXML
    protected Button btnAnnulla;
    @FXML
    private TabPane tabPane;
    @FXML
    private Label labelNome;
    @FXML
    private Label labelCognome;
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelIndirizzo;
    @FXML
    private Label labelData;
    @FXML
    private Label labelRuolo;
    @FXML
    private Label labelPassword;

    /**
     * Imposta l'oggetto Stage associato a questo controller.
     *
     * @param stage l'oggetto {@link Stage} della finestra corrente.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Imposta un flag per indicare se la pagina corrente è la pagina principale.
     *
     * @param paginaPrincipale {@code true} se è la pagina principale, {@code false} altrimenti.
     */
    public void setPrincipale(boolean paginaPrincipale) {
        this.paginaPrincipale = paginaPrincipale;
    }

    /**
     * Imposta l'indice della tab corrente.
     *
     * @param indiceTab l'indice della tab selezionata.
     */
    public void setIndiceTab(int indiceTab){
        this.indiceTab = indiceTab;
    }

    /**
     * Gestisce l'evento di clic sul pulsante "Annulla" o "Chiudi".
     * Chiude la finestra corrente.
     */
    @FXML
    public void chiudi() {
        SceneManager.chiudi(btnAnnulla);
    }

    /**
     * Abilita o disabilita un pulsante dell'interfaccia utente.
     *
     * @param bottone il pulsante da gestire.
     * @param abilita {@code true} per disabilitare il pulsante, {@code false} per abilitarlo.
     */
    public void disabilitaBottone(Button bottone, boolean abilita){
        bottone.setDisable(abilita);
    }
    
    /**
     * Carica e visualizza i dati dell'utente loggato nelle apposite etichette
     * della vista del profilo (sia per clienti che per ristoratori).
     */
    public void caricaDatiUtente() {
        String labelPasswordText = "********";
        labelNome.setText(utenteLoggato.getNome());
        labelCognome.setText(utenteLoggato.getCognome());
        labelUsername.setText(utenteLoggato.getUsername());
        labelIndirizzo.setText(utenteLoggato.getIndirizzo());
        labelData.setText(utenteLoggato.getDataDiNascita().toString());
        labelRuolo.setText(utenteLoggato.getRuolo().toString());
        labelPassword.setText(labelPasswordText);
    }

    /**
     * Seleziona una specifica tab all'interno di un {@link TabPane}.
     *
     * @param tab l'indice della tab da selezionare.
     */
    public void setTab(int tab) {
        tabPane.getSelectionModel().select(tab);
    }

    /**
     * Gestisce il processo di logout dell'utente. Resetta l'utente loggato
     * e apre la schermata di login.
     */
    @FXML
    public void logOut() {
        LoginController.utenteLoggato = null;
        utenteLoggato = null;
        SceneManager.apriLogin(stage);
    }
}