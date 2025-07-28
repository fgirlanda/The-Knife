package com.gruppo10.controller;

import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CardRistoranteController {

    private Stage stage;
    
    public Ristorante ristorante;

    private boolean paginaPrincipale;
    
    @FXML private HBox card;
    @FXML private ImageView imgRistorante;
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


    public void setPrincipale(boolean paginaPrincipale){
        this.paginaPrincipale = paginaPrincipale;
    }


    public void setRistorante(Ristorante ristorante){
        this.ristorante = ristorante;
    }
    

    public void setDati(){
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
                    controller.setPrincipale(paginaPrincipale);
                    controller.setDati();
                });   
    }
}
