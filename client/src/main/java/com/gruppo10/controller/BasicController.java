/*
 Francesco Girlanda 760616 VA
 Gabriele Gallon 761125 VA
 Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import com.gruppo10.ClientContext;
import com.gruppo10.classi.Utente;
import com.gruppo10.gui_elements.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Classe base per tutti i controller dell'applicazione.
 * Fornisce metodi e proprietà comuni per la gestione dell'interfaccia utente,
 * tra cui la gestione della finestra corrente, lo stato dell'utente loggato,
 * la gestione dei bottoni, il caricamento dei dati del profilo e la selezione delle tab.
 */
public class BasicController {

    /** Riferimento all'utente attualmente loggato. */
    private Utente utenteLoggato = LoginController.utenteLoggato;
    
    /** La finestra (stage) associata a questo controller. */
    protected Stage stage;

    /** Flag che indica se la pagina corrente è la pagina principale. */
    protected boolean paginaPrincipale;

    /** Indice della tab selezionata nel {@link TabPane}. */
    protected int indiceTab;

    /** Pulsante "Annulla" o "Chiudi" della UI. */
    @FXML
    protected Button btnAnnulla;

    /** Contenitore di tab dell'interfaccia. */
    @FXML
    private TabPane tabPane;

    /** Label per il nome dell'utente. */
    @FXML
    private Label labelNome;

    /** Label per il cognome dell'utente. */
    @FXML
    private Label labelCognome;

    /** Label per lo username dell'utente. */
    @FXML
    private Label labelUsername;

    /** Label per l'indirizzo dell'utente. */
    @FXML
    private Label labelIndirizzo;

    /** Label per la data di nascita dell'utente. */
    @FXML
    private Label labelData;

    /** Label per il ruolo dell'utente. */
    @FXML
    private Label labelRuolo;

    /** Label per la password (mostrata come asterischi). */
    @FXML
    private Label labelPassword;

    protected ClientContext clientContext;

    /**
     * Imposta lo {@link Stage} associato a questo controller.
     *
     * @param stage la finestra corrente dell'applicazione
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Imposta se la pagina corrente è la pagina principale.
     *
     * @param paginaPrincipale {@code true} se è la pagina principale, {@code false} altrimenti
     */
    public void setPrincipale(boolean paginaPrincipale) {
        this.paginaPrincipale = paginaPrincipale;
    }

    /**
     * Imposta l'indice della tab attualmente selezionata.
     *
     * @param indiceTab l'indice della tab da selezionare
     */
    public void setIndiceTab(int indiceTab){
        this.indiceTab = indiceTab;
    }

    /**
     * Gestisce l'evento di chiusura della finestra tramite il pulsante "Annulla" o "Chiudi".
     * Chiude la finestra associata al pulsante.
     */
    @FXML
    public void chiudi() {
        SceneManager.chiudi(btnAnnulla);
    }

    /**
     * Abilita o disabilita un {@link Button} dell'interfaccia.
     *
     * @param bottone il pulsante da modificare
     * @param abilita {@code true} per disabilitarlo, {@code false} per abilitarlo
     */
    public void disabilitaBottone(Button bottone, boolean abilita){
        bottone.setDisable(abilita);
    }
    
    /**
     * Carica e visualizza i dati dell'utente loggato nelle label della vista profilo.
     * La password viene sempre mostrata come asterischi per sicurezza.
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
     * Seleziona una tab specifica all'interno del {@link TabPane}.
     *
     * @param tab l'indice della tab da selezionare
     */
    public void setTab(int tab) {
        tabPane.getSelectionModel().select(tab);
    }

    /**
     * Esegue il logout dell'utente corrente.
     * Resetta l'utente loggato e apre la schermata di login.
     */
    @FXML
    public void logOut() {
        LoginController.utenteLoggato = null;
        utenteLoggato = null;
        SceneManager.apriLogin(stage);
    }

    public void setContext(ClientContext context) {
        this.clientContext = context;
    }
}