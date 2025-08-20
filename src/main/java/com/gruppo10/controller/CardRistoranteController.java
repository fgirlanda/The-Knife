/* 
Francesco Girlanda  760616 VA
Gabriele Gallon 761125 VA
Mattia Lambertoni 762595 VA
 */
package com.gruppo10.controller;

import java.nio.file.Paths;

import com.gruppo10.classi.Card;
import com.gruppo10.classi.Ristorante;
import com.gruppo10.classi.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CardRistoranteController extends BasicController implements Card<Ristorante> {

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
            SceneManager.apriPaginaRistorante(stage, ristorante, paginaPrincipale, indiceTab);
        });
    }

    @Override
    public void setItem(Ristorante ristorante, VBox contenitore) {
        this.ristorante = ristorante;
    }

    @Override
    public void setDati() {
        String cucina = this.ristorante.getTipoCucina().name();
        String path = Paths.get("images",cucina+".png").toString();
        Image immagine = new Image(path);
        imgRistorante.setImage(immagine);
        txtNomeRistorante.setText(this.ristorante.getNomeRistorante());
        txtRecensioni.setText(String.format("%.1f", this.ristorante.getMediaRec()) + " ★" + " ("
                + this.ristorante.getNumeroRecensioni() + " Recensione/i)");
        txtPrezzo.setText(this.ristorante.getPrezzo().toString());
        txtTipoCucina.setText(cucina);
    }
}
