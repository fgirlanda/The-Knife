package com.gruppo10.controller;

import com.gruppo10.classi.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class Controller {
    protected Stage stage;
    protected boolean paginaPrincipale;
    @FXML
    protected Button btnAnnulla;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setPrincipale(boolean paginaPrincipale) {
        this.paginaPrincipale = paginaPrincipale;
    }

    @FXML
    public void chiudi() {
        SceneManager.chiudi(btnAnnulla);
    }
}
