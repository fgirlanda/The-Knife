package com.gruppo10.controller;

import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CardRistoranteController extends Controller implements InterfacciaCard<Ristorante> {

    public Ristorante ristorante;

    @FXML
    private HBox card;

    @FXML
    private ImageView imgRistorante;

    @FXML
    private Text txtNomeRistorante;

    @FXML
    private Text txtRecensioni;

    @FXML
    private Text txtPrezzo;

    @FXML
    private Text txtTipoCucina;

    public void setOnClick(){
        card.setOnMouseClicked(_ -> { // _ = event
            SceneManager.apriPaginaRistorante(stage, ristorante, paginaPrincipale);
        });
    }

    @Override
    public void setItem(Ristorante ristorante, VBox contenitore) {
        this.ristorante = ristorante;
    }

    @Override
    public void setDati() {
        txtNomeRistorante.setText(this.ristorante.getNomeRistorante());
        txtRecensioni.setText(String.format("%.1f", this.ristorante.getMediaRec()) + " ★" + " ("
                + this.ristorante.getNumeroRecensioni() + " Recensione/i)");
        txtPrezzo.setText(this.ristorante.getPrezzo());
        txtTipoCucina.setText(this.ristorante.getTipoCucina().toString());
    }
}
