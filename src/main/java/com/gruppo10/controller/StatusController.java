package com.gruppo10.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StatusController {

    @FXML Label status;

    public void setTesto(String status) {
        this.status.setText(status);
    }

}
