package com.gruppo10.controller;

import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.Utente;
import com.gruppo10.classi.Ruolo;
import com.gruppo10.classi.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CardRistoranteController {

    private Stage stage;
    private HBox card;

    private Utente utenteLoggato = LoginController.utenteLoggato;
    public Ristorante ristorante;

    @FXML private ImageView imgRistorante;
    @FXML private Button btnPreferito;
    @FXML private Text txtNomeRistorante;
    @FXML private Text txtRecensioni;
    @FXML private Text txtPrezzo;
    @FXML private Text txtTipoCucina;


    public void initialize() {
        card.setOnMouseClicked(_ -> { // _ = event
            apriPaginaRistorante();
        });
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void setRistorante(Ristorante ristorante){
        this.ristorante = ristorante;
    }
    

    public void setDati(){
        if(utenteLoggato.getRuolo()!= Ruolo.CLIENTE) {
            btnPreferito.setVisible(false);
        } 
        txtNomeRistorante.setText(this.ristorante.getNomeRistorante());
        //txtRecensioni.setText(ristorante.getRecensioni().size() + " Recensioni");
        txtPrezzo.setText(this.ristorante.getPrezzo());
        txtTipoCucina.setText(this.ristorante.getTipoCucina().toString());
    }


    public void apriPaginaRistorante(){
        SceneManager.cambioScena(stage, "/GUI/pagina_ristorante.fxml", "The Knife", 
                (PaginaRistoranteController controller) -> {
                    controller.setStage(stage);
                    controller.setRistorante(this.ristorante);
                    controller.setDati();
                });
        
    }
}
