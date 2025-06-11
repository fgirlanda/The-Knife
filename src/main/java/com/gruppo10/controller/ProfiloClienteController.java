package com.gruppo10.controller;

import com.gruppo10.classi.Utente;

import javafx.stage.Stage;

public class ProfiloClienteController {

    private Stage stage;

    private Utente utenteloggato = LoginController.utenteLoggato;

    // Imposta il riferimento alla finestra principale (Stage)
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void modificaDati(){
    }
}
