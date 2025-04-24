package controller;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;


public class registrazioneController {

    private Stage stage;

    // Imposta il riferimento alla finestra principale (Stage)
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private RadioButton clienteRadioButton;

    @FXML
    private RadioButton ristoratoreRadioButton;

    private ToggleGroup ruoloGroup;

    @FXML
    public void initialize() {
        // Inizializza il ToggleGroup e associa i RadioButton
        ruoloGroup = new ToggleGroup();
        clienteRadioButton.setToggleGroup(ruoloGroup);
        ristoratoreRadioButton.setToggleGroup(ruoloGroup);
    }

    // Altri metodi e logica del controller
}
