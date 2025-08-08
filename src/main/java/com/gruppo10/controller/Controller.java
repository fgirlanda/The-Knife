package com.gruppo10.controller;

import javafx.stage.Stage;

public abstract class Controller {
    protected Stage stage;
    protected boolean paginaPrincipale;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setPrincipale(boolean paginaPrincipale){
        this.paginaPrincipale = paginaPrincipale;
    }
}
